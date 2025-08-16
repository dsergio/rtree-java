package rtree.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

import rtree.item.ILocationItem;
import rtree.item.IRType;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.log.PerformanceMetrics;
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.IDataStorage;
import rtree.storage.StorageType;

/**
 * Abstract base class for R-tree implementations.
 * @param <T> {@link rtree.item.IRType}
 *
 */
public abstract class RTreeBase<T extends IRType<T>> implements IRTree<T> {
	
	/**
	 * The maximum number of children per node.
	 */
	protected int maxChildren;
	
	/**
	 * The maximum number of items per node.
	 */
	protected int maxItems;
	
	/**
	 * The name of the R-tree.
	 */
	protected String treeName;
	
	/**
	 * The list of animal names used for random insertion.
	 */
	String[] animals = {"Abyssinian", "Adelie Penguin", "Affenpinscher", "Afghan Hound", "African Bush Elephant", "African Civet", "African Clawed Frog", "African Forest Elephant", "African Palm Civet", "African Penguin", "African Tree Toad", "African Wild Dog", "Ainu Dog", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "Albatross", "Aldabra Giant Tortoise", "Alligator", "Alpine Dachsbracke", "American Bulldog", "American Cocker Spaniel", "American Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Angelfish", "Ant", "Anteater", "Antelope", "Appenzeller Dog", "Arctic Fox", "Arctic Hare", "Arctic Wolf", "Armadillo", "Asian Elephant", "Asian Giant Hornet", "Asian Palm Civet", "Asiatic Black Bear", "Australian Cattle Dog", "Australian Kelpie Dog", "Australian Mist", "Australian Shepherd", "Australian Terrier", "Avocet", "Axolotl", "Aye Aye", "Baboon", "Bactrian Camel", "Badger", "Balinese", "Banded Palm Civet", "Bandicoot", "Barb", "Barn Owl", "Barnacle", "Barracuda", "Basenji Dog", "Basking Shark", "Basset Hound", "Bat", "Bavarian Mountain Hound", "Beagle", "Bear", "Bearded Collie", "Bearded Dragon", "Beaver", "Bedlington Terrier", "Beetle", "Bengal Tiger", "Bernese Mountain Dog", "Bichon Frise", "Binturong", "Bird", "Birds Of Paradise", "Birman", "Bison", "Black Bear", "Black Rhinoceros", "Black Russian Terrier", "Black Widow Spider", "Bloodhound", "Blue Lacy Dog", "Blue Whale", "Bluetick Coonhound", "Bobcat", "Bolognese Dog", "Bombay", "Bongo", "Bonobo", "Booby", "Border Collie", "Border Terrier", "Bornean Orang-utan", "Borneo Elephant", "Boston Terrier", "Bottle Nosed Dolphin", "Boxer Dog", "Boykin Spaniel", "Brazilian Terrier", "Brown Bear", "Budgerigar", "Buffalo", "Bull Mastiff", "Bull Shark", "Bull Terrier", "Bulldog", "Bullfrog", "Bumble Bee", "Burmese", "Burrowing Frog", "Butterfly", "Butterfly Fish", "Caiman", "Caiman Lizard", "Cairn Terrier", "Camel", "Canaan Dog", "Capybara", "Caracal", "Carolina Dog", "Cassowary", "Cat", "Caterpillar", "Catfish", "Cavalier King Charles Spaniel", "Centipede", "Cesky Fousek", "Chameleon", "Chamois", "Cheetah", "Chesapeake Bay Retriever", "Chicken", "Chihuahua", "Chimpanzee", "Chinchilla", "Chinese Crested Dog", "Chinook", "Chinstrap Penguin", "Chipmunk", "Chow Chow", "Cichlid", "Clouded Leopard", "Clown Fish", "Clumber Spaniel", "Coati", "Cockroach", "Collared Peccary", "Collie", "Common Buzzard", "Common Frog", "Common Loon", "Common Toad", "Coral", "Cottontop Tamarin", "Cougar", "Cow", "Coyote", "Crab", "Crab-Eating Macaque", "Crane", "Crested Penguin", "Crocodile", "Cross River Gorilla", "Curly Coated Retriever", "Cuscus", "Cuttlefish", "Dachshund", "Dalmatian", "Darwin's Frog", "Deer", "Desert Tortoise", "Deutsche Bracke", "Dhole", "Dingo", "Discus", "Doberman Pinscher", "Dodo", "Dog", "Dogo Argentino", "Dogue De Bordeaux", "Dolphin", "Donkey", "Dormouse", "Dragonfly", "Drever", "Duck", "Dugong", "Dunker", "Dusky Dolphin", "Dwarf Crocodile", "Eagle", "Earwig", "Eastern Gorilla", "Eastern Lowland Gorilla", "Echidna", "Edible Frog", "Egyptian Mau", "Electric Eel", "Elephant", "Elephant Seal", "Elephant Shrew", "Emperor Penguin", "Emperor Tamarin", "Emu", "English Cocker Spaniel", "English Shepherd", "English Springer Spaniel", "Entlebucher Mountain Dog", "Epagneul Pont Audemer", "Eskimo Dog", "Estrela Mountain Dog", "Falcon", "Fennec Fox", "Ferret", "Field Spaniel", "Fin Whale", "Finnish Spitz", "Fire-Bellied Toad", "Fish", "Fishing Cat", "Flamingo", "Flat Coat Retriever", "Flounder", "Fly", "Flying Squirrel", "Fossa", "Fox", "Fox Terrier", "French Bulldog", "Frigatebird", "Frilled Lizard", "Frog", "Fur Seal", "Galapagos Penguin", "Galapagos Tortoise", "Gar", "Gecko", "Gentoo Penguin", "Geoffroys Tamarin", "Gerbil", "German Pinscher", "German Shepherd", "Gharial", "Giant African Land Snail", "Giant Clam", "Giant Panda Bear", "Giant Schnauzer", "Gibbon", "Gila Monster", "Giraffe", "Glass Lizard", "Glow Worm", "Goat", "Golden Lion Tamarin", "Golden Oriole", "Golden Retriever", "Goose", "Gopher", "Gorilla", "Grasshopper", "Great Dane", "Great White Shark", "Greater Swiss Mountain Dog", "Green Bee-Eater", "Greenland Dog", "Grey Mouse Lemur", "Grey Reef Shark", "Grey Seal", "Greyhound", "Grizzly Bear", "Grouse", "Guinea Fowl", "Guinea Pig", "Guppy", "Hammerhead Shark", "Hamster", "Hare", "Harrier", "Havanese", "Hedgehog", "Hercules Beetle", "Hermit Crab", "Heron", "Highland Cattle", "Himalayan", "Hippopotamus", "Honey Bee", "Horn Shark", "Horned Frog", "Horse", "Horseshoe Crab", "Howler Monkey", "Human", "Humboldt Penguin", "Hummingbird", "Humpback Whale", "Hyena", "Ibis", "Ibizan Hound", "Iguana", "Impala", "Indian Elephant", "Indian Palm Squirrel", "Indian Rhinoceros", "Indian Star Tortoise", "Indochinese Tiger", "Indri", "Insect", "Irish Setter", "Irish WolfHound", "Jack Russel", "Jackal", "Jaguar", "Japanese Chin", "Japanese Macaque", "Javan Rhinoceros", "Javanese", "Jellyfish", "Kakapo", "Kangaroo", "Keel Billed Toucan", "Killer Whale", "King Crab", "King Penguin", "Kingfisher", "Kiwi", "Koala", "Komodo Dragon", "Kudu", "Labradoodle", "Labrador Retriever", "Ladybird", "Leaf-Tailed Gecko", "Lemming", "Lemur", "Leopard", "Leopard Cat", "Leopard Seal", "Leopard Tortoise", "Liger", "Lion", "Lionfish", "Little Penguin", "Lizard", "Llama", "Lobster", "Long-Eared Owl", "Lynx", "Macaroni Penguin", "Macaw", "Magellanic Penguin", "Magpie", "Maine Coon", "Malayan Civet", "Malayan Tiger", "Maltese", "Manatee", "Mandrill", "Manta Ray", "Marine Toad", "Markhor", "Marsh Frog", "Masked Palm Civet", "Mastiff", "Mayfly", "Meerkat", "Millipede", "Minke Whale", "Mole", "Molly", "Mongoose", "Mongrel", "Monitor Lizard", "Monkey", "Monte Iberia Eleuth", "Moorhen", "Moose", "Moray Eel", "Moth", "Mountain Gorilla", "Mountain Lion", "Mouse", "Mule", "Neanderthal", "Neapolitan Mastiff", "Newfoundland", "Newt", "Nightingale", "Norfolk Terrier", "Norwegian Forest", "Numbat", "Nurse Shark", "Ocelot", "Octopus", "Okapi", "Old English Sheepdog", "Olm", "Opossum", "Orang-utan", "Ostrich", "Otter", "Oyster", "Quail", "Quetzal", "Quokka", "Quoll", "Rabbit", "Raccoon", "Raccoon Dog", "Radiated Tortoise", "Ragdoll", "Rat", "Rattlesnake", "Red Knee Tarantula", "Red Panda", "Red Wolf", "Red-handed Tamarin", "Reindeer", "Rhinoceros", "River Dolphin", "River Turtle", "Robin", "Rock Hyrax", "Rockhopper Penguin", "Roseate Spoonbill", "Rottweiler", "Royal Penguin", "Russian Blue", "Sabre-Toothed Tiger", "Saint Bernard", "Salamander", "Sand Lizard", "Saola", "Scorpion", "Scorpion Fish", "Sea Dragon", "Sea Lion", "Sea Otter", "Sea Slug", "Sea Squirt", "Sea Turtle", "Sea Urchin", "Seahorse", "Seal", "Serval", "Sheep", "Shih Tzu", "Shrimp", "Siamese", "Siamese Fighting Fish", "Siberian", "Siberian Husky", "Siberian Tiger", "Silver Dollar", "Skunk", "Sloth", "Slow Worm", "Snail", "Snake", "Snapping Turtle", "Snowshoe", "Snowy Owl", "Somali", "South China Tiger", "Spadefoot Toad", "Sparrow", "Spectacled Bear", "Sperm Whale", "Spider Monkey", "Spiny Dogfish", "Sponge", "Squid", "Squirrel", "Squirrel Monkey", "Sri Lankan Elephant", "Staffordshire Bull Terrier", "Stag Beetle", "Starfish", "Stellers Sea Cow", "Stick Insect", "Stingray", "Stoat", "Striped Rocket Frog", "Sumatran Elephant", "Sumatran Orang-utan", "Sumatran Rhinoceros", "Sumatran Tiger", "Sun Bear", "Swan", "Tang", "Tapir", "Tarsier", "Tasmanian Devil", "Tawny Owl", "Termite", "Tetra", "Thorny Devil", "Tibetan Mastiff", "Tiffany", "Tiger", "Tiger Salamander", "Tiger Shark", "Tortoise", "Toucan", "Tree Frog", "Tropicbird", "Tuatara", "Turkey", "Turkish Angora", "Uakari", "Uguisu", "Umbrellabird", "Vampire Bat", "Vervet Monkey", "Vulture", "Wallaby", "Walrus", "Warthog", "Wasp", "Water Buffalo", "Water Dragon", "Water Vole", "Weasel", "Welsh Corgi", "West Highland Terrier", "Western Gorilla", "Western Lowland Gorilla", "Whale Shark", "Whippet", "White Faced Capuchin", "White Rhinoceros", "White Tiger", "Wild Boar", "Wildebeest", "Wolf", "Wolverine", "Wombat", "Woodlouse", "Woodpecker", "Woolly Mammoth", "Woolly Monkey", "Wrasse", "X-Ray Tetra", "Yak", "Yellow-Eyed Penguin", "Yorkshire Terrier", "Zebra", "Zebra Shark", "Zebu", "Zonkey", "Zorse"};
	
