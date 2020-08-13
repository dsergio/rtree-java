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
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.item.generic.RDouble;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.tree.generic.IRTreeGeneric;

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
		
		List<IRTreeGeneric<RDouble>> trees = rtreeService.fetchAll();
		
		List<RTreeDouble> returnRTreeList = new ArrayList<RTreeDouble>();
		
		for (IRTreeGeneric<RDouble> t : trees) {
			
			RTreeDouble tree = new RTreeDouble(t.getTreeName(), t.getNumDimensions(), null, null);
			returnRTreeList.add(tree);
		}
		
		return ResponseEntity.ok(returnRTreeList);
	}
	
	@ApiOperation(value="RTreeDouble_insert", notes = "Insert into RTree<Double", nickname = "RTreeDouble_insert")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> insert(@PathVariable String treeName, @RequestBody LocationItemDouble itemToInsert) {
		
		ILocationItemGeneric<RDouble> item = new LocationItemNDGeneric<RDouble>(itemToInsert.numberDimensions);
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
		
		IRTreeGeneric<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
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
		
		IRTreeGeneric<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			
			RTreeDouble treeRet = new RTreeDouble();
			
			for (IHyperRectangleGeneric<RDouble> r : tree.getRectangles()) {
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
			for (ILocationItemGeneric<RDouble> item : tree.getPoints()) {
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
	public @ResponseBody ResponseEntity<Map<RectangleDouble, List<LocationItemDouble>>> search(
			@PathVariable String treeName, @RequestBody RectangleDouble searchRectangleInput) {
		
		IHyperRectangleGeneric<RDouble> searchRectangle = new RectangleNDGeneric<RDouble>(searchRectangleInput.numberDimensions);
		for (int i = 0; i < searchRectangleInput.numberDimensions; i++) {
			RDouble rd1 = new RDouble(searchRectangleInput.dimensionArray1.get(i));
			searchRectangle.setDim1(i, rd1);
			RDouble rd2 = new RDouble(searchRectangleInput.dimensionArray2.get(i));
			searchRectangle.setDim2(i, rd2);
		}
		
		Map<IHyperRectangleGeneric<RDouble>, List<ILocationItemGeneric<RDouble>>> results = rtreeService.search(treeName, searchRectangle);
		
		Map<RectangleDouble, List<LocationItemDouble>> searchResults = new HashMap<RectangleDouble, List<LocationItemDouble>>();
		
		if (results != null) {
			
			for (IHyperRectangleGeneric<RDouble> r : results.keySet()) {
				List<ILocationItemGeneric<RDouble>> items = results.get(r);
				
				RectangleDouble eachR = new RectangleDouble();
				searchResults.put(eachR, new ArrayList<LocationItemDouble>());
				for (ILocationItemGeneric<RDouble> item : items) {
					LocationItemDouble i = new LocationItemDouble();
					i.type = item.getType();
					i.id = item.getId();
					i.itemProperties = item.getProperties();
					i.numberDimensions = item.getNumberDimensions();
					for (RDouble rd : item.getDimensionArray()) {
						i.dimensionArray.add(rd.getData());
					};
					searchResults.get(eachR).add(i);
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
		
		IRTreeGeneric<RDouble> t = rtreeService.create(rtreeCreate);
		
		RTreeDouble treeRet = null;
		
		if (t != null) {
			treeRet = new RTreeDouble(t.getTreeName(), t.getNumDimensions(), null, null);
			return ResponseEntity.ok(treeRet);
		}
		
		return ResponseEntity.ok(treeRet);
	}
}
