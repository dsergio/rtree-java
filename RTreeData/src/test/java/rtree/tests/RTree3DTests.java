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
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.RectangleND;
import rtree.storage.DataStorageInMemory;
import rtree.storage.IDataStorage;
import rtree.tree.IRTree;
import rtree.tree.RTreeND;

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

	@Test
	void InMemoryTree_CreateTree_Success() {
		// Arrange
		int N = 3;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage dataStorage = new DataStorageInMemory(logger);
		
		// Act
		try {
			new RTreeND(dataStorage, 4, 4, logger, N, "TestTree1");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}
	
	@Test
	void InMemoryTree_CreateTree_CorrectMaxChildrenAndItems() {
		// Arrange
		int N = 3;
		IRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage dataStorage = new DataStorageInMemory(logger);
		int numChildrenExpected = 2;
		int numItemsExpected = 6;
		try {
			tree = new RTreeND(dataStorage, numChildrenExpected, numItemsExpected, logger, N, "TestTree1");
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
	
	@Test
	void InMemoryTree_Insert_Success() {
		// Arrange
		int N = 3;
		IRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage dataStorage = new DataStorageInMemory(logger);
		
		try {
			tree = new RTreeND(dataStorage, 4, 4, logger, N, "TestTree1");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem item = new LocationItemND(N);
		item.setDim(0, 0);
		item.setDim(1, 0);
		item.setDim(2, 0);
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
	
	@Test
	void InMemoryTree_Query_Success() {
		// Arrange
		int N = 3;
		String treeName = "TestTree1";
		IRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		IDataStorage dataStorage = new DataStorageInMemory(logger);
		
		try {
			tree = new RTreeND(dataStorage, 4, 4, logger, N, treeName);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create tree");
			
		}
		ILocationItem item = new LocationItemND(N);
		item.setDim(0, 0);
		item.setDim(1, 0);
		item.setDim(2, 0);
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
		
		IHyperRectangle r = new RectangleND(N);
		r.setDim1(0, -1);
		r.setDim2(0, 1);
		r.setDim1(1, -1);
		r.setDim2(1, 1);
		r.setDim1(2, -1);
		r.setDim2(2, 1);
		
		// Act
		if (tree == null) {
			fail("Failed to create tree");
		} else {
			Map<IHyperRectangle, List<ILocationItem>> searchResult = tree.search(r);
			
			// Assert
			for (IHyperRectangle k : searchResult.keySet()) {
				List<ILocationItem> i = searchResult.get(k);
				for (ILocationItem j : i) {
					assertEquals(j.getDim(0), item.getDim(0));
					assertEquals(j.getDim(1), item.getDim(1));
				}
				
			}
		}
		
	}

}
