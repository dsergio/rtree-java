package co.dsergio.rtree.business.services.generic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.dsergio.rtree.business.dto.LocationItem;
import co.dsergio.rtree.business.dto.RTreeCreate;
import co.dsergio.rtree.business.dto.SearchRectangle;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.RTreeNDGeneric;

/**
 * TODO Generics
 * 
 * @author David Sergio
 *
 */
public class RTreeServiceBaseGeneric<T extends IRType<T>> extends EntityServiceBaseGeneric<T> {
	
	
	public RTreeServiceBaseGeneric(Class<T> clazz) {
		super(clazz);
	}
	
	public T getInstanceOf() {
		
		try {
			
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<IRTreeGeneric<T>> fetchAll() {
		List<IRTreeGeneric<T>> list = new ArrayList<IRTreeGeneric<T>>(dbContext.treeSetMap.values());
		return list;
	}
	
	public IRTreeGeneric<T> fetchByTreeName(String treeName) {	
		return dbContext.treeSetMap.get(treeName);
	}
	
	public IRTreeGeneric<T> create(RTreeCreate rtreeCreate) {
		
		IRTreeGeneric<T> t = null;
		try {
			t = new RTreeNDGeneric<T>(dbContext.getDataStorage(), rtreeCreate.maxChildren, rtreeCreate.maxItems, rtreeCreate.numDimensions, rtreeCreate.treeName, clazz);
			dbContext.treeSetMap.put(rtreeCreate.treeName, t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return t;
	}
	
	public Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> search(String treeName, IHyperRectangleGeneric<T> searchRectangleInput) {
		
		IRTreeGeneric<T> t = dbContext.treeSetMap.get(treeName);
		
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
		Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> results = t.search(searchRectangleInput);
		
		return results;
	}
	
	public void insert(String treeName, ILocationItemGeneric<T> item) {
		
		IRTreeGeneric<T> t = dbContext.treeSetMap.get(treeName);
		
		
//		ILocationItemGeneric<T> locationItem = new LocationItemNDGeneric<T>(item.getNumberDimensions());
//		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
//			
//			T val = getInstanceOf();
//			val.setData("" + item.getDim(i));
//			locationItem.setDim(i, val);
//		}
		
		try {
			t.insert(item);
			dbContext.treeSetMap.put(treeName, t);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
