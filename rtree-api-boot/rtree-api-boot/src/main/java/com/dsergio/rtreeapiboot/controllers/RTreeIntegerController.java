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
import co.dsergio.rtree.business.dto.LocationItemInteger;
import co.dsergio.rtree.business.dto.RTreeCreate;
import co.dsergio.rtree.business.dto.RTreeDouble;
import co.dsergio.rtree.business.dto.RTreeInteger;
import co.dsergio.rtree.business.dto.RectangleDouble;
import co.dsergio.rtree.business.dto.RectangleInteger;
import co.dsergio.rtree.business.services.RTreeServiceDouble;
import co.dsergio.rtree.business.services.RTreeServiceInteger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rtree.item.ILocationItem;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.item.RInteger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTree;

@RestController
@RequestMapping("api/RTreeInteger")
@Api(tags = "RTreeInteger")
public class RTreeIntegerController extends RTreeControllerBaseGeneric<RInteger>{
	
	public RTreeIntegerController() {
		super(new RTreeServiceInteger());
	}
	
	@ApiOperation(value="RTreeInteger_getAll", notes = "Get all RTree<Integer> structures from metadata", nickname = "RTreeInteger_getAll")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<RTreeInteger>> get(HttpServletRequest request, HttpServletResponse response) {
		
		List<IRTree<RInteger>> trees = rtreeService.fetchAll();
		
		List<RTreeInteger> returnRTreeList = new ArrayList<RTreeInteger>();
		
		for (IRTree<RInteger> t : trees) {
			
			RTreeInteger tree = new RTreeInteger(t.getTreeName(), t.getNumDimensions(), null, null);
			returnRTreeList.add(tree);
		}
		
		return ResponseEntity.ok(returnRTreeList);
	}
	
	@ApiOperation(value="RTreeInteger_insert", notes = "Insert into RTree<Integer", nickname = "RTreeInteger_insert")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeInteger> insert(@PathVariable String treeName, @RequestBody LocationItemInteger itemToInsert) {
		
		ILocationItem<RInteger> item = new LocationItem<RInteger>(itemToInsert.numberDimensions);
		item.setType(itemToInsert.type);
		if (itemToInsert.itemProperties != null) {
			for (String s : itemToInsert.itemProperties.keySet()) {
				item.setProperty(s, itemToInsert.itemProperties.get(s));
			}
		}
		for (int i = 0; i < itemToInsert.numberDimensions; i++) {
			Integer d = itemToInsert.dimensionArray.get(i);
			RInteger rd = new RInteger(d);
			item.setDim(i, rd);
		}
		
		rtreeService.insert(treeName, item);
		
		
		IRTree<RInteger> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			RTreeInteger treeRet = new RTreeInteger();
			treeRet.name = tree.getTreeName();
			treeRet.numDimensions = tree.getNumDimensions();
			
			return ResponseEntity.ok(treeRet);
		}
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeInteger_get", notes = "Get RTree<Integer> structure and data by treeName", nickname = "RTreeInteger_get")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeInteger> get(@PathVariable String treeName) {
		

		IRTree<RInteger> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			
			RTreeInteger treeRet = new RTreeInteger();
			treeRet.numDimensions = tree.getNumDimensions();
			treeRet.name = tree.getTreeName();
			treeRet.points = new ArrayList<LocationItemInteger>();
			treeRet.rectangles = new ArrayList<RectangleInteger>();
			
			for (IHyperRectangle<RInteger> r : tree.getAllRectangles()) {
				RectangleInteger rectInteger = new RectangleInteger();
				rectInteger.dimensionArray1 = new ArrayList<Integer>();
				rectInteger.dimensionArray2 = new ArrayList<Integer>();
				rectInteger.json = r.getJson();
				for (RInteger rd : r.getDimensionArray1()) {
					rectInteger.dimensionArray1.add(rd.getData());
				}
				for (RInteger rd : r.getDimensionArray2()) {
					rectInteger.dimensionArray2.add(rd.getData());
				}
				rectInteger.numberDimensions = r.getNumberDimensions();
				treeRet.rectangles.add(rectInteger);
			}
			for (ILocationItem<RInteger> item : tree.getAllPoints()) {
				LocationItemInteger i = new LocationItemInteger();
				i.dimensionArray = new ArrayList<Integer>();
				i.type = item.getType();
				i.id = item.getId();
				i.itemProperties = item.getProperties();
				i.numberDimensions = item.getNumberDimensions();
				i.json = item.getJson();
				for (RInteger rd : item.getDimensionArray()) {
					i.dimensionArray.add(rd.getData());
				};
				treeRet.points.add(i);
			}
			return ResponseEntity.ok(treeRet);
			
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeInteger_search", notes = "Search RTree<Integer> structure by rectangle", nickname = "RTreeInteger_search")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/search/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<RectangleInteger, List<LocationItemInteger>>> search(
			@PathVariable String treeName, @RequestBody RectangleInteger searchRectangleInput) {
		
		IHyperRectangle<RInteger> searchRectangle = new HyperRectangle<RInteger>(searchRectangleInput.numberDimensions);
		for (int i = 0; i < searchRectangleInput.numberDimensions; i++) {
			RInteger rd1 = new RInteger(searchRectangleInput.dimensionArray1.get(i));
			searchRectangle.setDim1(i, rd1);
			RInteger rd2 = new RInteger(searchRectangleInput.dimensionArray2.get(i));
			searchRectangle.setDim2(i, rd2);
		}
		
		Map<IHyperRectangle<RInteger>, List<ILocationItem<RInteger>>> results = rtreeService.search(treeName, searchRectangle);
		
		Map<RectangleInteger, List<LocationItemInteger>> searchResults = new HashMap<RectangleInteger, List<LocationItemInteger>>();
		
		if (results != null) {
			
			for (IHyperRectangle<RInteger> r : results.keySet()) {
				List<ILocationItem<RInteger>> items = results.get(r);
				
				RectangleInteger eachR = new RectangleInteger();
				searchResults.put(eachR, new ArrayList<LocationItemInteger>());
				for (ILocationItem<RInteger> item : items) {
					LocationItemInteger i = new LocationItemInteger();
					i.type = item.getType();
					i.id = item.getId();
					i.itemProperties = item.getProperties();
					i.numberDimensions = item.getNumberDimensions();
					for (RInteger rd : item.getDimensionArray()) {
						i.dimensionArray.add(rd.getData());
					};
					searchResults.get(eachR).add(i);
				}
				
			}
			
			return ResponseEntity.ok(searchResults);
		}
		
		return ResponseEntity.ok(null);
	}
	
	@ApiOperation(value="RTreeInteger_newTree", notes = "Create new RTree<Integer>", nickname = "RTreeInteger_newTree")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeInteger> create(@RequestBody RTreeCreate rtreeCreate) {
		
		IRTree<RInteger> t = rtreeService.create(rtreeCreate);
		
		RTreeInteger treeRet = null;
		
		if (t != null) {
			treeRet = new RTreeInteger(t.getTreeName(), t.getNumDimensions(), null, null);
			return ResponseEntity.ok(treeRet);
		}
		
		return ResponseEntity.ok(treeRet);
	}
}
