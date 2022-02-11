package com.example.cat.adoption.model;

import com.example.cat.adoption.adapter.JaxbBooleanAdapter;
import com.example.cat.adoption.adapter.JaxbDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlRootElement(name = "record")
public class Report {

	private int refId;
	private String name;
	private int age;
	private Date dob;
	private Boolean readyToAdopt;
	private String playfullness;

	@XmlAttribute
	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	@XmlElement
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlJavaTypeAdapter(JaxbDateAdapter.class)
	@XmlElement
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@XmlElement
	public String getPlayfullness() {
		return playfullness;
	}

	public void setPlayfullness(String playfullness) {
		this.playfullness = playfullness;
	}

	@XmlJavaTypeAdapter(JaxbBooleanAdapter.class)
	@XmlElement
	public Boolean getReadyToAdopt() {
		return readyToAdopt;
	}

	public void setReadyToAdopt(Boolean readyToAdopt) {
		this.readyToAdopt = readyToAdopt;
	}

	public String getCsvDob() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(getDob());

	}

}