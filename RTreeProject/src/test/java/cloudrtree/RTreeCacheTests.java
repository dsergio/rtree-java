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

class RTreeCacheTests {

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
		DataStorageBase dataStorage = new DataStorageInMemory(logger);
		RTree tree = null;
		
		
		int xExpected = 10;
		int yExpected = 10;
		
		ILocationItem item = new LocationItem(xExpected, yExpected, "testtype");
		
		// Act
		try {
			tree = new RTree(dataStorage, "TestTree1", 4, 4, logger, new SplitQuadratic());
			tree.insertType(item);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Assert
		assertEquals(xExpected, tree.cacheContainer.getNode("TestTree1").rectangle.getDim1(0));
		assertEquals(yExpected, tree.cacheContainer.getNode("TestTree1").rectangle.getDim1(1));
	}
	

}
