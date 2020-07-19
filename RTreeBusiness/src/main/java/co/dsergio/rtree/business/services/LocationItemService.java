package co.dsergio.rtree.business.services;

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

	public ILocationItem fetchByItemId(String id) {
		for (ILocationItem i : dbContext.locationItemSet) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}
}
