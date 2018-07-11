package com.olgaivancic.apdhibernate.controller;

import com.olgaivancic.apdhibernate.model.Country;
import com.olgaivancic.apdhibernate.view.Prompter;
import com.olgaivancic.apdhibernate.view.ScreenPrinter;

import java.util.List;

public class Processor {
    private final List<Country> countries;
    private final Prompter prompter;
    private final ScreenPrinter screenPrinter;

    public Processor(List<Country> countries, Prompter prompter, ScreenPrinter screenPrinter) {
        this.countries = countries;
        this.prompter = prompter;
        this.screenPrinter = screenPrinter;
    }

    // TODO: implement this method being the driver of the application
    public void run() {

    }

    // Welcome message

    // Ask to login???

    // Output the menu

}
