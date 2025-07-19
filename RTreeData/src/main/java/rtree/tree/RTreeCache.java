package rtree.tree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.item.LocationItem;
import rtree.log.ILogger;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public class RTreeCache<T extends IRType<T>> extends RTreeCacheBase<T> {

	Class<T> clazz;
	/**
	 * @param treeName
	 * @param logger
	 * @param dataStorage
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeCache(String treeName, ILogger logger, IDataStorage<T> dataStorage, int numDimensions, Class<T> clazz) throws Exception {
		super(treeName, logger, dataStorage, numDimensions);
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

	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {

		logger.log(numDimensions + "-dimensional CloudRTreeCache.updateNode on " + nodeId + " cacheContains: "
				+ cache.containsKey(nodeId) + ", with parameters: ");
//		logger.log("nodeId: " + nodeId);
//		logger.log("children: " +children);
//		logger.log("parent: " + parent);
//		logger.log("items: " + items);
		logger.log("rectangle: " + rectangle);

		IRTreeNode<T> n = null;
		if (cache.containsKey(nodeId)) {

//			logger.log("__CACHE: " + "cache contains " + nodeId);
			n = cache.get(nodeId);

		} else {
//			logger.log("__CACHE: " + "cache DOES NOT contain " + nodeId);
			
			n = new RTreeNode<T>(nodeId, children, parent, this, logger, clazz);
		}

		JSONParser parser;
		Object obj;

		IHyperRectangle<T> r = new HyperRectangle<T>(numDimensions);
		parser = new JSONParser();
		try {
			if (rectangle != null) {
				obj = parser.parse(rectangle);
				JSONObject rectObj = (JSONObject) obj;

				for (int i = 0; i < numDimensions; i++) {

					T dim1 = getInstanceOf();
					T dim2 = getInstanceOf();

					switch (i) {
					case 0:
						dim1.setData(rectObj.get("x1").toString());
						dim2.setData(rectObj.get("x2").toString());
						break;
					case 1:
						dim1.setData(rectObj.get("y1").toString());
						dim2.setData(rectObj.get("y2").toString());
						break;
					case 2:
						dim1.setData(rectObj.get("z1").toString());
						dim2.setData(rectObj.get("z2").toString());
						break;
					default:
						dim1.setData(rectObj.get(i + "_1").toString());
						dim2.setData(rectObj.get(i + "_2").toString());
						break;
					}

					r.setDim1(i, dim1);
					r.setDim2(i, dim2);
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
			n.setLocationItems(new ArrayList<ILocationItem<T>>());
			parser = new JSONParser();
			try {
				if (items != null) {
					obj = parser.parse(items);
					JSONArray arr = (JSONArray) obj;
					for (int i = 0; i < arr.size(); i++) {
						JSONObject row = (JSONObject) arr.get(i);

						ILocationItem<T> itemND = new LocationItem<T>(numDimensions);

						for (int j = 0; j < numDimensions; j++) {

							T val = getInstanceOf();

							switch (j) {
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
								val.setData(row.get(j + "").toString());
								break;
							}

							itemND.setDim(j, val);
						}

						itemND.setType(row.get("type").toString());
						
						JSONObject props = (JSONObject) row.get("properties");
						if (props != null) {
							for (Object propsKey : props.keySet()) {
								Object propsVal = props.get(propsKey);
								itemND.setProperty((String) propsKey, (String) propsVal);
							}
						}

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
