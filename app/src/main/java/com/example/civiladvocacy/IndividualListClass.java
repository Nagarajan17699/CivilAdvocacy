package com.example.civiladvocacy;

import java.io.Serializable;

public class IndividualListClass implements Serializable {

    public String imgURL;
    public String name;
    public String position;
    public String party;
    public String address;
    public String phone;
    public String email;
    public String website;
    public String facebook;
    public String twitter;
    public String youtube;

    public IndividualListClass(String imgURL, String name, String position) {
        this.imgURL = imgURL;
        this.name = name;
        this.position = position;
    }

    public IndividualListClass(String imgURL, String name, String position, String party, String address,
                               String phone, String email, String website, String facebook, String twitter,
                               String youtube) {
        this.imgURL = imgURL;
        this.name = name;
        this.position = position;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    @Override
    public String toString() {
        return "IndividualListClass{" +
                "imgURL='" + imgURL + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
