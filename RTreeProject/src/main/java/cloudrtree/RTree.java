package cloudrtree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cloudrtree.ILogger.LogLevel;

/**
 * 
 * R-Tree with configurable split algorithms and configurable storage implementations
 * 
 * @author David Sergio
 * 
 *
 */
public class RTree {

	private RTreeNode root;
	private int maxChildren;
	private int maxItems;
	private String treeName;
	String[] animals = {"Abyssinian", "Adelie Penguin", "Affenpinscher", "Afghan Hound", "African Bush Elephant", "African Civet", "African Clawed Frog", "African Forest Elephant", "African Palm Civet", "African Penguin", "African Tree Toad", "African Wild Dog", "Ainu Dog", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "Albatross", "Aldabra Giant Tortoise", "Alligator", "Alpine Dachsbracke", "American Bulldog", "American Cocker Spaniel", "American Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Angelfish", "Ant", "Anteater", "Antelope", "Appenzeller Dog", "Arctic Fox", "Arctic Hare", "Arctic Wolf", "Armadillo", "Asian Elephant", "Asian Giant Hornet", "Asian Palm Civet", "Asiatic Black Bear", "Australian Cattle Dog", "Australian Kelpie Dog", "Australian Mist", "Australian Shepherd", "Australian Terrier", "Avocet", "Axolotl", "Aye Aye", "Baboon", "Bactrian Camel", "Badger", "Balinese", "Banded Palm Civet", "Bandicoot", "Barb", "Barn Owl", "Barnacle", "Barracuda", "Basenji Dog", "Basking Shark", "Basset Hound", "Bat", "Bavarian Mountain Hound", "Beagle", "Bear", "Bearded Collie", "Bearded Dragon", "Beaver", "Bedlington Terrier", "Beetle", "Bengal Tiger", "Bernese Mountain Dog", "Bichon Frise", "Binturong", "Bird", "Birds Of Paradise", "Birman", "Bison", "Black Bear", "Black Rhinoceros", "Black Russian Terrier", "Black Widow Spider", "Bloodhound", "Blue Lacy Dog", "Blue Whale", "Bluetick Coonhound", "Bobcat", "Bolognese Dog", "Bombay", "Bongo", "Bonobo", "Booby", "Border Collie", "Border Terrier", "Bornean Orang-utan", "Borneo Elephant", "Boston Terrier", "Bottle Nosed Dolphin", "Boxer Dog", "Boykin Spaniel", "Brazilian Terrier", "Brown Bear", "Budgerigar", "Buffalo", "Bull Mastiff", "Bull Shark", "Bull Terrier", "Bulldog", "Bullfrog", "Bumble Bee", "Burmese", "Burrowing Frog", "Butterfly", "Butterfly Fish", "Caiman", "Caiman Lizard", "Cairn Terrier", "Camel", "Canaan Dog", "Capybara", "Caracal", "Carolina Dog", "Cassowary", "Cat", "Caterpillar", "Catfish", "Cavalier King Charles Spaniel", "Centipede", "Cesky Fousek", "Chameleon", "Chamois", "Cheetah", "Chesapeake Bay Retriever", "Chicken", "Chihuahua", "Chimpanzee", "Chinchilla", "Chinese Crested Dog", "Chinook", "Chinstrap Penguin", "Chipmunk", "Chow Chow", "Cichlid", "Clouded Leopard", "Clown Fish", "Clumber Spaniel", "Coati", "Cockroach", "Collared Peccary", "Collie", "Common Buzzard", "Common Frog", "Common Loon", "Common Toad", "Coral", "Cottontop Tamarin", "Cougar", "Cow", "Coyote", "Crab", "Crab-Eating Macaque", "Crane", "Crested Penguin", "Crocodile", "Cross River Gorilla", "Curly Coated Retriever", "Cuscus", "Cuttlefish", "Dachshund", "Dalmatian", "Darwin's Frog", "Deer", "Desert Tortoise", "Deutsche Bracke", "Dhole", "Dingo", "Discus", "Doberman Pinscher", "Dodo", "Dog", "Dogo Argentino", "Dogue De Bordeaux", "Dolphin", "Donkey", "Dormouse", "Dragonfly", "Drever", "Duck", "Dugong", "Dunker", "Dusky Dolphin", "Dwarf Crocodile", "Eagle", "Earwig", "Eastern Gorilla", "Eastern Lowland Gorilla", "Echidna", "Edible Frog", "Egyptian Mau", "Electric Eel", "Elephant", "Elephant Seal", "Elephant Shrew", "Emperor Penguin", "Emperor Tamarin", "Emu", "English Cocker Spaniel", "English Shepherd", "English Springer Spaniel", "Entlebucher Mountain Dog", "Epagneul Pont Audemer", "Eskimo Dog", "Estrela Mountain Dog", "Falcon", "Fennec Fox", "Ferret", "Field Spaniel", "Fin Whale", "Finnish Spitz", "Fire-Bellied Toad", "Fish", "Fishing Cat", "Flamingo", "Flat Coat Retriever", "Flounder", "Fly", "Flying Squirrel", "Fossa", "Fox", "Fox Terrier", "French Bulldog", "Frigatebird", "Frilled Lizard", "Frog", "Fur Seal", "Galapagos Penguin", "Galapagos Tortoise", "Gar", "Gecko", "Gentoo Penguin", "Geoffroys Tamarin", "Gerbil", "German Pinscher", "German Shepherd", "Gharial", "Giant African Land Snail", "Giant Clam", "Giant Panda Bear", "Giant Schnauzer", "Gibbon", "Gila Monster", "Giraffe", "Glass Lizard", "Glow Worm", "Goat", "Golden Lion Tamarin", "Golden Oriole", "Golden Retriever", "Goose", "Gopher", "Gorilla", "Grasshopper", "Great Dane", "Great White Shark", "Greater Swiss Mountain Dog", "Green Bee-Eater", "Greenland Dog", "Grey Mouse Lemur", "Grey Reef Shark", "Grey Seal", "Greyhound", "Grizzly Bear", "Grouse", "Guinea Fowl", "Guinea Pig", "Guppy", "Hammerhead Shark", "Hamster", "Hare", "Harrier", "Havanese", "Hedgehog", "Hercules Beetle", "Hermit Crab", "Heron", "Highland Cattle", "Himalayan", "Hippopotamus", "Honey Bee", "Horn Shark", "Horned Frog", "Horse", "Horseshoe Crab", "Howler Monkey", "Human", "Humboldt Penguin", "Hummingbird", "Humpback Whale", "Hyena", "Ibis", "Ibizan Hound", "Iguana", "Impala", "Indian Elephant", "Indian Palm Squirrel", "Indian Rhinoceros", "Indian Star Tortoise", "Indochinese Tiger", "Indri", "Insect", "Irish Setter", "Irish WolfHound", "Jack Russel", "Jackal", "Jaguar", "Japanese Chin", "Japanese Macaque", "Javan Rhinoceros", "Javanese", "Jellyfish", "Kakapo", "Kangaroo", "Keel Billed Toucan", "Killer Whale", "King Crab", "King Penguin", "Kingfisher", "Kiwi", "Koala", "Komodo Dragon", "Kudu", "Labradoodle", "Labrador Retriever", "Ladybird", "Leaf-Tailed Gecko", "Lemming", "Lemur", "Leopard", "Leopard Cat", "Leopard Seal", "Leopard Tortoise", "Liger", "Lion", "Lionfish", "Little Penguin", "Lizard", "Llama", "Lobster", "Long-Eared Owl", "Lynx", "Macaroni Penguin", "Macaw", "Magellanic Penguin", "Magpie", "Maine Coon", "Malayan Civet", "Malayan Tiger", "Maltese", "Manatee", "Mandrill", "Manta Ray", "Marine Toad", "Markhor", "Marsh Frog", "Masked Palm Civet", "Mastiff", "Mayfly", "Meerkat", "Millipede", "Minke Whale", "Mole", "Molly", "Mongoose", "Mongrel", "Monitor Lizard", "Monkey", "Monte Iberia Eleuth", "Moorhen", "Moose", "Moray Eel", "Moth", "Mountain Gorilla", "Mountain Lion", "Mouse", "Mule", "Neanderthal", "Neapolitan Mastiff", "Newfoundland", "Newt", "Nightingale", "Norfolk Terrier", "Norwegian Forest", "Numbat", "Nurse Shark", "Ocelot", "Octopus", "Okapi", "Old English Sheepdog", "Olm", "Opossum", "Orang-utan", "Ostrich", "Otter", "Oyster", "Quail", "Quetzal", "Quokka", "Quoll", "Rabbit", "Raccoon", "Raccoon Dog", "Radiated Tortoise", "Ragdoll", "Rat", "Rattlesnake", "Red Knee Tarantula", "Red Panda", "Red Wolf", "Red-handed Tamarin", "Reindeer", "Rhinoceros", "River Dolphin", "River Turtle", "Robin", "Rock Hyrax", "Rockhopper Penguin", "Roseate Spoonbill", "Rottweiler", "Royal Penguin", "Russian Blue", "Sabre-Toothed Tiger", "Saint Bernard", "Salamander", "Sand Lizard", "Saola", "Scorpion", "Scorpion Fish", "Sea Dragon", "Sea Lion", "Sea Otter", "Sea Slug", "Sea Squirt", "Sea Turtle", "Sea Urchin", "Seahorse", "Seal", "Serval", "Sheep", "Shih Tzu", "Shrimp", "Siamese", "Siamese Fighting Fish", "Siberian", "Siberian Husky", "Siberian Tiger", "Silver Dollar", "Skunk", "Sloth", "Slow Worm", "Snail", "Snake", "Snapping Turtle", "Snowshoe", "Snowy Owl", "Somali", "South China Tiger", "Spadefoot Toad", "Sparrow", "Spectacled Bear", "Sperm Whale", "Spider Monkey", "Spiny Dogfish", "Sponge", "Squid", "Squirrel", "Squirrel Monkey", "Sri Lankan Elephant", "Staffordshire Bull Terrier", "Stag Beetle", "Starfish", "Stellers Sea Cow", "Stick Insect", "Stingray", "Stoat", "Striped Rocket Frog", "Sumatran Elephant", "Sumatran Orang-utan", "Sumatran Rhinoceros", "Sumatran Tiger", "Sun Bear", "Swan", "Tang", "Tapir", "Tarsier", "Tasmanian Devil", "Tawny Owl", "Termite", "Tetra", "Thorny Devil", "Tibetan Mastiff", "Tiffany", "Tiger", "Tiger Salamander", "Tiger Shark", "Tortoise", "Toucan", "Tree Frog", "Tropicbird", "Tuatara", "Turkey", "Turkish Angora", "Uakari", "Uguisu", "Umbrellabird", "Vampire Bat", "Vervet Monkey", "Vulture", "Wallaby", "Walrus", "Warthog", "Wasp", "Water Buffalo", "Water Dragon", "Water Vole", "Weasel", "Welsh Corgi", "West Highland Terrier", "Western Gorilla", "Western Lowland Gorilla", "Whale Shark", "Whippet", "White Faced Capuchin", "White Rhinoceros", "White Tiger", "Wild Boar", "Wildebeest", "Wolf", "Wolverine", "Wombat", "Woodlouse", "Woodpecker", "Woolly Mammoth", "Woolly Monkey", "Wrasse", "X-Ray Tetra", "Yak", "Yellow-Eyed Penguin", "Yorkshire Terrier", "Zebra", "Zebra Shark", "Zebu", "Zonkey", "Zorse"};
	Random r = new Random();
	private boolean leafNodeSplit = false;
	private boolean branchSplit = false;
	private SplitBehavior splitBehavior;
	RTreeCache cacheContainer;
	private int minXInserted;
	private int minYInserted;
	private int maxXInserted;
	private int maxYInserted;
	private StorageType storageType;
	public enum StorageType {
		MYSQL,
		INMEMORY,
		DYNAMODB,
		SQLITE
	}
	private ILogger logger;
	
	
	/**
	 * Constructor
	 * 
	 * @param treeName String
	 * @throws Exception
	 */
	public RTree(String treeName) throws Exception {
		this(treeName, 4, 4);
	}
	
