package it.maxmin.domain.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class StateTest {

	@ParameterizedTest
	@ValueSource(strings = { "IT" })
	void testSameObjectEqualMethod(String code) {

		State state = State.newInstance().withId(1l).withName("Italy").withCode(code);

		Set<State> states = new HashSet<>();
		states.add(state);
		states.add(state);

		assertEquals(1, states.size());
	}

	@ParameterizedTest
	@CsvSource({ "IT, IT" })
	void testStatesEqualMethod(String code1, String code2) {

		State state1 = State.newInstance().withId(1l).withName("Italy").withCode(code1);

		State state2 = State.newInstance().withId(1l).withName("Italy").withCode(code2);

		Set<State> states = new HashSet<>();
		states.add(state1);
		states.add(state2);

		assertEquals(1, states.size());
	}

	@ParameterizedTest
	@CsvSource({ "IT, IE" })
	void testStatesNotEqualMethod(String code1, String code2) {

		State state1 = State.newInstance().withId(1l).withName("Italy").withCode(code1);

		State state2 = State.newInstance().withId(1l).withName("Italy").withCode(code2);

		Set<State> states = new HashSet<>();
		states.add(state1);
		states.add(state2);

		assertEquals(2, states.size());
	}

}
