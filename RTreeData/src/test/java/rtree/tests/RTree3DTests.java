package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.item.ILocationItem;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.item.RInteger;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.rectangle.HyperRectangle;
import rtree.rectangle.IHyperRectangle;
import rtree.storage.DataStorageInMemory;
import rtree.storage.IDataStorage;
import rtree.tree.IRTree;
import rtree.tree.RTree;

class RTree3DTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Disabled
	@Test
	void InMemoryTree_CreateTree_Success() {
		// Arrange
		int N = 3;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
		
		// Act
		try {
			new RTree<RDouble>(dataStorage, 4, 4, logger, N, "TestTree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}
	
//	@Disabled
	@Test
	void InMemoryTree_CreateTree_CorrectMaxChildrenAndItems() {
		// Arrange
		int N = 3;
		IRTree<RDouble> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
		int numChildrenExpected = 2;
		int numItemsExpected = 6;
		try {
			tree = new RTree<>(dataStorage, numChildrenExpected, numItemsExpected, logger, N, "TestTree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		if (tree == null) {
			fail("Failed to create tree");
		} else {
			
			// Act
			int numChildrenObserved = tree.getMaxChildren();
			int numItemsObserved = tree.getMaxItems();
			
			// Assert
			assertEquals(numChildrenExpected, numChildrenObserved);
			assertEquals(numItemsExpected, numItemsObserved);
		}
		
		
	}
	
//	@Disabled
	@Test
	void InMemoryTree_Insert_Success() {
		// Arrange
		int N = 3;
		IRTree<RDouble> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
		
		try {
			tree = new RTree<>(dataStorage, 4, 4, logger, N, "TestTree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem<RDouble> item = new LocationItem<>(N);
		item.setDim(0, new RDouble(0.0));
		item.setDim(1, new RDouble(0.0));
		item.setDim(2, new RDouble(0.0));
		item.setType("type");
		
		// Act
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				tree.insert(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Insert Exception");
			
		}
		
		// Assert
		assertTrue(true);
	}
	
//	@Disabled
	@Test
	void InMemoryTree_Query_Success() {
		// Arrange
		int N = 3;
		String treeName = "TestTree1";
		IRTree<RInteger> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage<RInteger> dataStorage = new DataStorageInMemory<>(logger, RInteger.class);
		
		try {
			tree = new RTree<>(dataStorage, 4, 4, logger, N, treeName, RInteger.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create tree");
			
		}
		ILocationItem<RInteger> item = new LocationItem<>(N);
		item.setDim(0, new RInteger(0));
		item.setDim(1, new RInteger(0));
		item.setDim(2, new RInteger(0));
		item.setType("type");
		
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				tree.insert(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Insert Exception");
			
		}
		
		IHyperRectangle<RInteger> r = new HyperRectangle<>(N);
		r.setDim1(0, new RInteger(-1));
		r.setDim2(0, new RInteger(1));
		r.setDim1(1, new RInteger(-1));
		r.setDim2(1, new RInteger(1));
		r.setDim1(2, new RInteger(-1));
		r.setDim2(2, new RInteger(1));
		
		// Act
		if (tree == null) {
			fail("Failed to create tree");
		} else {
			Map<IHyperRectangle<RInteger>, List<ILocationItem<RInteger>>> searchResult = tree.search(r);
			
			// Assert
			for (IHyperRectangle<RInteger> k : searchResult.keySet()) {
				List<ILocationItem<RInteger>> i = searchResult.get(k);
				for (ILocationItem<RInteger> j : i) {
					assertEquals(j.getDim(0).getData(), item.getDim(0).getData());
					assertEquals(j.getDim(1).getData(), item.getDim(1).getData());
				}
				
			}
		}
		
	}
	
//	@Disabled
	@Test
	void InMemoryTree_InsertSeveralItems_Success() {
		// Arrange
		int N = 3;
		Random r = new Random();
		String[] animals = {"Abyssinian", "Adelie Penguin", "Affenpinscher", "Afghan Hound", "African Bush Elephant", "African Civet", "African Clawed Frog", "African Forest Elephant", "African Palm Civet", "African Penguin", "African Tree Toad", "African Wild Dog", "Ainu Dog", "Airedale Terrier", "Akbash", "Akita", "Alaskan Malamute", "Albatross", "Aldabra Giant Tortoise", "Alligator", "Alpine Dachsbracke", "American Bulldog", "American Cocker Spaniel", "American Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier", "American Water Spaniel", "Anatolian Shepherd Dog", "Angelfish", "Ant", "Anteater", "Antelope", "Appenzeller Dog", "Arctic Fox", "Arctic Hare", "Arctic Wolf", "Armadillo", "Asian Elephant", "Asian Giant Hornet", "Asian Palm Civet", "Asiatic Black Bear", "Australian Cattle Dog", "Australian Kelpie Dog", "Australian Mist", "Australian Shepherd", "Australian Terrier", "Avocet", "Axolotl", "Aye Aye", "Baboon", "Bactrian Camel", "Badger", "Balinese", "Banded Palm Civet", "Bandicoot", "Barb", "Barn Owl", "Barnacle", "Barracuda", "Basenji Dog", "Basking Shark", "Basset Hound", "Bat", "Bavarian Mountain Hound", "Beagle", "Bear", "Bearded Collie", "Bearded Dragon", "Beaver", "Bedlington Terrier", "Beetle", "Bengal Tiger", "Bernese Mountain Dog", "Bichon Frise", "Binturong", "Bird", "Birds Of Paradise", "Birman", "Bison", "Black Bear", "Black Rhinoceros", "Black Russian Terrier", "Black Widow Spider", "Bloodhound", "Blue Lacy Dog", "Blue Whale", "Bluetick Coonhound", "Bobcat", "Bolognese Dog", "Bombay", "Bongo", "Bonobo", "Booby", "Border Collie", "Border Terrier", "Bornean Orang-utan", "Borneo Elephant", "Boston Terrier", "Bottle Nosed Dolphin", "Boxer Dog", "Boykin Spaniel", "Brazilian Terrier", "Brown Bear", "Budgerigar", "Buffalo", "Bull Mastiff", "Bull Shark", "Bull Terrier", "Bulldog", "Bullfrog", "Bumble Bee", "Burmese", "Burrowing Frog", "Butterfly", "Butterfly Fish", "Caiman", "Caiman Lizard", "Cairn Terrier", "Camel", "Canaan Dog", "Capybara", "Caracal", "Carolina Dog", "Cassowary", "Cat", "Caterpillar", "Catfish", "Cavalier King Charles Spaniel", "Centipede", "Cesky Fousek", "Chameleon", "Chamois", "Cheetah", "Chesapeake Bay Retriever", "Chicken", "Chihuahua", "Chimpanzee", "Chinchilla", "Chinese Crested Dog", "Chinook", "Chinstrap Penguin", "Chipmunk", "Chow Chow", "Cichlid", "Clouded Leopard", "Clown Fish", "Clumber Spaniel", "Coati", "Cockroach", "Collared Peccary", "Collie", "Common Buzzard", "Common Frog", "Common Loon", "Common Toad", "Coral", "Cottontop Tamarin", "Cougar", "Cow", "Coyote", "Crab", "Crab-Eating Macaque", "Crane", "Crested Penguin", "Crocodile", "Cross River Gorilla", "Curly Coated Retriever", "Cuscus", "Cuttlefish", "Dachshund", "Dalmatian", "Darwin's Frog", "Deer", "Desert Tortoise", "Deutsche Bracke", "Dhole", "Dingo", "Discus", "Doberman Pinscher", "Dodo", "Dog", "Dogo Argentino", "Dogue De Bordeaux", "Dolphin", "Donkey", "Dormouse", "Dragonfly", "Drever", "Duck", "Dugong", "Dunker", "Dusky Dolphin", "Dwarf Crocodile", "Eagle", "Earwig", "Eastern Gorilla", "Eastern Lowland Gorilla", "Echidna", "Edible Frog", "Egyptian Mau", "Electric Eel", "Elephant", "Elephant Seal", "Elephant Shrew", "Emperor Penguin", "Emperor Tamarin", "Emu", "English Cocker Spaniel", "English Shepherd", "English Springer Spaniel", "Entlebucher Mountain Dog", "Epagneul Pont Audemer", "Eskimo Dog", "Estrela Mountain Dog", "Falcon", "Fennec Fox", "Ferret", "Field Spaniel", "Fin Whale", "Finnish Spitz", "Fire-Bellied Toad", "Fish", "Fishing Cat", "Flamingo", "Flat Coat Retriever", "Flounder", "Fly", "Flying Squirrel", "Fossa", "Fox", "Fox Terrier", "French Bulldog", "Frigatebird", "Frilled Lizard", "Frog", "Fur Seal", "Galapagos Penguin", "Galapagos Tortoise", "Gar", "Gecko", "Gentoo Penguin", "Geoffroys Tamarin", "Gerbil", "German Pinscher", "German Shepherd", "Gharial", "Giant African Land Snail", "Giant Clam", "Giant Panda Bear", "Giant Schnauzer", "Gibbon", "Gila Monster", "Giraffe", "Glass Lizard", "Glow Worm", "Goat", "Golden Lion Tamarin", "Golden Oriole", "Golden Retriever", "Goose", "Gopher", "Gorilla", "Grasshopper", "Great Dane", "Great White Shark", "Greater Swiss Mountain Dog", "Green Bee-Eater", "Greenland Dog", "Grey Mouse Lemur", "Grey Reef Shark", "Grey Seal", "Greyhound", "Grizzly Bear", "Grouse", "Guinea Fowl", "Guinea Pig", "Guppy", "Hammerhead Shark", "Hamster", "Hare", "Harrier", "Havanese", "Hedgehog", "Hercules Beetle", "Hermit Crab", "Heron", "Highland Cattle", "Himalayan", "Hippopotamus", "Honey Bee", "Horn Shark", "Horned Frog", "Horse", "Horseshoe Crab", "Howler Monkey", "Human", "Humboldt Penguin", "Hummingbird", "Humpback Whale", "Hyena", "Ibis", "Ibizan Hound", "Iguana", "Impala", "Indian Elephant", "Indian Palm Squirrel", "Indian Rhinoceros", "Indian Star Tortoise", "Indochinese Tiger", "Indri", "Insect", "Irish Setter", "Irish WolfHound", "Jack Russel", "Jackal", "Jaguar", "Japanese Chin", "Japanese Macaque", "Javan Rhinoceros", "Javanese", "Jellyfish", "Kakapo", "Kangaroo", "Keel Billed Toucan", "Killer Whale", "King Crab", "King Penguin", "Kingfisher", "Kiwi", "Koala", "Komodo Dragon", "Kudu", "Labradoodle", "Labrador Retriever", "Ladybird", "Leaf-Tailed Gecko", "Lemming", "Lemur", "Leopard", "Leopard Cat", "Leopard Seal", "Leopard Tortoise", "Liger", "Lion", "Lionfish", "Little Penguin", "Lizard", "Llama", "Lobster", "Long-Eared Owl", "Lynx", "Macaroni Penguin", "Macaw", "Magellanic Penguin", "Magpie", "Maine Coon", "Malayan Civet", "Malayan Tiger", "Maltese", "Manatee", "Mandrill", "Manta Ray", "Marine Toad", "Markhor", "Marsh Frog", "Masked Palm Civet", "Mastiff", "Mayfly", "Meerkat", "Millipede", "Minke Whale", "Mole", "Molly", "Mongoose", "Mongrel", "Monitor Lizard", "Monkey", "Monte Iberia Eleuth", "Moorhen", "Moose", "Moray Eel", "Moth", "Mountain Gorilla", "Mountain Lion", "Mouse", "Mule", "Neanderthal", "Neapolitan Mastiff", "Newfoundland", "Newt", "Nightingale", "Norfolk Terrier", "Norwegian Forest", "Numbat", "Nurse Shark", "Ocelot", "Octopus", "Okapi", "Old English Sheepdog", "Olm", "Opossum", "Orang-utan", "Ostrich", "Otter", "Oyster", "Quail", "Quetzal", "Quokka", "Quoll", "Rabbit", "Raccoon", "Raccoon Dog", "Radiated Tortoise", "Ragdoll", "Rat", "Rattlesnake", "Red Knee Tarantula", "Red Panda", "Red Wolf", "Red-handed Tamarin", "Reindeer", "Rhinoceros", "River Dolphin", "River Turtle", "Robin", "Rock Hyrax", "Rockhopper Penguin", "Roseate Spoonbill", "Rottweiler", "Royal Penguin", "Russian Blue", "Sabre-Toothed Tiger", "Saint Bernard", "Salamander", "Sand Lizard", "Saola", "Scorpion", "Scorpion Fish", "Sea Dragon", "Sea Lion", "Sea Otter", "Sea Slug", "Sea Squirt", "Sea Turtle", "Sea Urchin", "Seahorse", "Seal", "Serval", "Sheep", "Shih Tzu", "Shrimp", "Siamese", "Siamese Fighting Fish", "Siberian", "Siberian Husky", "Siberian Tiger", "Silver Dollar", "Skunk", "Sloth", "Slow Worm", "Snail", "Snake", "Snapping Turtle", "Snowshoe", "Snowy Owl", "Somali", "South China Tiger", "Spadefoot Toad", "Sparrow", "Spectacled Bear", "Sperm Whale", "Spider Monkey", "Spiny Dogfish", "Sponge", "Squid", "Squirrel", "Squirrel Monkey", "Sri Lankan Elephant", "Staffordshire Bull Terrier", "Stag Beetle", "Starfish", "Stellers Sea Cow", "Stick Insect", "Stingray", "Stoat", "Striped Rocket Frog", "Sumatran Elephant", "Sumatran Orang-utan", "Sumatran Rhinoceros", "Sumatran Tiger", "Sun Bear", "Swan", "Tang", "Tapir", "Tarsier", "Tasmanian Devil", "Tawny Owl", "Termite", "Tetra", "Thorny Devil", "Tibetan Mastiff", "Tiffany", "Tiger", "Tiger Salamander", "Tiger Shark", "Tortoise", "Toucan", "Tree Frog", "Tropicbird", "Tuatara", "Turkey", "Turkish Angora", "Uakari", "Uguisu", "Umbrellabird", "Vampire Bat", "Vervet Monkey", "Vulture", "Wallaby", "Walrus", "Warthog", "Wasp", "Water Buffalo", "Water Dragon", "Water Vole", "Weasel", "Welsh Corgi", "West Highland Terrier", "Western Gorilla", "Western Lowland Gorilla", "Whale Shark", "Whippet", "White Faced Capuchin", "White Rhinoceros", "White Tiger", "Wild Boar", "Wildebeest", "Wolf", "Wolverine", "Wombat", "Woodlouse", "Woodpecker", "Woolly Mammoth", "Woolly Monkey", "Wrasse", "X-Ray Tetra", "Yak", "Yellow-Eyed Penguin", "Yorkshire Terrier", "Zebra", "Zebra Shark", "Zebu", "Zonkey", "Zorse"};

		IRTree<RDouble> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
//		IDataStorage dataStorage = new DataStorageSqlite(logger);
//		IDataStorage dataStorage = new DataStorageMySQL(logger);
		
		try {
			tree = new RTree<>(dataStorage, 4, 4, logger, N, "3D_Tree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Act
		for (int i = 0; i < 10; i++) {
			
			ILocationItem<RDouble> item = new LocationItem<>(N);
			
			item.setDim(0, new RDouble(r.nextInt(600) + 0.0));
			item.setDim(1, new RDouble(r.nextInt(600) + 0.0));
			item.setDim(2, new RDouble(r.nextInt(600) + 0.0));
			
//			item.setDim(3, r.nextInt(200));
//			item.setDim(4, r.nextInt(200));
//			item.setDim(5, r.nextInt(200));
//			
//			item.setDim(6, r.nextInt(200));
//			item.setDim(7, r.nextInt(200));
//			item.setDim(8, r.nextInt(200));
//
//			item.setDim(9, r.nextInt(200));
//			item.setDim(10, r.nextInt(200));
//			item.setDim(11, r.nextInt(200));
//			
//			item.setDim(12, r.nextInt(200));
//			item.setDim(13, r.nextInt(200));
//			item.setDim(14, r.nextInt(200));
			
			int animal_index = r.nextInt(animals.length);
			item.setType(animals[animal_index]);
			
			try {
				if (tree != null) {
					tree.insertRandomAnimal(item);
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail("could not insert 3D items");
			}
		}
		
		// Assert
		assertTrue(true);
	}

}
