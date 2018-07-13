package com.olgaivancic.apdhibernate.controller;

import com.olgaivancic.apdhibernate.model.Country;
import com.olgaivancic.apdhibernate.view.Prompter;
import com.olgaivancic.apdhibernate.view.ScreenPrinter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a session factory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {

        List<Country> countries = fetchAllCountries();

//        countries.forEach(System.out::println);

        ScreenPrinter screenPrinter = new ScreenPrinter(countries);
        Prompter prompter = new Prompter(countries, screenPrinter);
        Processor processor = new Processor(countries, prompter, screenPrinter, sessionFactory);
        processor.run();

        // TODO: not sure if this is the right place to close the session factory
        sessionFactory.close();
    }

    // TODO: implement this method, return an ArrayList of countries from the database
    @SuppressWarnings("unchecked")
    private static List<Country> fetchAllCountries() {
        // Open session
        Session session = sessionFactory.openSession();

        // Create criteria
        Criteria criteria = session.createCriteria(Country.class);

        // Get a list of contact objects according to the criteria object
        List<Country> countries = criteria.list();
        countries.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

        // Close session
        session.close();

        return countries;
    }
}
