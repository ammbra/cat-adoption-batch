package com.example.cat.adoption.adapter;


import javax.xml.bind.annotation.adapters.XmlAdapter;


public class JaxbBooleanAdapter extends XmlAdapter<String, Boolean> {

	public static final String YES = "yes";
	public static final String NO = "no";


	@Override
	public Boolean unmarshal(String s) throws Exception {
		return YES.equals(s);
	}


	@Override
	public String marshal(Boolean b) throws Exception {
		if (b) {
			return YES;
		}
		return NO;
	}


}