package it.maxmin.model.jdbc.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AddressTest {
	
	@ParameterizedTest
	@ValueSource(strings = {"30010"})
	void test_same_object_equal(String postalCode) {

		Address address = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode);
		
		Set<Address> addresses = new HashSet<>();
		addresses.add(address);
		addresses.add(address);

		assertEquals(1, addresses.size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 30030" })
	void test_addresses_equal(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);

		assertEquals(1, addresses.size());
	}

	@ParameterizedTest
	@CsvSource({ "54123, 30030" })
	void test_addresses_not_equal(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);

		assertEquals(2, addresses.size());
	}

}