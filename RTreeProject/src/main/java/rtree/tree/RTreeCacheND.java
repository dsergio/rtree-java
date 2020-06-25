package rtree.tree;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.rectangle.RectangleND;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeCacheND extends RTreeCacheBase {

	/**
	 * @param treeName
	 * @param logger
	 * @param dataStorage
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeCacheND(String treeName, ILogger logger, IDataStorage dataStorage, int numDimensions) throws Exception {
		super(treeName, logger, dataStorage, numDimensions);
		// TODO Auto-generated constructor stub
	}

	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {

		logger.log(numDimensions + "-dimensional CloudRTreeCache.updateNode on " + nodeId + " cacheContains: "
				+ cache.containsKey(nodeId) + ", with parameters: ");
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
			
			n = new RTreeNodeND(nodeId, children, parent, this, logger);
		}

		JSONParser parser;
		Object obj;

		IHyperRectangle r = new RectangleND(numDimensions);
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;

				for (int i = 0; i < numDimensions; i++) {

					int value1, value2;

					switch (i) {
					case 0:
						value1 = Integer.parseInt(rectObj.get("x1").toString());
						value2 = Integer.parseInt(rectObj.get("x2").toString());
						break;
					case 1:
						value1 = Integer.parseInt(rectObj.get("y1").toString());
						value2 = Integer.parseInt(rectObj.get("y2").toString());
						break;
					case 2:
						value1 = Integer.parseInt(rectObj.get("z1").toString());
						value2 = Integer.parseInt(rectObj.get("z2").toString());
						break;
					default:
						value1 = Integer.parseInt(rectObj.get(i + "_1").toString());
						value2 = Integer.parseInt(rectObj.get(i + "_2").toString());
						break;
					}

					r.setDim1(i, value1);
					r.setDim2(i, value2);
				}

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

						ILocationItem itemND = new LocationItemND(numDimensions);

						for (int j = 0; j < numDimensions; j++) {

							int value;

							switch (j) {
							case 0:
								value = Integer.parseInt(row.get("x").toString());

								break;
							case 1:
								value = Integer.parseInt(row.get("y").toString());

								break;
							case 2:
								value = Integer.parseInt(row.get("z").toString());

								break;
							default:
								value = Integer.parseInt(row.get(j + "").toString());

								break;
							}

							itemND.setDim(j, value);
						}

						itemND.setType(row.get("type").toString());

						n.getLocationItems().add(itemND);

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
