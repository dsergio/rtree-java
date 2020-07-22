package co.dsergio.rtree.business.services;

import java.util.ArrayList;
import java.util.List;

import rtree.item.ILocationItem;

public class LocationItemService extends EntityService {
	
	public List<ILocationItem> fetchAll() {
		
		List<ILocationItem> list = new ArrayList<ILocationItem>(dbContext.locationItemSetMap.values());
		return list;
	}
	

	public ILocationItem fetchByItemId(String id) {
		
		return dbContext.locationItemSetMap.get(id);
	}
}
