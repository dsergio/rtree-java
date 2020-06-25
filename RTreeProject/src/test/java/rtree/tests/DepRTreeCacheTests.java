package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

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
import rtree.storage.DepDataStorageBase;
import rtree.storage.DepDataStorageInMemory;
import rtree.tree.DepRTree;
import rtree.tree.DepSplitQuadratic;

/**
 * 
 * @author David Sergio
 *
 */
class DepRTreeCacheTests {

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
	void RTreeCache_UpdateNode_ReturnRectangle() {
		// Arrange
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		DepDataStorageBase dataStorage = new DepDataStorageInMemory(logger, "TestTree1");
		DepRTree tree = null;
		
		
		int xExpected = 10;
		int yExpected = 10;
		
		ILocationItem item = new LocationItem2D(xExpected, yExpected, "testtype");
		
		// Act Assert
		try {
			tree = new DepRTree(dataStorage, 4, 4, logger, new DepSplitQuadratic());
			tree.insertType(item);
			
			assertEquals(xExpected, tree.getCache().getNode("TestTree1").getRectangle().getDim1(0));
			assertEquals(yExpected, tree.getCache().getNode("TestTree1").getRectangle().getDim1(1));
			
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
				
	}
	

}
