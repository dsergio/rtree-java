package rtree.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.item.LocationItemBase;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;


/**
 * Abstract base class for R-tree nodes.
 * @param <T> Type of the items stored in the R-tree, extending IRType.
 * @author David Sergio
 *
 */
public abstract class RTreeNodeBase<T extends IRType<T>> implements IRTreeNode<T> {

	protected String nodeId;
	protected String parentId;
	protected List<String> children;
	
	protected List<ILocationItem<T>> locationItems;
	protected IHyperRectangle<T> rectangle;
	
	protected IRTreeCache<T> cache;
	protected ILogger logger;

	public RTreeNodeBase(String nodeId, String childrenStr, String parent, IRTreeCache<T> cache, ILogger logger) {
		this.cache = cache;
		this.logger = logger;
		this.nodeId = nodeId;
		
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			if (childrenStr != null && !childrenStr.equals("{}") && !childrenStr.equals("")) {
				if (children == null) {
					children = new ArrayList<String>();
				}
				obj = parser.parse(childrenStr);
				
				JSONArray jsonArr = (JSONArray) obj;
				if (jsonArr.size() > 0) {
					
					for (int i = 0; i < jsonArr.size(); i++) {
						children.add(jsonArr.get(i).toString());
					}
				}
			}
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			logger.log(e);
		}
		
		this.parentId = parent;
		this.locationItems = new ArrayList<ILocationItem<T>>();
		
		if (cache != null) {
			this.rectangle = new HyperRectangle<T>(cache.getTree().getNumDimensions());
		}
	}
	
	public void setLocationItems(ArrayList<ILocationItem<T>> locationItems) {
		this.locationItems = locationItems;
	}
	
	@Override
	public String getNodeId() {
		return nodeId;
	}
	
	@Override
	public String getNodeIdShort() {
		if (nodeId != null && nodeId.length() > 8) {
			return nodeId.substring(0, 8);
		} else {
			return nodeId;
		}
	}
	
	@Override
	public List<String> getChildren() {
		if (children != null) {
			return children;
		} else {
			children = new ArrayList<String>(); 
			return children;
		}
	}
	
	@Override
	public List<String> getChildrenShort() {
		List<String> childrenShort = new ArrayList<String>();
		
		if (children != null) {
			for (String s : children) {
				if (s != null && s.length() > 8) {
					childrenShort.add(s.substring(0, 8));
				} else {
					childrenShort.add(s);
				}
			}
		}
		
		return childrenShort;
	}
	
	@Override
	public void setChildrenJSON(String childrenStr) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			if (childrenStr != null && !childrenStr.equals("{}")) {
				children = new ArrayList<String>();
				
				obj = parser.parse(childrenStr);
				
				JSONArray jsonArr = (JSONArray) obj;
				if (jsonArr.size() > 0) {
					
					for (int i = 0; i < jsonArr.size(); i++) {
						children.add(jsonArr.get(i).toString());
					}
				}
			}
			
		} catch (ParseException e) {
			logger.log(e);
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONArray getChildrenJSON() {
		
		JSONArray arr = new JSONArray();
		if (children != null) {
			for (String s : children) {
				arr.add(s);
			}
		}
		return arr;
	}
	
	@Override
	public JSONArray getLocationItemsJSON() {
		
		return LocationItemBase.getItemsJSON(locationItems);
	}

	@Override
	public List<ILocationItem<T>> getLocationItems() {
		return locationItems;
	}

	@Override
	public void addLocationItem(ILocationItem<T> locationItem) throws IOException {
		locationItems.add(locationItem);
		updateRectangle();
		cache.updateNode(nodeId, this);
	}
	
	public abstract void updateRectangle();
	public abstract void updateRectangle(boolean goUp);
	public abstract void updateRectangle(IRTreeNode<T> node);
	

	@Override
	public int getNumberOfItems() {
		return locationItems.size();
	}

	@Override
	public boolean isLeafNode() {
		return children == null || children.size() == 0;
	}

	@Override
	public IHyperRectangle<T> getRectangle() {
		return rectangle;
	}

	@Override
	public void setRectangle(IHyperRectangle<T> rectangle) {
		this.rectangle = rectangle;
	}

	@Override
	public void setParent(String node) {
		this.parentId = node;
	}

	@Override
	public String getParent() {
		return parentId;
	}

	@Override
	public void setChildren(List<String> newChildren) {
		children = newChildren;

	}
	
	@Override
	public String toString() {
		
		String str = "RTreeNode\n * ";
		str += "nodeId: " + nodeId + ", ";
		str += "children (short): " + getChildrenShort() + ", ";
		str += "items (type): " + getLocationItemsType() + ", ";
		str += "parent: " + parentId + ", ";
		str += "rectangle: " + getRectangle().toString();
		
		return str;
	}

	private String getLocationItemsType() {
		String ret = "";
		if (locationItems == null || locationItems.size() == 0) {
			return "[]";
		}
		for (int i = 0; i < locationItems.size(); i++) {
			if (i < locationItems.size() - 1) {
				ret += locationItems.get(i).getType().toString() + ",";
			} else {
				ret += locationItems.get(i).getType().toString();
			}
		}
		return ret;
	}

	
	
	
	
}