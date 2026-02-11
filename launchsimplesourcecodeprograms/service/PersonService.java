package service;

import model.Person;

public class PersonService {
    public Person createNewPerson() {
        return new Person("Mizuki");
    }
}