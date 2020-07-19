package co.dsergio.rtree.business.services;

import java.util.List;

import rtree.tree.IRTree;

public class RTreeService extends EntityService {
	
	public List<IRTree> fetchAll() {
		return dbContext.treeSet;
	}
	
	public IRTree fetchByTreeName(String treeName) {
		for (IRTree t : dbContext.treeSet) {
			if (t.getTreeName().equals(treeName)) {
				return t;
			}
		}
		return null;
	}

}
