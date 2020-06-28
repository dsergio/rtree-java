package rtree.tree;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeNodeND extends RTreeNodeBase {

	/**
	 * @param nodeId
	 * @param childrenStr
	 * @param parent
	 * @param cache
	 * @param logger
	 */
	public RTreeNodeND(String nodeId, String childrenStr, String parent, IRTreeCache cache, ILogger logger) {
		super(nodeId, childrenStr, parent, cache, logger);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateRectangle() {
		updateRectangle(false);
	}
	
	@Override
	public void updateRectangle(boolean goUp) {
		System.out.println(rectangle.getNumberDimensions() + "-Dimensional RTreeNode.UpdateRectangle rectangle: " + rectangle + ", rectangle.getDim1(0): " + rectangle.getDim1(0));
		
		List<Integer> minimums = new ArrayList<Integer>();
		List<Integer> maximums = new ArrayList<Integer>();
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.add(null);
			maximums.add(null);
		}
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.set(i, rectangle.getDim1(i));
			maximums.set(i, rectangle.getDim2(i));
		}
		
		if (locationItems.size() > 0) {
			for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
				minimums.set(i, locationItems.get(0).getDim(i));
				maximums.set(i, minimums.get(i));
			}
			
			for (int i = 0; i < locationItems.size(); i++) {
				for (int i2 = 0; i2 < rectangle.getNumberDimensions(); i2++) {
					if (locationItems.get(i).getDim(i2) < minimums.get(i2)) {
						minimums.set(i2, locationItems.get(i).getDim(i2));
					}
					if (locationItems.get(i).getDim(i2) > maximums.get(i2)) {
						maximums.set(i2, locationItems.get(i).getDim(i2));
					}
				}
			}
		}
		
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			rectangle.setDim1(i, minimums.get(i));
			rectangle.setDim2(i, maximums.get(i));
		}
		
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
		logger.log(rectangle.getNumberDimensions() + "-Dimensional BRANCH UPDATE RECTANGLE:::: " + node.getNodeId() + " ... node.children: " + node.getChildren() + " node.parent: " + node.getParent());
		
		IHyperRectangle childSum = node.getRectangle();
		
		List<Integer> minimums = new ArrayList<Integer>();
		List<Integer> maximums = new ArrayList<Integer>();
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.add(null);
			maximums.add(null);
		}
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.set(i, node.getRectangle().getDim1(i));
			maximums.set(i, node.getRectangle().getDim2(i));
		}
		
		
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			IRTreeNode firstChild = cache.getNode(node.getChildren().get(0));
			if (firstChild != null && firstChild.getRectangle() != null) {
				for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
					minimums.set(i, firstChild.getRectangle().getDim1(i));
					maximums.set(i, firstChild.getRectangle().getDim2(i));

				}
			}
			
			
			if (firstChild != null) {
				childSum = firstChild.getRectangle();
			}
			
			for (String child : node.getChildren()) {
				
				IRTreeNode childNode = cache.getNode(child);
				if (childNode != null && childNode.getRectangle() != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangle childRectangle = cache.getNode(child).getRectangle();
					
					List<IHyperRectangle> temp = new ArrayList<IHyperRectangle>();
					temp.add(childSum);
					temp.add(childRectangle);
					childSum = HyperRectangleBase.sumRectanglesNDimensional(temp);
					
					for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
						if (minimums.get(i) > childRectangle.getDim1(i)) {
							minimums.set(i, childRectangle.getDim1(i));
						}
						if (maximums.get(i) < childRectangle.getDim2(i)) {
							maximums.set(i, childRectangle.getDim2(i));
						}
					}
				}
			}
			
			
			
			node.setRectangle(childSum);
			cache.updateNode(node.getNodeId(), null, null, null, node.getRectangle().getJson().toJSONString());
		}
		
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
					
					ILocationItem item = new LocationItemND(rectangle.getNumberDimensions());
					
					for (int i2 = 0; i2 < rectangle.getNumberDimensions(); i2++) {
						
						switch (i2) {
						case 0: 
							item.setDim(i2, Integer.parseInt(row.get("x").toString()));
							break;
						case 1:
							item.setDim(i2, Integer.parseInt(row.get("y").toString()));
							break;
						case 2:
							item.setDim(i2, Integer.parseInt(row.get("z").toString()));
							break;
						default:
							item.setDim(i2, Integer.parseInt(row.get("" + i2).toString()));
							break;
						}
						
						item.setType(row.get("type").toString());
					}
					
					this.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			logger.log("items: |" + items + "|");
			e.printStackTrace();
		}
		
	}

}
