package com.example.jeffrey.reactive.demo;

public class Customer {

    private String id;
    private String firstName;
    private String lastName;

    public Customer() {
        // must provide an empty constructor to avoid Jackson Databind error in WebClient
    }

    public Customer(String id, String firstName, String lastName) {
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
