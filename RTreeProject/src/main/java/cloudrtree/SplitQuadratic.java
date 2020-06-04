package cloudrtree;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;


/**
 * 
 * This class is the quadratic split algorithm.
 * 
 * @author David Sergio
 *
 */
public class SplitQuadratic extends SplitBehavior {

	public SplitQuadratic(RTreeCache cache, int maxChildren, RTreeNode root, String treeName, ILogger logger) {
		this.cache = cache;
		this.maxChildren = maxChildren;
		this.root = root;
		this.treeName = treeName;
		this.logger = logger;
	}

	@Override
	public void splitLeafNode(RTreeNode node, LocationItem locationItem) {
		
		// full leaf node, so split leaf node
		logger.log("We're full, splitting leaf node: " + node.nodeId + " node.getParent(): " + node.getParent() + " node.getNumberOfItems(): " + node.getNumberOfItems() + " items:");
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
		
		logger.log("Max area: " + maxArea + " points.get(index1): " + points.get(index1) + " points.get(index2): " + points.get(index2));
		
		String node1Id = UUID.randomUUID().toString();
		String node2Id = UUID.randomUUID().toString();
		
		RTreeNode node1 = new RTreeNode(node1Id, null, null, cache, logger);
		RTreeNode node2 = new RTreeNode(node2Id, null, null, cache, logger);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.nodeId);
		newChildren.add(node2.nodeId);
		

		if (node.getParent() == null) {
			
			logger.log("we're at the root");
			
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
			
			RTreeNode newParent = cache.getNode(node.getParent());
			
			List<String> childrenList = new ArrayList<String>();
			for (String s : newParent.getChildren()) {
				if (!s.equals(node.nodeId)) {
					childrenList.add(s);
				}
			}
			childrenList.add(node1.nodeId);
			childrenList.add(node2.nodeId);
			newParent.setChildren(childrenList);
			logger.log("/*/*/newParent's new children: " + newParent.getChildrenJSON().toJSONString());
			newParent.updateRectangle();
			cache.updateNode(newParent.nodeId, newParent.getChildrenJSON().toJSONString(), newParent.getParent(), "[]", newParent.getRectangle().getJson().toJSONString());
			
			node1.setParent(newParent.nodeId);
			node2.setParent(newParent.nodeId);
			
		}
		
		
		node1.locationItems.add(points.get(index1));
		node2.locationItems.add(points.get(index2));
		
		
		
		for (int i = 0; i < points.size(); i++) {
			if (i != index1 && i != index2) {
				
				Rectangle r1 = Rectangle.twoPointsRectangles(points.get(index1), points.get(i));
				Rectangle r2 = Rectangle.twoPointsRectangles(points.get(index2), points.get(i));
				
				logger.log("COMPARE seeds: " + points.get(i) + " r1.getArea(): " + r1.getArea() + " node1 area:" + node1.rectangle.getArea());
				logger.log("COMPARE seeds: " + points.get(i) + " r2.getArea(): " + r2.getArea() + " node2 area:" + node2.rectangle.getArea());
				
				if (r1.getArea() < r2.getArea()) {
					node1.locationItems.add(points.get(i));
					
				} else {
					node2.locationItems.add(points.get(i));
					
				}
			}
		}
		
		logger.log("splitting " + node.nodeId);
		System.out.print("POINTS DISTRIBUTION \n node1: " + node1.nodeId + "\n...node1");
		for (LocationItem i : node1.getPoints()) {
			System.out.print(" | " + i);
		}
		logger.log();
		
		System.out.print(" node2: " + node2.nodeId + "\n...node2");
		for (LocationItem i : node2.getPoints()) {
			System.out.print(" | " + i);
			
		}
		logger.log();
		
		node1.updateRectangle();
		node2.updateRectangle();
		
