package co.dsergio.rtree.business.services.generic;

import rtree.item.IRType;
import rtree.storage.ApplicationDbContext;

public abstract class EntityServiceBaseGeneric<T extends IRType<T>> {
	
	protected Class<T> clazz;
	protected ApplicationDbContext<T> dbContext;
	
	public EntityServiceBaseGeneric(Class<T> clazz) {
		dbContext = new ApplicationDbContext<T>(clazz);
		this.clazz = clazz;
	}

}
