package cloudrtree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * R-Tree with configurable split algorithms and configurable storage implementations
 * 
 * @author David Sergio
 * 
 *
 */
public class RTree {
	
	private int maxChildren;
	private int maxItems;
	private String treeName;
	String[] animals = {"Abyssinian", "Adelie Penguin", "Affenpinscher", "Afghan Hound", "African Bush Elephant", "African Civet", "African Clawed Frog", "African Forest Elephant", "African Palm Civet", "African Penguin", "African Tree Toad", "African Wild Dog", "Ainu Dog", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "Albatross", "Aldabra Giant Tortoise", "Alligator", "Alpine Dachsbracke", "American Bulldog", "American Cocker Spaniel", "American Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Angelfish", "Ant", "Anteater", "Antelope", "Appenzeller Dog", "Arctic Fox", "Arctic Hare", "Arctic Wolf", "Armadillo", "Asian Elephant", "Asian Giant Hornet", "Asian Palm Civet", "Asiatic Black Bear", "Australian Cattle Dog", "Australian Kelpie Dog", "Australian Mist", "Australian Shepherd", "Australian Terrier", "Avocet", "Axolotl", "Aye Aye", "Baboon", "Bactrian Camel", "Badger", "Balinese", "Banded Palm Civet", "Bandicoot", "Barb", "Barn Owl", "Barnacle", "Barracuda", "Basenji Dog", "Basking Shark", "Basset Hound", "Bat", "Bavarian Mountain Hound", "Beagle", "Bear", "Bearded Collie", "Bearded Dragon", "Beaver", "Bedlington Terrier", "Beetle", "Bengal Tiger", "Bernese Mountain Dog", "Bichon Frise", "Binturong", "Bird", "Birds Of Paradise", "Birman", "Bison", "Black Bear", "Black Rhinoceros", "Black Russian Terrier", "Black Widow Spider", "Bloodhound", "Blue Lacy Dog", "Blue Whale", "Bluetick Coonhound", "Bobcat", "Bolognese Dog", "Bombay", "Bongo", "Bonobo", "Booby", "Border Collie", "Border Terrier", "Bornean Orang-utan", "Borneo Elephant", "Boston Terrier", "Bottle Nosed Dolphin", "Boxer Dog", "Boykin Spaniel", "Brazilian Terrier", "Brown Bear", "Budgerigar", "Buffalo", "Bull Mastiff", "Bull Shark", "Bull Terrier", "Bulldog", "Bullfrog", "Bumble Bee", "Burmese", "Burrowing Frog", "Butterfly", "Butterfly Fish", "Caiman", "Caiman Lizard", "Cairn Terrier", "Camel", "Canaan Dog", "Capybara", "Caracal", "Carolina Dog", "Cassowary", "Cat", "Caterpillar", "Catfish", "Cavalier King Charles Spaniel", "Centipede", "Cesky Fousek", "Chameleon", "Chamois", "Cheetah", "Chesapeake Bay Retriever", "Chicken", "Chihuahua", "Chimpanzee", "Chinchilla", "Chinese Crested Dog", "Chinook", "Chinstrap Penguin", "Chipmunk", "Chow Chow", "Cichlid", "Clouded Leopard", "Clown Fish", "Clumber Spaniel", "Coati", "Cockroach", "Collared Peccary", "Collie", "Common Buzzard", "Common Frog", "Common Loon", "Common Toad", "Coral", "Cottontop Tamarin", "Cougar", "Cow", "Coyote", "Crab", "Crab-Eating Macaque", "Crane", "Crested Penguin", "Crocodile", "Cross River Gorilla", "Curly Coated Retriever", "Cuscus", "Cuttlefish", "Dachshund", "Dalmatian", "Darwin's Frog", "Deer", "Desert Tortoise", "Deutsche Bracke", "Dhole", "Dingo", "Discus", "Doberman Pinscher", "Dodo", "Dog", "Dogo Argentino", "Dogue De Bordeaux", "Dolphin", "Donkey", "Dormouse", "Dragonfly", "Drever", "Duck", "Dugong", "Dunker", "Dusky Dolphin", "Dwarf Crocodile", "Eagle", "Earwig", "Eastern Gorilla", "Eastern Lowland Gorilla", "Echidna", "Edible Frog", "Egyptian Mau", "Electric Eel", "Elephant", "Elephant Seal", "Elephant Shrew", "Emperor Penguin", "Emperor Tamarin", "Emu", "English Cocker Spaniel", "English Shepherd", "English Springer Spaniel", "Entlebucher Mountain Dog", "Epagneul Pont Audemer", "Eskimo Dog", "Estrela Mountain Dog", "Falcon", "Fennec Fox", "Ferret", "Field Spaniel", "Fin Whale", "Finnish Spitz", "Fire-Bellied Toad", "Fish", "Fishing Cat", "Flamingo", "Flat Coat Retriever", "Flounder", "Fly", "Flying Squirrel", "Fossa", "Fox", "Fox Terrier", "French Bulldog", "Frigatebird", "Frilled Lizard", "Frog", "Fur Seal", "Galapagos Penguin", "Galapagos Tortoise", "Gar", "Gecko", "Gentoo Penguin", "Geoffroys Tamarin", "Gerbil", "German Pinscher", "German Shepherd", "Gharial", "Giant African Land Snail", "Giant Clam", "Giant Panda Bear", "Giant Schnauzer", "Gibbon", "Gila Monster", "Giraffe", "Glass Lizard", "Glow Worm", "Goat", "Golden Lion Tamarin", "Golden Oriole", "Golden Retriever", "Goose", "Gopher", "Gorilla", "Grasshopper", "Great Dane", "Great White Shark", "Greater Swiss Mountain Dog", "Green Bee-Eater", "Greenland Dog", "Grey Mouse Lemur", "Grey Reef Shark", "Grey Seal", "Greyhound", "Grizzly Bear", "Grouse", "Guinea Fowl", "Guinea Pig", "Guppy", "Hammerhead Shark", "Hamster", "Hare", "Harrier", "Havanese", "Hedgehog", "Hercules Beetle", "Hermit Crab", "Heron", "Highland Cattle", "Himalayan", "Hippopotamus", "Honey Bee", "Horn Shark", "Horned Frog", "Horse", "Horseshoe Crab", "Howler Monkey", "Human", "Humboldt Penguin", "Hummingbird", "Humpback Whale", "Hyena", "Ibis", "Ibizan Hound", "Iguana", "Impala", "Indian Elephant", "Indian Palm Squirrel", "Indian Rhinoceros", "Indian Star Tortoise", "Indochinese Tiger", "Indri", "Insect", "Irish Setter", "Irish WolfHound", "Jack Russel", "Jackal", "Jaguar", "Japanese Chin", "Japanese Macaque", "Javan Rhinoceros", "Javanese", "Jellyfish", "Kakapo", "Kangaroo", "Keel Billed Toucan", "Killer Whale", "King Crab", "King Penguin", "Kingfisher", "Kiwi", "Koala", "Komodo Dragon", "Kudu", "Labradoodle", "Labrador Retriever", "Ladybird", "Leaf-Tailed Gecko", "Lemming", "Lemur", "Leopard", "Leopard Cat", "Leopard Seal", "Leopard Tortoise", "Liger", "Lion", "Lionfish", "Little Penguin", "Lizard", "Llama", "Lobster", "Long-Eared Owl", "Lynx", "Macaroni Penguin", "Macaw", "Magellanic Penguin", "Magpie", "Maine Coon", "Malayan Civet", "Malayan Tiger", "Maltese", "Manatee", "Mandrill", "Manta Ray", "Marine Toad", "Markhor", "Marsh Frog", "Masked Palm Civet", "Mastiff", "Mayfly", "Meerkat", "Millipede", "Minke Whale", "Mole", "Molly", "Mongoose", "Mongrel", "Monitor Lizard", "Monkey", "Monte Iberia Eleuth", "Moorhen", "Moose", "Moray Eel", "Moth", "Mountain Gorilla", "Mountain Lion", "Mouse", "Mule", "Neanderthal", "Neapolitan Mastiff", "Newfoundland", "Newt", "Nightingale", "Norfolk Terrier", "Norwegian Forest", "Numbat", "Nurse Shark", "Ocelot", "Octopus", "Okapi", "Old English Sheepdog", "Olm", "Opossum", "Orang-utan", "Ostrich", "Otter", "Oyster", "Quail", "Quetzal", "Quokka", "Quoll", "Rabbit", "Raccoon", "Raccoon Dog", "Radiated Tortoise", "Ragdoll", "Rat", "Rattlesnake", "Red Knee Tarantula", "Red Panda", "Red Wolf", "Red-handed Tamarin", "Reindeer", "Rhinoceros", "River Dolphin", "River Turtle", "Robin", "Rock Hyrax", "Rockhopper Penguin", "Roseate Spoonbill", "Rottweiler", "Royal Penguin", "Russian Blue", "Sabre-Toothed Tiger", "Saint Bernard", "Salamander", "Sand Lizard", "Saola", "Scorpion", "Scorpion Fish", "Sea Dragon", "Sea Lion", "Sea Otter", "Sea Slug", "Sea Squirt", "Sea Turtle", "Sea Urchin", "Seahorse", "Seal", "Serval", "Sheep", "Shih Tzu", "Shrimp", "Siamese", "Siamese Fighting Fish", "Siberian", "Siberian Husky", "Siberian Tiger", "Silver Dollar", "Skunk", "Sloth", "Slow Worm", "Snail", "Snake", "Snapping Turtle", "Snowshoe", "Snowy Owl", "Somali", "South China Tiger", "Spadefoot Toad", "Sparrow", "Spectacled Bear", "Sperm Whale", "Spider Monkey", "Spiny Dogfish", "Sponge", "Squid", "Squirrel", "Squirrel Monkey", "Sri Lankan Elephant", "Staffordshire Bull Terrier", "Stag Beetle", "Starfish", "Stellers Sea Cow", "Stick Insect", "Stingray", "Stoat", "Striped Rocket Frog", "Sumatran Elephant", "Sumatran Orang-utan", "Sumatran Rhinoceros", "Sumatran Tiger", "Sun Bear", "Swan", "Tang", "Tapir", "Tarsier", "Tasmanian Devil", "Tawny Owl", "Termite", "Tetra", "Thorny Devil", "Tibetan Mastiff", "Tiffany", "Tiger", "Tiger Salamander", "Tiger Shark", "Tortoise", "Toucan", "Tree Frog", "Tropicbird", "Tuatara", "Turkey", "Turkish Angora", "Uakari", "Uguisu", "Umbrellabird", "Vampire Bat", "Vervet Monkey", "Vulture", "Wallaby", "Walrus", "Warthog", "Wasp", "Water Buffalo", "Water Dragon", "Water Vole", "Weasel", "Welsh Corgi", "West Highland Terrier", "Western Gorilla", "Western Lowland Gorilla", "Whale Shark", "Whippet", "White Faced Capuchin", "White Rhinoceros", "White Tiger", "Wild Boar", "Wildebeest", "Wolf", "Wolverine", "Wombat", "Woodlouse", "Woodpecker", "Woolly Mammoth", "Woolly Monkey", "Wrasse", "X-Ray Tetra", "Yak", "Yellow-Eyed Penguin", "Yorkshire Terrier", "Zebra", "Zebra Shark", "Zebu", "Zonkey", "Zorse"};
	Random r = new Random();
//	private boolean leafNodeSplit = false;
//	private boolean branchSplit = false;
	private SplitBehavior splitBehavior;
	RTreeCache cacheContainer;
	private StorageType storageType;
	private ILogger logger;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private boolean boundariesSet = false;
	
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param treeName
	 * @throws Exception
	 */
	public RTree(DataStorageBase dataStorage, String treeName) throws Exception {
		this(dataStorage, treeName, 4, 4);
	}
	
