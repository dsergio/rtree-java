package rtree.tree;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;

public interface IRTree {
	
	 IRTreeCache getCache();
	 String getTreeName();
	 void updateRoot();
	 boolean metaDataExists() throws Exception;
	 int getMaxChildren();
	 int getMaxItems();
	 IRTreeNode getNode(String nodeId);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode node);
	 void addNode(String nodeId, String children, String parent, String items, String rectangle);
	 void insertType(ILocationItem locationItem) throws IOException;
	 void insert(ILocationItem locationItem) throws IOException;
	 Map<IHyperRectangle, List<ILocationItem>> search(IHyperRectangle searchRectangle);
	 void delete(ILocationItem toDelete);
	 List<IHyperRectangle> getRectangles();
	 List<ILocationItem> getPoints();
	 Map<ILocationItem, Integer> getPointsWithDepth();
	 void printTree();
	 JSONObject getJson();
	 int numAdds();
	 int numReads();
	 int numUpdates();
	 long getAddTime();
	 long getReadTime();
	 long getUpdateTime();
	 int getNumDimensions();
	 
}