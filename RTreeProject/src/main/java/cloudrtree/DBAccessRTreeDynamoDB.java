package cloudrtree;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;



/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class DBAccessRTreeDynamoDB implements DBAccessRTree {

	/*
	 * Before running the code: Fill in your AWS access credentials in the provided
	 * credentials file template, and be sure to move the file to the default
	 * location (~/.aws/credentials) where the sample code will load the credentials
	 * from. https://console.aws.amazon.com/iam/home?#security_credential
	 *
	 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep the
	 * credentials file in your source directory.
	 */

	/*
	 * Before running the code: Fill in your AWS access credentials in the provided
	 * credentials file template, and be sure to move the file to the default
	 * location (~/.aws/credentials) where the sample code will load the credentials
	 * from. https://console.aws.amazon.com/iam/home?#security_credential
	 *
	 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep the
	 * credentials file in your source directory.
	 */
	
	private int numReads = 0;
	private int numAdds = 0;
	private int numUpdates = 0;
	
	private long readTime = 0;
	private long addTime = 0;
	private long updateTime = 0;

	private AmazonDynamoDB dynamoDB;
	private boolean dynamoLog = true;;

	public DBAccessRTreeDynamoDB() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		dynamoLog = false;
	}

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed automatically.
	 * Client parameters, such as proxies, can be specified in an optional
	 * ClientConfiguration object when constructing a client.
	 *
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.ProfilesConfigFile
	 * @see com.amazonaws.ClientConfiguration
	 */
	@Override
	public void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials) for
		 * Linux and Mac machines.
		 */
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-west-2")
				.build();

	}

	public void createTable(String name) {
		try {
			String tableName = name;

			// Create a table with a primary hash key named 'name', which holds a string
			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
					.withKeySchema(new KeySchemaElement().withAttributeName("nodeId").withKeyType(KeyType.HASH))
					.withAttributeDefinitions(new AttributeDefinition().withAttributeName("nodeId")
							.withAttributeType(ScalarAttributeType.S))
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L));

			// Create table if it does not exist yet
			TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
			// wait for the table to move into ACTIVE state
			try {
				TableUtils.waitUntilActive(dynamoDB, tableName);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Describe our new table
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
			System.out.println("Table Description: " + tableDescription);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
	public CloudRTreeNode addCloudRTreeNode(String nodeId, String children, String parent, String items, String rectangle, String treeName, CloudRTreeCache cache) {
		Map<String, AttributeValue> item = newNode(nodeId, children, parent, items, rectangle);
		addItemToTable(item, treeName);
		
		CloudRTreeNode node = new CloudRTreeNode(nodeId, children, parent, cache);
		return node;
	}

	public void addItemToTable(Map<String, AttributeValue> item, String tableName) {
		try {

			// Add an item
			long time = System.currentTimeMillis();
			
			PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
			PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
			
			if (dynamoLog) {
				System.out.println(" -> DynamoDB ADD " + item.values().toString());
				System.out.println("..." + putItemResult);
			}
			numAdds++;
			
			addTime += (System.currentTimeMillis() - time);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
	public void updateItem(String tableName, String nodeId, String children, String parent, String items, String rectangle) {
		try {

			// Update item
//			UpdateItemRequest updateItemRequest = new UpdateItemRequest(tableName, item, null);
			
			long time = System.currentTimeMillis();
			Map<String,AttributeValue> key = new HashMap<>();
		    key.put("nodeId",new AttributeValue().withS(nodeId));
		    
			UpdateItemRequest updateItemRequest = new UpdateItemRequest()
			        .withTableName(tableName)
			        .withKey(key);
			        		
    		Map<String, AttributeValueUpdate> map = new HashMap<>();
    		if (children != null) {
    			map.put("children", new AttributeValueUpdate(new AttributeValue(children),"PUT"));
    		}
    		if (parent != null) {
    			map.put("parent", new AttributeValueUpdate(new AttributeValue(parent),"PUT"));
    		}
    		if (items != null) {
    			map.put("items", new AttributeValueUpdate(new AttributeValue(items),"PUT"));
    		}
    		if (rectangle != null) {
    			map.put("rectangle", new AttributeValueUpdate(new AttributeValue(rectangle),"PUT"));
    		}
	        
	        updateItemRequest.setAttributeUpdates(map);
			
			UpdateItemResult updateItemResult = dynamoDB.updateItem(updateItemRequest);
			
			if (dynamoLog ) {
				System.out.println(" -> DynamoDB UPDATE " + nodeId);
				System.out.println("..." + updateItemResult);
			}
			numUpdates++;
			updateTime += (System.currentTimeMillis() - time);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
	public CloudRTreeNode getCloudRTreeNode(String tableName, String nodeId, CloudRTreeCache cache) {
		
		JSONParser parser;
		Object obj;
		
		String children = null;
		String parent = null;
		String items = null;
		String rectangle = null;
		
		Map<String, AttributeValue> cloudNode = getNode(tableName, nodeId);
		if (cloudNode == null) {
			return null;
		} else {
			if (cloudNode.get("children") != null) {
				children = cloudNode.get("children").getS();
				if (children.equals("delete")) { 
					children = null;
				}
			}
			if (cloudNode.get("parent") != null) {
				parent = cloudNode.get("parent").getS();
				if (parent.equals("delete")) { 
					parent = null;
				}
			}
			if (cloudNode.get("items") != null) {
				items = cloudNode.get("items").getS();
				if (items.equals("delete")) { 
					items = null;
				}
			}
			
			if (cloudNode.get("rectangle") != null) {
				rectangle = cloudNode.get("rectangle").getS();
				if (rectangle.equals("delete")) { 
					rectangle = null;
				}
			}
			
		}
//		System.out.println("getNode children: " + children + ", parent: " + parent + ", items: " + items + ", rectangle: " + rectangle);
		
		Rectangle r = new Rectangle();
		
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
//		System.out.println("rectangle: " + r);
		
		
		CloudRTreeNode newNode =  new CloudRTreeNode(nodeId, children, parent, cache);
		
		newNode.setRectangle(r);
		
		parser = new JSONParser();
		
		try {
			if (items != null) {
				
				obj = parser.parse(items);
				JSONArray arr = (JSONArray) obj;
				for (int i = 0; i < arr.size(); i++) {
					JSONObject row = (JSONObject) arr.get(i);
					LocationItem item = new LocationItem(Integer.parseInt(row.get("x").toString()), Integer.parseInt(row.get("y").toString()), row.get("type").toString());
					newNode.locationItems.add(item);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newNode;
	}
	
	public Map<String, AttributeValue> getNode(String tableName, String nodeId) {
		try {

			long time = System.currentTimeMillis();
			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
			Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS(nodeId));
			
			scanFilter.put("nodeId", condition);
			ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
			ScanResult scanResult = dynamoDB.scan(scanRequest);
			
			if (dynamoLog) {
				System.out.println(" -> DynamoDB getNode('" + nodeId + "')...");
				System.out.println("..." + scanResult.toString());
			}
			numReads++;
			
			readTime += (System.currentTimeMillis() - time);
			
			if (scanResult.getCount() == 1) {
				return scanResult.getItems().get(0);
			} else {
				return null;
			}

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		
		return null;
	}

	public Map<String, AttributeValue> newNode(String nodeId, String children, String parent, String items, String rectangle) {
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		item.put("nodeId", new AttributeValue(nodeId));
		
		if (children != null) {
			item.put("children", new AttributeValue(children));
		}
		if (parent != null) {
			item.put("parent", new AttributeValue(parent));
		}
		if (items != null) {
			item.put("items", new AttributeValue(items));
		}
		if (rectangle != null) {
			item.put("rectangle", new AttributeValue(rectangle));
		}
		
		return item;
	}

	public Map<String, AttributeValue> newNode(String treeName, int maxChildren, int maxItems) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		item.put("nodeId", new AttributeValue(treeName));
		
		if (maxChildren > 1) {
			item.put("maxChildren", new AttributeValue("" + maxChildren));
		}
		if (maxChildren > 1) {
			item.put("maxItems", new AttributeValue("" + maxItems));
		}
		return item;
	}

	public int getNumReads() {
		return numReads;
	}

	public int getNumAdds() {
		return numAdds;
	}

	public int getNumUpdates() {
		return numUpdates;
	}
	
	public long getReadTime() {
		return readTime;
	}

	public long getAddTime() {
		return addTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToMetaData(String tableName, int maxChildren, int maxItems) {
		Map<String, AttributeValue> item = newNode(tableName, maxChildren, maxItems);
		addItemToTable(item, "metadata");
	}

	@Override
	public boolean metaDataExists(String treeName) {
		Map<String, AttributeValue> metadata = getNode("metadata", treeName);
		return (metadata != null);
	}

	@Override
	public int getMaxChildren(String treeName) {
		Map<String, AttributeValue> metadata = getNode("metadata", treeName);
		int maxChildren = -1;
		if (metadata == null) {
			return -1;
		} else {
			if (metadata.get("maxChildren") != null) {
				maxChildren = Integer.parseInt(metadata.get("maxChildren").getS());
			}
		}
		return maxChildren;
	}

	@Override
	public int getMaxItems(String treeName) {
		int maxItems = -1;
		Map<String, AttributeValue> metadata = getNode("metadata", treeName);
		
		if (metadata == null) {
			return -1;
		} else {
			if (metadata.get("maxItems") != null) {
				maxItems = Integer.parseInt(metadata.get("maxItems").getS());
			}
		}
		return maxItems;
	}

	
	
//	public Map<String, AttributeValue> newTree(String rootId, String size) {
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		
//		item.put("id", new AttributeValue(rootId));
//		item.put("data", new AttributeValue(size));
//
//		return item;
//	}

//	private Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans) {
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		item.put("name", new AttributeValue(name));
//		item.put("year", new AttributeValue().withN(Integer.toString(year)));
//		item.put("rating", new AttributeValue(rating));
//		item.put("fans", new AttributeValue().withSS(fans));
//
//		return item;
//	}

}
