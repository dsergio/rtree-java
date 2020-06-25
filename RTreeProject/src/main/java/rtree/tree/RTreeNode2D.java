package rtree.tree;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeNode2D extends RTreeNodeBase {

	/**
	 * @param nodeId
	 * @param childrenStr
	 * @param parent
	 * @param cache
	 * @param logger
	 */
	public RTreeNode2D(String nodeId, String childrenStr, String parent, IRTreeCache cache, ILogger logger) {
		super(nodeId, childrenStr, parent, cache, logger);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateRectangle() {
		updateRectangle(false);
	}
	
	@Override
	public void updateRectangle(boolean goUp) {
		System.out.println("RTreeNode.UpdateRectangle rectangle: " + rectangle + ", rectangle.getDim1(0): " + rectangle.getDim1(0));
		
		int minX = rectangle.getDim1(0);
		int maxX = rectangle.getDim2(0);
		int minY = rectangle.getDim1(1);
		int maxY = rectangle.getDim2(1);

		if (locationItems.size() > 0) {
			minX = locationItems.get(0).getDim(0);
			maxX = minX;
			minY = locationItems.get(0).getDim(1);
			maxY = minY;
			for (int i = 0; i < locationItems.size(); i++) {
				if (locationItems.get(i).getDim(0) < minX) {
					minX = locationItems.get(i).getDim(0);
				}
				if (locationItems.get(i).getDim(0) > maxX) {
					maxX = locationItems.get(i).getDim(0);
				}
				if (locationItems.get(i).getDim(1) < minY) {
					minY = locationItems.get(i).getDim(1);
				}
				if (locationItems.get(i).getDim(1) > maxY) {
					maxY = locationItems.get(i).getDim(1);
				}
			}

		}

		rectangle.setDim1(0, minX);
		rectangle.setDim2(0, maxX);
		rectangle.setDim1(1, minY);
		rectangle.setDim2(1, maxY);
		
		cache.updateNode(nodeId, null, null, null, rectangle.getJson().toJSONString());
		logger.log("updated rectangle for " + nodeId + " new rectangle: " + rectangle);
		
		// logger.log("This node has a bounding box of " + rectangle.toString()
		// + " has parent? " + (parent != null) + "... child rect: " + childRectangle);

		if (parent != null && cache.getNode(parent) != null && goUp) {
			updateRectangle(cache.getNode(parent));
		}
	}
	
	@Override
	public void updateRectangle(IRTreeNode node) {
		
		logger.log("BRANCH UPDATE RECTANGLE:::: " + node.getNodeId() + " ... node.children: " + node.getChildren() + " node.parent: " + node.getParent());
		
		IHyperRectangle childSum = node.getRectangle();
		
		int minX = node.getRectangle().getDim1(0);
		int maxX = node.getRectangle().getDim2(0);
		int minY = node.getRectangle().getDim1(1);
		int maxY = node.getRectangle().getDim2(1);
		
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			IRTreeNode firstChild = cache.getNode(node.getChildren().get(0));
			if (firstChild != null && firstChild.getRectangle() != null) {
				minX = firstChild.getRectangle().getDim1(0);
				minY = firstChild.getRectangle().getDim1(1);
				maxX = firstChild.getRectangle().getDim2(0);
				maxY = firstChild.getRectangle().getDim2(1);
			}
			
			
			if (firstChild != null) {
				childSum = firstChild.getRectangle();
			}
			
			for (String child : node.getChildren()) {
				
				IRTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.getRectangle() != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangle childRectangle = cache.getNode(child).getRectangle();
					
					childSum = Rectangle2D.sumRectangles2D(childSum, childRectangle);
					
					if (minX > childRectangle.getDim1(0)) {
						minX = childRectangle.getDim1(0);
					}
					if (minY > childRectangle.getDim1(1)) {
						minY = childRectangle.getDim1(1);
					}
					if (maxX < childRectangle.getDim2(0)) {
						maxX = childRectangle.getDim2(0);
					}
					if (maxY < childRectangle.getDim2(1)) {
						maxY = childRectangle.getDim2(1);
					}
					
				}
				
				
			}
			
			node.setRectangle(childSum);
			cache.updateNode(node.getNodeId(), null, null, null, node.getRectangle().getJson().toJSONString());
		}
		
		

//		node.rectangle.setX1(minX);
//		node.rectangle.setX2(maxX);
//		node.rectangle.setY1(minY);
//		node.rectangle.setY2(maxY);
		
		if (node.getParent() != null && cache.getNode(node.getParent()) != null) {
			updateRectangle(cache.getNode(node.getParent()));
		}
		
	}
	
	@Override
	public void setItemsJson(String items) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		this.locationItems = new ArrayList<ILocationItem>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					ILocationItem item = new LocationItem2D(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					this.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.log("items: |" + items + "|");
			e.printStackTrace();
		}
		
	}

}
