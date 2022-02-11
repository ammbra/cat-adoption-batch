package com.example.cat.adoption.model;


public class Record {
    private int refId;
    private String name;
    private int age;
    private String dob;
    private Boolean readyToAdopt;
    private String playfullness;

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public Boolean getReadyToAdopt() {
        return readyToAdopt;
    }

    public void setReadyToAdopt(Boolean readyToAdopt) {
        this.readyToAdopt = readyToAdopt;
    }

    public String getPlayfullness() {
        return playfullness;
    }

    public void setPlayfullness(String playfullness) {
        this.playfullness = playfullness;
    }
}
