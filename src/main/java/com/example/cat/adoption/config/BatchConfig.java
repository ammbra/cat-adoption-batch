package com.example.cat.adoption.config;

import com.example.cat.adoption.model.Record;
import com.example.cat.adoption.step.FilterRecordProcessor;
import com.example.cat.adoption.step.FilterReportProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;

import com.example.cat.adoption.listener.JobCompletionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

	public static final String[] NAMES = new String[]{"refId", "name", "age", "dob", "playfullness", "readyToAdopt"};
	public static final String RECORD = "record";

	@Value("${input.xml}")
	Resource xmlReport;

	@Value("${output.csv}")
	Resource csvReport;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource datasource;

	@Bean
	public Job processJob() {

		return jobBuilderFactory.get("reportJob")
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(runFirstStep()).next(runSecondStep()).end().build();
	}

	@Bean
	public Step runFirstStep() {
		Jaxb2Marshaller reportUnmarshaller = new Jaxb2Marshaller();
		reportUnmarshaller.setClassesToBeBound(com.example.cat.adoption.model.Report.class);
		StaxEventItemReader xmlItemReader = new StaxEventItemReader();
		xmlItemReader.setFragmentRootElementName(RECORD);
		xmlItemReader.setResource(xmlReport);
		xmlItemReader.setStrict(false);
		xmlItemReader.setUnmarshaller(reportUnmarshaller);

		FlatFileItemWriter csvItemwriter = new FlatFileItemWriter();
		csvItemwriter.setResource(csvReport);
		csvItemwriter.setForceSync(false);
		csvItemwriter.setAppendAllowed(true);

		DelimitedLineAggregator lineAggregator = new DelimitedLineAggregator();
		lineAggregator.setDelimiter(",");
		BeanWrapperFieldExtractor fieldExtractor = new BeanWrapperFieldExtractor();
		fieldExtractor.setNames(NAMES);
		lineAggregator.setFieldExtractor(fieldExtractor);
		csvItemwriter.setLineAggregator(lineAggregator);


		return stepBuilderFactory.get("step1").<String, String> chunk(5)
				.reader(xmlItemReader).processor(new FilterReportProcessor())
				.writer(csvItemwriter).build();
	}

	@Bean
	public Step runSecondStep() {
		FlatFileItemReader reader = new FlatFileItemReaderBuilder<Record>()
				.name("employeeItemReader")
				.resource(csvReport)
				.delimited()
				.names(NAMES)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Record>() {{
					setTargetType(Record.class);
				}}).build();

		return stepBuilderFactory
				.get("step2").chunk(5)
				.reader(reader).processor(new FilterRecordProcessor())
				.writer(writer())
				.build();
	}

	@Bean
	public JdbcBatchItemWriter<Record> writer() {
		return new JdbcBatchItemWriterBuilder<Record>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Record>())
				.sql("INSERT INTO REPORT (refId, name, age, playfullness, readyToAdopt) VALUES (:refId, :name, :age, :playfullness, :readyToAdopt)")
				.dataSource(datasource)
				.build();
	}


	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

}
