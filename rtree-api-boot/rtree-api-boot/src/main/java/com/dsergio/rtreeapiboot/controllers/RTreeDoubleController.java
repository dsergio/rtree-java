package com.dsergio.rtreeapiboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
import co.dsergio.rtree.business.services.RTreeServiceDouble;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import rtree.item.ILocationItem;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTree;

@RestController
@RequestMapping("api/RTreeDouble")
@Tag(name = "RTreeDouble", description = "Operations related to RTreeDouble")
public class RTreeDoubleController extends RTreeControllerBaseGeneric<RDouble> {
	
	public RTreeDoubleController() {
		super(new RTreeServiceDouble());
	}
	
	@Operation(
		    summary = "RTreeDouble_getAll",
		    description = "Get all RTree<Double> structures from metadata",
		    operationId = "RTreeDouble_getAll"
		)
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
	
	@Operation(summary = "RTreeDouble_insert", description = "Insert a new item into RTree<Double>", operationId = "RTreeDouble_insert")
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
			
			treeRet.points = new ArrayList<>();
			for (ILocationItem<RDouble> i : tree.getAllLocationItems()) {
				LocationItemDouble li = new LocationItemDouble();
				li.id = i.getId();
				li.type = i.getType();
				li.itemProperties = i.getProperties();
				li.numberDimensions = i.getNumberDimensions();
				li.dimensionArray = new ArrayList<Double>();
				for (RDouble rd : i.getDimensionArray()) {
					li.dimensionArray.add(rd.getData());
				}
				treeRet.points.add(li);
			}
			treeRet.rectangles = new ArrayList<>();
			
			Map<IHyperRectangle<RDouble>, Integer> rectanglesWithDepth = tree.getAllRectanglesWithDepth();
			
			for (IHyperRectangle<RDouble> r : rectanglesWithDepth.keySet()) {
				RectangleDouble rectDouble = new RectangleDouble();
				rectDouble.dimensionArray1 = new ArrayList<Double>();
				rectDouble.dimensionArray2 = new ArrayList<Double>();
				rectDouble.depth = rectanglesWithDepth.get(r);
				for (RDouble rd : r.getDimensionArray1()) {
					rectDouble.dimensionArray1.add(rd.getData());
				}
				for (RDouble rd : r.getDimensionArray2()) {
					rectDouble.dimensionArray2.add(rd.getData());
				}
				rectDouble.numberDimensions = r.getNumberDimensions();
				treeRet.rectangles.add(rectDouble);
			}
			
			return ResponseEntity.ok(treeRet);
		}
		return ResponseEntity.ok(null);
	}
	
	@Operation(summary = "RTreeDouble_insert_geo", description = "Insert a new geo into RTree<Double>", operationId = "RTreeDouble_insert_geo")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}/geo/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> insert_geo(@PathVariable String treeName, @RequestBody LocationItemDouble itemToInsert) {
		
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
		
		rtreeService.insert_geo(treeName, item);
		
		IRTree<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			RTreeDouble treeRet = new RTreeDouble();
			treeRet.name = tree.getTreeName();
			treeRet.numDimensions = tree.getNumDimensions();
			
			treeRet.points = new ArrayList<>();
			for (ILocationItem<RDouble> i : tree.getAllLocationItems()) {
				LocationItemDouble li = new LocationItemDouble();
				li.id = i.getId();
				li.type = i.getType();
				li.itemProperties = i.getProperties();
				li.numberDimensions = i.getNumberDimensions();
				li.dimensionArray = new ArrayList<Double>();
				for (RDouble rd : i.getDimensionArray()) {
					li.dimensionArray.add(rd.getData());
				}
				treeRet.points.add(li);
			}
			treeRet.rectangles = new ArrayList<>();
			
			Map<IHyperRectangle<RDouble>, Integer> rectanglesWithDepth = tree.getAllRectanglesWithDepth();
			
			for (IHyperRectangle<RDouble> r : rectanglesWithDepth.keySet()) {
				RectangleDouble rectDouble = new RectangleDouble();
				rectDouble.dimensionArray1 = new ArrayList<Double>();
				rectDouble.dimensionArray2 = new ArrayList<Double>();
				rectDouble.depth = rectanglesWithDepth.get(r);
				for (RDouble rd : r.getDimensionArray1()) {
					rectDouble.dimensionArray1.add(rd.getData());
				}
				for (RDouble rd : r.getDimensionArray2()) {
					rectDouble.dimensionArray2.add(rd.getData());
				}
				rectDouble.numberDimensions = r.getNumberDimensions();
				treeRet.rectangles.add(rectDouble);
			}
			
			return ResponseEntity.ok(treeRet);
		}
		return ResponseEntity.ok(null);
	}
	
	@Operation(summary = "RTreeDouble_get", description = "Get RTree<Double> structure and data by treeName", operationId = "RTreeDouble_get")
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/{treeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<RTreeDouble> get(@PathVariable String treeName) {
		
		IRTree<RDouble> tree = rtreeService.fetchByTreeName(treeName);
		
		if (tree != null) {
			
			RTreeDouble treeRet = new RTreeDouble();
			treeRet.rectangles = new ArrayList<>();
			treeRet.points = new ArrayList<>();
			
			Map<IHyperRectangle<RDouble>, Integer> rectanglesWithDepth = tree.getAllRectanglesWithDepth();
			
			for (IHyperRectangle<RDouble> r : rectanglesWithDepth.keySet()) {
				RectangleDouble rectDouble = new RectangleDouble();
				rectDouble.dimensionArray1 = new ArrayList<Double>();
				rectDouble.dimensionArray2 = new ArrayList<Double>();
				rectDouble.depth = rectanglesWithDepth.get(r);
				for (RDouble rd : r.getDimensionArray1()) {
					rectDouble.dimensionArray1.add(rd.getData());
				}
				for (RDouble rd : r.getDimensionArray2()) {
					rectDouble.dimensionArray2.add(rd.getData());
				}
				rectDouble.numberDimensions = r.getNumberDimensions();
				treeRet.rectangles.add(rectDouble);
			}
			for (ILocationItem<RDouble> item : tree.getAllLocationItems()) {
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
	
	@Operation(summary = "RTreeDouble_search", description = "Search RTree<Double> structure by rectangle", operationId = "RTreeDouble_search")
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
	
	@Operation(summary = "RTreeDouble_newTree", description = "Create new RTree<Double>", operationId = "RTreeDouble_newTree")
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
	
	@Operation(summary = "RTreeDouble_delete", description = "Delete RTree<Double> by treeName", operationId = "RTreeDouble_delete")
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
