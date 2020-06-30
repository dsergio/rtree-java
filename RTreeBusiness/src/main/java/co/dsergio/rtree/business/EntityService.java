package co.dsergio.rtree.business;

import rtree.storage.ApplicationDbContext;

public abstract class EntityService {
	
	protected ApplicationDbContext dbContext = new ApplicationDbContext();

}
