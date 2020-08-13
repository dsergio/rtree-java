package co.dsergio.rtree.business.services.generic;

import rtree.item.generic.IRType;
import rtree.storage.generic.ApplicationDbContextGeneric;

public abstract class EntityServiceBaseGeneric<T extends IRType<T>> {
	
	protected Class<T> clazz;
	protected ApplicationDbContextGeneric<T> dbContext;
	
	public EntityServiceBaseGeneric(Class<T> clazz) {
		dbContext = new ApplicationDbContextGeneric<T>(clazz);
		this.clazz = clazz;
	}

}
