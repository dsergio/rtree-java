package rtree.tree.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.generic.HyperRectangleBaseGeneric;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.IDataStorage;
import rtree.storage.generic.IDataStorageGeneric;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeNDGeneric<T extends IRType<T>> extends RTreeBaseGeneric<T> {

	/**
	 * @param dataStorage
	 * @param logger
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, ILogger logger, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, logger, treeName, clazz);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param logger
	 * @param splitBehavior
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, int maxChildren, int maxItems, ILogger logger, int numDimensions, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, numDimensions, treeName, clazz);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param logger
	 * @param splitBehavior
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, int maxChildren, int maxItems, int numDimensions, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, maxChildren, maxItems, numDimensions, treeName, clazz);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param logger
	 * @param splitBehavior
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, int maxChildren, int maxItems, ILogger logger, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, treeName, clazz);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, int maxChildren, int maxItems, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, maxChildren, maxItems, treeName, clazz);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @throws Exception
	 */
	public RTreeNDGeneric(IDataStorageGeneric<T> dataStorage, String treeName, Class<T> clazz) throws Exception {
		super(dataStorage, treeName, clazz);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param locationItem
	 * @throws IOException
	 */
	@Override
	public void insertType(ILocationItemGeneric<T> locationItem) throws IOException {
		
		logger.log("~~INSERT: " + "GOING TO INSERT: " + locationItem);
		cache.getDBAccess().addItem(locationItem.getId(), locationItem.getNumberDimensions(), locationItem.getLocationJson().toJSONString(), locationItem.getType());
		insertNDimensional(locationItem, getNode(treeName));
//		printTree();
	}
	
	/**
	 * Insert a random type of "animal" into the N-Dimensional tree
	 * 
	 * @param LocationItem locationItem
	 * @return void
	 * 
	 */
	@Override
	public void insert(ILocationItemGeneric<T> locationItem) throws IOException {
		
		if (numDimensions != locationItem.getNumberDimensions()) {
			throw new IllegalArgumentException("The parameter locationItem dimension must equal the tree dimension.");
		}
		
		int x = r.nextInt(animals.length);
		locationItem.setType(animals[x]);
		
		logger.log();
		logger.log();
//		leafNodeSplit = false;
//		branchSplit = false;
		logger.log("~~INSERT: N:" + numDimensions + " START");
		cache.getDBAccess().addItem(locationItem.getId(), locationItem.getNumberDimensions(), locationItem.getLocationJson().toJSONString(), locationItem.getType());
		insertNDimensional(locationItem, getNode(treeName));
		logger.log("**************************************************************************************************************************");
//		printTree();
	}
	
	private void insertNDimensional(ILocationItemGeneric<T> locationItem, IRTreeNodeGeneric<T> node) throws IOException {
		
		int itemNumDimensions = locationItem.getNumberDimensions();
		if (itemNumDimensions != numDimensions) {
			throw new IllegalArgumentException("Item dimension must equal tree dimension.");
		}
		
//		cache.getDBAccess().addItem(locationItem.getId(), locationItem.getNumberDimensions(), locationItem.getLocationJson().toJSONString(), locationItem.getType());
		
		
		if (node == null && getNode(treeName) == null) { // empty tree
			
			IHyperRectangleGeneric<T> rND = new RectangleNDGeneric<T>(itemNumDimensions);
			
			for (int i = 0; i < itemNumDimensions; i++) {
				rND.setDim1(i, locationItem.getDim(i));
				rND.setDim2(i, locationItem.getDim(i));
			}
			addNode(treeName, null, null, null, rND.getJson().toJSONString());
			node = getNode(treeName);
			
		}
		
		if (node == null) {
			return;
		}
		
		logger.log("~~INSERT: " + locationItem + " into " + node.getNodeId() + " node.parent: " + node.getParent());
		
		if (node.isLeafNode()) {
			
			if (node.getNumberOfItems() < maxItems) {
				
				logger.log("~~INSERT: " + "Is leaf node and less than max, so let's add to " + node.getNodeId());
				node.addItem(locationItem);
				
				boolean updateBoundaries = false;
				
				for (int i = 0; i < numDimensions; i++) {
					
					if (maximums.get(i) == null || locationItem.getDim(i).compareTo(maximums.get(i)) > 0 || !boundariesSet) {
						maximums.set(i, locationItem.getDim(i));
						updateBoundaries = true;
					}
					if (minimums.get(i) == null || locationItem.getDim(i).compareTo(minimums.get(i)) < 0 || !boundariesSet) {
						minimums.set(i, locationItem.getDim(i));
						updateBoundaries = true;
					}
					
				}
				
				if (updateBoundaries) {
					cache.getDBAccess().updateMetaDataBoundariesNDimensional(minimums, maximums, treeName);
					boundariesSet = true;
				}
				
			} else {
				
				// SPLIT
				splitBehavior.splitLeafNode(node, locationItem);
//				leafNodeSplit = true;
//				branchSplit = splitBehavior.didBranchSplit();
				
			}
		} else {
			// not a leaf node
			logger.log("~~INSERT: " + "not a leaf node, drill down");
			
			List<String> childrenArr = node.getChildren();
			
			for (String s : childrenArr) {
				IRTreeNodeGeneric<T> child = getNode(s);
				logger.log("~~INSERT: " + "child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem)) {
					insertNDimensional(locationItem, child);
					
					return;
					// we're done here
				}
			}
			
			

			double minEnlargementArea = getEnlargementAreaNDimensional(getNode(childrenArr.get(0)), locationItem);
			int minEnlargementAreaIndex = 0;
			
			for (int i = 0; i < childrenArr.size(); i++) {
				IRTreeNodeGeneric<T> child = getNode(childrenArr.get(i));
				if (child.isLeafNode()) {
					if (getEnlargementAreaNDimensional(getNode(childrenArr.get(i)), locationItem) < minEnlargementArea) {
						minEnlargementArea = getEnlargementAreaNDimensional(getNode(childrenArr.get(i)), locationItem);
						minEnlargementAreaIndex = i;
					}
				}
			}
			
//			logger.log("min enlargement area for " + locationItem + " is " + minEnlargementArea + ", index=" + minEnlargementAreaIndex);
			
			IHyperRectangleGeneric<T> sumRectangle = HyperRectangleBaseGeneric.sumRectanglesNDimensional(node.getRectangle(), locationItem);
			node.setRectangle(sumRectangle);
			cache.updateNode(node.getNodeId(), null, null, null, node.getRectangle().getJson().toJSONString());
			addToRectangle(getNode(node.getParent()), node.getRectangle());
			
			insertNDimensional(locationItem, getNode(childrenArr.get(minEnlargementAreaIndex)));
		}
	}

	private double getEnlargementAreaNDimensional(IRTreeNodeGeneric<T> node, ILocationItemGeneric<T> item) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + item);
			return 0;
		}
		
		IHyperRectangleGeneric<T> r = node.getRectangle();
		
		List<T> min = new ArrayList<T>();
		List<T> max = new ArrayList<T>();
		
		for (int i = 0; i < numDimensions; i++) {
			
			if (item.getDim(i).compareTo(r.getDim1(i)) < 0 && item.getDim(i).compareTo(r.getDim2(i)) < 0) {
				min.add(item.getDim(i));
			} else if (r.getDim1(i).compareTo(item.getDim(i)) < 0 && r.getDim1(i).compareTo(r.getDim2(i)) < 0) {
				min.add(r.getDim1(i));
			} else {
				min.add(r.getDim2(i));
			}
			
			if (item.getDim(i).compareTo(r.getDim1(i)) > 0 && item.getDim(i).compareTo(r.getDim2(i)) > 0) {
				max.add(item.getDim(i));
			} else if (r.getDim1(i).compareTo(item.getDim(i)) > 0 && r.getDim1(i).compareTo(r.getDim2(i)) > 0) {
				max.add(r.getDim1(i));
			} else {
				max.add(r.getDim2(i));
			}
		}
		
		IHyperRectangleGeneric<T> newRect = new RectangleNDGeneric<T>(numDimensions);
		for (int i = 0; i < numDimensions; i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect.getSpace() - r.getSpace();
	}
	
	/**
	 * Query the R-Tree structure and retrieve the items that fall inside the parameter search rectangle
	 * 
	 * @param Rectangle searchRectangle
	 * @return a map of rectangles containing search results
	 * 
	 */
	@Override
	public Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> search(IHyperRectangleGeneric<T> searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		long time = System.currentTimeMillis();
		Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> result = new HashMap<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>>();
		searchNDimensional(searchRectangle, getNode(treeName), result, 0);
		
		
		logger.log("SEARCH consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}

	private void searchNDimensional(IHyperRectangleGeneric<T> searchRectangle, IRTreeNodeGeneric<T> node, Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> result, int depth) {
		
		if (node == null) {
			return;
		}
		
		node.getRectangle().setLevel(depth);
		if (node.isLeafNode()) {
			
			for (ILocationItemGeneric<T> item : node.getLocationItems()) {
				
				boolean searchRectangleContains = true;
				
				for (int i = 0; i < numDimensions; i++) {
					
					if (!(item.getDim(i).compareTo(searchRectangle.getDim1(i)) > 0 && item.getDim(i).compareTo(searchRectangle.getDim2(i)) < 0)) {
						searchRectangleContains = false;
					}
				
				}
				
				if (searchRectangleContains) {
					logger.log("Merry Christmas " + item + " nodeId: " + node.getNodeId());
					
					if (result.containsKey(searchRectangle)) {
						if (!result.get(searchRectangle).contains(item)) {
							result.get(searchRectangle).add(item);
						}
					} else {
						result.put(searchRectangle, new ArrayList<ILocationItemGeneric<T>>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					IHyperRectangleGeneric<T> r = getNode(child).getRectangle();
					
					if (HyperRectangleBaseGeneric.rectanglesOverlapNDimensional(r, searchRectangle)) {
						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
						result.put(r, new ArrayList<ILocationItemGeneric<T>>());
						searchNDimensional(searchRectangle, getNode(child), result, depth + 1);
					} else {
						logger.log("NO overlap: r: " + r + " searchRectangle: " + searchRectangle);
					}
				}
			}
		}
	}
	
	/**
	 * Delete item from tree
	 * 
	 * @param toDelete Item to be deleted
	 * 
	 */
	@Override
	public void delete(ILocationItemGeneric<T> toDelete) {
		deleteNDimensional(toDelete, getNode(treeName));
	}
	
	private void deleteNDimensional(ILocationItemGeneric<T> toDelete, IRTreeNodeGeneric<T> node) {
		
		if (node.isLeafNode()) {
			for (int i = 0; i < node.getNumberOfItems(); i++) {
				logger.log("comparing node " + node.getLocationItems().get(i) + " and " + toDelete);
				
				boolean itemEqualsToDelete = true;
				
				for (int j = 0; j < numDimensions; j++) {
					if (!(node.getLocationItems().get(i).getDim(j).equals(toDelete.getDim(j)) && node.getLocationItems().get(i).getType().equals(toDelete.getType()))) {
						itemEqualsToDelete = false;
					}
				}
				
				
				if (itemEqualsToDelete) {
					node.getLocationItems().remove(i);
					
					cache.updateNode(node.getNodeId(), null, null, node.getItemsJSON().toJSONString(), null);
					
					node.updateRectangle(true);
					
					logger.log("deleted " + toDelete);
					
				}
			}
			
		} else {
			if (node != null && node.getChildren() != null) {
				
				for (String s : node.getChildren()) {
					IRTreeNodeGeneric<T> child = getNode(s);
					IHyperRectangleGeneric<T> r = child.getRectangle();
					if (r.containsPoint(toDelete)) {
						deleteNDimensional(toDelete, child);
					}
				}
			}
		}
	}

	
}
