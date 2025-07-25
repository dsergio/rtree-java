package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public class RTree<T extends IRType<T>> extends RTreeBase<T> {

	private int resultCount = 0;
	private int maxResults = 50;

	/**
	 * @param dataStorage
	 * @param logger
	 * @throws Exception
	 */
	public RTree(IDataStorage<T> dataStorage, ILogger logger, String treeName, Class<T> className) throws Exception {
		super(dataStorage, logger, treeName, className);
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
	public RTree(IDataStorage<T> dataStorage, int maxChildren, int maxItems, ILogger logger, int numDimensions, String treeName, Class<T> className) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, numDimensions, treeName, className);
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
	public RTree(IDataStorage<T> dataStorage, int maxChildren, int maxItems, int numDimensions, String treeName, Class<T> className) throws Exception {
		super(dataStorage, maxChildren, maxItems, numDimensions, treeName, className);
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param logger
	 * @param splitBehavior
	 * @throws Exception
	 */
	public RTree(IDataStorage<T> dataStorage, int maxChildren, int maxItems, ILogger logger, String treeName, Class<T> className) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, treeName, className);
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTree(IDataStorage<T> dataStorage, int maxChildren, int maxItems, String treeName, Class<T> className) throws Exception {
		super(dataStorage, maxChildren, maxItems, treeName, className);
	}

	/**
	 * @param dataStorage
	 * @throws Exception
	 */
	public RTree(IDataStorage<T> dataStorage, String treeName, Class<T> className) throws Exception {
		super(dataStorage, treeName, className);
	}
	
	@Override
	public void insert(ILocationItem<T> locationItem) throws IOException {	
		insert(locationItem, cache.getNode(treeName));
	}
	
	@Override
	public void insertRandomAnimal(ILocationItem<T> locationItem) throws IOException {
		int x = r.nextInt(animals.length);
		locationItem.setType(animals[x]);
		insert(locationItem, cache.getNode(treeName));
	}
	
	/**
	 * Insert a location item into the RTree.
	 * @param locationItem
	 * @param node
	 * @throws IOException
	 */
	private void insert(ILocationItem<T> locationItem, IRTreeNode<T> node) throws IOException {
		
		int itemNumDimensions = locationItem.getNumberDimensions();
		
		if (itemNumDimensions != numDimensions) {
			throw new IllegalArgumentException("Item dimension must equal tree dimension.");
		}
		logger.log();
		logger.log("[INSERT] " + locationItem);
		
		cache.getDBAccess().addItem(locationItem.getId(), locationItem.getNumberDimensions(), locationItem.getLocationJson().toJSONString(), locationItem.getType(), locationItem.getPropertiesJson().toJSONString());	
		
		if (node == null && cache.getNode(treeName) == null) { // empty tree
			
			IHyperRectangle<T> newRectangle = new HyperRectangle<T>(itemNumDimensions);
			for (int i = 0; i < itemNumDimensions; i++) {
				newRectangle.setDim1(i, locationItem.getDim(i));
				newRectangle.setDim2(i, locationItem.getDim(i));
			}
			
			IRTreeNode<T> newNode = new RTreeNode<T>(treeName, null, null, cache, logger, className);
			newNode.setRectangle(newRectangle);
			cache.putNode(treeName, newNode);
			
			node = cache.getNode(treeName);
			
		}
		
		if (node == null) {
			return;
		}
		
		logger.log("[INSERT] " + locationItem + " into " + node.getNodeId() + " node.parent: " + node.getParent());
		
		if (node.isLeafNode()) {
			
			if (node.getNumberOfItems() < maxItems) {
				
				logger.log("[INSERT] " + " this node is a leaf node and items are less than max, so let's add to " + node.getNodeId());
				node.addLocationItem(locationItem);
				
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
			
			cache.getNode(treeName);
			
		} else {
			// not a leaf node
			logger.log("[INSERT] " + " not a leaf node, drill down");
			
			List<String> childrenArr = node.getChildren();
			
			for (String s : childrenArr) {
				IRTreeNode<T> child = cache.getNode(s);
				logger.log("[INSERT] " + " child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem)) {
					insert(locationItem, child);
					
					return;
					// we're done here
				}
			}

			double minEnlargementArea = getEnlargementArea(cache.getNode(childrenArr.get(0)), locationItem);
			int minEnlargementAreaIndex = 0;
			
			for (int i = 0; i < childrenArr.size(); i++) {
				IRTreeNode<T> child = cache.getNode(childrenArr.get(i));
				if (child.isLeafNode()) {
					if (getEnlargementArea(cache.getNode(childrenArr.get(i)), locationItem) < minEnlargementArea) {
						minEnlargementArea = getEnlargementArea(cache.getNode(childrenArr.get(i)), locationItem);
						minEnlargementAreaIndex = i;
					}
				}
			}
						
			IHyperRectangle<T> sumRectangle = HyperRectangleBase.sumRectanglesNDimensional(node.getRectangle(), locationItem);
			node.setRectangle(sumRectangle);
			
			cache.updateNode(node.getNodeId(), node);
			
			addToRectangle(cache.getNode(node.getParent()), node.getRectangle());
			
			insert(locationItem, cache.getNode(childrenArr.get(minEnlargementAreaIndex)));
		}
	}

	private double getEnlargementArea(IRTreeNode<T> node, ILocationItem<T> item) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + item);
			return 0;
		}
		
		IHyperRectangle<T> r = node.getRectangle();
		
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
		
		IHyperRectangle<T> newRect = new HyperRectangle<T>(numDimensions);
		for (int i = 0; i < numDimensions; i++) {
			newRect.setDim1(i, min.get(i));
			newRect.setDim2(i, max.get(i));
		}
		
		return newRect.getSpace() - r.getSpace();
	}
	
	
	@Override
	public Map<IHyperRectangle<T>, List<ILocationItem<T>>> search(IHyperRectangle<T> searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		logger.log("[SEARCH] ...");
		
		long time = System.currentTimeMillis();
		Map<IHyperRectangle<T>, List<ILocationItem<T>>> result = new HashMap<IHyperRectangle<T>, List<ILocationItem<T>>>();
		resultCount  = 0;
		search(searchRectangle, cache.getNode(treeName), result, 0);
		
		
		logger.log("[SEARCH] consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}
	
	/**
	 * Recursively search the RTree for items within the specified rectangle.
	 * @param searchRectangle
	 * @param node
	 * @param result
	 * @param depth
	 */
	private void search(
			IHyperRectangle<T> searchRectangle, 
			IRTreeNode<T> node, 
			Map<IHyperRectangle<T>, 
			List<ILocationItem<T>>> result, 
			int depth
			) {
		
		if (node == null || resultCount >= maxResults) {
			return;
		}
		
		node.getRectangle().setLevel(depth);
		if (node.isLeafNode()) {
			
			for (ILocationItem<T> item : node.getLocationItems()) {
				
				boolean searchRectangleContains = true;
				
				for (int i = 0; i < numDimensions; i++) {
					
					if (!(item.getDim(i).compareTo(searchRectangle.getDim1(i)) > 0 && item.getDim(i).compareTo(searchRectangle.getDim2(i)) < 0)) {
						searchRectangleContains = false;
					}
				
				}
				
				if (searchRectangleContains) {
					logger.log("[SEARCH] Merry Christmas " + item + " nodeId: " + node.getNodeId());
					resultCount++;
					if (result.containsKey(searchRectangle)) {
						if (!result.get(searchRectangle).contains(item)) {
							result.get(searchRectangle).add(item);
						}
					} else {
						result.put(searchRectangle, new ArrayList<ILocationItem<T>>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					if (cache.getNode(child) != null) {
						IHyperRectangle<T> r = cache.getNode(child).getRectangle();
						
						if (HyperRectangleBase.rectanglesOverlap(r, searchRectangle)) {
	//						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
							result.put(r, new ArrayList<ILocationItem<T>>());
							search(searchRectangle, cache.getNode(child), result, depth + 1);
						} else {
	//						logger.log("NO overlap: r: " + r + " searchRectangle: " + searchRectangle);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void delete(ILocationItem<T> toDelete) {
		delete(toDelete, cache.getNode(treeName));
	}
	
	private void delete(ILocationItem<T> toDelete, IRTreeNode<T> node) {
		
//		logger.log("deleting " + toDelete + " from leaf node " + node.getNodeId() + " (" + (node.isLeafNode() ? "leaf" : "branch") + ")");
		
		if (node.isLeafNode()) {
			
			for (int i = 0; i < node.getNumberOfItems(); i++) {
				
				ILocationItem<T> item = node.getLocationItems().get(i);
				
				logger.log("[DELETE] compare item " + item + " (ID: " + item.getId() + ") and " + toDelete + " (ID: " + toDelete.getId() + ")");
				
				boolean itemEqualsToDelete = false;
				
				if (item.getId().equals(toDelete.getId())) {
					logger.log("item.getId equals toDelete id, so delete it");
					itemEqualsToDelete = true;
				} else {
					// put this check in a search method, then pass in the result
//					for (int j = 0; j < numDimensions; j++) {
//						if (!(node.getLocationItems().get(i).getDim(j).equals(toDelete.getDim(j)) && node.getLocationItems().get(i).getType().equals(toDelete.getType()))) {
//							itemEqualsToDelete = false;
//						}
//					}
				}
				
				if (itemEqualsToDelete) {
					
					node.getLocationItems().remove(i);
					
//					cache.updateNode(node.getNodeId(), null, null, node.getItemsJSON().toJSONString(), null);
					cache.updateNode(node.getNodeId(), node);
					
					node.updateRectangle(true);
					
					// update metadata boundaries
					// TODO
					
					logger.log("deleted " + toDelete);
				}
			}
			
		} else {
			if (node != null && node.getChildren() != null) {
				
				for (String s : node.getChildren()) {
					IRTreeNode<T> child = cache.getNode(s);
					IHyperRectangle<T> r = child.getRectangle();
					if (r.containsPoint(toDelete)) {
						delete(toDelete, child);
					}
				}
			}
		}
	}

	@Override
	public void delete() {
		
		List<ILocationItem<T>> items = getAllLocationItems();
		for (ILocationItem<T> item : items) {
			delete(item);
		}
		
	}

	
}
