package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.ILocationItem;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeND extends RTreeBase {

	/**
	 * @param dataStorage
	 * @param logger
	 * @throws Exception
	 */
	public RTreeND(IDataStorage dataStorage, ILogger logger, String treeName) throws Exception {
		super(dataStorage, logger, treeName);
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
	public RTreeND(IDataStorage dataStorage, int maxChildren, int maxItems, ILogger logger, int numDimensions, String treeName) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, numDimensions, treeName);
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
	public RTreeND(IDataStorage dataStorage, int maxChildren, int maxItems, ILogger logger, String treeName) throws Exception {
		super(dataStorage, maxChildren, maxItems, logger, treeName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTreeND(IDataStorage dataStorage, int maxChildren, int maxItems, String treeName) throws Exception {
		super(dataStorage, maxChildren, maxItems, treeName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataStorage
	 * @throws Exception
	 */
	public RTreeND(IDataStorage dataStorage, String treeName) throws Exception {
		super(dataStorage, treeName);
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
	public void insert(ILocationItem locationItem) throws IOException {
		
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
		insertNDimensional(locationItem, getNode(treeName));
		logger.log("**************************************************************************************************************************");
//		printTree();
	}
	
	private void insertNDimensional(ILocationItem locationItem, IRTreeNode node) throws IOException {
		
		int itemNumDimensions = locationItem.getNumberDimensions();
		if (itemNumDimensions != numDimensions) {
			throw new IllegalArgumentException("Item dimension must equal tree dimension.");
		}
		
		if (node == null && getNode(treeName) == null) { // empty tree
			
			IHyperRectangle rND = new RectangleND(itemNumDimensions);
			
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
					
					if (maximums.get(i) == null || locationItem.getDim(i) > maximums.get(i) || !boundariesSet) {
						maximums.set(i, locationItem.getDim(i));
						updateBoundaries = true;
					}
					if (minimums.get(i) == null || locationItem.getDim(i) < minimums.get(i) || !boundariesSet) {
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
				IRTreeNode child = getNode(s);
				logger.log("~~INSERT: " + "child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem)) {
					insertNDimensional(locationItem, child);
					
					return;
					// we're done here
				}
			}
			
			

			int minEnlargementArea = getEnlargementAreaNDimensional(getNode(childrenArr.get(0)), locationItem);
			int minEnlargementAreaIndex = 0;
			
			for (int i = 0; i < childrenArr.size(); i++) {
				IRTreeNode child = getNode(childrenArr.get(i));
				if (child.isLeafNode()) {
					if (getEnlargementAreaNDimensional(getNode(childrenArr.get(i)), locationItem) < minEnlargementArea) {
						minEnlargementArea = getEnlargementAreaNDimensional(getNode(childrenArr.get(i)), locationItem);
						minEnlargementAreaIndex = i;
					}
				}
			}
			
//			logger.log("min enlargement area for " + locationItem + " is " + minEnlargementArea + ", index=" + minEnlargementAreaIndex);
			
			IHyperRectangle sumRectangle = HyperRectangleBase.sumRectanglesNDimensional(node.getRectangle(), locationItem);
			node.setRectangle(sumRectangle);
			cache.updateNode(node.getNodeId(), null, null, null, node.getRectangle().getJson().toJSONString());
			addToRectangle(getNode(node.getParent()), node.getRectangle());
			
			insertNDimensional(locationItem, getNode(childrenArr.get(minEnlargementAreaIndex)));
		}
	}

	private int getEnlargementAreaNDimensional(IRTreeNode node, ILocationItem item) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + item);
			return 0;
		}
		
		IHyperRectangle r = node.getRectangle();
		
		List<Integer> min = new ArrayList<Integer>();
		List<Integer> max = new ArrayList<Integer>();
		
		for (int i = 0; i < numDimensions; i++) {
			min.add(Math.min(Math.min(item.getDim(i),  r.getDim1(i)), r.getDim2(i)));
			max.add(Math.max(Math.max(item.getDim(i),  r.getDim1(i)), r.getDim2(i)));
		}
		
		IHyperRectangle newRect = new RectangleND(numDimensions);
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
	public Map<IHyperRectangle, List<ILocationItem>> search(IHyperRectangle searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		long time = System.currentTimeMillis();
		Map<IHyperRectangle, List<ILocationItem>> result = new HashMap<IHyperRectangle, List<ILocationItem>>();
		searchNDimensional(searchRectangle, getNode(treeName), result, 0);
		
		
		logger.log("SEARCH consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}

	private void searchNDimensional(IHyperRectangle searchRectangle, IRTreeNode node, Map<IHyperRectangle, List<ILocationItem>> result, int depth) {
		
		if (node == null) {
			return;
		}
		
		node.getRectangle().setLevel(depth);
		if (node.isLeafNode()) {
			
			for (ILocationItem item : node.getLocationItems()) {
				
				boolean searchRectangleContains = true;
				
				for (int i = 0; i < numDimensions; i++) {
					if (!(item.getDim(i) > searchRectangle.getDim1(i) && item.getDim(i) < searchRectangle.getDim2(i))) {
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
						result.put(searchRectangle, new ArrayList<ILocationItem>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					IHyperRectangle r = getNode(child).getRectangle();
					
					if (HyperRectangleBase.rectanglesOverlapNDimensional(r, searchRectangle)) {
						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
						result.put(r, new ArrayList<ILocationItem>());
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
	public void delete(ILocationItem toDelete) {
		deleteNDimensional(toDelete, getNode(treeName));
	}
	
	private void deleteNDimensional(ILocationItem toDelete, IRTreeNode node) {
		
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
					IRTreeNode child = getNode(s);
					IHyperRectangle r = child.getRectangle();
					if (r.containsPoint(toDelete)) {
						deleteNDimensional(toDelete, child);
					}
				}
			}
		}
	}

	
}