	/**
	 * Random number generator for random operations.
	 */
	Random r = new Random();
	
	/**
	 * The split behavior used for the R-tree.
	 */
	protected SplitBehaviorBase<T> splitBehavior;
	
	/**
	 * The cache used for the R-tree.
	 */
	protected IRTreeCache<T> cache;
	
	/**
	 * The performance metrics for the R-tree.
	 */
	protected StorageType storageType;
	
	/**
	 * The logger used for logging operations in the R-tree.
	 */
	protected ILogger logger;
	
	/**
	 * The minimum X coordinate for the R-tree.
	 */
	protected int minX;
	
	/**
	 * The maximum X coordinate for the R-tree.
	 */
	protected int maxX;
	
	/**
	 * The minimum Y coordinate for the R-tree.
	 */
	protected int minY;
	
	/**
	 * The maximum Y coordinate for the R-tree.
	 */
	protected int maxY;
	
	/**
	 * Indicates whether the boundaries of the R-tree have been set.
	 */
	protected boolean boundariesSet = false;
	
	/**
	 * The number of dimensions for the R-tree.
	 */
	protected int numDimensions;
	
	/**
	 * The list of minimum values for each dimension.
	 */
	protected List<T> maximums;
	
	/**
	 * The list of maximum values for each dimension.
	 */
	protected List<T> minimums;
	
