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
import rtree.rectangle.HyperRectangleBase;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.IDataStorage;
import rtree.storage.StorageType;
import rtree.storage.IDataStorage;

/**
 * 
 * @author David Sergio
 *
 */
public abstract class RTreeBase<T extends IRType<T>> implements IRTree<T> {
	
	protected int maxChildren;
	protected int maxItems;
	protected String treeName;
	String[] animals = {"Abyssinian", "Adelie Penguin", "Affenpinscher", "Afghan Hound", "African Bush Elephant", "African Civet", "African Clawed Frog", "African Forest Elephant", "African Palm Civet", "African Penguin", "African Tree Toad", "African Wild Dog", "Ainu Dog", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "Albatross", "Aldabra Giant Tortoise", "Alligator", "Alpine Dachsbracke", "American Bulldog", "American Cocker Spaniel", "American Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Angelfish", "Ant", "Anteater", "Antelope", "Appenzeller Dog", "Arctic Fox", "Arctic Hare", "Arctic Wolf", "Armadillo", "Asian Elephant", "Asian Giant Hornet", "Asian Palm Civet", "Asiatic Black Bear", "Australian Cattle Dog", "Australian Kelpie Dog", "Australian Mist", "Australian Shepherd", "Australian Terrier", "Avocet", "Axolotl", "Aye Aye", "Baboon", "Bactrian Camel", "Badger", "Balinese", "Banded Palm Civet", "Bandicoot", "Barb", "Barn Owl", "Barnacle", "Barracuda", "Basenji Dog", "Basking Shark", "Basset Hound", "Bat", "Bavarian Mountain Hound", "Beagle", "Bear", "Bearded Collie", "Bearded Dragon", "Beaver", "Bedlington Terrier", "Beetle", "Bengal Tiger", "Bernese Mountain Dog", "Bichon Frise", "Binturong", "Bird", "Birds Of Paradise", "Birman", "Bison", "Black Bear", "Black Rhinoceros", "Black Russian Terrier", "Black Widow Spider", "Bloodhound", "Blue Lacy Dog", "Blue Whale", "Bluetick Coonhound", "Bobcat", "Bolognese Dog", "Bombay", "Bongo", "Bonobo", "Booby", "Border Collie", "Border Terrier", "Bornean Orang-utan", "Borneo Elephant", "Boston Terrier", "Bottle Nosed Dolphin", "Boxer Dog", "Boykin Spaniel", "Brazilian Terrier", "Brown Bear", "Budgerigar", "Buffalo", "Bull Mastiff", "Bull Shark", "Bull Terrier", "Bulldog", "Bullfrog", "Bumble Bee", "Burmese", "Burrowing Frog", "Butterfly", "Butterfly Fish", "Caiman", "Caiman Lizard", "Cairn Terrier", "Camel", "Canaan Dog", "Capybara", "Caracal", "Carolina Dog", "Cassowary", "Cat", "Caterpillar", "Catfish", "Cavalier King Charles Spaniel", "Centipede", "Cesky Fousek", "Chameleon", "Chamois", "Cheetah", "Chesapeake Bay Retriever", "Chicken", "Chihuahua", "Chimpanzee", "Chinchilla", "Chinese Crested Dog", "Chinook", "Chinstrap Penguin", "Chipmunk", "Chow Chow", "Cichlid", "Clouded Leopard", "Clown Fish", "Clumber Spaniel", "Coati", "Cockroach", "Collared Peccary", "Collie", "Common Buzzard", "Common Frog", "Common Loon", "Common Toad", "Coral", "Cottontop Tamarin", "Cougar", "Cow", "Coyote", "Crab", "Crab-Eating Macaque", "Crane", "Crested Penguin", "Crocodile", "Cross River Gorilla", "Curly Coated Retriever", "Cuscus", "Cuttlefish", "Dachshund", "Dalmatian", "Darwin's Frog", "Deer", "Desert Tortoise", "Deutsche Bracke", "Dhole", "Dingo", "Discus", "Doberman Pinscher", "Dodo", "Dog", "Dogo Argentino", "Dogue De Bordeaux", "Dolphin", "Donkey", "Dormouse", "Dragonfly", "Drever", "Duck", "Dugong", "Dunker", "Dusky Dolphin", "Dwarf Crocodile", "Eagle", "Earwig", "Eastern Gorilla", "Eastern Lowland Gorilla", "Echidna", "Edible Frog", "Egyptian Mau", "Electric Eel", "Elephant", "Elephant Seal", "Elephant Shrew", "Emperor Penguin", "Emperor Tamarin", "Emu", "English Cocker Spaniel", "English Shepherd", "English Springer Spaniel", "Entlebucher Mountain Dog", "Epagneul Pont Audemer", "Eskimo Dog", "Estrela Mountain Dog", "Falcon", "Fennec Fox", "Ferret", "Field Spaniel", "Fin Whale", "Finnish Spitz", "Fire-Bellied Toad", "Fish", "Fishing Cat", "Flamingo", "Flat Coat Retriever", "Flounder", "Fly", "Flying Squirrel", "Fossa", "Fox", "Fox Terrier", "French Bulldog", "Frigatebird", "Frilled Lizard", "Frog", "Fur Seal", "Galapagos Penguin", "Galapagos Tortoise", "Gar", "Gecko", "Gentoo Penguin", "Geoffroys Tamarin", "Gerbil", "German Pinscher", "German Shepherd", "Gharial", "Giant African Land Snail", "Giant Clam", "Giant Panda Bear", "Giant Schnauzer", "Gibbon", "Gila Monster", "Giraffe", "Glass Lizard", "Glow Worm", "Goat", "Golden Lion Tamarin", "Golden Oriole", "Golden Retriever", "Goose", "Gopher", "Gorilla", "Grasshopper", "Great Dane", "Great White Shark", "Greater Swiss Mountain Dog", "Green Bee-Eater", "Greenland Dog", "Grey Mouse Lemur", "Grey Reef Shark", "Grey Seal", "Greyhound", "Grizzly Bear", "Grouse", "Guinea Fowl", "Guinea Pig", "Guppy", "Hammerhead Shark", "Hamster", "Hare", "Harrier", "Havanese", "Hedgehog", "Hercules Beetle", "Hermit Crab", "Heron", "Highland Cattle", "Himalayan", "Hippopotamus", "Honey Bee", "Horn Shark", "Horned Frog", "Horse", "Horseshoe Crab", "Howler Monkey", "Human", "Humboldt Penguin", "Hummingbird", "Humpback Whale", "Hyena", "Ibis", "Ibizan Hound", "Iguana", "Impala", "Indian Elephant", "Indian Palm Squirrel", "Indian Rhinoceros", "Indian Star Tortoise", "Indochinese Tiger", "Indri", "Insect", "Irish Setter", "Irish WolfHound", "Jack Russel", "Jackal", "Jaguar", "Japanese Chin", "Japanese Macaque", "Javan Rhinoceros", "Javanese", "Jellyfish", "Kakapo", "Kangaroo", "Keel Billed Toucan", "Killer Whale", "King Crab", "King Penguin", "Kingfisher", "Kiwi", "Koala", "Komodo Dragon", "Kudu", "Labradoodle", "Labrador Retriever", "Ladybird", "Leaf-Tailed Gecko", "Lemming", "Lemur", "Leopard", "Leopard Cat", "Leopard Seal", "Leopard Tortoise", "Liger", "Lion", "Lionfish", "Little Penguin", "Lizard", "Llama", "Lobster", "Long-Eared Owl", "Lynx", "Macaroni Penguin", "Macaw", "Magellanic Penguin", "Magpie", "Maine Coon", "Malayan Civet", "Malayan Tiger", "Maltese", "Manatee", "Mandrill", "Manta Ray", "Marine Toad", "Markhor", "Marsh Frog", "Masked Palm Civet", "Mastiff", "Mayfly", "Meerkat", "Millipede", "Minke Whale", "Mole", "Molly", "Mongoose", "Mongrel", "Monitor Lizard", "Monkey", "Monte Iberia Eleuth", "Moorhen", "Moose", "Moray Eel", "Moth", "Mountain Gorilla", "Mountain Lion", "Mouse", "Mule", "Neanderthal", "Neapolitan Mastiff", "Newfoundland", "Newt", "Nightingale", "Norfolk Terrier", "Norwegian Forest", "Numbat", "Nurse Shark", "Ocelot", "Octopus", "Okapi", "Old English Sheepdog", "Olm", "Opossum", "Orang-utan", "Ostrich", "Otter", "Oyster", "Quail", "Quetzal", "Quokka", "Quoll", "Rabbit", "Raccoon", "Raccoon Dog", "Radiated Tortoise", "Ragdoll", "Rat", "Rattlesnake", "Red Knee Tarantula", "Red Panda", "Red Wolf", "Red-handed Tamarin", "Reindeer", "Rhinoceros", "River Dolphin", "River Turtle", "Robin", "Rock Hyrax", "Rockhopper Penguin", "Roseate Spoonbill", "Rottweiler", "Royal Penguin", "Russian Blue", "Sabre-Toothed Tiger", "Saint Bernard", "Salamander", "Sand Lizard", "Saola", "Scorpion", "Scorpion Fish", "Sea Dragon", "Sea Lion", "Sea Otter", "Sea Slug", "Sea Squirt", "Sea Turtle", "Sea Urchin", "Seahorse", "Seal", "Serval", "Sheep", "Shih Tzu", "Shrimp", "Siamese", "Siamese Fighting Fish", "Siberian", "Siberian Husky", "Siberian Tiger", "Silver Dollar", "Skunk", "Sloth", "Slow Worm", "Snail", "Snake", "Snapping Turtle", "Snowshoe", "Snowy Owl", "Somali", "South China Tiger", "Spadefoot Toad", "Sparrow", "Spectacled Bear", "Sperm Whale", "Spider Monkey", "Spiny Dogfish", "Sponge", "Squid", "Squirrel", "Squirrel Monkey", "Sri Lankan Elephant", "Staffordshire Bull Terrier", "Stag Beetle", "Starfish", "Stellers Sea Cow", "Stick Insect", "Stingray", "Stoat", "Striped Rocket Frog", "Sumatran Elephant", "Sumatran Orang-utan", "Sumatran Rhinoceros", "Sumatran Tiger", "Sun Bear", "Swan", "Tang", "Tapir", "Tarsier", "Tasmanian Devil", "Tawny Owl", "Termite", "Tetra", "Thorny Devil", "Tibetan Mastiff", "Tiffany", "Tiger", "Tiger Salamander", "Tiger Shark", "Tortoise", "Toucan", "Tree Frog", "Tropicbird", "Tuatara", "Turkey", "Turkish Angora", "Uakari", "Uguisu", "Umbrellabird", "Vampire Bat", "Vervet Monkey", "Vulture", "Wallaby", "Walrus", "Warthog", "Wasp", "Water Buffalo", "Water Dragon", "Water Vole", "Weasel", "Welsh Corgi", "West Highland Terrier", "Western Gorilla", "Western Lowland Gorilla", "Whale Shark", "Whippet", "White Faced Capuchin", "White Rhinoceros", "White Tiger", "Wild Boar", "Wildebeest", "Wolf", "Wolverine", "Wombat", "Woodlouse", "Woodpecker", "Woolly Mammoth", "Woolly Monkey", "Wrasse", "X-Ray Tetra", "Yak", "Yellow-Eyed Penguin", "Yorkshire Terrier", "Zebra", "Zebra Shark", "Zebu", "Zonkey", "Zorse"};
	Random r = new Random();
	protected SplitBehaviorBase<T> splitBehavior;
	protected IRTreeCache<T> cache;
	protected StorageType storageType;
	protected ILogger logger;
	protected int minX;
	protected int maxX;
	protected int minY;
	protected int maxY;
	protected boolean boundariesSet = false;
	
