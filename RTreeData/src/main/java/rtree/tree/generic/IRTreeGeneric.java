package rtree.tree.generic;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.IRType;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.tree.IRTreeCache;
import rtree.tree.IRTreeNode;

public interface IRTreeGeneric<T extends IRType<T>> {
	
	 IRTreeCacheGeneric<T> getCache();
	 String getTreeName();
	 void updateRoot();
	 boolean metaDataExists() throws Exception;
	 int getMaxChildren();
	 int getMaxItems();
	 IRTreeNodeGeneric<T> getNode(String nodeId);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNodeGeneric<T> node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 void insertType(ILocationItemGeneric<T> locationItem) throws IOException;
	 void insert(ILocationItemGeneric<T> locationItem) throws IOException;
	 Map<IHyperRectangleGeneric<T>, List<ILocationItemGeneric<T>>> search(IHyperRectangleGeneric<T> searchRectangle);
	 void delete(ILocationItemGeneric<T> toDelete);
	 List<IHyperRectangleGeneric<T>> getRectangles();
	 List<ILocationItemGeneric<T>> getPoints();
	 Map<ILocationItemGeneric<T>, Integer> getPointsWithDepth();
	 void printTree();
	 JSONObject getJson();
	 int numAdds();
	 int numReads();
	 int numUpdates();
	 long getAddTime();
	 long getReadTime();
	 long getUpdateTime();
	 int getNumDimensions();
	 List<T> getMin();
	 List<T> getMax();
	 
}