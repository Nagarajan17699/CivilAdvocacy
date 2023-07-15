package com.example.civiladvocacy;

import java.util.ArrayList;
import java.util.HashMap;

public class Officials {

    public String name;
    public HashMap<String, String> address;
    public String party;
    public String phoneNumber;
    public String url;
    public String email;
    public String photoUrl;
    public HashMap<String, String> channels;

    public Officials(String name, HashMap<String, String> address, String party, String phoneNumber, String url,
                     String email, String photoUrl, HashMap<String, String> channels) {
        this.name = name;
        this.address = address;
        this.party = party;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Officials{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", party='" + party + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", channels=" + channels +
                '}';
    }
}