		logger.log("adding to the cache... node1.getItemsJSON().toJSONString(): " + node1.getItemsJSON().toJSONString() + ", node1.getItemsJSON().toJSONString(): " + node2.getItemsJSON().toJSONString());
		cache.addNode(node1.nodeId, null, node1.getParent(), node1.getItemsJSON().toJSONString(), node1.getRectangle().getJson().toJSONString(), node1);
		cache.addNode(node2.nodeId, null, node1.getParent(), node2.getItemsJSON().toJSONString(), node2.getRectangle().getJson().toJSONString(), node2);
		
		cache.remove(node1.getParent()); // ?
		
//		logger.log("node1 num: " + node1.getNumberOfItems() + " node2 num: " + node2.getNumberOfItems());
		
		splitBranchNode(cache.getNode(node1.getParent()));
		
		if (!node.nodeId.equals(treeName)) {
			cache.updateNode(node.nodeId, null, null, "[]", null);
		}
		
		logger.log();
		cache.printCache();
	}

	
	public void splitBranchNode(RTreeNode node) {
		
		if (node == null) {
			return;
		}
		
		List<String> childNodes = node.getChildren();
		
		
		if (childNodes != null && childNodes.size() > maxChildren) {
			
			logger.log("in split branch childNodes.size(): " + childNodes.size() + ", maxChildren: " + maxChildren);
			
			logger.log("***** Oops too many children, let's split the branch childNodes.size(): " + childNodes.size() + " node: " + node.nodeId);
			branchSplit = true;
			int maxArea = 0;
			int index1 = 0; // seed 1
			int index2 = 0; // seed 2
			
			List<Rectangle> rectangles = new ArrayList<Rectangle>();
			
			for (int i = 0; i < childNodes.size(); i++) {
				rectangles.add(cache.getNode(childNodes.get(i)).getRectangle());
			}
			
			for (int i = 0; i < rectangles.size(); i++) {
				for (int j = 0; j < rectangles.size(); j++) {
					int a = Rectangle.areaRectangles(rectangles.get(i), rectangles.get(j));
					if (a > maxArea) {
						maxArea = a;
						index1 = i;
						index2 = j;
					}
				}
			}
			
			
			List<String> childNodes1 = new ArrayList<String>();
			List<String> childNodes2 = new ArrayList<String>();
			childNodes1.add(childNodes.get(index1));
			childNodes2.add(childNodes.get(index2));
			
			logger.log("The two rectangle seeds are " + rectangles.get(index1) + " (" + childNodes.get(index1) + ") and " + rectangles.get(index2) + " (" + childNodes.get(index2) + ")");
			
			for (int i = 0; i < childNodes.size(); i++) {
				if (i != index1 && i != index2) {
					if (Rectangle.areaRectangles(cache.getNode(childNodes.get(index1)).rectangle, cache.getNode(childNodes.get(i)).rectangle) <  Rectangle.areaRectangles(cache.getNode(childNodes.get(index2)).rectangle, cache.getNode(childNodes.get(i)).rectangle)) {
						childNodes1.add(childNodes.get(i));
					} else {
						childNodes2.add(childNodes.get(i));
					}
				}
			}
			logger.log("SPLITBRANCH  childNodes1 size: " + childNodes1.size() + " childNodes2 size: " + childNodes2.size());
			
			String node1Id = UUID.randomUUID().toString();
			String node2Id = UUID.randomUUID().toString();
			
			RTreeNode node1 = new RTreeNode(node1Id, null, null, cache, logger);
			RTreeNode node2 = new RTreeNode(node2Id, null, null, cache, logger);
			
			List<RTreeNode> newChildren = new ArrayList<RTreeNode>();
			newChildren.add(node1);
			newChildren.add(node2);
			node1.setChildren(childNodes1);
			node2.setChildren(childNodes2);
			for (String child : childNodes1) {
				cache.getNode(child).setParent(node1.nodeId);
			}
			for (String child : childNodes2) {
				cache.getNode(child).setParent(node2.nodeId);
			}
			
			
			if (node.getParent() == null) {
				
				logger.log("we're at the root");
				
//				root = new CloudRTreeNode(null, null, null);
//				root.setChildren(newChildren);
				
				JSONArray newChildrenArr = new JSONArray();
				newChildrenArr.add(node1.nodeId);
				newChildrenArr.add(node2.nodeId);
				
				logger.log("*****newChildrenArr: " + newChildrenArr);
				cache.updateNode(treeName, newChildrenArr.toJSONString(), null, null, cache.getNode(treeName).getRectangle().getJson().toJSONString());
				root = cache.getNode(treeName);
				node1.setParent(root.nodeId);
				node2.setParent(root.nodeId);
				
//				node1.updateRectangle();
//				node2.updateRectangle();
				List<Rectangle> listRect1 = new ArrayList<Rectangle>();
				for (String s : node1.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				Rectangle node1SumRect = Rectangle.sumRectangles(listRect1);
				
				List<Rectangle> listRect2 = new ArrayList<Rectangle>();
				for (String s : node2.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				Rectangle node2SumRect = Rectangle.sumRectangles(listRect2);
				
				node1.setRectangle(node1SumRect);
				node2.setRectangle(node2SumRect);
				cache.addNode(node1.nodeId, node1.getChildrenJSON().toJSONString(), treeName, null, node1SumRect.getJson().toJSONString(), node1);
				cache.addNode(node2.nodeId, node2.getChildrenJSON().toJSONString(), treeName, null, node2SumRect.getJson().toJSONString(), node2);
				
				for (String child : childNodes1) {
					logger.log("=== childNodes1 |" + child);
					cache.getNode(child).setParent(node1.nodeId);
					cache.updateNode(child, null, node1.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : childNodes2) {
					logger.log("=== childNodes2 |" + child);
					cache.getNode(child).setParent(node2.nodeId);
					cache.updateNode(child, null, node2.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(root);
				
			} else {
				RTreeNode newParent = cache.getNode(node.getParent());
				
				JSONArray newChildrenArr = new JSONArray();
				newChildrenArr.add(node1.nodeId);
				newChildrenArr.add(node2.nodeId);
				for (String ch : newParent.children) {
					if (!node.nodeId.equals(ch)) {
						newChildrenArr.add(ch);
					}
				}
				logger.log("*****newChildrenArr: " + newChildrenArr);
				cache.updateNode(newParent.nodeId, newChildrenArr.toJSONString(), null, null, cache.getNode(newParent.nodeId).getRectangle().getJson().toJSONString());
				newParent = cache.getNode(newParent.nodeId);
				
				node1.setParent(newParent.nodeId);
				node2.setParent(newParent.nodeId);
				
//				node1.updateRectangle();
//				node2.updateRectangle();
				List<Rectangle> listRect1 = new ArrayList<Rectangle>();
				for (String s : node1.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect1.add(r);
				}
				Rectangle node1SumRect = Rectangle.sumRectangles(listRect1);
				
				List<Rectangle> listRect2 = new ArrayList<Rectangle>();
				for (String s : node2.children) {
					Rectangle r = cache.getNode(s).getRectangle();
					listRect2.add(r);
				}
				Rectangle node2SumRect = Rectangle.sumRectangles(listRect2);
				
				node1.setRectangle(node1SumRect);
				node2.setRectangle(node2SumRect);
				cache.addNode(node1.nodeId, node1.getChildrenJSON().toJSONString(), newParent.nodeId, null, node1SumRect.getJson().toJSONString(), node1);
				cache.addNode(node2.nodeId, node2.getChildrenJSON().toJSONString(), newParent.nodeId, null, node2SumRect.getJson().toJSONString(), node2);
				
				for (String child : childNodes1) {
					logger.log("=== childNodes1 |" + child);
					cache.getNode(child).setParent(node1.nodeId);
					cache.updateNode(child, null, node1.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				for (String child : childNodes2) {
					logger.log("=== childNodes2 |" + child);
					cache.getNode(child).setParent(node2.nodeId);
					cache.updateNode(child, null, node2.nodeId, cache.getNode(child).getItemsJSON().toJSONString(), cache.getNode(child).getRectangle().getJson().toJSONString());
				}
				
				splitBranchNode(newParent);
			}
		}
	}
}
