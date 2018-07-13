package com.olgaivancic.apdhibernate.view;

import com.olgaivancic.apdhibernate.model.Country;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScreenPrinter {
    private List<Country> countries;

    public ScreenPrinter(List<Country> countries) {
        this.countries = countries;
    }

    public void outputMenu(Map<String,String> menu) {
        System.out.println("\tWhat would you like to do? Your options are:");

        for (Map.Entry<String, String> option : menu.entrySet()) {
            if (option.getKey().equals("quit")) {
                System.out.printf("\t%s - %s%n", option.getKey(), option.getValue());
            } else {
                System.out.printf("\t%s. - %s%n", option.getKey(), option.getValue());
            }
        }

    }

    public void outputTable() {
        // Output the header
        System.out.printf(Locale.US,"%n%-6s%-34s%14s%11s", "Code", "Country", "Internet Users", "Literacy\n");
        System.out.printf(Locale.US, "%50s%13s","per 100", "Rate\n");
        for (int i = 0; i < 64; i++) {
            System.out.print('-');
        }
        System.out.println();

        // Output every country line by line
        countries.forEach(country -> {
            String formattedInternetRate = "";
            String formattedLiteracyRate = "";

            if (country.getInternetUsers() == null) {
                formattedInternetRate = "--";
            } else {
                formattedInternetRate = String.format("%.2f", country.getInternetUsers());
            }

            if (country.getAdultLiteracyRate() == null) {
                formattedLiteracyRate = "--";
            } else {
                formattedLiteracyRate = String.format("%.2f", country.getAdultLiteracyRate());
            }

            System.out.printf(Locale.US,"%-6s%-34s%14s%10s%n",
                    country.getCode(),
                    country.getName(),
                    formattedInternetRate,
                    formattedLiteracyRate);
        });
        System.out.println("\n");
    }

    public void outputMinOrMax(String valueType, String rateTypeString, Map<Country, Float> mapCountryToRate) {
        Map.Entry<Country,Float> entry = mapCountryToRate.entrySet().iterator().next();
        Country country = entry.getKey();
        Float rate = entry.getValue();
        System.out.printf("%nCountry with the %s %s - %s (%.2f per 100 people).",
                valueType, rateTypeString, country.getName(), rate);
    }

    public void outputCorrelation(double correlationCoefficient) {
        System.out.printf("%nThe correlation efficient between the amount of internet users%n" +
                "and adult literacy rate is %.2f.%n", correlationCoefficient);
        System.out.println("***Please, note that only countries with complete data where used for " +
                "the calculations***\n");
    }
}
