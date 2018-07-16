package com.olgaivancic.apdhibernate.controller;

import com.olgaivancic.apdhibernate.view.Prompter;
import com.olgaivancic.apdhibernate.view.ScreenPrinter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class Application {
    // Hold a reusable reference to a session factory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        ScreenPrinter screenPrinter = new ScreenPrinter();
        Prompter prompter = new Prompter(screenPrinter);
        Processor processor = new Processor(sessionFactory, screenPrinter, prompter);
        processor.run();

        sessionFactory.close();
    }
}
