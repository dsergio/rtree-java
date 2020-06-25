package rtree.tree;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.log.ILogger;
import rtree.rectangle.Rectangle2D;
import rtree.storage.IDataStorage;

public class RTreeCache2D extends RTreeCacheBase {

	/**
	 * @param treeName
	 * @param logger
	 * @param dataStorage
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeCache2D(String treeName, ILogger logger, IDataStorage dataStorage, int numDimensions) throws Exception {
		super(treeName, logger, dataStorage, numDimensions);
		// TODO Auto-generated constructor stub
	}

	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {

		logger.log("calling CloudRTreeCache.updateNode on " + nodeId + " cacheContains: " + cache.containsKey(nodeId)
				+ ", with parameters: ");
//		logger.log("nodeId: " + nodeId);
//		logger.log("children: " +children);
//		logger.log("parent: " + parent);
//		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);

		IRTreeNode n = null;
		if (cache.containsKey(nodeId)) {

//			logger.log("__CACHE: " + "cache contains " + nodeId);
			n = cache.get(nodeId);

		} else {
//			logger.log("__CACHE: " + "cache DOES NOT contain " + nodeId);
			
			n = new RTreeNode2D(nodeId, children, parent, this, logger);
			
		}

		JSONParser parser;
		Object obj;

		Rectangle2D r = new Rectangle2D();
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;
				r = new Rectangle2D(Integer.parseInt(rectObj.get("x1").toString()),
						Integer.parseInt(rectObj.get("x2").toString()), Integer.parseInt(rectObj.get("y1").toString()),
						Integer.parseInt(rectObj.get("y2").toString()));
			}

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (children != null) {
			n.setChildren(children);
		}
		if (rectangle != null) {
			n.setRectangle(r);
		}
		if (parent != null) {
			n.setParent(parent);
		}

		if (items != null) {
			n.setLocationItems(new ArrayList<ILocationItem>());
			parser = new JSONParser();
			try {
				if (items != null) {
					obj = parser.parse(items);
					JSONArray arr = (JSONArray) obj;
					for (int i = 0; i < arr.size(); i++) {
						JSONObject row = (JSONObject) arr.get(i);
						ILocationItem item = new LocationItem2D(Integer.parseInt(row.get("x").toString()),
								Integer.parseInt(row.get("y").toString()), row.get("type").toString());
						n.getLocationItems().add(item);

					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		cache.put(nodeId, n);

		dbAccess.updateItem(treeName, nodeId, children, parent, items, rectangle);

	}


}
