package it.maxmin.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	
    @GetMapping(path = "home")
    public String home(Model model) {
    	LOGGER.debug("CALLED HELLO ENDPOINT");
        model.addAttribute("message", "Spring MVC JspExample!!");
        return "home";
    }
}
