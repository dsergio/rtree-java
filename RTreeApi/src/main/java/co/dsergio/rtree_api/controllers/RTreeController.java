package co.dsergio.rtree_api.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.dsergio.rtree.business.RTreeService;
import rtree.tree.IRTree;

@Controller
@RequestMapping("/rtree")
public class RTreeController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String create(HttpServletRequest request, HttpServletResponse response) {
		
		
		RTreeService rtreeService = new RTreeService();
		List<IRTree> trees = rtreeService.fetchAll();
		
		JSONArray jsonArray = new JSONArray();
		
		for (IRTree t : trees) {
			jsonArray.add(t.getJson().toJSONString());
		}
		
		return jsonArray.toJSONString();
	}
}