	/**
	 * Constructor
	 * 
	 * @param treeName
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTree(String treeName, int maxChildren, int maxItems) throws Exception {
		this(treeName, maxChildren, maxItems, StorageType.MYSQL, new LoggerStdOut(LogLevel.PROD)); // default to MySQL, PROD
	}
	
	/**
	 * Constructor
	 * 
	 * @param treeName
	 * @param maxChildren
	 * @param maxItems
	 * @param storageType
	 * @throws Exception
	 */
	public RTree(String treeName, int maxChildren, int maxItems, StorageType storageType, ILogger logger) throws Exception {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.treeName = treeName;
		this.storageType = storageType;
		this.logger = logger;
		System.out.println("Cloud RTree initializing. Log level set to " + logger.getLogLevel() + ".");
		init();
	}

	
	/**
	 * Initialize the split behavior, the cache container, the max children and items, and the root node configuration
	 * 
	 * @throws Exception 
	 */
	public void init() throws Exception {
		
		
		try {
			cacheContainer = new RTreeCache(treeName, storageType, logger);
		} catch (Exception e) {
			logger.log("Cache initialization failed.");
			e.printStackTrace();
			throw new Exception("CloudRTree initialization failed.");
		}
		
		if (!metaDataExists()) {
			addToMetaData(treeName, maxChildren, maxItems);
		}
		
		maxChildren = getMaxChildren();
		maxItems = getMaxItems();
		
		root = getNode(treeName);
		if (root == null) {
			addNode(treeName, null, null, null, null, null);
			root = new RTreeNode(treeName, null, null, cacheContainer, logger);
		}
		splitBehavior = new SplitQuadratic(cacheContainer, maxChildren, root, treeName, logger);
	}
	
