package com.olgaivancic.apdhibernate.view;

import com.olgaivancic.apdhibernate.model.Country;

import java.util.List;

public class ScreenPrinter {
    private final List<Country> countries;

    public ScreenPrinter(List<Country> countries) {
        this.countries = countries;
    }
}
