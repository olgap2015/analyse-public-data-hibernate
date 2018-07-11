package com.olgaivancic.apdhibernate.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class Country {

    @Id
    private String code;

    @Column
    private String name;

    @Column
    float internetUsers;

    @Column
    float adultLiteracyRate;

    // default constructor
    public Country() {}

    public Country (CountryBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(float internetUsers) {
        this.internetUsers = internetUsers;
    }

    public float getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(float adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        float internetUsers;
        float adultLiteracyRate;

        public CountryBuilder(String name) {
            this.name = name;
        }

        public CountryBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public CountryBuilder withInternetUsers(float internetUsers) {
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(float adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }
}
