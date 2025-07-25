package rtree.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.item.LocationItemBase;
import rtree.log.LogLevel;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;

public class SplitQuadratic<T extends IRType<T>> extends SplitBehaviorBase<T> {
	
	final String description;
	Class<T> className;
	
	public SplitQuadratic(Class<T> className) {
		description = "ND Quadratic Split";
		this.className = className;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void splitLeafNode(IRTreeNode<T> node, ILocationItem<T> locationItem) {
		
		// full leaf node, so split leaf node
		
		// current node location items
		List<ILocationItem<T>> nodeItems = node.getLocationItems();
		
		// add the new location item that caused the split
		nodeItems.add(locationItem);
		
		// find worst combination of two locations, use these as the seeds for the rest of the location items.
		double maxArea = 0;
		int seed1 = 0;
		int seed2 = 0;
		for (int i = 0; i < nodeItems.size(); i++) {
			for (int j = 0; j < nodeItems.size(); j++) {
				
				double a = LocationItemBase.space(nodeItems.get(i), nodeItems.get(j));
				if (a > maxArea) {
					maxArea = a;
					seed1 = i;
					seed2 = j;
				}
			}
		}
		
		// first create two new nodes, with new IDs
		IRTreeNode<T> node1 = new RTreeNode<T>(UUID.randomUUID().toString(), null, null, cache, logger, className);
		IRTreeNode<T> node2 = new RTreeNode<T>(UUID.randomUUID().toString(), null, null, cache, logger, className);
		
		// the current node's parent's children will be replaced with the new nodes created above
		ArrayList<String> nodeParentSplitChildren = new ArrayList<String>();
		nodeParentSplitChildren.add(node1.getNodeId());
		nodeParentSplitChildren.add(node2.getNodeId());
		

		if (node.getParent() == null) { // node is root (parent is null)
			
			IRTreeNode<T> root = cache.getNode(treeName);
			
			// set the root's children to the new nodes, and the new nodes' parent to the root
			root.setChildren(nodeParentSplitChildren);
			node1.setParent(treeName);
			node2.setParent(treeName);
	        
			// expand the rectangle with the new location item, and set the root's rectangle to the new rectangle
	        IHyperRectangle<T> rootRectangle = HyperRectangleBase.sumRectanglesNDimensional(node.getRectangle(), locationItem);
	        root.setRectangle(rootRectangle);
	        cache.updateNode(treeName, root);
			
		} else { // parent is not null
						
			IRTreeNode<T> nodeParent = cache.getNode(node.getParent());
			
			// the node parent may have other children
			List<String> nodeParentExistingChildren = new ArrayList<String>();
			for (String s : nodeParent.getChildren()) {
				if (!s.equals(node.getNodeId())) {
					nodeParentExistingChildren.add(s);
				}
			}
			
			// add the new nodes to the parents children
			nodeParentExistingChildren.add(node1.getNodeId());
			nodeParentExistingChildren.add(node2.getNodeId());
			nodeParent.setChildren(nodeParentExistingChildren);
			
			
			// 
			nodeParent.updateRectangle();
			
			cache.updateNode(nodeParent.getNodeId(), nodeParent);
			
			node1.setParent(nodeParent.getNodeId());
			node2.setParent(nodeParent.getNodeId());
			
		}
		
		// first add the two seeds to the new nodes
		node1.getLocationItems().add(nodeItems.get(seed1));
		node2.getLocationItems().add(nodeItems.get(seed2));
		
		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
			node1.getRectangle().setDim1(i, nodeItems.get(seed1).getDim(i));
			node1.getRectangle().setDim2(i, nodeItems.get(seed1).getDim(i));
			
			node2.getRectangle().setDim1(i, nodeItems.get(seed2).getDim(i));
			node2.getRectangle().setDim2(i, nodeItems.get(seed2).getDim(i));
		}
		
		// then distribute the rest of the location items to the two new nodes
		// if the rectangle of the first seed is smaller than the rectangle of the second seed, add it to node1, otherwise add it to node2
		//
		for (int i = 0; i < nodeItems.size(); i++) {
			if (i != seed1 && i != seed2) {
				
				IHyperRectangle<T> r1 = HyperRectangleBase.twoPointsRectangles(nodeItems.get(seed1), nodeItems.get(i));
				IHyperRectangle<T> r2 = HyperRectangleBase.twoPointsRectangles(nodeItems.get(seed2), nodeItems.get(i));
				
				logger.log("[SPLIT-LEAF] " + "COMPARE seeds: " + nodeItems.get(i) + " r1.getArea(): " + r1.getSpace() + " node1 area:" + node1.getRectangle().getSpace());
				logger.log("[SPLIT-LEAF] " + "COMPARE seeds: " + nodeItems.get(i) + " r2.getArea(): " + r2.getSpace() + " node2 area:" + node2.getRectangle().getSpace());
				
				if (r1.getSpace() < r2.getSpace()) {
					node1.getLocationItems().add(nodeItems.get(i));
					
				} else {
					node2.getLocationItems().add(nodeItems.get(i));
					
				}
			}
		}
		
		// then add the new nodes to the cache and update the rectangles
		cache.putNode(node1.getNodeId(), node1);
		cache.putNode(node2.getNodeId(), node2);
		
		node1.updateRectangle();
		node2.updateRectangle();
		
		// split the branch nodes if necessary
		splitBranchNode(cache.getNode(node1.getParent()));
		
		cache.printCache();
	}


