package co.dsergio.rtree.business.services;

import rtree.item.IRType;
import rtree.storage.ApplicationDbContext;

public abstract class EntityServiceBase<T extends IRType<T>> {
	
	protected Class<T> className;
	protected ApplicationDbContext<T> dbContext;
	
	public EntityServiceBase(Class<T> className) {
		dbContext = new ApplicationDbContext<T>(className);
		this.className = className;
	}

}
