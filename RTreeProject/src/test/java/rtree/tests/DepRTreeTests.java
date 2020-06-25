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
import rtree.item.LocationItem2D;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.storage.DepDataStorageBase;
import rtree.storage.DepDataStorageInMemory;
import rtree.tree.DepRTree;
import rtree.tree.DepSplitQuadratic;

class DepRTreeTests {

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
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DepDataStorageBase dataStorage = new DepDataStorageInMemory(logger, "TestTree1");
		
		// Act
		try {
			new DepRTree(dataStorage, 4, 4, logger, new DepSplitQuadratic());
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
		DepRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DepDataStorageBase dataStorage = new DepDataStorageInMemory(logger, "TestTree1");
		int numChildrenExpected = 2;
		int numItemsExpected = 6;
		try {
			tree = new DepRTree(dataStorage, numChildrenExpected, numItemsExpected, logger, new DepSplitQuadratic());
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
		DepRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DepDataStorageBase dataStorage = new DepDataStorageInMemory(logger, "TestTree1");
		
		try {
			tree = new DepRTree(dataStorage, 4, 4, logger, new DepSplitQuadratic());
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		LocationItem2D item = new LocationItem2D(0, 0, null);
		
		
		// Act
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
		
		// Assert
		assertTrue(true);
	}
	
	@Test
	void InMemoryTree_Query_Success() {
		// Arrange
		DepRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DepDataStorageBase dataStorage = new DepDataStorageInMemory(logger, "TestTree1");
		
		try {
			tree = new DepRTree(dataStorage, 4, 4, logger, new DepSplitQuadratic());
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem item = new LocationItem2D(0, 0, null);
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
		
		IHyperRectangle r = new Rectangle2D();
		r.setDim1(0, -1);
		r.setDim2(0, 1);
		r.setDim1(1, -1);
		r.setDim2(1, 1);
		
		// Act
		if (tree == null) {
			fail("Failed to create tree");
		} else {
			Map<IHyperRectangle, List<ILocationItem>> searchResult = tree.search2D(r);
			
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
