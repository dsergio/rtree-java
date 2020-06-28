package co.dsergio.rtree.business;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;

public class LocationItemService {
	
	public ILocationItem get(int id) {
		return null;
	}
	
	public ILocationItem create(String type, int N) {
		
		ILocationItem item = new LocationItemND(N);
		item.setType(type);
		return item;
		
	}
}
