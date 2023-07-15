package com.example.civiladvocacy;

import java.util.ArrayList;

public class Offices {

    public String name;
    public ArrayList<Integer> officIndices;

    public Offices(String name_, ArrayList<Integer> officeIndices) {
        name = name_;
        officIndices = officeIndices;
    }

    @Override
    public String toString() {
        return "Offices{" +
                "name='" + name + '\'' +
                ", officIndices=" + officIndices +
                '}';
    }
}
