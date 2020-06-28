package co.dsergio.rtree.api.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import co.dsergio.rtree.business.LocationItemService;
import rtree.item.ILocationItem;

@Controller
@RequestMapping("/api/location-item")
public class LocationItemController {
	
	@RequestMapping("/get/{id}")
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response) {
		
		int i = Integer.parseInt(request.getParameter("n1"));
		int j = Integer.parseInt(request.getParameter("n2"));
		int k = 0;
		
		LocationItemService locationItemService = new LocationItemService();
		ILocationItem item = locationItemService.get(1);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("result");
		mv.addObject("result", k);
		
		return mv;
	}
	
	@RequestMapping("/create")
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
		
		
		LocationItemService locationItemService = new LocationItemService();
		ILocationItem item = locationItemService.create("my type", 3);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("result");
		mv.addObject("result", item.getJson());
		
		return mv;
	}
}
