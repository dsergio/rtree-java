package co.dsergio.rtree.business;

import java.util.List;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.tree.IRTree;

public class LocationItemService extends EntityService {
	
	public List<ILocationItem> fetchAll() {
		return dbContext.locationItemSet;
	}
	
	public ILocationItem create(String type, int N) {
		
		ILocationItem item = new LocationItemND(N);
		item.setType(type);
		
		return item;
		
	}

	public ILocationItem get(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