	/**
	 * Metadata table persistently stores the configuration of the R-Tree (the max children and max items)
	 * 
	 * @param tableName
	 * @param maxChildren
	 * @param maxItems
	 * @return void
	 * @throws Exception 
	 * 
	 */
	public void addToMetaData(String tableName, int maxChildren, int maxItems) throws Exception {
		cacheContainer.getDBAccess().addToMetaData(tableName, maxChildren, maxItems);
	}
	
	/**
	 * Check if metadata exists for the member treeName
	 * 
	 * @param
	 * @return true if metadata for this tree is defined
	 * @throws Exception 
	 * 
	 */
	public boolean metaDataExists() throws Exception {
		return cacheContainer.getDBAccess().metaDataExists(treeName);
	}
	
	/**
	 * Get max children
	 * 
	 * @return maxChildren
	 * 
	 */
	public int getMaxChildren() {
		return cacheContainer.getDBAccess().getMaxChildren(treeName);
	}
	
	/**
	 * Get max items
	 * 
	 * @return maxItems
	 * 
	 */
	public int getMaxItems() {
		return cacheContainer.getDBAccess().getMaxItems(treeName);
	}
	
	
	public int getMaxChildrenVar() {
		return maxChildren;
	}
	
	public int getMaxItemsVar() {
		return maxItems;
	}
	
