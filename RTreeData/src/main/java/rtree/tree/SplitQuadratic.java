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
				
				logger.log("COMPARE seeds: " + nodeItems.get(i) + " r1.getArea(): " + r1.getSpace() + " node1 area:" + node1.getRectangle().getSpace(), "splitLeafNode", LogLevel.DEBUG, true);
				logger.log("COMPARE seeds: " + nodeItems.get(i) + " r2.getArea(): " + r2.getSpace() + " node2 area:" + node2.getRectangle().getSpace(), "splitLeafNode", LogLevel.DEBUG, true);
				
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
		
		node.setLocationItems(new ArrayList<ILocationItem<T>>());
		cache.updateNode(node.getNodeId(), node);
		
		node1.updateRectangle();
		node2.updateRectangle();
		
		// split the branch nodes if necessary
		splitBranchNode(cache.getNode(node1.getParent()));
		
		cache.printCache();
	}


	@Override
	public void splitBranchNode(IRTreeNode<T> node) {
		
		if (node == null) {
			return;
		}
		
		List<String> nodeChildren = node.getChildren();
		
		
		if (nodeChildren != null && nodeChildren.size() > maxChildren) {
			
			logger.log("nodeChildren.size(): " + nodeChildren.size() + ", maxChildren: " + maxChildren, "splitBranchNode", LogLevel.DEBUG, true);
			logger.log("***** Oops too many children, let's split the branch " + node.getNodeId(), "splitBranchNode", LogLevel.DEBUG, true);
			branchSplit = true;
			
			
			double maxArea = 0;
			int seed1 = 0;
			int seed2 = 0;
			
			List<IHyperRectangle<T>> nodeChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
			
			for (int i = 0; i < nodeChildren.size(); i++) {
				nodeChildrenRectangles.add(cache.getNode(nodeChildren.get(i)).getRectangle());
			}
			
			// find the rectangle that is the largest combination of any 2 children - quadratic
			for (int i = 0; i < nodeChildrenRectangles.size(); i++) {
				for (int j = 0; j < nodeChildrenRectangles.size(); j++) {
					double a = HyperRectangleBase.areaRectangles(nodeChildrenRectangles.get(i), nodeChildrenRectangles.get(j));
					if (a > maxArea) {
						maxArea = a;
						seed1 = i;
						seed2 = j;
					}
				}
			}
			
			// distribute the node's children to the node's parent's newly split children
			List<String> nodeParentChild1Children = new ArrayList<String>();
			List<String> nodeParentChild2Children = new ArrayList<String>();
			nodeParentChild1Children.add(nodeChildren.get(seed1));
			nodeParentChild2Children.add(nodeChildren.get(seed2));
			
			logger.log("The two rectangle seeds are " + nodeChildrenRectangles.get(seed1) + " (" + nodeChildren.get(seed1) + 
					") and " + nodeChildrenRectangles.get(seed2) + " (" + nodeChildren.get(seed2) + ")", "splitBranchNode", LogLevel.DEBUG, true);
			logger.log("SEED index1: " + nodeChildren.get(seed1), "splitBranchNode", LogLevel.DEBUG, true);
			logger.log("SEED index2: " + nodeChildren.get(seed2), "splitBranchNode", LogLevel.DEBUG, true);
			
			// add the node's children to the node's parent's new child1 or child2 children by smallest combined rectangle 
			for (int i = 0; i < nodeChildren.size(); i++) {
				if (i != seed1 && i != seed2) {
					if (HyperRectangleBase.areaRectangles(cache.getNode(nodeChildren.get(seed1)).getRectangle(), cache.getNode(nodeChildren.get(i)).getRectangle()) <  HyperRectangleBase.areaRectangles(cache.getNode(nodeChildren.get(seed2)).getRectangle(), cache.getNode(nodeChildren.get(i)).getRectangle())) {
						if (!nodeParentChild1Children.contains(nodeChildren.get(i))) {
							nodeParentChild1Children.add(nodeChildren.get(i));
						}
					} else {
						if (!nodeParentChild2Children.contains(nodeChildren.get(i))) {
							nodeParentChild2Children.add(nodeChildren.get(i));
						}
					}
				}
			}
			logger.log("nodesParentsNewChild1Children: " + nodeParentChild1Children, "splitBranchNode", LogLevel.DEBUG, true);
			logger.log("nodesParentsNewChild2Children: " + nodeParentChild2Children, "splitBranchNode", LogLevel.DEBUG, true);
			
			IRTreeNode<T> child1 = new RTreeNode<T>(UUID.randomUUID().toString(), null, null, cache, logger, className);
			IRTreeNode<T> child2 = new RTreeNode<T>(UUID.randomUUID().toString(), null, null, cache, logger, className);
			
			List<IRTreeNode<T>> nodeParentChildren = new ArrayList<IRTreeNode<T>>();
			nodeParentChildren.add(child1);
			nodeParentChildren.add(child2);
			child1.setChildren(nodeParentChild1Children);
			child2.setChildren(nodeParentChild2Children);
			
			for (String child : nodeParentChild1Children) {
				IRTreeNode<T> c = cache.getNode(child);
				c.setParent(child1.getNodeId());
				cache.updateNode(c.getNodeId(), c);
			}
			for (String child : nodeParentChild2Children) {
				IRTreeNode<T> c = cache.getNode(child);
				c.setParent(child2.getNodeId());
				cache.updateNode(c.getNodeId(), c);
			}
			
			
			if (node.getNodeId().equals(treeName)) {
				
				logger.log("we're at the root", "splitBranchNode", LogLevel.DEBUG, true);
				
				IRTreeNode<T> root = getNode(treeName);
				
				root.setChildren(new ArrayList<String>());
				root.getChildren().add(child1.getNodeId());
				root.getChildren().add(child2.getNodeId());
				
				cache.updateNode(treeName, root);

				child1.setParent(treeName);
				child2.setParent(treeName);
				
				List<IHyperRectangle<T>> child1ChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
				for (String s : child1.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					child1ChildrenRectangles.add(r);
				}
				IHyperRectangle<T> child1Rectangle = HyperRectangleBase.sumRectangles(child1ChildrenRectangles);
				
				List<IHyperRectangle<T>> child2ChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
				for (String s : child2.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					child2ChildrenRectangles.add(r);
				}
				IHyperRectangle<T> child2Rectangle = HyperRectangleBase.sumRectangles(child2ChildrenRectangles);
				
				child1.setRectangle(child1Rectangle);
				child2.setRectangle(child2Rectangle);
				
				cache.putNode(child1.getNodeId(), child1);
				cache.putNode(child2.getNodeId(), child2);
				
			} else { // not at root
				
				logger.log("Not at root", "splitBranchNode", LogLevel.DEBUG, true);
				
				
				IRTreeNode<T> nodeParent = cache.getNode(node.getParent());
				
				logger.log("nodesParent.children: " + nodeParent.getChildren(), "splitBranchNode", LogLevel.DEBUG, true);
				
				nodeParent.getChildren().add(child1.getNodeId());
				nodeParent.getChildren().add(child2.getNodeId());
				nodeParent.getChildren().remove(node.getNodeId());
				
				child1.setParent(nodeParent.getNodeId());
				child2.setParent(nodeParent.getNodeId());
				
				List<IHyperRectangle<T>> child1ChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
				for (String s : child1.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					child1ChildrenRectangles.add(r);
				}
				IHyperRectangle<T> child1Rectangle = HyperRectangleBase.sumRectangles(child1ChildrenRectangles);
				
				List<IHyperRectangle<T>> child2ChildrenRectangles = new ArrayList<IHyperRectangle<T>>();
				for (String s : child2.getChildren()) {
					IHyperRectangle<T> r = cache.getNode(s).getRectangle();
					child2ChildrenRectangles.add(r);
				}
				IHyperRectangle<T> child2Rectangle = HyperRectangleBase.sumRectangles(child2ChildrenRectangles);
				
				child1.setRectangle(child1Rectangle);
				child2.setRectangle(child2Rectangle);
				
				cache.putNode(child1.getNodeId(), child1);
				cache.putNode(child2.getNodeId(), child2);
				
				splitBranchNode(nodeParent);
			}
		}
	}

	
}
