package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
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
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.storage.DataStorageInMemory;
import rtree.storage.IDataStorage;
import rtree.tree.IRTree;
import rtree.tree.RTree;

class RTreeBulkInsertTests {
	
	// Helper method to generate n d-dimensional points
	private List<ILocationItem<RDouble>> generateItems(int n) {
	    Random random = new Random();
	    List<ILocationItem<RDouble>> items = new ArrayList<ILocationItem<RDouble>>();
	    
	    for (int i = 0; i < n; i++) {
	    	ILocationItem<RDouble> item = new LocationItem<RDouble>(2);  // 2D points
	    	item.setDim(0, new RDouble(random.nextDouble() * 100));  // Random x coordinate
	    	item.setDim(1, new RDouble(random.nextDouble() * 100));  // Random y coordinate
	    	item.setType("Point" + i);  // Set a unique type for each item
	        items.add(item);  // Create a new Item with the generated data
	    }
	    return items;
	}
	
//	@Disabled
	@ParameterizedTest
    @CsvSource({
        "20"
    })
	void InMemoryTree_BulkInsert_Success(int n) {
		// Arrange
		int N = 2;
		IRTree<RDouble> tree = null;
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		IDataStorage<RDouble> dataStorage = new DataStorageInMemory<>(logger, RDouble.class);
		
		try {
			tree = new RTree<>(dataStorage, 4, 20, logger, N, "TestTree1", RDouble.class);
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Act
		try {
			if (tree == null) {
				fail("Failed to create tree");
			} else {
				
				
				List<ILocationItem<RDouble>> items = generateItems(n);
				
				for (ILocationItem<RDouble> item : items) {
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
