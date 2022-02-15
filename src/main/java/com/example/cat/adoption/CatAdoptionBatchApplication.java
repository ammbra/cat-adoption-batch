package com.example.cat.adoption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"com.example.*"})
public class CatAdoptionBatchApplication implements CommandLineRunner {
	public static final String TIME = "time";
	public static final String BATCH = "BATCH_";
	public static final String ISOLATION_READ_COMMITTED = "ISOLATION_READ_COMMITTED";
	private static Logger LOGGER = LoggerFactory
			.getLogger(CatAdoptionBatchApplication.class);

	@Autowired
	Job processJob;

	@Autowired
    DataSource datasource;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private ApplicationContext context;

	@Bean(name = "launcher")
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}


	private JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(datasource);
		factory.setTablePrefix(BATCH);
		factory.setIsolationLevelForCreate(ISOLATION_READ_COMMITTED);
		factory.setDatabaseType(DatabaseType.H2.getProductName());
		factory.setTransactionManager(platformTransactionManager);
		factory.afterPropertiesSet();
		return ((JobRepository) factory.getObject());
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CatAdoptionBatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Done");
		SpringApplication.exit(context);
	}
}