	/**
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param treeName
	 * @param storageType
	 * @param logger
	 * @throws Exception
	 */
	public RTree(DataStorageBase dataStorage, String treeName, ILogger logger) throws Exception {
		this(dataStorage, treeName, 4, 4, logger, new SplitQuadratic());
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param treeName
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTree(DataStorageBase dataStorage, String treeName, int maxChildren, int maxItems) throws Exception {
		this(dataStorage, treeName, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEV), new SplitQuadratic()); // default to MySQL, DEV
	}
	
	/**
	 * 
	 * @param dataStorage
	 * @param treeName
	 * @param maxChildren
	 * @param maxItems
	 * @param storageType
	 * @param logger
	 * @param splitBehavior
	 * @throws Exception
	 */
	public RTree(DataStorageBase dataStorage, String treeName, int maxChildren, int maxItems, ILogger logger, SplitBehavior splitBehavior) throws Exception {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.treeName = treeName;
		this.storageType = dataStorage.storageType;
		this.logger = logger;
		this.splitBehavior = splitBehavior;
		
		System.out.println("Cloud R-Tree initializing...");
		System.out.println(" > Storage Type set to " + storageType + ".");
		System.out.println(" > Split Behavior set to " + splitBehavior.getDescription() + ".");
		System.out.println(" > Log level set to " + logger.getLogLevel() + ".");
		System.out.println(" > Tree Name set to " + treeName + ".");
		System.out.println(" > Max Children set to " + maxChildren + ".");
		System.out.println(" > Max Items set to " + maxItems + ".");
		System.out.println();
		init(dataStorage);
	}

	
	/**
	 * Initialize the split behavior, the cache container, the max children and items, and the root node configuration
	 * 
	 * @throws Exception 
	 */
	private void init(IDataStorage dataStorage) throws Exception {
		
		try {
			cacheContainer = new RTreeCache(treeName, logger, dataStorage);
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
		splitBehavior.initialize(maxChildren, treeName, cacheContainer, logger);
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
	public void addToMetaData(String treeName, int maxChildren, int maxItems) throws Exception {
		cacheContainer.getDBAccess().addToMetaData(treeName, maxChildren, maxItems);
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
	 * Add node with parameters
	 * 
	 * @param nodeId
	 * @param children
	 * @param parent
	 * @param items
	 * @param rectangle
	 */
	public void addNode(String nodeId, String children, String parent, String items, String rectangle) {
		cacheContainer.addNode(nodeId, children, parent, items, rectangle);
	}
	
	/**
	 * 
	 * @param locationItem
	 * @throws IOException
	 */
	public void insertType(ILocationItem locationItem) throws IOException {
		
		logger.log("~~INSERT: " + "GOING TO INSERT: " + locationItem);
//		leafNodeSplit = false;
//		branchSplit = false;
		insert(locationItem, getNode(treeName));
//		printTree();
	}
	
	/**
	 * Insert a random type of "animal" into the tree
	 * 
	 * @param LocationItem locationItem
	 * @return void
	 * 
	 */
	public void insert(ILocationItem locationItem) throws IOException {
		int x = r.nextInt(animals.length);
		locationItem.setType(animals[x]);
		
		logger.log();
		logger.log();
//		leafNodeSplit = false;
//		branchSplit = false;
		logger.log("~~INSERT: " + "START");
		insert(locationItem, getNode(treeName));
		logger.log("**************************************************************************************************************************");
//		printTree();
	}
	
	private void insert(ILocationItem locationItem, RTreeNode node) throws IOException {
		
		if (node == null && getNode(treeName) == null) {
			IHyperRectangle r = new Rectangle(locationItem.getDim(0), locationItem.getDim(0), locationItem.getDim(1), locationItem.getDim(1));
			addNode(treeName, null, null, null, r.getJson().toJSONString());
			node = getNode(treeName);
		}
		
		if (node == null) {
			return;
		}
		
		logger.log("~~INSERT: " + locationItem + " into " + node.nodeId + " node.parent: " + node.parent);
		
		if (node.isLeafNode()) {
			
			if (node.getNumberOfItems() < maxItems) {
				
				logger.log("~~INSERT: " + "Is leaf node and less than max, so let's add to " + node.nodeId);
				node.addItem(locationItem);
				
				boolean updateBoundaries = false;
				if (locationItem.getDim(0) > maxX || !boundariesSet) {
					maxX = locationItem.getDim(0);
					updateBoundaries = true;
				}
				if (locationItem.getDim(0) < minX || !boundariesSet) {
					minX = locationItem.getDim(0);
					updateBoundaries = true;
				}
				if (locationItem.getDim(1) > maxY || !boundariesSet) {
					maxY = locationItem.getDim(1);
					updateBoundaries = true;
				}
				if (locationItem.getDim(1) < minY || !boundariesSet) {
					minY = locationItem.getDim(1);
					updateBoundaries = true;
				}
				
				if (updateBoundaries) {
					cacheContainer.getDBAccess().updateMetaDataBoundaries(minX, maxX, minY, maxY);
					boundariesSet = true;
				}
				
			} else {
				
				// SPLIT
				splitBehavior.splitLeafNode(node, locationItem);
//				leafNodeSplit = true;
//				branchSplit = splitBehavior.didBranchSplit();
				
			}
		} else {
			// not a leaf node
			logger.log("~~INSERT: " + "not a leaf node, drill down");
			
			List<String> childrenArr = node.getChildren();
			
			for (String s : childrenArr) {
				RTreeNode child = getNode(s);
				logger.log("~~INSERT: " + "child: " + child.toString());
				if (child.getRectangle().containsPoint(locationItem)) {
					insert(locationItem, child);
					
					return;
					// we're done here
				}
			}
			
			int x = locationItem.getDim(0);
			int y = locationItem.getDim(1);
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
//			logger.log("min enlargement area for " + locationItem + " is " + minEnlargementArea + ", index=" + minEnlargementAreaIndex);
			
			IHyperRectangle sumRectangle = Rectangle.sumRectangles(node.rectangle, locationItem);
			node.setRectangle(sumRectangle);
			updateNode(node.nodeId, null, null, null, node.getRectangle().getJson().toJSONString());
			addToRectangle(getNode(node.parent), node.rectangle);
			
			insert(locationItem, getNode(childrenArr.get(minEnlargementAreaIndex)));
		}
	}
	
	
	private void addToRectangle(RTreeNode node, IHyperRectangle r) {
		if (node == null) {
			return;
		}
		IHyperRectangle sumRectangle = Rectangle.sumRectangles(node.rectangle, r);
		node.setRectangle(sumRectangle);
		updateNode(node.nodeId, null, null, null, node.getRectangle().getJson().toJSONString());
		addToRectangle(getNode(node.parent), sumRectangle);
	}
	
	private int getEnlargementArea(RTreeNode node, int x, int y) {
		
		if (node.getNumberOfItems() == 0) {
			logger.log("empty, so enlargement is 0 for " + x + ", " + y);
			return 0;
		}
		
		IHyperRectangle r = node.getRectangle();
		int minX = Math.min(Math.min(x,  r.getDim1(0)), r.getDim2(0));
		int maxX = Math.max(Math.max(x,  r.getDim1(0)), r.getDim2(0));
		
		int minY = Math.min(Math.min(y,  r.getDim1(1)), r.getDim2(1));
		int maxY = Math.max(Math.max(y,  r.getDim1(1)), r.getDim2(1));
		
		IHyperRectangle newRect = new Rectangle(minX, maxX, minY, maxY);
		
		return newRect.getSpace() - r.getSpace();
	}
	

	
	public List<IHyperRectangle> getRectangles() {
		List<IHyperRectangle> allRectangles = new ArrayList<IHyperRectangle>();
		getRectangles(getNode(treeName), allRectangles, 0);
		return allRectangles;
	}
	
	private void getRectangles(RTreeNode node, List<IHyperRectangle> rectangles, int depth) {
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
	public List<ILocationItem> getPoints() {
		List<ILocationItem> points = new ArrayList<ILocationItem>();
		getPoints(getNode(treeName), points, 0);
		logger.log("RTree.getPoints() points: " + points);
		logger.log("RTree.getPoints() returned a list of size " + points.size());
		return points;
	}
	
	private void getPoints(RTreeNode node, List<ILocationItem> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {
			points.addAll(node.getPoints());
		} else {
			for (String s : node.getChildren()) {
				RTreeNode child = getNode(s);
				getPoints(child, points, depth);
			}
		}
		
	}
	
	
	public Map<ILocationItem, Integer> getPointsWithDepth() {
		Map<ILocationItem, Integer> points = new HashMap<ILocationItem, Integer>();
		
		getPointsWithDepth(getNode(treeName), points, 0);
		return points;
	}
	
	private void getPointsWithDepth(RTreeNode node, Map<ILocationItem, Integer> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {
			for (ILocationItem i : node.getPoints()) {
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
		for (int i = 0; i < maxItems; i++) {
			logger.logExact("\titem");
		}
		for (int i = 0; i < maxChildren; i++) {
			logger.logExact("\tchild");
		}
		logger.log();
		printTree(getNode(treeName), 0);
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
		
		
		List<ILocationItem> tempPoints = node.getPoints();
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
	public Map<IHyperRectangle, List<ILocationItem>> search(IHyperRectangle searchRectangle) {
		int curAdds = numAdds();
		int curUpdates = numUpdates();
		int curReads = numReads();
		
		long time = System.currentTimeMillis();
		Map<IHyperRectangle, List<ILocationItem>> result = new HashMap<IHyperRectangle, List<ILocationItem>>();
		search(searchRectangle, getNode(treeName), result, 0);
		
		
		logger.log("SEARCH consumed " + (numAdds() - curAdds)  + " adds, " + (numUpdates() - curUpdates) + " updates, " +
				(numReads() - curReads) + " reads, and " + (System.currentTimeMillis() - time) + "ms to complete.");
		return result;
	}
	
	private void search(IHyperRectangle searchRectangle, RTreeNode node, Map<IHyperRectangle, List<ILocationItem>> result, int depth) {
		
		if (node == null) {
			return;
		}
		
		node.rectangle.setLevel(depth);
		if (node.isLeafNode()) {
			
			for (ILocationItem item : node.items()) {
				
				if (item.getDim(0) > searchRectangle.getDim1(0) && item.getDim(0) < searchRectangle.getDim2(0) && item.getDim(1) > searchRectangle.getDim1(1) && item.getDim(1) < searchRectangle.getDim2(1)) {
					logger.log("Merry Christmas " + item + " nodeId: " + node.nodeId);
					
					if (result.containsKey(searchRectangle)) {
						if (!result.get(searchRectangle).contains(item)) {
							result.get(searchRectangle).add(item);
						}
					} else {
						result.put(searchRectangle, new ArrayList<ILocationItem>());
						result.get(searchRectangle).add(item);
					}
				}
			}
		} else {
			
			if (node != null && node.getChildren() != null) {
				
				for (String child : node.getChildren()) {
					
					IHyperRectangle r = getNode(child).getRectangle();
					
					if (Rectangle.rectanglesOverlap(r, searchRectangle)) {
						logger.log("Rectangles overlap: r: " + r + " searchRectangle: " + searchRectangle);
						result.put(r, new ArrayList<ILocationItem>());
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
	public void delete(ILocationItem toDelete) {
		delete(toDelete, getNode(treeName));
	}
	
	private void delete(ILocationItem toDelete, RTreeNode node) {
		
		if (node.isLeafNode()) {
			for (int i = 0; i < node.getNumberOfItems(); i++) {
				logger.log("comparing node " + node.items().get(i) + " and " + toDelete);
				if (node.items().get(i).getDim(0).equals(toDelete.getDim(0)) && node.items().get(i).getDim(1).equals(toDelete.getDim(1)) && node.items().get(i).getType().equals(toDelete.getType())) {
					node.items().remove(i);
					
					updateNode(node.nodeId, null, null, node.getItemsJSON().toJSONString(), null);
					
					node.updateRectangle(true);
					
					logger.log("deleted " + toDelete);
					
				}
			}
			
		} else {
			if (node != null && node.getChildren() != null) {
				
				for (String s : node.getChildren()) {
					RTreeNode child = getNode(s);
					IHyperRectangle r = child.getRectangle();
					if (r.containsPoint(toDelete)) {
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
		getNode(treeName);
	}
}
