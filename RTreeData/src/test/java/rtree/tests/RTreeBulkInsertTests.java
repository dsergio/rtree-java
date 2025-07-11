package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.storage.DataStorageInMemory;
import rtree.storage.IDataStorage;
import rtree.tree.IRTree;
import rtree.tree.RTree2D;

class RTreeBulkInsertTests {
	
	// Helper method to generate n d-dimensional points
	private LocationItem2D[] generateItems(int n) {
	    Random random = new Random();
	    LocationItem2D[] items = new LocationItem2D[n];
	    
	    for (int i = 0; i < n; i++) {
	        items[i] = new LocationItem2D(random.nextInt(), random.nextInt(), null);  // Create a new Item with the generated data
	    }
	    return items;
	}
	
	@Disabled
	@ParameterizedTest
    @CsvSource({
        "20"
    })
	void InMemoryTree_BulkInsert_Success(int n) {
		// Arrange
		int N = 2;
		IRTree tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		IDataStorage dataStorage = new DataStorageInMemory(logger);
		
		try {
			tree = new RTree2D(dataStorage, 4, 20, logger, N, "TestTree1");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Act
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				
				
				LocationItem2D[] items = generateItems(n);
				
				for (LocationItem2D item : items) {
					tree.insert(item);
				}
			}
		} catch (IOException e) {
			fail("Insert Exception");
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}
	


}