	/**
	 * Get node with given nodeId
	 * 
	 * @param nodeId
	 * @return CloudRTreeNode object defined by nodeId
	 */
	public RTreeNode getNode(String nodeId) {
		return cacheContainer.getNode(nodeId);
	}
	
	/**
	 * Update node with given Id
	 * 
	 * @param nodeId
	 * @param children
	 * @param parent
	 * @param items
	 * @param rectangle
	 */
	public void updateNode(String nodeId, String children, String parent, String items, String rectangle) {
		cacheContainer.updateNode(nodeId, children, parent, items, rectangle);
	}
	
	/**
	 * Add node with parameters
	 * 
	 * @param nodeId
	 * @param children
	 * @param parent
	 * @param items
	 * @param rectangle
	 * @param node
	 */
	public void addNode(String nodeId, String children, String parent, String items, String rectangle, RTreeNode node) {
		cacheContainer.addNode(nodeId, children, parent, items, rectangle, node);
	}
	
	/**
	 * 
	 * @param locationItem
	 * @throws IOException
	 */
	public void insertType(LocationItem locationItem) throws IOException {
		
		logger.log("GOING TO INSERT: " + locationItem);
		if (locationItem.getX() < minXInserted) {
			minXInserted = locationItem.getX();
		}
		if (locationItem.getX() > maxXInserted) {
			maxXInserted = locationItem.getX();
		}
		if (locationItem.getY() < minYInserted) {
			minYInserted = locationItem.getY();
		}
		if (locationItem.getY() > maxYInserted) {
			maxYInserted = locationItem.getY();
		}
		leafNodeSplit = false;
		branchSplit = false;
		insert(locationItem, root);
		
//		printTree();
		
		logger.log("MIN X: " + minXInserted + " MIN Y: " + minYInserted + " MAX X: " + maxXInserted + " MAX Y: " + maxYInserted);
	}
	
