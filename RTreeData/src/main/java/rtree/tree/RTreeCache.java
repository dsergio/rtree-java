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

	Class<T> className;
	
	/**
	 * @param treeName
	 * @param logger
	 * @param dataStorage
	 * @param numDimensions
	 * @throws Exception
	 */
	public RTreeCache(IRTree<T> tree, ILogger logger, IDataStorage<T> dataStorage, Class<T> className) throws Exception {
		
		super(tree, logger, dataStorage);
		this.className = className;
	}
	
	public T getInstanceOf() {
		
		try {
			return className.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			logger.log(e);
		}
		
		return null;
	}

}
