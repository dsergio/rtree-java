package rtree.tree.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;

import rtree.item.ILocationItem;
import rtree.item.LocationItemBase;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.item.generic.LocationItemBaseGeneric;
import rtree.log.LogLevel;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.generic.HyperRectangleBaseGeneric;
import rtree.rectangle.generic.IHyperRectangleGeneric;

public class SplitQuadraticNDGeneric<T extends IRType<T>> extends SplitBehaviorBaseGeneric<T> {
	
	final String description;
	Class<T> clazz;
	
	public SplitQuadraticNDGeneric(Class<T> clazz) {
		description = "ND Quadratic Split";
		this.clazz = clazz;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void splitLeafNode(IRTreeNodeGeneric<T> node, ILocationItemGeneric<T> locationItem) {
		
		// full leaf node, so split leaf node
		logger.log("#SPLIT-LEAF: " + " We're full, splitting leaf node: " + node.getNodeId() + " node.getParent(): " + node.getParent() + " node.getNumberOfItems(): " + node.getNumberOfItems() + " items:");
		for (ILocationItemGeneric<T> item : node.getPoints()) {
			logger.log(item + " ***** ");
		}
		logger.log();
		
		double maxArea = 0;
		int index1 = 0; // seed 1
		int index2 = 0; // seed 2
		
		List<ILocationItemGeneric<T>> points = node.getPoints();
		points.add(locationItem);
		
		// find worst combination of two points, use these as the seeds for the rest of the points
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < points.size(); j++) {
				
				double a = LocationItemBaseGeneric.space(points.get(i), points.get(j));
				if (a > maxArea) {
					maxArea = a;
					index1 = i;
					index2 = j;
				}
			}
		}
		
		logger.log("#SPLIT-LEAF: " + "Max area: " + maxArea + " points.get(index1): " + points.get(index1) + " points.get(index2): " + points.get(index2));
		
		String node1Id = UUID.randomUUID().toString();
		String node2Id = UUID.randomUUID().toString();
		
