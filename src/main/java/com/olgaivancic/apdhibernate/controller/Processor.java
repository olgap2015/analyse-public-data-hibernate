package com.olgaivancic.apdhibernate.controller;

import com.olgaivancic.apdhibernate.model.Country;
import com.olgaivancic.apdhibernate.view.Prompter;
import com.olgaivancic.apdhibernate.view.ScreenPrinter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Processor {
    private List<Country> countries;
    private Prompter prompter;
    private ScreenPrinter screenPrinter;
    private Map<String, String> menu;
    private SessionFactory sessionFactory;


    public Processor(List<Country> countries, Prompter prompter, ScreenPrinter screenPrinter, SessionFactory sessionFactory) {
        this.countries = countries;
        this.prompter = prompter;
        this.screenPrinter = screenPrinter;
        this.menu = createMenu();
        this.sessionFactory = sessionFactory;
    }

    // TODO: implement this method being the driver of the application
    public void run() {
        // Welcome message
        System.out.println("Welcome to the World Bank Database!\n");

        String choice = "";
        do {
            try {
                choice = prompter.promptAction(menu);

                switch (choice.toLowerCase()) {
                    case "1":
                        // TODO: output a well formatted table of all countries
                        screenPrinter.outputTable();
                        break;
                    case "2":
                        // TODO: output statistics
                        break;
                    case "3":
                        // TODO: Edit a country in the database
                        break;
                    case "4":
                        // TODO: add a country to the database
                        break;
                    case "5":
                        // TODO: Delete a country
                        break;
                    case "quit":
                        System.out.println("Thanks for using the World Bank Database. Bye!");
                        break;
                    default:
                        System.out.printf("Unknown choice: \"%s\". Please, try again!  %n%n",
                                choice);
                }
            } catch (IOException ioe) {
                System.out.println("Unable to read your input. Please, try again!");
            }
        } while (!choice.equals("quit"));
    }

    private static Map<String, String> createMenu() {
        Map<String, String> menu = new TreeMap<>();
        menu.put("1", "VIEW a complete TABLE of the countries");
        menu.put("2", "VIEW STATISTICS based on the data from the database");
        menu.put("3", "EDIT data of the particular country");
        menu.put("4", "ADD a country to the database");
        menu.put("5", "DELETE a country from the database");
        menu.put("quit", "QUIT the program");
        return menu;
    }


}
