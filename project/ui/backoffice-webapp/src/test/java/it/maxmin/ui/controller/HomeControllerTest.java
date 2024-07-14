package it.maxmin.ui.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import it.maxmin.ui.config.WebConfig;

@SpringJUnitWebConfig(classes = WebConfig.class)
public class HomeControllerTest {

	MockMvc mockMvc;

	public HomeControllerTest(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testHome() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/home")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("home"))
				.andExpect(forwardedUrl("/WEB-INF/views/home.jsp"))
				.andExpect(model().attribute("message", is("Spring MVC JspExample!!")));
	}
}
