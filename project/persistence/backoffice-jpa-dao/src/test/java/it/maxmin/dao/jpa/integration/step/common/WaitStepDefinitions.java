package it.maxmin.dao.jpa.integration.step.common;

import static java.util.concurrent.CompletableFuture.delayedExecutor;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.cucumber.java.en.When;

public class WaitStepDefinitions {

	@When("I wait a little")
	public void wait_a_little() {
		runAsync(() -> {
		}, delayedExecutor(2000, MILLISECONDS)).join();
	}
}