	@SuppressWarnings("unchecked")
	public void splitBranchNode(IRTreeNode<T> node) {
		
		if (node == null) {
			return;
		}
		
		List<String> nodesChildren = node.getChildren();
		
		
		if (nodesChildren != null && nodesChildren.size() > maxChildren) {
			
			logger.log("@~SPLIT BRANCH: " + " nodesChildren.size(): " + nodesChildren.size() + ", maxChildren: " + maxChildren);
			logger.log("@~SPLIT BRANCH: " + "***** Oops too many children, let's split the branch " + node.getNodeId());
			branchSplit = true;
			
			
			double maxArea = 0;
			int index1 = 0; // seed 1
			int index2 = 0; // seed 2
			
			List<IHyperRectangle<T>> nodeChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
			
			for (int i = 0; i < nodesChildren.size(); i++) {
				nodeChildrenRectangles.add(cache.getNode(nodesChildren.get(i)).getRectangle());
			}
			
			// find the rectangle that is the largest combination of any 2 children - quadratic
			for (int i = 0; i < nodeChildrenRectangles.size(); i++) {
				for (int j = 0; j < nodeChildrenRectangles.size(); j++) {
					double a = HyperRectangleBase.areaRectangles(nodeChildrenRectangles.get(i), nodeChildrenRectangles.get(j));
					if (a > maxArea) {
						maxArea = a;
						index1 = i;
						index2 = j;
					}
				}
			}
			
			// distribute the node's children to the node's parent's newly split children
			List<String> nodesParentsNewChild1Children = new ArrayList<String>();
			List<String> nodesParentsNewChild2Children = new ArrayList<String>();
			nodesParentsNewChild1Children.add(nodesChildren.get(index1));
			nodesParentsNewChild2Children.add(nodesChildren.get(index2));
			
			logger.log("@~SPLIT BRANCH: " + "The two rectangle seeds are " + nodeChildrenRectangles.get(index1) + " (" + nodesChildren.get(index1) + ") and " + nodeChildrenRectangles.get(index2) + " (" + nodesChildren.get(index2) + ")");
			logger.log("@~SPLIT BRANCH: " + "SEED index1: " + nodesChildren.get(index1));
			logger.log("@~SPLIT BRANCH: " + "SEED index2: " + nodesChildren.get(index2));
			
			// add the node's children to the node's parent's new child1 or child2 children by smallest combined rectangle 
			for (int i = 0; i < nodesChildren.size(); i++) {
				if (i != index1 && i != index2) {
					if (HyperRectangleBase.areaRectangles(cache.getNode(nodesChildren.get(index1)).getRectangle(), cache.getNode(nodesChildren.get(i)).getRectangle()) <  HyperRectangleBase.areaRectangles(cache.getNode(nodesChildren.get(index2)).getRectangle(), cache.getNode(nodesChildren.get(i)).getRectangle())) {
						if (!nodesParentsNewChild1Children.contains(nodesChildren.get(i))) {
							nodesParentsNewChild1Children.add(nodesChildren.get(i));
						}
					} else {
						if (!nodesParentsNewChild2Children.contains(nodesChildren.get(i))) {
							nodesParentsNewChild2Children.add(nodesChildren.get(i));
						}
					}
				}
			}
			logger.log("@~SPLIT BRANCH: " + " nodesParentsNewChild1Children: " + nodesParentsNewChild1Children);
			logger.log("@~SPLIT BRANCH: " + " nodesParentsNewChild2Children: " + nodesParentsNewChild2Children);
			
			String nodesParentsNewChild1Id = UUID.randomUUID().toString();
			String nodesParentsNewChild2Id = UUID.randomUUID().toString();
			
			IRTreeNode<T> nodesParentsNewChild1 = new RTreeNode<T>(nodesParentsNewChild1Id, null, null, cache, logger, className);
			IRTreeNode<T> nodesParentsNewChild2 = new RTreeNode<T>(nodesParentsNewChild2Id, null, null, cache, logger, className);
			
			List<IRTreeNode<T>> nodesParentsNewChildren = new ArrayList<IRTreeNode<T>>();
			nodesParentsNewChildren.add(nodesParentsNewChild1);
			nodesParentsNewChildren.add(nodesParentsNewChild2);
			nodesParentsNewChild1.setChildren(nodesParentsNewChild1Children);
			nodesParentsNewChild2.setChildren(nodesParentsNewChild2Children);
			for (String child : nodesParentsNewChild1Children) {
				cache.getNode(child).setParent(nodesParentsNewChild1.getNodeId());
			}
			for (String child : nodesParentsNewChild2Children) {
				cache.getNode(child).setParent(nodesParentsNewChild2.getNodeId());
			}
			
			
			if (node.getNodeId().equals(treeName)) {
				
				logger.log("@~SPLIT BRANCH: " + "we're at the root");
				

				
				JSONArray rootsNewChildrenArr = new JSONArray();
				rootsNewChildrenArr.add(nodesParentsNewChild1.getNodeId());
				rootsNewChildrenArr.add(nodesParentsNewChild2.getNodeId());
				
				
				
				logger.log("@~SPLIT BRANCH: " + "*****rootsNewChildrenArr: " + rootsNewChildrenArr);
				IRTreeNode<T> rootTemp = getNode(treeName);
				rootTemp.setChildrenJSON(rootsNewChildrenArr.toJSONString());
//				cache.updateNode(treeName, rootsNewChildrenArr.toJSONString(), null, null, cache.getNode(treeName).getRectangle().getJson().toJSONString());
				cache.updateNode(treeName, rootTemp);

				nodesParentsNewChild1.setParent(treeName);
				nodesParentsNewChild2.setParent(treeName);
				
				List<IHyperRectangle<T>> listRect1 = new ArrayList<IHyperRectangle<T>>();
				for (String s : nodesParentsNewChild1.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				IHyperRectangle<T> node1SumRect = HyperRectangleBase.sumRectangles(listRect1);
				
				List<IHyperRectangle<T>> listRect2 = new ArrayList<IHyperRectangle<T>>();
				for (String s : nodesParentsNewChild2.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				IHyperRectangle<T> node2SumRect = HyperRectangleBase.sumRectangles(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				
				IRTreeNode<T> node1Temp = cache.getNode(nodesParentsNewChild1.getNodeId());
				node1Temp.setChildrenJSON(nodesParentsNewChild1.getChildrenJSON().toJSONString());
				node1Temp.setRectangle(node1SumRect);
				node1Temp.setParent(treeName);
				IRTreeNode<T> node2Temp = cache.getNode(nodesParentsNewChild2.getNodeId());
				node2Temp.setRectangle(node2SumRect);
				node2Temp.setChildrenJSON(nodesParentsNewChild2.getChildrenJSON().toJSONString());
				node2Temp.setParent(treeName);
				
//				cache.addNode(nodesParentsNewChild1.getNodeId(), nodesParentsNewChild1.getChildrenJSON().toJSONString(), treeName, null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
//				cache.addNode(nodesParentsNewChild2.getNodeId(), nodesParentsNewChild2.getChildrenJSON().toJSONString(), treeName, null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				cache.putNode(nodesParentsNewChild1.getNodeId(), node1Temp);
				cache.putNode(nodesParentsNewChild2.getNodeId(), node2Temp);
				
				for (String child : nodesParentsNewChild1Children) {
					IRTreeNode<T> tempNode = cache.getNode(child);
					tempNode.setParent(nodesParentsNewChild1.getNodeId());
					
//					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
					cache.putNode(child, tempNode);
					
				}
				for (String child : nodesParentsNewChild2Children) {
					IRTreeNode<T> tempNode = cache.getNode(child);
					tempNode.setParent(nodesParentsNewChild2.getNodeId());
//					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
					cache.putNode(child, tempNode);
				}
				
				splitBranchNode(getNode(treeName));
				
			} else { // not at root
				
				logger.log("@~SPLIT BRANCH: " + " not at root");
				node = cache.getNode(node.getNodeId());
				IRTreeNode<T> nodesParent = cache.getNode(node.getParent());
				logger.log("@~SPLIT BRANCH: " + "nodesParent.children: " + nodesParent.getChildren());
				nodesParent.getChildren().add(nodesParentsNewChild1.getNodeId());
				nodesParent.getChildren().add(nodesParentsNewChild2.getNodeId());
				nodesParent.getChildren().remove(node.getNodeId());
				
				JSONArray nodesParentsNewChildrenArr = new JSONArray();
				nodesParentsNewChildrenArr.add(nodesParentsNewChild1.getNodeId());
				nodesParentsNewChildrenArr.add(nodesParentsNewChild2.getNodeId());
				
				for (String s : nodesParent.getChildren()) {
					if (!node.getNodeId().equals(s)) {
						if (!nodesParentsNewChildrenArr.contains(s)) {
							nodesParentsNewChildrenArr.add(s);
						}
					}
				}
				
				logger.log("@~SPLIT BRANCH: " + "*****nodesParentsNewChildrenArr: " + nodesParentsNewChildrenArr);
				
				IRTreeNode<T> nodesParentTemp = cache.getNode(nodesParent.getNodeId());
				nodesParentTemp.setChildrenJSON(nodesParentsNewChildrenArr.toJSONString());
//				cache.updateNode(nodesParent.getNodeId(), nodesParentsNewChildrenArr.toJSONString(), null, null, cache.getNode(nodesParent.getNodeId()).getRectangle().getJson().toJSONString());
				cache.updateNode(nodesParent.getNodeId(), nodesParentTemp);
				
				nodesParent = cache.getNode(nodesParent.getNodeId());
				
				nodesParentsNewChild1.setParent(nodesParent.getNodeId());
				nodesParentsNewChild2.setParent(nodesParent.getNodeId());
				
				List<IHyperRectangle<T>> listRect1 = new ArrayList<IHyperRectangle<T>>();
				for (String s : nodesParentsNewChild1.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				IHyperRectangle<T> node1SumRect = HyperRectangleBase.sumRectangles(listRect1);
				
				List<IHyperRectangle<T>> listRect2 = new ArrayList<IHyperRectangle<T>>();
				for (String s : nodesParentsNewChild2.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				IHyperRectangle<T> node2SumRect = HyperRectangleBase.sumRectangles(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				
				IRTreeNode<T> node1Temp = cache.getNode(nodesParentsNewChild1.getNodeId());
				node1Temp.setChildrenJSON(nodesParentsNewChild1.getChildrenJSON().toJSONString());
				node1Temp.setRectangle(node1SumRect);
				node1Temp.setParent(nodesParent.getNodeId());
				IRTreeNode<T> node2Temp = cache.getNode(nodesParentsNewChild2.getNodeId());
				node2Temp.setRectangle(node2SumRect);
				node2Temp.setChildrenJSON(nodesParentsNewChild2.getChildrenJSON().toJSONString());
				node2Temp.setParent(nodesParent.getNodeId());
				
//				cache.addNode(nodesParentsNewChild1.getNodeId(), nodesParentsNewChild1.getChildrenJSON().toJSONString(), nodesParent.getNodeId(), null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
//				cache.addNode(nodesParentsNewChild2.getNodeId(), nodesParentsNewChild2.getChildrenJSON().toJSONString(), nodesParent.getNodeId(), null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				cache.putNode(nodesParentsNewChild1.getNodeId(), node1Temp);
				cache.putNode(nodesParentsNewChild2.getNodeId(), node2Temp);
				
				for (String child : nodesParentsNewChild1Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild1Children[]: " + child);
					IRTreeNode<T> tempNode = cache.getNode(child);
					tempNode.setParent(nodesParentsNewChild1.getNodeId());
//					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
					cache.putNode(child, tempNode);
				}
				for (String child : nodesParentsNewChild2Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild2Children[]: " + child);
					IRTreeNode<T> tempNode = cache.getNode(child);
					tempNode.setParent(nodesParentsNewChild2.getNodeId());
//					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
					cache.putNode(child, tempNode);
				}
				
				splitBranchNode(nodesParent);
			}
		}
	}

	
}
