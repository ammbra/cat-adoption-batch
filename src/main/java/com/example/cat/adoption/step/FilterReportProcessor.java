package com.example.cat.adoption.step;

import com.example.cat.adoption.model.Report;
import org.springframework.batch.item.ItemProcessor;

public class FilterReportProcessor implements ItemProcessor<Report, Report> {

	@Override
	public Report process(Report item) throws Exception {
		if(item.getAge()==0){
			return null;
		}
		return item;
	}

}