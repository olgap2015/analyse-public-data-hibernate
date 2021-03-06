package com.olgaivancic.apdhibernate.controller;

import com.olgaivancic.apdhibernate.model.Country;
import com.olgaivancic.apdhibernate.model.Country.CountryBuilder;
import com.olgaivancic.apdhibernate.view.Prompter;
import com.olgaivancic.apdhibernate.view.ScreenPrinter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Processor {
    private SessionFactory sessionFactory;
    private List<Country> countries;
    private Prompter prompter;
    private ScreenPrinter screenPrinter;
    private Map<String, String> menu;


    public Processor(SessionFactory sessionFactory, ScreenPrinter screenPrinter, Prompter prompter) {
        this.sessionFactory = sessionFactory;
        this.countries = new ArrayList<>();
        this.screenPrinter = screenPrinter;
        this.prompter = prompter;
        this.menu = createMenu();
    }

    public void run() {
        // Welcome message
        System.out.println("\nWelcome to the World Bank Database!\n");

        fetchAllCountries();

        String choice = "";
        do {
            try {
                choice = prompter.promptAction(menu);

                switch (choice.toLowerCase()) {
                    case "1":
                        // Output the table view of the database
                        screenPrinter.outputTable(countries);
                        break;
                    case "2":
                        // Calculate and show statistics
                        calculateAndShowStatistics();
                        break;
                    case "3":
                        // Edit data for a particular country
                        editCountry();
                        fetchAllCountries();
                        break;
                    case "4":
                        // Add a new country to the database table
                        addCountry();
                        fetchAllCountries();
                        break;
                    case "5":
                        deleteCountry();
                        fetchAllCountries();
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

    private void deleteCountry() throws IOException {
        String countryCode = prompter.promptForCountry("delete", countries);
        Country country = findCountryById(countryCode);
        delete(country);
        System.out.println("\nThe country was successfully deleted.\n");
    }

    private void delete(Country country) {
        // Open session
        Session session = sessionFactory.openSession();

        // begin transaction
        session.beginTransaction();

        // Delete the contact
        session.delete(country);

        // commit the transaction
        session.getTransaction().commit();

        // close the session
        session.close();
    }

    @SuppressWarnings("unchecked")
//    private List<Country> fetchAllCountries() {
    private void fetchAllCountries() {
        // Open session
        Session session = sessionFactory.openSession();

        // Create criteria
        Criteria criteria = session.createCriteria(Country.class);

        // Get a list of contact objects according to the criteria object
        countries = criteria.list();
        countries.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

        // Close session
        session.close();

//        return countries;
    }

    private void addCountry() throws IOException {
        System.out.println("\nPlease, enter data for each field\n");
        String countryCode = prompter.promptForNewCountryCode();
        String countryName = prompter.promptForNewCountryName();
        Float internetUsers = prompter.promptForNewFloatValue("Internet Users");
        Float adultLiteracyRate = prompter.promptForNewFloatValue("Adult LiteracyRate");
        // Push above values into a new Country object
        Country newCountry = new CountryBuilder(countryName)
                .withCode(countryCode)
                .withInternetUsers(internetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
        // Save the country into the database
        save(newCountry);
        System.out.println("\nThe country was successfully added to the database.\n");
    }

    private void save(Country newCountry) {
        // Open the session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        session.save(newCountry);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private void editCountry() throws IOException {
        String countryCode = prompter.promptForCountry("edit", countries);
        Country country = findCountryById(countryCode);
        System.out.println("Here is what we have in the database about this country:\n");
        System.out.println(country.toString());
        System.out.println("Please, enter new data for each field for this country or retype the old data\n");
        String newCountryName = prompter.promptForNewCountryName();
        Float newInternetUsers = prompter.promptForNewFloatValue("Internet Users");
        Float newAdultLiteracyRate = prompter.promptForNewFloatValue("Adult LiteracyRate");
        // Set new values for the country
        country.setName(newCountryName);
        country.setInternetUsers(newInternetUsers);
        country.setAdultLiteracyRate(newAdultLiteracyRate);
        // Update the country in the database
        update(country);
        System.out.println("\nThe country was successfully updated in the database.\n");
    }

    private void update(Country country) {
        // open the session
        Session session = sessionFactory.openSession();

        // begin the transaction
        session.beginTransaction();

        // use the session to update the contact
        session.update(country);

        // commit the transaction
        session.getTransaction().commit();

        // close the session
        session.close();
    }

    private void calculateAndShowStatistics() {
        // Find countries with max and min for both indicators
        Map<Country, Float> countryWithMinInternetUsers = findCountryWithMinInternetUsers();
        Map<Country, Float> countryWithMinLiteracyRate = findCountryWithMinLiteracyRate();
        Map<Country, Float> countryWithMaxInternetUsers = findCountryWithMaxInternetUsers();
        Map<Country, Float> countryWithMaxLiteracyRate = findCountryWithMaxLiteracyRate();

        // Output header
        System.out.println("\nStatistics based on the data provided in the database\n");

        // Output minimums
        screenPrinter.outputMinOrMax("minimum", "amount of Internet Users", countryWithMinInternetUsers);
        screenPrinter.outputMinOrMax("minimum", "Adult Literacy Rate",countryWithMinLiteracyRate);

        // Output maximums
        screenPrinter.outputMinOrMax("maximum", "amount of Internet Users",countryWithMaxInternetUsers);
        screenPrinter.outputMinOrMax("maximum", "Adult Literacy Rate",countryWithMaxLiteracyRate);
        System.out.println();

        // Find countries that have all the data
        List<Country> countriesWithoutMissingData = findAllCountiesWithoutMissingData();

        // Form two separate lists with just indicators
        List<Float> listOfInternetUsersRates = getListOfInternetUsersRates(countriesWithoutMissingData);
        List<Float> listOfLiteracyRates = getListOfLiteracyRates(countriesWithoutMissingData);

        // Calculate the correlation coefficient
        double correlationCoefficient = calculateCorCoeff(listOfInternetUsersRates, listOfLiteracyRates);
//      double pearsonCoefficient = calculateCoeffThirdParty(listOfInternetUsersRates, listOfLiteracyRates);

        // output correlation coefficient
        screenPrinter.outputCorrelation(correlationCoefficient);
    }

    // Returns a country from a database based on the unique country id
    private Country findCountryById(String countryCode) {
        return countries.stream()
                .filter(country -> country.getCode().equals(countryCode))
                .findFirst()
                .orElseThrow(com.olgaivancic.apdhibernate.controller.NotFoundException::new);
    }

//     Calculates the coefficient using the third party Apache Commons library
//    private double calculateCoeffThirdParty(List<Float> list1, List<Float> list2) {
//        double[] list1AsArray = list1.stream()
//                .mapToDouble(f -> f != null ? f : Float.NaN)
//                .toArray();
//        double[] list2AsArray = list2.stream()
//                .mapToDouble(f -> f != null ? f : Float.NaN)
//                .toArray();
//        double corr = new PearsonsCorrelation().correlation(list1AsArray, list2AsArray);
//        return corr;
//    }

    private List<Country> findAllCountriesWithoutMissingLiteracyData() {
        return countries.stream()
                .filter(country -> country.getAdultLiteracyRate()!= null)
                .collect(Collectors.toList());
    }

    private List<Country> findAllCountriesWithoutMissingInternetUsersData() {
        return countries.stream()
                .filter(country -> country.getInternetUsers()!= null)
                .collect(Collectors.toList());
    }
    // Calculates correlation coefficient
    private double calculateCorCoeff(List<Float> list1, List<Float> list2) {
        double correlationCoefficient = 0;

        if (list1.size() == list2.size()) {
            // Size of the list of values
            float listSize = (float)list1.size();

            List<Float> list1MultiplList2 = new ArrayList<>();
            float sumOfTheMultiplicationList1ToList2 = 0;

            for(int i = 0; i < list1.size(); i++) {
                // Multiply corresponding elements of the list
                list1MultiplList2.add(list1.get(i) * list2.get(i));
                // Sum up the elements of the resulting list
                sumOfTheMultiplicationList1ToList2 = sumOfTheMultiplicationList1ToList2 + list1MultiplList2.get(i);
            }

            List<Float> squaresOfElementsList1 = new ArrayList<>();
            float sumOfSquaresList1 = 0;
            for(float element: list1) {
                // Square elements of list2
                float squareOfElements = element * element;
                squaresOfElementsList1.add(squareOfElements);
                // Sum up the elements of the resulting list
                sumOfSquaresList1 = sumOfSquaresList1 + squareOfElements;
            }

            List<Float> squaresOfElementsList2 = new ArrayList<>();
            float sumOfSquaresList2 = 0;
            for(float element: list2) {
                // Square elements of list2
                float squareOfElements = element * element;
                squaresOfElementsList2.add(squareOfElements);
                // Sum up the elements of the resulting list
                sumOfSquaresList2 = sumOfSquaresList2 + squareOfElements;
            }

            // Sum of the elements of list 1
            float sumOfList1 = 0;
            for (float element : list1) {
                sumOfList1 = sumOfList1 + element;
            }

            // Sum of the elements of list 2
            float sumOfList2 = 0;
            for (float element : list2) {
                sumOfList2 = sumOfList2 + element;
            }

            // Calculate the dividend of the correlation formula
            double dividend = (listSize * sumOfTheMultiplicationList1ToList2) - (sumOfList1 * sumOfList2);
            // Calculate the divider of the correlation formula
            double divider = Math.sqrt((listSize * sumOfSquaresList1 - sumOfList1 * sumOfList1) *
                    (listSize * sumOfSquaresList2 - sumOfList2 * sumOfList2));
            // Calculate the correlation coefficient
            correlationCoefficient = dividend / divider;

        } else return 0;
        return correlationCoefficient;
    }

    private static List<Float> getListOfLiteracyRates(List<Country> countries) {
        List<Float> listOfLiteracyRates = new ArrayList<>();
        countries.forEach(country -> listOfLiteracyRates.add(country.getAdultLiteracyRate()));
        return listOfLiteracyRates;
    }

    private static List<Float> getListOfInternetUsersRates(List<Country> countries) {
        List<Float> listOfInternetUsersRates = new ArrayList<>();
        countries.forEach(country -> listOfInternetUsersRates.add(country.getInternetUsers()));
        return listOfInternetUsersRates;
    }

    private List<Country> findAllCountiesWithoutMissingData() {
        return countries.stream().filter(country -> country.getInternetUsers()!= null &&
                country.getAdultLiteracyRate() != null).collect(Collectors.toList());
    }

    private Map<Country, Float> findCountryWithMinLiteracyRate() {
        Map<Country, Float> map = new HashMap<>();
        Comparator<Country> literacyRateComparator = Comparator.comparing(Country::getAdultLiteracyRate);
        List<Country> countries = findAllCountriesWithoutMissingLiteracyData();
        Country country = countries.stream().min(literacyRateComparator).get();
        String countryCode = country.getCode();
        // Add country and its rate to the map
        map.put(country, country.getAdultLiteracyRate());
        return map;
    }

    private Map<Country, Float> findCountryWithMaxLiteracyRate() {
        Map<Country, Float> map = new HashMap<>();
        Comparator<Country> literacyRateComparator = Comparator.comparing(Country::getAdultLiteracyRate);
        List<Country> countries = findAllCountriesWithoutMissingLiteracyData();
        Country country = countries.stream().max(literacyRateComparator).get();
        String countryCode = country.getCode();
        // Add country and its rate to the map
        map.put(country, country.getAdultLiteracyRate());
        return map;
    }

    private Map<Country, Float> findCountryWithMinInternetUsers() {
        Map<Country, Float> map = new HashMap<>();
        Comparator<Country> internetUsersComparator = Comparator.comparing(Country::getInternetUsers);
        List<Country> countries = findAllCountriesWithoutMissingInternetUsersData();
        Country country = countries.stream().min(internetUsersComparator).get();
        String countryCode = country.getCode();
        // Add country and its rate to the map
        map.put(country, country.getInternetUsers());
        return map;
    }

    private Map<Country, Float> findCountryWithMaxInternetUsers() {
        Map<Country, Float> map = new HashMap<>();
        Comparator<Country> internetUsersComparator = Comparator.comparing(Country::getInternetUsers);
        // List of coutries without missing Internet Users Data
        List<Country> countries = findAllCountriesWithoutMissingInternetUsersData();
        // Country with the max amount of Internet Users
        Country country = countries.stream().max(internetUsersComparator).get();
        // Get the code of that country
        String countryCode = country.getCode();
        // Add country and its rate to the map
        map.put(country, country.getInternetUsers());
        return map;
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
