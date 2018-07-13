package com.olgaivancic.apdhibernate.view;

import com.olgaivancic.apdhibernate.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public String promptForCountry(String action) throws IOException {
        String countryCode = "";
        List<String> listOfCountryCodes = new ArrayList<>();
        countries.forEach(country -> {
            listOfCountryCodes.add(country.getCode());
        });
        boolean codeMatches;
        do {
            System.out.println("\nPlease, enter the unique code of the country that " +
                    "you'd like to " + action + ":\n");
            countryCode = reader.readLine().toUpperCase();
            codeMatches = listOfCountryCodes.contains(countryCode);
            if (codeMatches == false) {
                System.out.println("\nThe database doesn't contain the code you entered! " +
                        "Please, try again.\n");
            }
        } while (codeMatches == false);
        return countryCode;
    }

    public String promptForNewCountryName() throws IOException {
        String newCountryName = "";
        boolean nameIsTooLong;
        do {
            System.out.println("\nNew country name: ");
            newCountryName = reader.readLine();
            nameIsTooLong = newCountryName.length() > 32;
            if (nameIsTooLong) {
                System.out.println("\nThe country name can't contain more than 32 characters! " +
                        "Please, try again.\n");
            }
        } while (newCountryName.isEmpty() || nameIsTooLong);
        return newCountryName;
    }

    public Float promptForNewFloatValue(String valueName) throws IOException {
        float newFloatValue;
        do {
            String floatValueString = "";
            System.out.printf("%nNew %s (for null value just press ENTER): ", valueName);
            try {
                floatValueString = reader.readLine();
            } catch (IOException ioe) {
                System.out.println("Unable to read your input. Please, try again!");
            }
            if (floatValueString.toLowerCase().equals("")) {
                return null;
            }
            newFloatValue = Float.valueOf(floatValueString);
            if (newFloatValue < 0.00 || newFloatValue > 100.00) {
                System.out.println("\nThe value has to be positive and not more that 100! Try again.\n");
            }
        } while (newFloatValue < 0.00 || newFloatValue > 100.00);
        return newFloatValue;
    }

    public String promptForNewCountryCode() throws IOException {
        String countryCode = "";
        List<String> listOfCountryCodes = new ArrayList<>();
        countries.forEach(country -> {
            listOfCountryCodes.add(country.getCode());
        });
        boolean codeExists;
        boolean codeIsTooLong;
        boolean codeIsOnlyLetters;
        do {
            System.out.println("\nNew country code: ");
            countryCode = reader.readLine().toUpperCase();
            codeExists = listOfCountryCodes.contains(countryCode);
            codeIsTooLong = countryCode.length() > 3;
            codeIsOnlyLetters = countryCode.matches("[a-zA-Z]+");
            if (codeIsTooLong || !codeIsOnlyLetters) {
                System.out.println("\nThe code has to contain 3 letters! Please, try again!\n");
            }
            if (codeExists) {
                System.out.println("\nThe database already contains a country with this code! " +
                        "Please, try again.\n");
            }
        } while (codeIsTooLong || codeExists || countryCode.isEmpty() || !codeIsOnlyLetters);
        return countryCode;
    }
}
