package cloudrtree;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * Description TBD
 * 
 * @author David Sergio
 *
 */
public class DBAccessRTreeInMemory implements DBAccessRTree {
	
	private int maxItems;
	private int maxChildren;
	
	private Map<String, CloudRTreeNode> localData;
	
	public DBAccessRTreeInMemory() {
		localData = new HashMap<String, CloudRTreeNode>();
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createTable(String tableName) {
		// TODO Auto-generated method stub
	}

	@Override
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle,
			String treeName, CloudRTreeCache cache) {
		
		System.out.println("Calling DBAccessRTreeLocal.addCloudRTreeNode with parameters: ");
		System.out.println("nodeId: " + nodeId);
		System.out.println("children: " + children);
		System.out.println("parent: " + parent);
		System.out.println("items: " + items);
		System.out.println("rectangle: " + rectangle);
		
		// construct the node
		CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache);
		localData.put(nodeId, node);
		return node;
	}

	@Override
	public void updateItem(String tableName, String nodeId, String children, String parent, String items,
			String rectangle) {
		
		System.out.println("Calling DBAccessRTreeLocal.updateItem with parameters: ");
		System.out.println("tableName: " + tableName);
		System.out.println("nodeId: " + nodeId);
		System.out.println("children: " + children);
		System.out.println("parent: " + parent);
		System.out.println("items: " + items);
		System.out.println("rectangle: " + rectangle);
		
		
		CloudRTreeNode node = localData.get(nodeId);
		node.setChildren(children);
		node.setParent(parent);
		
		Rectangle r = new Rectangle();
		
		JSONParser parser;
		Object obj;
		
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle(Integer.parseInt(rectObj.get("x1").toString()), 
						Integer.parseInt(rectObj.get("x2").toString()), 
						Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		node.setRectangle(r);

	}

	@Override
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache) {
		
		System.out.println("Calling DBAccessRTreeLocal.getCloudRTreeNode with parameters: ");
		System.out.println("tableName: " + tableName);
		System.out.println("nodeId: " + nodeId);
		
		return localData.get(nodeId);
		
	}

	@Override
	public int getNumAdds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumReads() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUpdates() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAddTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getReadTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getUpdateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addToMetaData(String tableName, int maxChildren, int maxItems) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean metaDataExists(String treeName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxChildren(String treeName) {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getMaxItems(String treeName) {
		// TODO Auto-generated method stub
		return 4;
	}

}