	/**
	 * The class type of the items stored in the R-tree.
	 */
	Class<T> className;
	
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, String treeName, Class<T> className) throws Exception {
		this(dataStorage, 4, 4, treeName, className);
	}
	
	/**
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param logger the logger to use for logging
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, ILogger logger, String treeName, Class<T> className) throws Exception {
		this(dataStorage, 4, 4, logger, treeName, className); // default to Quadratic
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param maxChildren the maximum number of children per node
	 * @param maxItems the maximum number of items per node
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEBUG), treeName, className); // default to DEV, Quadratic
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param maxChildren the maximum number of children per node
	 * @param maxItems the maximum number of items per node
	 * @param numDimensions the number of dimensions for the R-tree
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, int numDimensions, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEBUG), numDimensions, treeName, className); // default to DEV, Quadratic
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * default numDimensions: 2
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param maxChildren the maximum number of children per node
	 * @param maxItems the maximum number of items per node
	 * @param logger the logger to use for logging
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, ILogger logger, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEBUG), 2, treeName, className); // default to DEV, Quadratic, 2-D
	}
	
	/**
	 * The constructor initializes the R-tree with the specified parameters.
	 * 
	 * @param dataStorage the data storage implementation to use
	 * @param maxChildren the maximum number of children per node
	 * @param maxItems the maximum number of items per node
	 * @param logger the logger to use for logging
	 * @param numDimensions the number of dimensions for the R-tree
	 * @param treeName the name of the R-tree
	 * @param className the class of the items stored in the R-tree
	 * @throws Exception if initialization fails
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, ILogger logger, int numDimensions, String treeName, Class<T> className) throws Exception {
		this.maxChildren = maxChildren;
		this.maxItems = maxItems;
		this.treeName = treeName;
		this.storageType = dataStorage.getStorageType();
		this.logger = logger;
		this.className = className;
		
		this.numDimensions = numDimensions;
		
		minimums = new ArrayList<T>();
		maximums = new ArrayList<T>();
		for (int i = 0; i < numDimensions; i++) {
			minimums.add(null);
			maximums.add(null);
		}
		
		init(dataStorage);
		
		String logLevels = "";
		int numValues = LogLevel.values().length;
		int c = 0;
		for (LogLevel l : LogLevel.values()) {
            logLevels += l + " (" + l.ordinal() + ")";
            if (c < (numValues - 1)) {
            	logLevels += ", ";
            }
            c++;
        }
		
		System.out.println("R-Tree initializing...");
		System.out.println(" > Storage Type set to " + storageType + ".");
		System.out.println(" > Split Behavior set to " + splitBehavior.getDescription() + ".");
		System.out.println(" > Log level set to " + logger.getLogLevel() + ".");
		System.out.println(" > > Log Levels: " + logLevels + ".");
		System.out.println(" > Tree Name set to " + treeName + ".");
		System.out.println(" > Max Children set to " + maxChildren + ".");
		System.out.println(" > Max Items set to " + maxItems + ".");
		System.out.println(" > N set to " + numDimensions + ".");
		System.out.println();
		
	}

	
	private void init(IDataStorage<T> dataStorage) throws Exception {
		
		try {
			
			// TODO pull these out, use dependency injection instead
			cache = new RTreeCache<T>(this, logger, dataStorage, className);
			this.splitBehavior = new SplitQuadratic<T>(className);
			
		} catch (Exception e) {
			logger.log("Cache initialization failed.", "init", LogLevel.ERROR, true);
			e.printStackTrace();
			throw new Exception("CloudRTree initialization failed.");
		}
		
		if (!metaDataExists()) {
			cache.getDBAccess().addToMetaData(treeName, maxChildren, maxItems, numDimensions);
		}
		
		
		
		maxChildren = getMaxChildren();
		maxItems = getMaxItems();
		splitBehavior.initialize(maxChildren, treeName, cache, logger);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();
		obj.put("name", treeName);
		obj.put("numDimensions", numDimensions);
		return obj;
	}
	
	public abstract void insertRandomAnimal(ILocationItem<T> locationItem) throws IOException;
	public abstract Map<IHyperRectangle<T>, List<ILocationItem<T>>> search(IHyperRectangle<T> searchRectangle);
	public abstract void delete(ILocationItem<T> toDelete);
	
	
	/**
	 * Checks if the metadata for the R-tree exists in the database.
	 * @return true if metadata exists, false otherwise
	 * @throws Exception if there is an error checking for metadata existence
	 */
	protected boolean metaDataExists() throws Exception {
		return cache.getDBAccess().metaDataExists(treeName);
	}
	
	@Override
	public int getMaxChildren() {
		return cache.getDBAccess().getMaxChildren(treeName);
	}
	
	@Override
	public int getMaxItems() {
		return cache.getDBAccess().getMaxItems(treeName);
	}
	
	@Override
	public List<T> getMin() {
		return cache.getDBAccess().getMin(treeName);
	}
	
	@Override
	public List<T> getMax() {
		return cache.getDBAccess().getMax(treeName);
	}
	
	/**
	 * Adds the given rectangle to the specified node's rectangle in the R-tree.
	 * @param node the R-tree node to which the rectangle will be added
	 * @param r the rectangle to be added
	 */
	protected void addToRectangle(IRTreeNode<T> node, IHyperRectangle<T> r) {
		if (node == null) {
			return;
		}
		List<IHyperRectangle<T>> temp = new ArrayList<IHyperRectangle<T>>();
		temp.add(node.getRectangle());
		temp.add(r);
		IHyperRectangle<T> sumRectangle = HyperRectangleBase.sumRectangles(temp);
		node.setRectangle(sumRectangle);
		
		cache.updateNode(node.getNodeId(), node);
		
		addToRectangle(cache.getNode(node.getParent()), sumRectangle);
	}
	
	@Override
	public List<IHyperRectangle<T>> getAllRectangles() {
		List<IHyperRectangle<T>> allRectangles = new ArrayList<IHyperRectangle<T>>();
		getRectangles(cache.getNode(treeName), allRectangles, 0);
		return allRectangles;
	}

	private void getRectangles(IRTreeNode<T> node, List<IHyperRectangle<T>> rectangles, int depth) {
		depth++;
		if (node != null && node.getRectangle() != null) {
			node.getRectangle().setLevel(depth);
			rectangles.add(node.getRectangle());
		}
		if (node != null && !node.isLeafNode()) {
			for (String s : node.getChildren()) {
				IRTreeNode<T> child = cache.getNode(s);
				getRectangles(child, rectangles, depth);
			}
		}
		
	}
	
	@Override
	public Map<IHyperRectangle<T>, Integer> getAllRectanglesWithDepth() {
		Map<IHyperRectangle<T>, Integer> rectangles = new HashMap<IHyperRectangle<T>, Integer>();
		getRectanglesWithDepth(cache.getNode(treeName), rectangles, 0);
		return rectangles;
	}
	
	private void getRectanglesWithDepth(IRTreeNode<T> node, Map<IHyperRectangle<T>, Integer> rectanglesWithDepth, int depth) {
		depth++;
		if (node != null && node.getRectangle() != null) {
			node.getRectangle().setLevel(depth);
			rectanglesWithDepth.put(node.getRectangle(), depth);
		}
		if (node != null && !node.isLeafNode()) {
			for (String s : node.getChildren()) {
				IRTreeNode<T> child = cache.getNode(s);
				getRectanglesWithDepth(child, rectanglesWithDepth, depth);
			}
		}
		
	}
	
	@Override
	public List<ILocationItem<T>> getAllLocationItems() {
		List<ILocationItem<T>> points = new ArrayList<ILocationItem<T>>();
		getLocationItems(cache.getNode(treeName), points, 0);
		logger.log("getAllLocationItems() returned a list of size " + points.size(), "getAllLocationItems", LogLevel.DEBUG, true);
		return points;
	}
	
	private void getLocationItems(IRTreeNode<T> node, List<ILocationItem<T>> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {			
			points.addAll(node.getLocationItems());
		} else {
			for (String s : node.getChildren()) {
				logger.log("getLocationItems() getting child node: " + s, "getLocationItems", LogLevel.DEBUG, true);
				IRTreeNode<T> child = cache.getNode(s);
				getLocationItems(child, points, depth);
			}
		}
		
	}
	
	@Override
	public Map<ILocationItem<T>, Integer> getAllLocationItemsWithDepth() {
		Map<ILocationItem<T>, Integer> points = new HashMap<ILocationItem<T>, Integer>();
		
		getPointsWithDepth(cache.getNode(treeName), points, 0);
		return points;
	}
	
	private void getPointsWithDepth(IRTreeNode<T> node, Map<ILocationItem<T>, Integer> points, int depth) {
		depth++;
		if (node == null) {
			return;
		}
		if (node.isLeafNode()) {
			for (ILocationItem<T> i : node.getLocationItems()) {
				points.put(i, depth);
			}
		} else {
			for (String s : node.getChildren()) {
				IRTreeNode<T> child = cache.getNode(s);
				getPointsWithDepth(child, points, depth);
			}
		}
		
	}
	
	
	@Override
	public void printTree() {
		LogLevel temp = logger.getLogLevel();
		logger.setLogLevel(LogLevel.DEBUG);
		logger.log("PRINTING TREE: ", "printTree", LogLevel.DEBUG, true);
		logger.log();
		logger.log("nodeId\tparent\trectangle\tnumber children\tnumber items\tdepth", "CSV", LogLevel.DEBUG, true);
		for (int i = 0; i < maxItems; i++) {
			logger.log("\titem", "CSV", LogLevel.DEBUG, false);
		}
		for (int i = 0; i < maxChildren; i++) {
			logger.log("\tchild", "CSV", LogLevel.DEBUG, false);
		}
		printTree(cache.getNode(treeName), 0);
		logger.setLogLevel(temp);
	}

	private void printTree(IRTreeNode<T> node, int depth) {
		if (node == null) {
			return;
		}
		int numChildren = 0;
		if (node.getChildren() != null) {
			numChildren = node.getChildren().size();
		}
		
		logger.log(node.getNodeIdShort() + "\t" + node.getParent() + "\t" + node.getRectangle() + 
				"\t" + numChildren + "\t" + node.getNumberOfItems() +  "\t" + depth + "\t", "CSV", LogLevel.DEBUG, true);
		
		
		List<ILocationItem<T>> tempPoints = node.getLocationItems();
		for (int i = 0; i < maxItems; i++) {
			if (i < tempPoints.size()) {
				logger.log(tempPoints.get(i).getType() + "\t", "CSV", LogLevel.DEBUG, false);
			} else {
				logger.log("\t", "CSV", LogLevel.DEBUG, false);
			}
			
		}
		
		List<String> tempChildren = node.getChildrenShort();
		for (int i = 0; i < maxChildren; i++) {
			if (i < tempChildren.size()) {
				logger.log(tempChildren.get(i) + "\t", "CSV", LogLevel.DEBUG, false);
			} else {
				logger.log("\t", "CSV", LogLevel.DEBUG, false);
			}
		}
		
		
		logger.log();
		depth++;
		if (node.getChildren() != null) {
			for (String s : node.getChildren()) {
				IRTreeNode<T> child = cache.getNode(s);
				printTree(child, depth);
			}
		}
	}
	
	
	@Override
	public String getTreeName() {
		return treeName;
	}
	
	
	@Override
	public int getNumDimensions() {
		return numDimensions;
	}
	
	@Override
	public PerformanceMetrics getPerformance() {
		return cache.getDBAccess().getPerformance();
	}
}


