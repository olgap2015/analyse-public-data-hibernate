package com.olgaivancic.apdhibernate.view;

import com.olgaivancic.apdhibernate.model.Country;

import java.util.List;

public class Prompter {

    private List<Country> countries;

    public Prompter(List<Country> countries) {
        this.countries = countries;
    }
}
