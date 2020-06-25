package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import rtree.item.ILocationItem;
import rtree.item.LocationItemND;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.storage.DepDataStorageSQLBase;
import rtree.storage.DepDataStorageSqlite;
import rtree.tree.DepRTree;
import rtree.tree.DepSplitQuadratic;

/**
 * 
 * @author David Sergio
 *
 */
class DepDataStorageSqliteTests {
	
	DepDataStorageSQLBase storage = null;

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
		if (storage != null) {
			storage.close();
		}
	}

	@Test
	void CreateDBAccess_Success() {
		// Arrange
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "TestTree1");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			fail("Failed to create database access object");
		}
		
		// Assert
		assertTrue(true);
	}
	
	@Test
	void Create3DTreeInsert_Success() {
		// Arrange
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		int N = 3;
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "TestTree_ND");
			DepRTree tree = new DepRTree(storage, 4, 4, new LoggerStdOut(LogLevel.DEV), new DepSplitQuadratic(), N);
			
			ILocationItem item = new LocationItemND(N);
			item.setDim(0, 5);
			item.setDim(1, 10);
			item.setDim(2, 15);
			
			tree.insertND(item);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create database access object");
		}
				
		// Assert
		assertTrue(true);
	}
	
	@Test
	void Create3DTreeInsertSeveralItems_Success() {
		// Arrange
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		int N = 3;
		Random r = new Random();
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "3D_TestTree_1");
			DepRTree tree = new DepRTree(storage, 4, 4, new LoggerStdOut(LogLevel.DEV), new DepSplitQuadratic(), N);
			
			for (int i = 0; i < 10; i++) {
				ILocationItem item = new LocationItemND(N);
				
				int x = r.nextInt(100);
				int y = r.nextInt(100);
				int z = r.nextInt(100);
				
				item.setDim(0, x);
				item.setDim(1, y);
				item.setDim(2, z);
				
				tree.insertND(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create database access object");
		}
				
		// Assert
		assertTrue(true);
	}
	
	@Test
	void Create4DTreeInsertSeveralItems_Success() {
		// Arrange
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		int N = 4;
		Random r = new Random();
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "4D_TestTree_1");
			DepRTree tree = new DepRTree(storage, 4, 4, new LoggerStdOut(LogLevel.DEV), new DepSplitQuadratic(), N);
			
			for (int i = 0; i < 10; i++) {
				ILocationItem item = new LocationItemND(N);
				
				int x = r.nextInt(100);
				int y = r.nextInt(100);
				int z = r.nextInt(100);
				int d4 = r.nextInt(100);
				
				item.setDim(0, x);
				item.setDim(1, y);
				item.setDim(2, z);
				item.setDim(3, d4);
				
				tree.insertND(item);
			}
			
			tree.printTree();
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create database access object");
		}
				
		// Assert
		assertTrue(true);
	}
	
	@Test
	void Create8DTreeInsertSeveralItems_Success() {
		// Arrange
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		int N = 8;
		Random r = new Random();
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "8D_TestTree_1");
			DepRTree tree = new DepRTree(storage, 4, 4, new LoggerStdOut(LogLevel.DEV), new DepSplitQuadratic(), N);
			
			for (int i = 0; i < 10; i++) {
				ILocationItem item = new LocationItemND(N);
				
				int x = r.nextInt(100);
				int y = r.nextInt(100);
				int z = r.nextInt(100);
				int d4 = r.nextInt(100);
				int d5 = r.nextInt(100);
				int d6 = r.nextInt(100);
				int d7 = r.nextInt(100);
				int d8 = r.nextInt(100);
				
				item.setDim(0, x);
				item.setDim(1, y);
				item.setDim(2, z);
				item.setDim(3, d4);
				item.setDim(4, d5);
				item.setDim(5, d6);
				item.setDim(6, d7);
				item.setDim(7, d8);
				
				tree.insertND(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create database access object");
		}
				
		// Assert
		assertTrue(true);
	}
	
	@Test
	void Create1DTreeInsertSeveralItems_Success() {
		// Arrange
		
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		int N = 1;
		Random r = new Random();
		
		// Act
		try {
			
			storage = new DepDataStorageSqlite(logger, "1D_TestTree_1");
			DepRTree tree = new DepRTree(storage, 4, 4, new LoggerStdOut(LogLevel.DEV), new DepSplitQuadratic(), N);
			
			for (int i = 0; i < 10; i++) {
				ILocationItem item = new LocationItemND(N);
				
				int x = r.nextInt(100);
				
				item.setDim(0, x);
				
				tree.insertND(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to create database access object");
		}
				
		// Assert
		assertTrue(true);
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "", "++", " " })
	void CreateDBAccess_IllegalTreeName_ThrowsIllegalArgumentException(String treeName) {
		// Arrange
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		
		// Act Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			 new DepDataStorageSqlite(logger, treeName);
		});
		
	}

}
