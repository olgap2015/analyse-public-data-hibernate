package com.olgaivancic.apdhibernate.view;

import com.olgaivancic.apdhibernate.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class Prompter {

    private List<Country> countries;
    private ScreenPrinter screenPrinter;
    private BufferedReader reader;

    public Prompter(List<Country> countries, ScreenPrinter screenPrinter) {
        this.countries = countries;
        this.screenPrinter = screenPrinter;
        this.reader = new BufferedReader(new InputStreamReader(System.in));

    }

    public String promptAction(Map<String, String> menu) throws IOException {
        String choice = "";
        do {
            screenPrinter.outputMenu(menu);
            choice = reader.readLine();
        } while (choice.length() == 0);
        return choice;
    }
}
