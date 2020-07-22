package co.dsergio.rtree.business.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.dsergio.rtree.business.dto.LocationItem;
import co.dsergio.rtree.business.dto.RTreeCreate;
import co.dsergio.rtree.business.dto.SearchRectangle;
import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.RectangleND;
import rtree.tree.IRTree;
import rtree.tree.RTreeND;

public class RTreeService extends EntityService {
	
	public List<IRTree> fetchAll() {
		
		List<IRTree> list = new ArrayList<IRTree>(dbContext.treeSetMap.values());
		return list;
	}
	
	public IRTree fetchByTreeName(String treeName) {
		
		return dbContext.treeSetMap.get(treeName);
	}
	
	public IRTree create(RTreeCreate rtreeCreate) {
		
		
		IRTree t = null;
		try {
			t = new RTreeND(dbContext.getDataStorage(), rtreeCreate.maxChildren, rtreeCreate.maxItems, rtreeCreate.numDimensions, rtreeCreate.treeName);
			dbContext.treeSetMap.put(rtreeCreate.treeName, t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return t;
	}
	
	public Map<IHyperRectangle, List<ILocationItem>> search(String treeName, SearchRectangle searchRectangleInput) {
		
		IRTree t = dbContext.treeSetMap.get(treeName);
		IHyperRectangle searchR = new RectangleND(searchRectangleInput.numberDimensions);
		for (int i = 0; i < searchRectangleInput.numberDimensions; i++) {
			searchR.setDim1(i, searchRectangleInput.dimensionArray1.get(i));
			searchR.setDim2(i, searchRectangleInput.dimensionArray2.get(i));
		}
		Map<IHyperRectangle, List<ILocationItem>> results = t.search(searchR);
		
		return results;
	}
	
	public void insert(String treeName, LocationItem item) {
		
		IRTree t = dbContext.treeSetMap.get(treeName);
		
		
		ILocationItem locationItem = new LocationItemND(item.numberDimensions);
		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
			locationItem.setDim(i, item.dimensionArray.get(i));
		}
		
		try {
			t.insert(locationItem);
			dbContext.treeSetMap.put(treeName, t);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
