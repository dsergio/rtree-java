package com.dsergio.rtreeapiboot.controllers;

import java.util.ArrayList;
import java.util.HashMap;
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

import co.dsergio.rtree.business.dto.LocationItemDouble;
import co.dsergio.rtree.business.dto.RTreeCreate;
import co.dsergio.rtree.business.dto.RTreeDouble;
import co.dsergio.rtree.business.dto.RectangleDouble;
import co.dsergio.rtree.business.services.RTreeService;
import co.dsergio.rtree.business.services.generic.RTreeServiceBaseGeneric;
import co.dsergio.rtree.business.services.generic.RTreeServiceDouble;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rtree.item.ILocationItem;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTree;

@RestController
@RequestMapping("api/RTreeDouble")
@Api(tags = "RTreeDouble")
public class RTreeDoubleController extends RTreeControllerBaseGeneric<RDouble> {
	
	public RTreeDoubleController() {
		super(new RTreeServiceDouble());
	}
	
	@ApiOperation(value="RTreeDouble_getAll", notes = "Get all RTree<Double> structures from metadata", nickname = "RTreeDouble_getAll")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<RTreeDouble>> get(HttpServletRequest request, HttpServletResponse response) {
		
		List<IRTree<RDouble>> trees = rtreeService.fetchAll();
		
		List<RTreeDouble> returnRTreeList = new ArrayList<RTreeDouble>();
		
		for (IRTree<RDouble> t : trees) {
			
			RTreeDouble tree = new RTreeDouble(t.getTreeName(), t.getNumDimensions(), null, null);
			returnRTreeList.add(tree);
		}
		
		return ResponseEntity.ok(returnRTreeList);
	}
	
	@ApiOperation(value="RTreeDouble_insert", notes = "Insert into RTree<Double", nickname = "RTreeDouble_insert")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> insert(@PathVariable String treeName, @RequestBody LocationItemDouble itemToInsert) {
		
		ILocationItem<RDouble> item = new LocationItem<RDouble>(itemToInsert.numberDimensions);
		item.setType(itemToInsert.type);
		for (String s : itemToInsert.itemProperties.keySet()) {
			item.setProperty(s, itemToInsert.itemProperties.get(s));
		}
		for (int i = 0; i < itemToInsert.numberDimensions; i++) {
			Double d = itemToInsert.dimensionArray.get(i);
			RDouble rd = new RDouble(d);
			item.setDim(i, rd);
		}
		
		rtreeService.insert(treeName, item);
		
		IRTree<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			RTreeDouble treeRet = new RTreeDouble();
			treeRet.name = tree.getTreeName();
			treeRet.numDimensions = tree.getNumDimensions();
			
			return ResponseEntity.ok(treeRet);
		}
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeDouble_get", notes = "Get RTree<Double> structure and data by treeName", nickname = "RTreeDouble_get")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> get(@PathVariable String treeName) {
		
		IRTree<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			
			RTreeDouble treeRet = new RTreeDouble();
			treeRet.rectangles = new ArrayList<>();
			treeRet.points = new ArrayList<>();
			
			for (IHyperRectangle<RDouble> r : tree.getAllRectangles()) {
				RectangleDouble rectDouble = new RectangleDouble();
				rectDouble.dimensionArray1 = new ArrayList<Double>();
				rectDouble.dimensionArray2 = new ArrayList<Double>();
				for (RDouble rd : r.getDimensionArray1()) {
					rectDouble.dimensionArray1.add(rd.getData());
				}
				for (RDouble rd : r.getDimensionArray2()) {
					rectDouble.dimensionArray2.add(rd.getData());
				}
				rectDouble.numberDimensions = r.getNumberDimensions();
				treeRet.rectangles.add(rectDouble);
			}
			for (ILocationItem<RDouble> item : tree.getAllPoints()) {
				LocationItemDouble i = new LocationItemDouble();
				i.dimensionArray = new ArrayList<Double>();
				i.type = item.getType();
				i.id = item.getId();
				i.itemProperties = item.getProperties();
				i.numberDimensions = item.getNumberDimensions();
				for (RDouble rd : item.getDimensionArray()) {
					i.dimensionArray.add(rd.getData());
				};
				treeRet.points.add(i);
			}
			
			return ResponseEntity.ok(treeRet);
			
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeDouble_search", notes = "Search RTree<Double> structure by rectangle", nickname = "RTreeDouble_search")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/search/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<LocationItemDouble>> search(
			@PathVariable String treeName, @RequestBody RectangleDouble searchRectangleInput) {
		
		IHyperRectangle<RDouble> searchRectangle = new HyperRectangle<RDouble>(searchRectangleInput.numberDimensions);
		for (int i = 0; i < searchRectangleInput.numberDimensions; i++) {
			RDouble rd1 = new RDouble(searchRectangleInput.dimensionArray1.get(i));
			searchRectangle.setDim1(i, rd1);
			RDouble rd2 = new RDouble(searchRectangleInput.dimensionArray2.get(i));
			searchRectangle.setDim2(i, rd2);
		}
		
		Map<IHyperRectangle<RDouble>, List<ILocationItem<RDouble>>> results = rtreeService.search(treeName, searchRectangle);
		
		List<LocationItemDouble> searchResults = new ArrayList<LocationItemDouble>();
		
		if (results != null) {
			
			for (IHyperRectangle<RDouble> r : results.keySet()) {
				List<ILocationItem<RDouble>> items = results.get(r);
				
				RectangleDouble eachR = new RectangleDouble();

				for (ILocationItem<RDouble> item : items) {
					LocationItemDouble i = new LocationItemDouble();
					i.type = item.getType();
					i.id = item.getId();
					i.itemProperties = item.getProperties();
					i.numberDimensions = item.getNumberDimensions();
					i.dimensionArray = new ArrayList<Double>();
					for (RDouble rd : item.getDimensionArray()) {
						i.dimensionArray.add(rd.getData());
					};
					searchResults.add(i);
				}
				
			}
			
			return ResponseEntity.ok(searchResults);
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeDouble_newTree", notes = "Create new RTree<Double>", nickname = "RTreeDouble_newTree")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> create(@RequestBody RTreeCreate rtreeCreate) {
		
		IRTree<RDouble> t = rtreeService.create(rtreeCreate);
		
		RTreeDouble treeRet = null;
		
		if (t != null) {
			treeRet = new RTreeDouble(t.getTreeName(), t.getNumDimensions(), null, null);
			return ResponseEntity.ok(treeRet);
		}
		
		return ResponseEntity.ok(treeRet);
	}
	
	@ApiOperation(value="RTreeDouble_delete", notes = "Delete RTree<Double> by treeName", nickname = "RTreeDouble_delete")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<String>> delete(@PathVariable String treeName) {
		
		List<String> ret = new ArrayList<String>();
		
		try {
			rtreeService.delete(treeName);
			
			ret.add("Tree " + treeName + " deleted successfully");
			return ResponseEntity.ok(ret);
		} catch (Exception e) {
			ret.add("Error deleting tree: " + e.getMessage());
			return ResponseEntity.status(500).body(ret);
		}
	}
}
