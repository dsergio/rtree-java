package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

class RTree2DTests {

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
		int N = 2;
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		IDataStorage<RInteger> dataStorage = new DataStorageInMemory<>(logger, RInteger.class);
		
		// Act
		try {
			new RTree<RInteger>(dataStorage, 4, 4, logger, N, "TestTree1", RInteger.class);
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
		int N = 2;
		IRTree<RInteger> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		IDataStorage<RInteger> dataStorage = new DataStorageInMemory<>(logger, RInteger.class);
		int numChildrenExpected = 2;
		int numItemsExpected = 6;
		try {
			tree = new RTree<RInteger>(dataStorage, numChildrenExpected, numItemsExpected, logger, N, "TestTree1", RInteger.class);
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
		int N = 2;
		IRTree<RDouble> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
		
		try {
			tree = new RTree<>(dataStorage, 4, 20, logger, N, "TestTree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem<RDouble> item = new LocationItem<>(2);
		
		item.setDim(0, new RDouble(0.0));
		item.setDim(1, new RDouble(0.0));
		item.setType("TestType");
		
		
		// Act
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				tree.insert(item);
			}
		} catch (IOException e) {
			fail("Insert Exception " + e.getMessage());
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}
	
//	@Disabled
	@Test
	void InMemoryTree_Query_Success() {
		// Arrange
		int N = 2;
		IRTree<RInteger> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		IDataStorage<RInteger> dataStorage = new DataStorageInMemory<>(logger, RInteger.class);
		
		try {
			tree = new RTree<>(dataStorage, 4, 4, logger, N, "TestTree1", RInteger.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem<RInteger> item = new LocationItem<>(2);
		item.setDim(0, new RInteger(0));
		item.setDim(1, new RInteger(0));
		item.setType("TestType");
		
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				tree.insert(item);
			}
		} catch (IOException e) {
			fail("Insert Exception");
			e.printStackTrace();
		}
		
		IHyperRectangle<RInteger> r = new HyperRectangle<>(2);
		r.setDim1(0, new RInteger(-1));
		r.setDim2(0, new RInteger(1));
		r.setDim1(1, new RInteger(-1));
		r.setDim2(1, new RInteger(1));
		
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
	void InMemoryTree_DeleteTree_Success() {
		// Arrange
		int N = 2;
		ILogger logger = new LoggerStdOut(LogLevel.DEBUG);
		IDataStorage<RInteger> dataStorage = new DataStorageInMemory<>(logger, RInteger.class);
		
		RTree<RInteger> tree = null;
		try {
			tree = new RTree<RInteger>(dataStorage, 4, 4, logger, N, "TestTree1", RInteger.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ILocationItem<RInteger> item = new LocationItem<>(2);
		item.setDim(0, new RInteger(0));
		item.setDim(1, new RInteger(0));
		item.setType("TestType");
		
		try {
			tree.insert(item);
			
		} catch (IOException e) {
			fail("Insert Exception");
			e.printStackTrace();
		}

		// Act
		try {
			
			tree.delete();
			
		} catch (Exception e) {
			fail("Failed to delete tree");
			e.printStackTrace();
		}

		// Assert
		assertTrue(true);

	}

}