	/**
	 * Insert a random type of "animal" into the tree
	 * 
	 * @param LocationItem locationItem
	 * @return void
	 * 
	 */
	public void insert(LocationItem locationItem) throws IOException {
		int x = r.nextInt(animals.length);
		locationItem.setType(animals[x]);
		
		logger.log();
		leafNodeSplit = false;
		branchSplit = false;
		insert(locationItem, root);
		logger.log();
//		printTree();
	}
	
	private void insert(LocationItem locationItem, RTreeNode node) throws IOException {
		
		if (node == null) {
			return;
		}
		
		cacheContainer.printCache();
		
		logger.log("~~INSERT: " + locationItem + " into " + node.nodeId + " node.parent: " + node.parent);
		
		
		
		if (node.isLeafNode()) {
			
			if (node.getNumberOfItems() < maxItems) {
				
				logger.log("Is leaf node and less than max, so let's add to " + node.nodeId);
				node.addItem(locationItem, node);
				
			} else {
				
				// SPLIT
				splitBehavior.splitLeafNode(node, locationItem);
				leafNodeSplit = true;
				branchSplit = splitBehavior.didBranchSplit();
				
			}
		} else {
			// not a leaf node
			logger.log("not a leaf node");
			
			List<String> childrenArr = node.getChildren();
			
			for (String s : childrenArr) {
				RTreeNode child = getNode(s);
				logger.log("child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem.getX(), locationItem.getY())) {
					insert(locationItem, child);
					
					return;
					// we're done here
				}
			}
			
			int x = locationItem.getX();
			int y = locationItem.getY();
			int minEnlargementArea = getEnlargementArea(getNode(childrenArr.get(0)), x, y);
			int minEnlargementAreaIndex = 0;
			
			for (int i = 0; i < childrenArr.size(); i++) {
				RTreeNode child = getNode(childrenArr.get(i));
				if (child.isLeafNode()) {
					if (getEnlargementArea(getNode(childrenArr.get(i)), x, y) < minEnlargementArea) {
						minEnlargementArea = getEnlargementArea(getNode(childrenArr.get(i)), x, y);
						minEnlargementAreaIndex = i;
					}
				}
			}
			logger.log("min enlargement area for " + locationItem + " is " + minEnlargementArea + ", index=" + minEnlargementAreaIndex);
			
			Rectangle sumRectangle = Rectangle.sumRectangles(node.rectangle, locationItem);
			node.setRectangle(sumRectangle);
			updateNode(node.nodeId, null, null, null, node.getRectangle().getJson().toJSONString());
			
			addToRectangle(getNode(node.parent), node.rectangle);
			
			insert(locationItem, getNode(childrenArr.get(minEnlargementAreaIndex)));
			
		}
	}
	
	
	private void addToRectangle(RTreeNode node, Rectangle r) {
		if (node == null) {
			return;
		}
		Rectangle sumRectangle = Rectangle.sumRectangles(node.rectangle, r);
		node.setRectangle(sumRectangle);
		updateNode(node.nodeId, null, null, null, node.getRectangle().getJson().toJSONString());
		addToRectangle(getNode(node.parent), sumRectangle);
	}
	
