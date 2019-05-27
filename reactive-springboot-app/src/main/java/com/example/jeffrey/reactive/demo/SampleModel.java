package com.example.jeffrey.reactive.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleModel {

    private String id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    public SampleModel() { }

    public SampleModel(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }


}
