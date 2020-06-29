package co.dsergio.rtree_api.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import co.dsergio.rtree.business.LocationItemService;
import rtree.item.ILocationItem;

@Controller
@RequestMapping("/item")
public class LocationItemController {
	
	@RequestMapping(value="/getView/{id}", method = RequestMethod.GET)
	public ModelAndView getView(@PathVariable int id) {
		
		
		LocationItemService locationItemService = new LocationItemService();
		ILocationItem item = locationItemService.get(id);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("locationItemView");
		
		if (item != null) {
			mv.addObject("locationItem", item.getJson().toJSONString());
		} else {
			mv.addObject("locationItem", "not found");
		}
		
		return mv;
	}
	
	@RequestMapping(value="/createView", method = RequestMethod.GET)
	public ModelAndView createView(HttpServletRequest request, HttpServletResponse response) {
		
		
		LocationItemService locationItemService = new LocationItemService();
		ILocationItem item = locationItemService.create("my type", 3);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("locationItemView");
		mv.addObject("locationItem", item.getJson());
		
		return mv;
	}
	
	@RequestMapping(value="/create", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String create(HttpServletRequest request, HttpServletResponse response) {
		
		
		LocationItemService locationItemService = new LocationItemService();
		ILocationItem item = locationItemService.create("my type", 3);
		item.setDim(0, 10);
		item.setDim(1, 11);
		item.setDim(2, 12);
		
		return item.getJson().toJSONString();
	}
	
	
}
