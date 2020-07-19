package com.dsergio.rtreeapiboot;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.dsergio.rtree.business.dto.RTree;
import co.dsergio.rtree.business.services.RTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;
import rtree.tree.IRTree;

@RestController
@RequestMapping("api/RTree")
@Api(tags = "RTree")
public class RTreeController {
	
	
	@ApiOperation(value="RTree_getAll", notes = "Get all RTree structures from metadata", nickname = "RTree_getAll")
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "https://localhost:44381")
	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<RTree>> get(HttpServletRequest request, HttpServletResponse response) {
		
		RTreeService rtreeService = new RTreeService();
		List<IRTree> trees = rtreeService.fetchAll();
		
		JSONArray jsonArray = new JSONArray();
		
		List<RTree> returnRTreeList = new ArrayList<RTree>();
		
		for (IRTree t : trees) {
			jsonArray.add(t.getJson());
			
			RTree tree = new RTree(t.getTreeName(), t.getNumDimensions(), null, null);
			returnRTreeList.add(tree);
		}
		
		
		
		return ResponseEntity.ok(returnRTreeList);
		
//		return jsonArray.toJSONString();
	}
	
	@ApiOperation(value="RTree_get", notes = "Get RTree structure by treeName", nickname = "RTree_get")
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "https://localhost:44381")
	@RequestMapping(value="/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTree> get(@PathVariable String treeName) {
		
		
		RTreeService rtreeService = new RTreeService();
		IRTree tree = rtreeService.fetchByTreeName(treeName);
		
		JSONObject ret = new JSONObject();
		
		if (tree != null) {
			
			
			RTree treeRet = new RTree(tree.getTreeName(), tree.getNumDimensions(), tree.getRectangles(), tree.getPoints());
			return ResponseEntity.ok(treeRet);
			
			
//			List<ILocationItem> points = tree.getPoints();
//			
//			if (points.size() > 0) {
//				ILocationItem i = points.get(0);
//				int N = i.getNumberDimensions();
//				ret.put("N", N);
//			} else {
//				ret.put("N", 0);
//			}
//			
//			List<IHyperRectangle> rectangles = tree.getRectangles();
//			
//			JSONArray arr = new JSONArray();
//			for (ILocationItem item : points) {
//				arr.add(item.getJson());
//			}
//			ret.put("points", arr);
//			
//			arr = new JSONArray();
//			for (IHyperRectangle r : rectangles) {
//				arr.add(r.getJson());
//			}
//			ret.put("rectangles", arr);
//			
//			return ret.toJSONString();
		}
		
		return null;
	}
}
