package co.dsergio.rtree.business.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.dsergio.rtree.business.dto.RTreeCreate;
import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.rectangle.IHyperRectangle;
import rtree.tree.IRTree;
import rtree.tree.RTree;

/**
 * TODO Generics
 * 
 * @author David Sergio
 *
 */
public class RTreeServiceBase<T extends IRType<T>> extends EntityServiceBase<T> {
	
	
	public RTreeServiceBase(Class<T> className) {
		super(className);
	}
	
	public T getInstanceOf() {
		
		try {
			
			return className.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<IRTree<T>> fetchAll() {
		List<IRTree<T>> list = new ArrayList<IRTree<T>>(dbContext.treeSetMap.values());
		return list;
	}
	
	public IRTree<T> fetchByTreeName(String treeName) {	
		return dbContext.treeSetMap.get(treeName);
	}
	
	public IRTree<T> create(RTreeCreate rtreeCreate) {
		
		IRTree<T> t = null;
		try {
			t = new RTree<T>(dbContext.getDataStorage(), rtreeCreate.maxChildren, rtreeCreate.maxItems, rtreeCreate.numDimensions, rtreeCreate.treeName, className);
			dbContext.treeSetMap.put(rtreeCreate.treeName, t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return t;
	}
	
	public Map<IHyperRectangle<T>, List<ILocationItem<T>>> search(String treeName, IHyperRectangle<T> searchRectangleInput) {
		
		IRTree<T> t = dbContext.treeSetMap.get(treeName);
		
//		IHyperRectangleGeneric<T> searchR = new RectangleNDGeneric<T>(searchRectangleInput.getNumberDimensions());
//		for (int i = 0; i < searchRectangleInput.getNumberDimensions(); i++) {
//			
//			T dim1 = getInstanceOf();
//			T dim2 = getInstanceOf();
//			dim1.setData("" + searchRectangleInput.getDim1(i));
//			dim2.setData("" + searchRectangleInput.getDim2(i));
//			
//			searchR.setDim1(i, dim1);
//			searchR.setDim2(i, dim2);
//		}
		Map<IHyperRectangle<T>, List<ILocationItem<T>>> results = t.search(searchRectangleInput);
		
		return results;
	}
	
	public void insert(String treeName, ILocationItem<T> item) {
		
		IRTree<T> t = dbContext.treeSetMap.get(treeName);
		
		
//		ILocationItemGeneric<T> locationItem = new LocationItemNDGeneric<T>(item.getNumberDimensions());
//		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
//			
//			T val = getInstanceOf();
//			val.setData("" + item.getDim(i));
//			locationItem.setDim(i, val);
//		}
		
		try {
			t.insertRandomAnimal(item);
//			t.insertRandomWACity(item);
//			t.insert(item);
			dbContext.treeSetMap.put(treeName, t);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void insert_geo(String treeName, ILocationItem<T> item) {
		
		IRTree<T> t = dbContext.treeSetMap.get(treeName);
		
		
//		ILocationItemGeneric<T> locationItem = new LocationItemNDGeneric<T>(item.getNumberDimensions());
//		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
//			
//			T val = getInstanceOf();
//			val.setData("" + item.getDim(i));
//			locationItem.setDim(i, val);
//		}
		
		try {
//			t.insertRandomAnimal(item);
			t.insertRandomWACity(item);
//			t.insert(item);
			dbContext.treeSetMap.put(treeName, t);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void delete(String treeName) {
		
		IRTree<T> t = dbContext.treeSetMap.get(treeName);
		if (t != null) {
			try {
				t.delete();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dbContext.treeSetMap.remove(treeName);
//			dbContext.getDataStorage().clearData();
		}
	}

}
