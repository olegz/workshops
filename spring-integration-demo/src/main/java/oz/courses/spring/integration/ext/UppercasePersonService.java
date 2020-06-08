package oz.courses.spring.integration.ext;

public class UppercasePersonService {

	public Person uppercase(Person incomingPerson) {
		Person person = new Person();
		person.setName(incomingPerson.getName().toUpperCase());
		return person;
	}
}
