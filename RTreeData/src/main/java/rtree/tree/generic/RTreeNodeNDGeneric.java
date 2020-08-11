package rtree.tree.generic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.log.ILogger;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.generic.HyperRectangleBaseGeneric;
import rtree.rectangle.generic.IHyperRectangleGeneric;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeNodeNDGeneric<T extends IRType<T>> extends RTreeNodeBaseGeneric<T> {

	Class<T> clazz;
	/**
	 * @param nodeId
	 * @param childrenStr
	 * @param parent
	 * @param cache
	 * @param logger
	 */
	public RTreeNodeNDGeneric(String nodeId, String childrenStr, String parent, IRTreeCacheGeneric<T> cache, ILogger logger, Class<T> clazz) {
		super(nodeId, childrenStr, parent, cache, logger);
		this.clazz = clazz;
	}
	
	public T getInstanceOf() {
		
		try {
			
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void updateRectangle() {
		updateRectangle(false);
	}
	
	@Override
	public void updateRectangle(boolean goUp) {
		System.out.println(rectangle.getNumberDimensions() + "-Dimensional RTreeNode.UpdateRectangle rectangle: " + rectangle + ", rectangle.getDim1(0): " + rectangle.getDim1(0));
		
		List<T> minimums = new ArrayList<T>();
		List<T> maximums = new ArrayList<T>();
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
					
					if (locationItems.get(i).getDim(i2).compareTo(minimums.get(i2)) < 0) {
						minimums.set(i2, locationItems.get(i).getDim(i2));
					}
					if (locationItems.get(i).getDim(i2).compareTo(maximums.get(i2)) > 0) {
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
	public void updateRectangle(IRTreeNodeGeneric<T> node) {
		logger.log(rectangle.getNumberDimensions() + "-Dimensional BRANCH UPDATE RECTANGLE:::: " + node.getNodeId() + " ... node.children: " + node.getChildren() + " node.parent: " + node.getParent());
		
		IHyperRectangleGeneric<T> childSum = node.getRectangle();
		
		List<T> minimums = new ArrayList<T>();
		List<T> maximums = new ArrayList<T>();
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.add(null);
			maximums.add(null);
		}
		for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
			minimums.set(i, node.getRectangle().getDim1(i));
			maximums.set(i, node.getRectangle().getDim2(i));
		}
		
		
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			IRTreeNodeGeneric<T> firstChild = cache.getNode(node.getChildren().get(0));
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
				
				IRTreeNodeGeneric<T> childNode = cache.getNode(child);
				if (childNode != null && childNode.getRectangle() != null) {
					
					logger.log("childSum: " + childSum);
					IHyperRectangleGeneric<T> childRectangle = cache.getNode(child).getRectangle();
					
					List<IHyperRectangleGeneric<T>> temp = new ArrayList<IHyperRectangleGeneric<T>>();
					temp.add(childSum);
					temp.add(childRectangle);
					childSum = HyperRectangleBaseGeneric.sumRectanglesNDimensional(temp);
					
					for (int i = 0; i < rectangle.getNumberDimensions(); i++) {
						
						if (minimums.get(i).compareTo(childRectangle.getDim1(i)) > 0) {
							minimums.set(i, childRectangle.getDim1(i));
						}
						
						if (maximums.get(i).compareTo(childRectangle.getDim2(i)) < 0) {
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
		this.locationItems = new ArrayList<ILocationItemGeneric<T>>();
		
		try {
			if (items != null && !items.equals("") && !items.equals("delete")) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					
					ILocationItemGeneric<T> item = new LocationItemNDGeneric<T>(rectangle.getNumberDimensions());
					
					for (int i2 = 0; i2 < rectangle.getNumberDimensions(); i2++) {
						
						T val = getInstanceOf();
						
						switch (i2) {
						case 0: 
							val.setData(row.get("x").toString());
							break;
						case 1:
							val.setData(row.get("y").toString());
							break;
						case 2:
							val.setData(row.get("z").toString());
							break;
						default:
							val.setData(row.get("" + i2).toString());
							break;
						}
						item.setDim(i2, val);
						item.setType(row.get("type").toString());
						
						JSONObject props = (JSONObject) row.get("properties");
						if (props != null) {
							for (Object propsKey : props.keySet()) {
								Object propsVal = props.get(propsKey);
								item.setProperty((String) propsKey, (String) propsVal);
							}
						}
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
