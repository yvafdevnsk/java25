import model.Person;
import service.PersonService;

public class MultiFileSourceCodePrograms {
    public static void main(String[] args) {
        PersonService service = new PersonService();
        Person person = service.createNewPerson();
        IO.println(person.printName() + " has been created!");
    }
}