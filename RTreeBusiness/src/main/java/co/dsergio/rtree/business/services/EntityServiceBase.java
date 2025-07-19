package co.dsergio.rtree.business.services;

import rtree.item.IRType;
import rtree.storage.ApplicationDbContext;

public abstract class EntityServiceBase<T extends IRType<T>> {
	
	protected Class<T> clazz;
	protected ApplicationDbContext<T> dbContext;
	
	public EntityServiceBase(Class<T> clazz) {
		dbContext = new ApplicationDbContext<T>(clazz);
		this.clazz = clazz;
	}

}
