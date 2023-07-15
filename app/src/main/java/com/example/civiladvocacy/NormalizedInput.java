package com.example.civiladvocacy;

public class NormalizedInput {

    public String line1;
    public String city;
    public String state;
    public String zip;

    public NormalizedInput(String line1, String city, String state, String zip) {
        this.line1 = line1;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "NormalizedInput{" +
                "line1='" + line1 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
