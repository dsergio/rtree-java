package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RTreeTests {

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
		DataStorageBase dataStorage = new DataStorageInMemory(logger);
		
		// Act
		try {
			RTree tree = new RTree(dataStorage, "TestTree1", 4, 4, logger, new SplitQuadratic());
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
		RTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DataStorageBase dataStorage = new DataStorageInMemory(logger);
		int numChildrenExpected = 2;
		int numItemsExpected = 6;
		try {
			tree = new RTree(dataStorage, "TestTree1", numChildrenExpected, numItemsExpected, logger, new SplitQuadratic());
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Act
		int numChildrenObserved = tree.getMaxChildren();
		int numItemsObserved = tree.getMaxItems();
		
		// Assert
		assertEquals(numChildrenExpected, numChildrenObserved);
		assertEquals(numItemsExpected, numItemsObserved);
	}
	
	@Test
	void InMemoryTree_Insert_Success() {
		// Arrange
		RTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DataStorageBase dataStorage = new DataStorageInMemory(logger);
		
		try {
			tree = new RTree(dataStorage, "TestTree1", 4, 4, logger, new SplitQuadratic());
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		LocationItem item = new LocationItem(0, 0, null);
		
		
		// Act
		try {
			tree.insert(item);
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
		RTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		DataStorageBase dataStorage = new DataStorageInMemory(logger);
		
		try {
			tree = new RTree(dataStorage, "TestTree1", 4, 4, logger, new SplitQuadratic());
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		ILocationItem item = new LocationItem(0, 0, null);
		try {
			tree.insert(item);
		} catch (IOException e) {
			fail("Insert Exception");
			e.printStackTrace();
		}
		
		IHyperRectangle r = new Rectangle();
		r.setDim1(0, -1);
		r.setDim2(0, 1);
		r.setDim1(1, -1);
		r.setDim2(1, 1);
		
		// Act
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
