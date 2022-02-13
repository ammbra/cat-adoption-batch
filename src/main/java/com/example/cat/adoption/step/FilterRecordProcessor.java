package com.example.cat.adoption.step;

import org.springframework.batch.item.ItemProcessor;
import com.example.cat.adoption.model.Record;

public class FilterRecordProcessor implements ItemProcessor<Record, Record> {

	@Override
	public Record process(Record item) {
		if (!item.getReadyToAdopt())
			return null;
		return item;
	}
}