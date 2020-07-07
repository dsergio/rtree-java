package co.dsergio.rtree_api.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.dsergio.rtree.business.RTreeService;
import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;
import rtree.tree.IRTree;

@Controller
@RequestMapping("/rtree")
public class RTreeController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String get(HttpServletRequest request, HttpServletResponse response) {
		
		
		RTreeService rtreeService = new RTreeService();
		List<IRTree> trees = rtreeService.fetchAll();
		
		JSONArray jsonArray = new JSONArray();
		
		for (IRTree t : trees) {
			jsonArray.add(t.getJson().toJSONString());
		}
		
		return jsonArray.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/get/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String get(@PathVariable String treeName) {
		
		
		RTreeService rtreeService = new RTreeService();
		IRTree tree = rtreeService.fetchByTreeName(treeName);
		
		JSONObject ret = new JSONObject();
		
		if (tree != null) {
			List<ILocationItem> points = tree.getPoints();
			
			if (points.size() > 0) {
				ILocationItem i = points.get(0);
				int N = i.getNumberDimensions();
				ret.put("N", N);
			} else {
				ret.put("N", 0);
			}
			
			List<IHyperRectangle> rectangles = tree.getRectangles();
			
			JSONArray arr = new JSONArray();
			for (ILocationItem item : points) {
				arr.add(item.getJson());
			}
			ret.put("points", arr);
			
			arr = new JSONArray();
			for (IHyperRectangle r : rectangles) {
				arr.add(r.getJson());
			}
			ret.put("rectangles", arr);
			
			return ret.toJSONString();
		}
		
		return null;
	}
}
