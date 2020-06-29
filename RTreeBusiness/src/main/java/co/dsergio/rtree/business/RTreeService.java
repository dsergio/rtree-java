package co.dsergio.rtree.business;

import java.util.List;

import rtree.storage.ApplicationDbContext;
import rtree.tree.IRTree;

public class RTreeService {
	
	public List<IRTree> fetchAll() {
		
		ApplicationDbContext applicationDbContext = new ApplicationDbContext();
		return applicationDbContext.treeSet;
		
	}

}
