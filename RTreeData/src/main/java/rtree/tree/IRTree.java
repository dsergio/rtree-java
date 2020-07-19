package rtree.tree;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.rectangle.IHyperRectangle;

public interface IRTree {

	public IRTreeCache getCache();
	public String getTreeName();
	public void updateRoot();
	
	public boolean metaDataExists() throws Exception;
	public int getMaxChildren();
	public int getMaxItems();

	public IRTreeNode getNode(String nodeId);
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, IRTreeNode node);
	public void addNode(String nodeId, String children, String parent, String items, String rectangle);

	public void insertType(ILocationItem locationItem) throws IOException;
	public void insert(ILocationItem locationItem) throws IOException;
	
	public Map<IHyperRectangle, List<ILocationItem>> search(IHyperRectangle searchRectangle);
	public void delete(ILocationItem toDelete);
	
	public List<IHyperRectangle> getRectangles();
	public List<ILocationItem> getPoints();
	public Map<ILocationItem, Integer> getPointsWithDepth();
	public void printTree();
	public JSONObject getJson();

	public int numAdds();
	public int numReads();
	public int numUpdates();
	public long getAddTime();
	public long getReadTime();
	public long getUpdateTime();
	
	public int getNumDimensions();
}