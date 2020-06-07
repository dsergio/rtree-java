package cloudrtree;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;

import cloudrtree.ILogger.LogLevel;


/**
 * 
 * This class is the quadratic split algorithm.
 * 
 * @author David Sergio
 *
 */
public class SplitQuadratic extends SplitBehavior {
	
	final String description;
	
	public SplitQuadratic() {
		description = "Quadratic Split";
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void splitLeafNode(RTreeNode node, LocationItem locationItem) {
		
		// full leaf node, so split leaf node
		logger.log("#SPLIT-LEAF: " + " We're full, splitting leaf node: " + node.nodeId + " node.getParent(): " + node.getParent() + " node.getNumberOfItems(): " + node.getNumberOfItems() + " items:");
		for (LocationItem item : node.getPoints()) {
			System.out.print(item + " ***** ");
		}
		logger.log();
		
		int maxArea = 0;
		int index1 = 0; // seed 1
		int index2 = 0; // seed 2
		
		List<LocationItem> points = node.getPoints();
		points.add(locationItem);
		
		// find worst combination of two points, use these as the seeds for the rest of the points
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < points.size(); j++) {
				
				int a = LocationItem.area(points.get(i), points.get(j));
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
		
		RTreeNode node1 = new RTreeNode(node1Id, null, null, cache, logger);
		RTreeNode node2 = new RTreeNode(node2Id, null, null, cache, logger);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.nodeId);
		newChildren.add(node2.nodeId);
		

		if (node.getParent() == null) {
			
			logger.log("#SPLIT-LEAF: " + "we're at the root");
			logger.log("#SPLIT-LEAF: " + "root.children: " + root.children);
			
			String newRootId = treeName;
			root = new RTreeNode(newRootId, null, null, cache, logger);
			root.setRectangle(Rectangle.sumRectangles(node.getRectangle(), locationItem));
			
			root.setChildren(newChildren);
			node1.setParent(treeName);
			node2.setParent(treeName);
			
			
	        JSONArray childrenArr = root.getChildrenJSON();
	        logger.log("trying to add " + childrenArr.toJSONString());
			cache.updateNode(treeName, childrenArr.toJSONString(), null, "[]", Rectangle.sumRectangles(node.getRectangle(), locationItem).getJson().toJSONString());
			
			
		} else { // parent is not null
			
			logger.log("#SPLIT-LEAF: " + "parent is not null");
			
			RTreeNode nodesParent = cache.getNode(node.getParent());
			logger.log("#SPLIT-LEAF: " + "nodesParent.children: " + nodesParent.children);
			
			List<String> nodesParentsChildrenList = new ArrayList<String>();
			for (String s : nodesParent.getChildren()) {
				if (!s.equals(node.nodeId)) {
					nodesParentsChildrenList.add(s);
				}
			}
			nodesParentsChildrenList.add(node1.nodeId);
			nodesParentsChildrenList.add(node2.nodeId);
			nodesParent.setChildren(nodesParentsChildrenList);
			logger.log("#SPLIT-LEAF: " + "node's parent's new children: " + nodesParent.getChildrenJSON().toJSONString());
			nodesParent.updateRectangle();
			cache.updateNode(nodesParent.nodeId, nodesParent.getChildrenJSON().toJSONString(), nodesParent.getParent(), "[]", nodesParent.getRectangle().getJson().toJSONString());
			
			node1.setParent(nodesParent.nodeId);
			node2.setParent(nodesParent.nodeId);
			
		}
		
		
		node1.locationItems.add(points.get(index1));
		node2.locationItems.add(points.get(index2));
		
		
		
		for (int i = 0; i < points.size(); i++) {
			if (i != index1 && i != index2) {
				
				Rectangle r1 = Rectangle.twoPointsRectangles(points.get(index1), points.get(i));
				Rectangle r2 = Rectangle.twoPointsRectangles(points.get(index2), points.get(i));
				
				logger.log("#SPLIT-LEAF: " + "COMPARE seeds: " + points.get(i) + " r1.getArea(): " + r1.getArea() + " node1 area:" + node1.rectangle.getArea());
				logger.log("#SPLIT-LEAF: " + "COMPARE seeds: " + points.get(i) + " r2.getArea(): " + r2.getArea() + " node2 area:" + node2.rectangle.getArea());
				
				if (r1.getArea() < r2.getArea()) {
					node1.locationItems.add(points.get(i));
					
				} else {
					node2.locationItems.add(points.get(i));
					
				}
			}
		}
		
		logger.log("#SPLIT-LEAF: " + " Splitting " + node.nodeId);
		logger.log("#SPLIT-LEAF: " + "POINTS DISTRIBUTION \n node1: " + node1.nodeId + "\n...node1");
		for (LocationItem i : node1.getPoints()) {
			logger.logExact(" | " + i);
		}
		logger.log();
		
		logger.log("#SPLIT-LEAF: " + " node2: " + node2.nodeId + "\n...node2");
		for (LocationItem i : node2.getPoints()) {
			logger.logExact(" | " + i);
			
		}
		logger.log();
		
		node1.updateRectangle();
		node2.updateRectangle();
		
		logger.log("#SPLIT-LEAF: " + "adding to the cache... node1.getItemsJSON().toJSONString(): " + node1.getItemsJSON().toJSONString());
		logger.log("#SPLIT-LEAF: " + "adding to the cache... node2.getItemsJSON().toJSONString(): " + node2.getItemsJSON().toJSONString());
		cache.addNode(node1.nodeId, null, node1.getParent(), node1.getItemsJSON().toJSONString(), node1.getRectangle().getJson().toJSONString(), node1);
		cache.addNode(node2.nodeId, null, node1.getParent(), node2.getItemsJSON().toJSONString(), node2.getRectangle().getJson().toJSONString(), node2);
		
		cache.remove(node1.getParent()); // ?
		
//		logger.log("node1 num: " + node1.getNumberOfItems() + " node2 num: " + node2.getNumberOfItems());
		
		splitBranchNode(cache.getNode(node1.getParent()));
		
//		if (!node.nodeId.equals(treeName)) {
//			cache.updateNode(node.nodeId, null, null, "[]", null);
//		}
		
		logger.log();
		cache.printCache();
	}

	
	public void splitBranchNode(RTreeNode node) {
		
		LogLevel temp = logger.getLogLevel();
		logger.setLogLevel(LogLevel.DEV2);
		
		if (node == null) {
			return;
		}
		
		List<String> nodesChildren = node.getChildren();
		
		
		if (nodesChildren != null && nodesChildren.size() > maxChildren) {
			
			logger.log("@~SPLIT BRANCH: " + " nodesChildren.size(): " + nodesChildren.size() + ", maxChildren: " + maxChildren);
			logger.log("@~SPLIT BRANCH: " + "***** Oops too many children, let's split the branch " + node.nodeId);
			branchSplit = true;
			
			
			int maxArea = 0;
			int index1 = 0; // seed 1
			int index2 = 0; // seed 2
			
			List<Rectangle> nodeChildrenRectangles = new ArrayList<Rectangle>();
			
			for (int i = 0; i < nodesChildren.size(); i++) {
				nodeChildrenRectangles.add(cache.getNode(nodesChildren.get(i)).getRectangle());
			}
			
			// find the rectangle that is the largest combination of any 2 children - quadratic
			for (int i = 0; i < nodeChildrenRectangles.size(); i++) {
				for (int j = 0; j < nodeChildrenRectangles.size(); j++) {
					int a = Rectangle.areaRectangles(nodeChildrenRectangles.get(i), nodeChildrenRectangles.get(j));
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
					if (Rectangle.areaRectangles(cache.getNode(nodesChildren.get(index1)).rectangle, cache.getNode(nodesChildren.get(i)).rectangle) <  Rectangle.areaRectangles(cache.getNode(nodesChildren.get(index2)).rectangle, cache.getNode(nodesChildren.get(i)).rectangle)) {
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
			
			RTreeNode nodesParentsNewChild1 = new RTreeNode(nodesParentsNewChild1Id, null, null, cache, logger);
			RTreeNode nodesParentsNewChild2 = new RTreeNode(nodesParentsNewChild2Id, null, null, cache, logger);
			
			List<RTreeNode> nodesParentsNewChildren = new ArrayList<RTreeNode>();
			nodesParentsNewChildren.add(nodesParentsNewChild1);
			nodesParentsNewChildren.add(nodesParentsNewChild2);
			nodesParentsNewChild1.setChildren(nodesParentsNewChild1Children);
			nodesParentsNewChild2.setChildren(nodesParentsNewChild2Children);
			for (String child : nodesParentsNewChild1Children) {
				cache.getNode(child).setParent(nodesParentsNewChild1.nodeId);
			}
			for (String child : nodesParentsNewChild2Children) {
				cache.getNode(child).setParent(nodesParentsNewChild2.nodeId);
			}
			
			
			if (node.nodeId.equals(treeName)) {
				
				logger.log("@~SPLIT BRANCH: " + "we're at the root");
				

				
				JSONArray rootsNewChildrenArr = new JSONArray();
				rootsNewChildrenArr.add(nodesParentsNewChild1.nodeId);
				rootsNewChildrenArr.add(nodesParentsNewChild2.nodeId);
				
				
				root = new RTreeNode(treeName, null, null, cache, logger);
				root.children = new ArrayList<String>();
				root.children.add(nodesParentsNewChild1Id);
				root.children.add(nodesParentsNewChild2Id);
				
				logger.log("@~SPLIT BRANCH: " + "*****rootsNewChildrenArr: " + rootsNewChildrenArr);
				cache.updateNode(treeName, rootsNewChildrenArr.toJSONString(), null, null, cache.getNode(treeName).getRectangle().getJson().toJSONString());
				root = cache.getNode(treeName);
				nodesParentsNewChild1.setParent(root.nodeId);
				nodesParentsNewChild2.setParent(root.nodeId);
				
//				node1.updateRectangle();
//				node2.updateRectangle();
				List<Rectangle> listRect1 = new ArrayList<Rectangle>();
				for (String s : nodesParentsNewChild1.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				Rectangle node1SumRect = Rectangle.sumRectangles(listRect1);
				
				List<Rectangle> listRect2 = new ArrayList<Rectangle>();
				for (String s : nodesParentsNewChild2.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				Rectangle node2SumRect = Rectangle.sumRectangles(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				cache.addNode(nodesParentsNewChild1.nodeId, nodesParentsNewChild1.getChildrenJSON().toJSONString(), treeName, null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
				cache.addNode(nodesParentsNewChild2.nodeId, nodesParentsNewChild2.getChildrenJSON().toJSONString(), treeName, null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				for (String child : nodesParentsNewChild1Children) {
//					logger.log("=== childNodes1 |" + child);
					cache.getNode(child).setParent(nodesParentsNewChild1.nodeId);
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : nodesParentsNewChild2Children) {
//					logger.log("=== childNodes2 |" + child);
					cache.getNode(child).setParent(nodesParentsNewChild2.nodeId);
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(root);
				
			} else { // not at root
				
				logger.log("@~SPLIT BRANCH: " + " not at root");
				node = cache.getNode(node.nodeId);
				RTreeNode nodesParent = cache.getNode(node.getParent());
				logger.log("@~SPLIT BRANCH: " + "nodesParent.children: " + nodesParent.children);
				nodesParent.children.add(nodesParentsNewChild1.nodeId);
				nodesParent.children.add(nodesParentsNewChild2.nodeId);
				nodesParent.children.remove(node.nodeId);
				
				JSONArray nodesParentsNewChildrenArr = new JSONArray();
				nodesParentsNewChildrenArr.add(nodesParentsNewChild1.nodeId);
				nodesParentsNewChildrenArr.add(nodesParentsNewChild2.nodeId);
				
				for (String s : nodesParent.children) {
					if (!node.nodeId.equals(s)) {
						if (!nodesParentsNewChildrenArr.contains(s)) {
							nodesParentsNewChildrenArr.add(s);
						}
					}
				}
				
				logger.log("@~SPLIT BRANCH: " + "*****nodesParentsNewChildrenArr: " + nodesParentsNewChildrenArr);
				cache.updateNode(nodesParent.nodeId, nodesParentsNewChildrenArr.toJSONString(), null, null, cache.getNode(nodesParent.nodeId).getRectangle().getJson().toJSONString());
				nodesParent = cache.getNode(nodesParent.nodeId);
				
				nodesParentsNewChild1.setParent(nodesParent.nodeId);
				nodesParentsNewChild2.setParent(nodesParent.nodeId);
				
//				node1.updateRectangle();
//				node2.updateRectangle();
				List<Rectangle> listRect1 = new ArrayList<Rectangle>();
				for (String s : nodesParentsNewChild1.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				Rectangle node1SumRect = Rectangle.sumRectangles(listRect1);
				
				List<Rectangle> listRect2 = new ArrayList<Rectangle>();
				for (String s : nodesParentsNewChild2.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				Rectangle node2SumRect = Rectangle.sumRectangles(listRect2);
				
				nodesParentsNewChild1.setRectangle(node1SumRect);
				nodesParentsNewChild2.setRectangle(node2SumRect);
				cache.addNode(nodesParentsNewChild1.nodeId, nodesParentsNewChild1.getChildrenJSON().toJSONString(), nodesParent.nodeId, null, node1SumRect.getJson().toJSONString(), nodesParentsNewChild1);
				cache.addNode(nodesParentsNewChild2.nodeId, nodesParentsNewChild2.getChildrenJSON().toJSONString(), nodesParent.nodeId, null, node2SumRect.getJson().toJSONString(), nodesParentsNewChild2);
				
				for (String child : nodesParentsNewChild1Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild1Children[]: " + child);
					cache.getNode(child).setParent(nodesParentsNewChild1.nodeId);
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild1.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : nodesParentsNewChild2Children) {
					logger.log("@~SPLIT BRANCH: " + "nodesParentsNewChild2Children[]: " + child);
					cache.getNode(child).setParent(nodesParentsNewChild2.nodeId);
					cache.updateNode(child, cache.getNode(child).getChildrenJSON().toJSONString(), nodesParentsNewChild2.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(nodesParent);
			}
		}
		logger.setLogLevel(temp);
	}



	
}