		IRTreeNodeGeneric<T> node1 = new RTreeNodeNDGeneric<T>(node1Id, null, null, cache, logger, clazz);
		IRTreeNodeGeneric<T> node2 = new RTreeNodeNDGeneric<T>(node2Id, null, null, cache, logger, clazz);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.getNodeId());
		newChildren.add(node2.getNodeId());
		

		if (node.getParent() == null) {
			
			logger.log("#SPLIT-LEAF: " + "we're at the root");
			logger.log("#SPLIT-LEAF: " + "root.children: " + getNode(treeName).getChildren());
			
			
			IRTreeNodeGeneric<T> rootTemp = new RTreeNodeNDGeneric<T>(treeName, null, null, cache, logger, clazz);
			rootTemp.setChildren(newChildren);
			
			node1.setParent(treeName);
			node2.setParent(treeName);
			
			
//	        JSONArray childrenArr = root.getChildrenJSON();
			JSONArray childrenArr = rootTemp.getChildrenJSON();
	        logger.log("trying to add " + childrenArr.toJSONString());
			cache.updateNode(treeName, childrenArr.toJSONString(), null, "[]", HyperRectangleBaseGeneric.sumRectanglesNDimensional(node.getRectangle(), locationItem).getJson().toJSONString());
			
			
		} else { // parent is not null
			
			logger.log("#SPLIT-LEAF: " + "parent is not null");
			
			IRTreeNodeGeneric<T> nodesParent = cache.getNode(node.getParent());
			logger.log("#SPLIT-LEAF: " + "nodesParent.children: " + nodesParent.getChildren());
			
			List<String> nodesParentsChildrenList = new ArrayList<String>();
			for (String s : nodesParent.getChildren()) {
				if (!s.equals(node.getNodeId())) {
					nodesParentsChildrenList.add(s);
				}
			}
			nodesParentsChildrenList.add(node1.getNodeId());
			nodesParentsChildrenList.add(node2.getNodeId());
			nodesParent.setChildren(nodesParentsChildrenList);
			logger.log("#SPLIT-LEAF: " + "node's parent's new children: " + nodesParent.getChildrenJSON().toJSONString());
			nodesParent.updateRectangle();
			cache.updateNode(nodesParent.getNodeId(), nodesParent.getChildrenJSON().toJSONString(), nodesParent.getParent(), "[]", nodesParent.getRectangle().getJson().toJSONString());
			
			node1.setParent(nodesParent.getNodeId());
			node2.setParent(nodesParent.getNodeId());
			
		}
		
		
		node1.getLocationItems().add(points.get(index1));
		node2.getLocationItems().add(points.get(index2));
		
		for (int i = 0; i < locationItem.getNumberDimensions(); i++) {
			node1.getRectangle().setDim1(i, points.get(index1).getDim(i));
			node1.getRectangle().setDim2(i, points.get(index1).getDim(i));
			
			node2.getRectangle().setDim1(i, points.get(index2).getDim(i));
			node2.getRectangle().setDim2(i, points.get(index2).getDim(i));
		}
		
		
		for (int i = 0; i < points.size(); i++) {
			if (i != index1 && i != index2) {
				
				IHyperRectangleGeneric<T> r1 = HyperRectangleBaseGeneric.twoPointsRectanglesNDimensional(points.get(index1), points.get(i));
				IHyperRectangleGeneric<T> r2 = HyperRectangleBaseGeneric.twoPointsRectanglesNDimensional(points.get(index2), points.get(i));
				
				logger.log("#SPLIT-LEAF: " + "COMPARE seeds: " + points.get(i) + " r1.getArea(): " + r1.getSpace() + " node1 area:" + node1.getRectangle().getSpace());
				logger.log("#SPLIT-LEAF: " + "COMPARE seeds: " + points.get(i) + " r2.getArea(): " + r2.getSpace() + " node2 area:" + node2.getRectangle().getSpace());
				
				if (r1.getSpace() < r2.getSpace()) {
					node1.getLocationItems().add(points.get(i));
					
				} else {
					node2.getLocationItems().add(points.get(i));
					
				}
			}
		}
		
		logger.log("#SPLIT-LEAF: " + " Splitting " + node.getNodeId());
		logger.log("#SPLIT-LEAF: " + "POINTS DISTRIBUTION \n node1: " + node1.getNodeId() + "\n...node1");
		for (ILocationItemGeneric<T> i : node1.getPoints()) {
			logger.logExact(" | " + i);
		}
		logger.log();
		
		logger.log("#SPLIT-LEAF: " + " node2: " + node2.getNodeId() + "\n...node2");
		for (ILocationItemGeneric<T> i : node2.getPoints()) {
			logger.logExact(" | " + i);
			
		}
		logger.log();
		
		
		
		logger.log("#SPLIT-LEAF: " + "adding to the cache... node1.getItemsJSON().toJSONString(): " + node1.getItemsJSON().toJSONString());
		logger.log("#SPLIT-LEAF: " + "adding to the cache... node2.getItemsJSON().toJSONString(): " + node2.getItemsJSON().toJSONString());
		cache.addNode(node1.getNodeId(), null, node1.getParent(), node1.getItemsJSON().toJSONString(), node1.getRectangle().getJson().toJSONString(), node1);
		cache.addNode(node2.getNodeId(), null, node1.getParent(), node2.getItemsJSON().toJSONString(), node2.getRectangle().getJson().toJSONString(), node2);
		
		node1.updateRectangle();
		node2.updateRectangle();
		
		splitBranchNode(cache.getNode(node1.getParent()));
		
		logger.log();
		cache.printCache();
	}


	@SuppressWarnings("unchecked")
	public void splitBranchNode(IRTreeNodeGeneric<T> node) {
		
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
			
			List<IHyperRectangleGeneric<T>> nodeChildrenRectangles = new ArrayList<IHyperRectangleGeneric<T>>();
			
			for (int i = 0; i < nodesChildren.size(); i++) {
				nodeChildrenRectangles.add(cache.getNode(nodesChildren.get(i)).getRectangle());
			}
			
			// find the rectangle that is the largest combination of any 2 children - quadratic
			for (int i = 0; i < nodeChildrenRectangles.size(); i++) {
				for (int j = 0; j < nodeChildrenRectangles.size(); j++) {
					double a = HyperRectangleBaseGeneric.areaRectanglesNDimensional(nodeChildrenRectangles.get(i), nodeChildrenRectangles.get(j));
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
					if (HyperRectangleBaseGeneric.areaRectanglesNDimensional(cache.getNode(nodesChildren.get(index1)).getRectangle(), cache.getNode(nodesChildren.get(i)).getRectangle()) <  HyperRectangleBaseGeneric.areaRectanglesNDimensional(cache.getNode(nodesChildren.get(index2)).getRectangle(), cache.getNode(nodesChildren.get(i)).getRectangle())) {
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
			
			IRTreeNodeGeneric<T> nodesParentsNewChild1 = new RTreeNodeNDGeneric<T>(nodesParentsNewChild1Id, null, null, cache, logger, clazz);
			IRTreeNodeGeneric<T> nodesParentsNewChild2 = new RTreeNodeNDGeneric<T>(nodesParentsNewChild2Id, null, null, cache, logger, clazz);
			
			List<IRTreeNodeGeneric<T>> nodesParentsNewChildren = new ArrayList<IRTreeNodeGeneric<T>>();
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
				cache.updateNode(treeName, rootsNewChildrenArr.toJSONString(), null, null, cache.getNode(treeName).getRectangle().getJson().toJSONString());

				nodesParentsNewChild1.setParent(treeName);
				nodesParentsNewChild2.setParent(treeName);
				
				List<IHyperRectangleGeneric<T>> listRect1 = new ArrayList<IHyperRectangleGeneric<T>>();
				for (String s : nodesParentsNewChild1.getChildren()) {
					IHyperRectangleGeneric<T> r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				IHyperRectangleGeneric<T> node1SumRect = HyperRectangleBaseGeneric.sumRectanglesNDimensional(listRect1);
				
				List<IHyperRectangleGeneric<T>> listRect2 = new ArrayList<IHyperRectangleGeneric<T>>();
				for (String s : nodesParentsNewChild2.getChildren()) {
					IHyperRectangleGeneric<T> r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				IHyperRectangleGeneric<T> node2SumRect = HyperRectangleBaseGeneric.sumRectanglesNDimensional(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				cache.addNode(nodesParentsNewChild1.getNodeId(), nodesParentsNewChild1.getChildrenJSON().toJSONString(), treeName, null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
				cache.addNode(nodesParentsNewChild2.getNodeId(), nodesParentsNewChild2.getChildrenJSON().toJSONString(), treeName, null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				for (String child : nodesParentsNewChild1Children) {
					cache.getNode(child).setParent(nodesParentsNewChild1.getNodeId());
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : nodesParentsNewChild2Children) {
					cache.getNode(child).setParent(nodesParentsNewChild2.getNodeId());
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(getNode(treeName));
				
			} else { // not at root
				
				logger.log("@~SPLIT BRANCH: " + " not at root");
				node = cache.getNode(node.getNodeId());
				IRTreeNodeGeneric<T> nodesParent = cache.getNode(node.getParent());
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
				cache.updateNode(nodesParent.getNodeId(), nodesParentsNewChildrenArr.toJSONString(), null, null, cache.getNode(nodesParent.getNodeId()).getRectangle().getJson().toJSONString());
				nodesParent = cache.getNode(nodesParent.getNodeId());
				
				nodesParentsNewChild1.setParent(nodesParent.getNodeId());
				nodesParentsNewChild2.setParent(nodesParent.getNodeId());
				
				List<IHyperRectangleGeneric<T>> listRect1 = new ArrayList<IHyperRectangleGeneric<T>>();
				for (String s : nodesParentsNewChild1.getChildren()) {
					IHyperRectangleGeneric<T> r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				IHyperRectangleGeneric<T> node1SumRect = HyperRectangleBaseGeneric.sumRectanglesNDimensional(listRect1);
				
				List<IHyperRectangleGeneric<T>> listRect2 = new ArrayList<IHyperRectangleGeneric<T>>();
				for (String s : nodesParentsNewChild2.getChildren()) {
					IHyperRectangleGeneric<T> r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				IHyperRectangleGeneric<T> node2SumRect = HyperRectangleBaseGeneric.sumRectanglesNDimensional(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				cache.addNode(nodesParentsNewChild1.getNodeId(), nodesParentsNewChild1.getChildrenJSON().toJSONString(), nodesParent.getNodeId(), null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
				cache.addNode(nodesParentsNewChild2.getNodeId(), nodesParentsNewChild2.getChildrenJSON().toJSONString(), nodesParent.getNodeId(), null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				for (String child : nodesParentsNewChild1Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild1Children[]: " + child);
					cache.getNode(child).setParent(nodesParentsNewChild1.getNodeId());
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : nodesParentsNewChild2Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild2Children[]: " + child);
					cache.getNode(child).setParent(nodesParentsNewChild2.getNodeId());
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.getNodeId(), cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(nodesParent);
			}
		}
	}

	
}
