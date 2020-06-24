package rtree.tree;

import org.apache.commons.lang3.NotImplementedException;

import rtree.item.ILocationItem;

/**
 * 
 * Linear Split Algorithm
 * Implementation is in progress.
 * 
 * @author David Sergio
 *
 */
public class SplitLinear extends SplitBehavior {

	final String description;
	
	public SplitLinear() {
		description = "Linear Split";
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 */
	@Override
	public void splitLeafNode2D(RTreeNode node, ILocationItem locationItem) {
		
		// linear split
		throw new NotImplementedException("Linear splitLeafNode is not yet implemented.");
		
//		List<ILocationItem> points = node.getPoints();
//		points.add(locationItem);
//		
//		String node1Id = UUID.randomUUID().toString();
//		String node2Id = UUID.randomUUID().toString();
//		
//		RTreeNode node1 = new RTreeNode(node1Id, null, null, cache, logger);
//		RTreeNode node2 = new RTreeNode(node2Id, null, null, cache, logger);
//		ArrayList<String> newChildren = new ArrayList<String>();
//		newChildren.add(node1.nodeId);
//		newChildren.add(node2.nodeId);
//		
//
//		if (node.nodeId.equals(treeName)) {
//			
//			logger.log("#SPLIT-LEAF-LINEAR: " + "we're at the root");
//			logger.log("#SPLIT-LEAF-LINEAR: " + "root.children: " + getNode(treeName).children);
//			
//			RTreeNode rootTemp = new RTreeNode(treeName, null, null, cache, logger);
//			rootTemp.setChildren(newChildren);
//			
//			node1.setParent(treeName);
//			node2.setParent(treeName);
//			
//			JSONArray childrenArr = rootTemp.getChildrenJSON();
//	        logger.log("trying to add " + childrenArr.toJSONString());
//			cache.updateNode(treeName, childrenArr.toJSONString(), null, "[]", Rectangle.sumRectangles(node.getRectangle(), locationItem).getJson().toJSONString());
//			
//			
//		} else { // parent is not null
//			
//			logger.log("#SPLIT-LEAF-LINEAR: " + "parent is not null");
//			
//			RTreeNode nodesParent = cache.getNode(node.getParent());
//			logger.log("#SPLIT-LEAF-LINEAR: " + "nodesParent.children: " + nodesParent.children);
//			
//			List<String> nodesParentsChildrenList = new ArrayList<String>();
//			for (String s : nodesParent.getChildren()) {
//				if (!s.equals(node.nodeId)) {
//					nodesParentsChildrenList.add(s);
//				}
//			}
//			nodesParentsChildrenList.add(node1.nodeId);
//			nodesParentsChildrenList.add(node2.nodeId);
//			nodesParent.setChildren(nodesParentsChildrenList);
//			logger.log("#SPLIT-LEAF: " + "node's parent's new children: " + nodesParent.getChildrenJSON().toJSONString());
//			nodesParent.updateRectangle();
//			cache.updateNode(nodesParent.nodeId, nodesParent.getChildrenJSON().toJSONString(), nodesParent.getParent(), "[]", nodesParent.getRectangle().getJson().toJSONString());
//			
//			node1.setParent(nodesParent.nodeId);
//			node2.setParent(nodesParent.nodeId);
//			
//		}
		
	}

	/**
	 * 
	 */
	public void splitBranchNode2D(RTreeNode node) {
		throw new NotImplementedException("Linear splitBranchNode is not yet implemented.");
	}

	@Override
	public void splitLeafNodeNDimensional(RTreeNode node, ILocationItem locationItem) {
		throw new NotImplementedException("Linear splitLeafNodeNDimensional is not yet implemented.");
		
	}

	@Override
	public void splitBranchNodeNDimensional(RTreeNode node) {
		throw new NotImplementedException("Linear splitBranchNodeNDimensional is not yet implemented.");
		
	}

}
