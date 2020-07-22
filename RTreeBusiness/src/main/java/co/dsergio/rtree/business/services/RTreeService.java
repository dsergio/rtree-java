package co.dsergio.rtree.business.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.dsergio.rtree.business.dto.LocationItem;
import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.tree.IRTree;

public class RTreeService extends EntityService {
	
	public List<IRTree> fetchAll() {
		
		List<IRTree> list = new ArrayList<IRTree>(dbContext.treeSetMap.values());
		return list;
//		return dbContext.treeSet;
	}
	
	public IRTree fetchByTreeName(String treeName) {
		
		return dbContext.treeSetMap.get(treeName);
		
//		for (IRTree t : dbContext.treeSet) {
//			if (t.getTreeName().equals(treeName)) {
//				return t;
//			}
//		}
//		return null;
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
		
		
//		for (IRTree t : dbContext.treeSet) {
//			if (t.getTreeName().equals(treeName)) {
//				
//				ILocationItem i = new LocationItemND(item.numberDimensions);
//				
//				try {
//					t.insert(i);
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}

}
