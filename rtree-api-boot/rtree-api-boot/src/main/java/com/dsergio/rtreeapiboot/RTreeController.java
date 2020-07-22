package com.dsergio.rtreeapiboot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.dsergio.rtree.business.dto.LocationItem;
import co.dsergio.rtree.business.dto.RTree;
import co.dsergio.rtree.business.dto.RTreeCreate;
import co.dsergio.rtree.business.dto.SearchRectangle;
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<RTree>> get(HttpServletRequest request, HttpServletResponse response) {
		
		RTreeService rtreeService = new RTreeService();
		List<IRTree> trees = rtreeService.fetchAll();
		
		List<RTree> returnRTreeList = new ArrayList<RTree>();
		
		for (IRTree t : trees) {
			
			RTree tree = new RTree(t.getTreeName(), t.getNumDimensions(), null, null);
			returnRTreeList.add(tree);
		}
		
		return ResponseEntity.ok(returnRTreeList);
	}
	
	@ApiOperation(value="RTree_insert", notes = "Insert into RTree", nickname = "RTree_insert")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTree> insert(@PathVariable String treeName, @RequestBody LocationItem item) {
		
		item.itemId = null;
		item.itemType = null;
		
		RTreeService rtreeService = new RTreeService();
		rtreeService.insert(treeName, item);
		
		
		IRTree tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			RTree treeRet = new RTree(tree.getTreeName(), tree.getNumDimensions(), tree.getRectangles(), tree.getPoints());
			return ResponseEntity.ok(treeRet);
		}
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTree_get", notes = "Get RTree structure by treeName", nickname = "RTree_get")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTree> get(@PathVariable String treeName) {
		
		RTreeService rtreeService = new RTreeService();
		IRTree tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			
			RTree treeRet = new RTree(tree.getTreeName(), tree.getNumDimensions(), tree.getRectangles(), tree.getPoints());
			return ResponseEntity.ok(treeRet);
			
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTree_search", notes = "Search RTree structure by rectangle", nickname = "RTree_search")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/search/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<IHyperRectangle, List<ILocationItem>>> search(@PathVariable String treeName, @RequestBody SearchRectangle searchRectangle) {
		
		RTreeService rtreeService = new RTreeService();
		
		Map<IHyperRectangle, List<ILocationItem>> results = rtreeService.search(treeName, searchRectangle);
		
		if (results != null) {
			
			return ResponseEntity.ok(results);
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTree_newTree", notes = "Create new RTree", nickname = "RTree_newTree")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTree> create(@RequestBody RTreeCreate rtreeCreate) {
		
		RTreeService rtreeService = new RTreeService();
		
		IRTree t = rtreeService.create(rtreeCreate);
		
		RTree treeRet = null;
		
		if (t != null) {
			treeRet = new RTree(t.getTreeName(), t.getNumDimensions(), null, null);
			return ResponseEntity.ok(treeRet);
		}
		
		return ResponseEntity.ok(treeRet);
	}
}
