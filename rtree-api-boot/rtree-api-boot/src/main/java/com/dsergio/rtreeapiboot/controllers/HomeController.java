package com.dsergio.rtreeapiboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Hidden;


@Controller
@RequestMapping("/")
@Hidden
public class HomeController {
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/swagger-ui/index.html";
	}
}