	private int getEnlargementArea(RTreeNode node, int x, int y) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + x + ", " + y);
			return 0;
		}
		
		Rectangle r = node.getRectangle();
		int minX = Math.min(Math.min(x,  r.getX1()), r.getX2());
		int maxX = Math.max(Math.max(x,  r.getX1()), r.getX2());
		
		int minY = Math.min(Math.min(y,  r.getY1()), r.getY2());
		int maxY = Math.max(Math.max(y,  r.getY1()), r.getY2());
		
		Rectangle newRect = new Rectangle(minX, maxX, minY, maxY);
		
		return newRect.getArea() - r.getArea();
	}
	

	
	public List<Rectangle> getRectangles() {
		List<Rectangle> allRectangles = new ArrayList<Rectangle>();
		getRectangles(getNode(treeName), allRectangles, 0);
		return allRectangles;
	}
	
	private void getRectangles(RTreeNode node, List<Rectangle> rectangles, int depth) {
		depth++;
		if (node != null && node.getRectangle() != null) {
			node.getRectangle().setLevel(depth);
			rectangles.add(node.getRectangle());
		}
		if (node != null && !node.isLeafNode()) {
			for (String s : node.getChildren()) {
				RTreeNode child = getNode(s);
				getRectangles(child, rectangles, depth);
			}
		}
		
	}
	
	/**
	 * Get all items in this tree
	 * Caution: could be a big operation
	 * 
	 * @return all items in this tree
	 * 
	 */
	public List<LocationItem> getPoints() {
		List<LocationItem> points = new ArrayList<LocationItem>();
		getPoints(root, points, 0);
		return points;
	}
	
	private void getPoints(RTreeNode node, List<LocationItem> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {
			points.addAll(node.getPoints());
		} else {
			for (String s : node.getChildren()) {
				RTreeNode child = getNode(s);
				points.addAll(child.getPoints());
				getPoints(child, points, depth);
			}
		}
		
	}
	
	
	public Map<LocationItem, Integer> getPointsWithDepth() {
		Map<LocationItem, Integer> points = new HashMap<LocationItem, Integer>();
		
		getPointsWithDepth(root, points, 0);
		return points;
	}
	
	private void getPointsWithDepth(RTreeNode node, Map<LocationItem, Integer> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {
			for (LocationItem i : node.getPoints()) {
				points.put(i, depth);
			}
		} else {
			for (String s : node.getChildren()) {
				RTreeNode child = getNode(s);
				getPointsWithDepth(child, points, depth);
			}
		}
		
	}
	
	
	public void printTree() {
		LogLevel temp = logger.getLogLevel();
		logger.setLogLevel(LogLevel.DEV);
		logger.log("PRINTING TREE: ");
		logger.logExact("nodeId\tparent\trectangle\tnumber children\tnumber items\tdepth\titems");
//		for (int i = 0; i < maxItems; i++) {
//			logger.logExact("\titem");
//		}
		for (int i = 0; i < maxChildren; i++) {
			logger.logExact("\tchild");
		}
		logger.log();
		printTree(root, 0);
		logger.log();
		logger.setLogLevel(temp);
	}

	private void printTree(RTreeNode node, int depth) {
		if (node == null) {
			return;
		}
		int numChildren = 0;
		if (node.getChildren() != null) {
			numChildren = node.getChildren().size();
		}
		
		logger.logExact("_" + node.nodeId + "\t" + node.getParent() + "\t" + node.getRectangle() + 
				"\t" + numChildren + "\t" + node.getNumberOfItems() +  "\t" + depth);
		
		
		List<LocationItem> tempPoints = node.getPoints();
		logger.logExact("\t");
		for (int i = 0; i < tempPoints.size(); i++) {
			logger.logExact(tempPoints.get(i) + ";");
			
		}
		
		if (node.getChildren() != null) {
			for (String s : node.getChildren()) {
				logger.logExact("\t" + s);
			}
		}
		logger.log();
		depth++;
		if (node.getChildren() != null) {
			for (String s : node.getChildren()) {
				RTreeNode child = getNode(s);
				printTree(child, depth);
			}
		}
	}
	
	/**
	 * Query the R-Tree structure and retrieve the items that fall inside the parameter search rectangle
	 * 
	 * @param Rectangle searchRectangle
	 * @return a map of rectangles containing search results
	 * 
	 */
	public Map<Rectangle, List<LocationItem>> search(Rectangle searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		long time = System.currentTimeMillis();
		Map<Rectangle, List<LocationItem>> result = new HashMap<Rectangle, List<LocationItem>>();
		search(searchRectangle, getNode(treeName), result, 0);
		
		
		logger.log("SEARCH consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}
	
	private void search(Rectangle searchRectangle, RTreeNode node, Map<Rectangle, List<LocationItem>> result, int depth) {
		node.rectangle.setLevel(depth);
		if (node.isLeafNode()) {
			
			for (LocationItem item : node.items()) {
				
				if (item.getX() > searchRectangle.getX1() && item.getX() < searchRectangle.getX2() && item.getY() > searchRectangle.getY1() && item.getY() < searchRectangle.getY2()) {
					logger.log("Merry Christmas " + item + " nodeId: " + node.nodeId);
					
					if (result.containsKey(searchRectangle)) {
						if (!result.get(searchRectangle).contains(item)) {
							result.get(searchRectangle).add(item);
						}
					} else {
						result.put(searchRectangle, new ArrayList<LocationItem>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					Rectangle r = getNode(child).getRectangle();
					
					if (Rectangle.rectanglesOverlap(r, searchRectangle)) {
						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
						result.put(r, new ArrayList<LocationItem>());
						search(searchRectangle, getNode(child), result, depth + 1);
					} else {
						logger.log("NO overlap: r: " + r + " searchRectangle: " + searchRectangle);
					}
				}
			}
		}
	}
	
	/**
	 * Delete item from tree
	 * 
	 * @param toDelete Item to be deleted
	 * 
	 */
	public void delete(LocationItem toDelete) {
		delete(toDelete, root);
	}
	
	private void delete(LocationItem toDelete, RTreeNode node) {
		
		if (node.isLeafNode()) {
			for (int i = 0; i < node.getNumberOfItems(); i++) {
				logger.log("comparing node " + node.items().get(i) + " and " + toDelete);
				if (node.items().get(i).getX() == toDelete.getX() && node.items().get(i).getY() == toDelete.getY() && node.items().get(i).getType().equals(toDelete.getType())) {
					node.items().remove(i);
					node.updateRectangle(true);
					
					if (node.getParent() != null) {
						
					}
					
					logger.log("deleted " + toDelete);
					updateNode(node.nodeId, null, null, node.getItemsJSON().toJSONString(), node.getRectangle().getJson().toJSONString());
				}
			}
			
		} else {
			if (node != null && node.getChildren() != null) {
				
				for (String s : node.getChildren()) {
					RTreeNode child = getNode(s);
					Rectangle r = child.getRectangle();
					if (r.containsPoint(toDelete.getX(), toDelete.getY())) {
						delete(toDelete, child);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return tree name
	 */
	public String getName() {
		return treeName;
	}
	
	/**
	 * 
	 * @return number of add operations
	 */
	public int numAdds() {
		return cacheContainer.getDBAccess().getNumAdds();
	}
	
	/**
	 * 
	 * @return number of read operations
	 */
	public int numReads() {
		return cacheContainer.getDBAccess().getNumReads();
	}
	
	/**
	 * 
	 * @return number of update operations
	 */
	public int numUpdates() {
		return cacheContainer.getDBAccess().getNumUpdates();
	}
	
	/**
	 * 
	 * @return total add time
	 */
	public long getAddTime() {
		return cacheContainer.getDBAccess().getAddTime();
	}
	
	/**
	 * 
	 * @return total read time
	 */
	public long getReadTime() {
		return cacheContainer.getDBAccess().getReadTime();
	}
	
	/**
	 * 
	 * @return total update time
	 */
	public long getUpdateTime() {
		return cacheContainer.getDBAccess().getUpdateTime();
	}

	
	public void updateRoot() {
		root = getNode(treeName);
	}
}
