package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.storage.IDataStorage;

public class RTree2D extends RTreeBase {

	/**
	 * @param dataStorage
	 * @param logger
	 * @throws Exception
	 */
	public RTree2D(IDataStorage dataStorage, ILogger logger) throws Exception {
		super(dataStorage, logger);
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
	public RTree2D(IDataStorage dataStorage, int maxChildren, int maxItems, ILogger logger, int numDimensions) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, numDimensions);
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
	public RTree2D(IDataStorage dataStorage, int maxChildren, int maxItems, ILogger logger) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTree2D(IDataStorage dataStorage, int maxChildren, int maxItems) throws Exception {
		super(dataStorage, maxChildren, maxItems);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @throws Exception
	 */
	public RTree2D(IDataStorage dataStorage) throws Exception {
		super(dataStorage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param locationItem
	 * @throws IOException
	 */
	@Override
	public void insertType(ILocationItem locationItem) throws IOException {
		
		logger.log("~~INSERT: " + "GOING TO INSERT: " + locationItem);
		insert(locationItem, getNode(treeName));
//		printTree();
	}
	
	/**
	 * Insert a random type of "animal" into the tree
	 * 
	 * @param LocationItem locationItem
	 * @return void
	 * 
	 */
	@Override
	public void insert(ILocationItem locationItem) throws IOException {
		int x = r.nextInt(animals.length);
		locationItem.setType(animals[x]);
		
		logger.log();
		logger.log();
		logger.log("~~INSERT: " + "START");
		insert(locationItem, getNode(treeName));
		logger.log("**************************************************************************************************************************");
//		printTree();
	}
	
	private void insert(ILocationItem locationItem, IRTreeNode node) throws IOException {
		
		if (node == null && getNode(treeName) == null) {
			IHyperRectangle r = new Rectangle2D(locationItem.getDim(0), locationItem.getDim(0), locationItem.getDim(1), locationItem.getDim(1));
			addNode(treeName, null, null, null, r.getJson().toJSONString());
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
				if (locationItem.getDim(0) > maxX || !boundariesSet) {
					maxX = locationItem.getDim(0);
					updateBoundaries = true;
				}
				if (locationItem.getDim(0) < minX || !boundariesSet) {
					minX = locationItem.getDim(0);
					updateBoundaries = true;
				}
				if (locationItem.getDim(1) > maxY || !boundariesSet) {
					maxY = locationItem.getDim(1);
					updateBoundaries = true;
				}
				if (locationItem.getDim(1) < minY || !boundariesSet) {
					minY = locationItem.getDim(1);
					updateBoundaries = true;
				}
				
				if (updateBoundaries) {
					cache.getDBAccess().updateMetaDataBoundaries(minX, maxX, minY, maxY);
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
				IRTreeNode child = getNode(s);
				logger.log("~~INSERT: " + "child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem)) {
					insert(locationItem, child);
					
					return;
					// we're done here
				}
			}
			
			int x = locationItem.getDim(0);
			int y = locationItem.getDim(1);
			int minEnlargementArea = getEnlargementArea(getNode(childrenArr.get(0)), x, y);
			int minEnlargementAreaIndex = 0;
			
			for (int i = 0; i < childrenArr.size(); i++) {
				IRTreeNode child = getNode(childrenArr.get(i));
				if (child.isLeafNode()) {
					if (getEnlargementArea(getNode(childrenArr.get(i)), x, y) < minEnlargementArea) {
						minEnlargementArea = getEnlargementArea(getNode(childrenArr.get(i)), x, y);
						minEnlargementAreaIndex = i;
					}
				}
			}
//			logger.log("min enlargement area for " + locationItem + " is " + minEnlargementArea + ", index=" + minEnlargementAreaIndex);
			
			IHyperRectangle sumRectangle = Rectangle2D.sumRectangles2D(node.getRectangle(), locationItem);
			node.setRectangle(sumRectangle);
			cache.updateNode(node.getNodeId(), null, null, null, node.getRectangle().getJson().toJSONString());
			addToRectangle(getNode(node.getParent()), node.getRectangle());
			
			insert(locationItem, getNode(childrenArr.get(minEnlargementAreaIndex)));
		}
	}
	
	private int getEnlargementArea(IRTreeNode node, int x, int y) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + x + ", " + y);
			return 0;
		}
		
		IHyperRectangle r = node.getRectangle();
		int minX = Math.min(Math.min(x,  r.getDim1(0)), r.getDim2(0));
		int maxX = Math.max(Math.max(x,  r.getDim1(0)), r.getDim2(0));
		
		int minY = Math.min(Math.min(y,  r.getDim1(1)), r.getDim2(1));
		int maxY = Math.max(Math.max(y,  r.getDim1(1)), r.getDim2(1));
		
		IHyperRectangle newRect = new Rectangle2D(minX, maxX, minY, maxY);
		
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
	public Map<IHyperRectangle, List<ILocationItem>> search(IHyperRectangle searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		long time = System.currentTimeMillis();
		Map<IHyperRectangle, List<ILocationItem>> result = new HashMap<IHyperRectangle, List<ILocationItem>>();
		search2D(searchRectangle, getNode(treeName), result, 0);
		
		
		logger.log("SEARCH consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}
	
	private void search2D(IHyperRectangle searchRectangle, IRTreeNode node, Map<IHyperRectangle, List<ILocationItem>> result, int depth) {
		
		if (node == null) {
			return;
		}
		
		node.getRectangle().setLevel(depth);
		if (node.isLeafNode()) {
			
			for (ILocationItem item : node.getLocationItems()) {
				
				if (item.getDim(0) > searchRectangle.getDim1(0) && item.getDim(0) < searchRectangle.getDim2(0) && item.getDim(1) > searchRectangle.getDim1(1) && item.getDim(1) < searchRectangle.getDim2(1)) {
					logger.log("Merry Christmas " + item + " nodeId: " + node.getNodeId());
					
					if (result.containsKey(searchRectangle)) {
						if (!result.get(searchRectangle).contains(item)) {
							result.get(searchRectangle).add(item);
						}
					} else {
						result.put(searchRectangle, new ArrayList<ILocationItem>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					IHyperRectangle r = getNode(child).getRectangle();
					
					if (Rectangle2D.rectanglesOverlap2D(r, searchRectangle)) {
						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
						result.put(r, new ArrayList<ILocationItem>());
						search2D(searchRectangle, getNode(child), result, depth + 1);
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
	public void delete(ILocationItem toDelete) {
		delete2D(toDelete, getNode(treeName));
	}
	
	private void delete2D(ILocationItem toDelete, IRTreeNode node) {
		
		if (node.isLeafNode()) {
			for (int i = 0; i < node.getNumberOfItems(); i++) {
				logger.log("comparing node " + node.getLocationItems().get(i) + " and " + toDelete);
				if (node.getLocationItems().get(i).getDim(0).equals(toDelete.getDim(0)) && node.getLocationItems().get(i).getDim(1).equals(toDelete.getDim(1)) && node.getLocationItems().get(i).getType().equals(toDelete.getType())) {
					node.getLocationItems().remove(i);
					
					cache.updateNode(node.getNodeId(), null, null, node.getItemsJSON().toJSONString(), null);
					
					node.updateRectangle(true);
					
					logger.log("deleted " + toDelete);
					
				}
			}
			
		} else {
			if (node != null && node.getChildren() != null) {
				
				for (String s : node.getChildren()) {
					IRTreeNode child = getNode(s);
					IHyperRectangle r = child.getRectangle();
					if (r.containsPoint(toDelete)) {
						delete2D(toDelete, child);
					}
				}
			}
		}
	}

}