	protected int numDimensions;
	protected List<T> maximums;
	protected List<T> minimums;
	Class<T> className;
	
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @throws Exception
	 */
	public RTreeBase(IDataStorage<T> dataStorage, String treeName, Class<T> className) throws Exception {
		this(dataStorage, 4, 4, treeName, className);
	}
	
	/**
	 * default maxChildren: 4
	 * default maxItems: 4
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param storageType
	 * @param logger
	 * @throws Exception
	 */
	public RTreeBase(IDataStorage<T> dataStorage, ILogger logger, String treeName, Class<T> className) throws Exception {
		this(dataStorage, 4, 4, logger, treeName, className); // default to Quadratic
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEV), treeName, className); // default to DEV, Quadratic
	}
	
	/**
	 * default LogLevel: LogLevel.DEV
	 * default SplitBehavior: SplitQuadratic
	 * 
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @throws Exception
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, int numDimensions, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEV), numDimensions, treeName, className); // default to DEV, Quadratic
	}
	
	/**
	 * 
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param storageType
	 * @param logger
	 * @param splitBehavior
	 * @throws Exception
	 */
	public RTreeBase(IDataStorage<T> dataStorage, int maxChildren, int maxItems, ILogger logger, String treeName, Class<T> className) throws Exception {
		this(dataStorage, maxChildren, maxItems, new LoggerStdOut(LogLevel.DEV), 2, treeName, className); // default to DEV, Quadratic, 2-D
	}
	
	/**
	 * 
	 * @param dataStorage
	 * @param maxChildren
	 * @param maxItems
	 * @param storageType
	 * @param logger
	 * @param numDimensions
	 * @throws Exception
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
		
		System.out.println("R-Tree initializing...");
		System.out.println(" > Storage Type set to " + storageType + ".");
		System.out.println(" > Split Behavior set to " + splitBehavior.getDescription() + ".");
		System.out.println(" > Log level set to " + logger.getLogLevel() + ".");
		System.out.println(" > Tree Name set to " + treeName + ".");
		System.out.println(" > Max Children set to " + maxChildren + ".");
		System.out.println(" > Max Items set to " + maxItems + ".");
		System.out.println(" > N set to " + numDimensions + ".");
		System.out.println();
		
	}

	
	/**
	 * Initialize the split behavior, the cache container, the max children and items, and the root node configuration
	 * 
	 * @throws Exception 
	 */
	private void init(IDataStorage<T> dataStorage) throws Exception {
		
		try {
			
			// TODO pull these out, use dependency injection instead
			cache = new RTreeCache<T>(this, logger, dataStorage, className);
			this.splitBehavior = new SplitQuadratic<T>(className);
			
		} catch (Exception e) {
			logger.log("Cache initialization failed.");
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
	
	
	
//	@SuppressWarnings("unchecked")
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
		logger.log("[RTREE] getAllLocationItems() returned a list of size " + points.size());
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
				logger.log("[RTREE] getLocationItems() getting child node: " + s);
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
		logger.setLogLevel(LogLevel.DEV);
		logger.log("[RTREE] PRINTING TREE: ");
		logger.log();
		logger.logExact("nodeId\tparent\trectangle\tnumber children\tnumber items\tdepth");
		for (int i = 0; i < maxItems; i++) {
			logger.logExact("\titem");
		}
		for (int i = 0; i < maxChildren; i++) {
			logger.logExact("\tchild");
		}
		logger.log();
		printTree(cache.getNode(treeName), 0);
		logger.log();
		logger.log();
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
		
		logger.logExact(node.getNodeIdShort() + "\t" + node.getParent() + "\t" + node.getRectangle() + 
				"\t" + numChildren + "\t" + node.getNumberOfItems() +  "\t" + depth + "\t");
		
		
		List<ILocationItem<T>> tempPoints = node.getLocationItems();
		for (int i = 0; i < maxItems; i++) {
			if (i < tempPoints.size()) {
				logger.logExact(tempPoints.get(i).getType() + "\t");
			} else {
				logger.logExact("\t");
			}
			
		}
		
		List<String> tempChildren = node.getChildrenShort();
		for (int i = 0; i < maxChildren; i++) {
			if (i < tempChildren.size()) {
				logger.logExact(tempChildren.get(i) + "\t");
			} else {
				logger.logExact("\t");
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
	
	/**
	 * 
	 * @return number of add operations
	 */
	@Override
	public int numAdds() {
		return cache.getDBAccess().getNumAdds();
	}
	
	/**
	 * 
	 * @return number of read operations
	 */
	@Override
	public int numReads() {
		return cache.getDBAccess().getNumReads();
	}
	
	/**
	 * 
	 * @return number of update operations
	 */
	@Override
	public int numUpdates() {
		return cache.getDBAccess().getNumUpdates();
	}
	
	/**
	 * 
	 * @return total add time
	 */
	@Override
	public long getAddTime() {
		return cache.getDBAccess().getAddTime();
	}
	
	/**
	 * 
	 * @return total read time
	 */
	@Override
	public long getReadTime() {
		return cache.getDBAccess().getReadTime();
	}
	
	/**
	 * 
	 * @return total update time
	 */
	@Override
	public long getUpdateTime() {
		return cache.getDBAccess().getUpdateTime();
	}
	
	@Override
	public int getNumDimensions() {
		return numDimensions;
	}
}
