package com.dsergio.rtreeapiboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/")
@ApiIgnore
public class HomeController {
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/swagger-ui/index.html";
	}
}
