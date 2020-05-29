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

class CloudRTreeTests {

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
		
		// Act
		try {
			CloudRTree tree = new CloudRTree("TestTree1", 4, 4, "InMemory");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}
	
	@Test
	void InMemoryTree_Insert_Success() {
		// Arrange
		CloudRTree tree = null;
		
		try {
			tree = new CloudRTree("TestTree1", 4, 4, "InMemory");
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
		CloudRTree tree = null;
		
		try {
			tree = new CloudRTree("TestTree1", 4, 4, "InMemory");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		LocationItem item = new LocationItem(0, 0, null);
		try {
			tree.insert(item);
		} catch (IOException e) {
			fail("Insert Exception");
			e.printStackTrace();
		}
		
		Rectangle r = new Rectangle();
		r.setX1(-1);
		r.setX2(1);
		r.setY1(-1);
		r.setY2(1);
		
		// Act
		Map<Rectangle, List<LocationItem>> searchResult = tree.search(r);
		
		// Assert
		for (Rectangle k : searchResult.keySet()) {
			List<LocationItem> i = searchResult.get(k);
			for (LocationItem j : i) {
				assertEquals(j.getX(), item.getX());
				assertEquals(j.getY(), item.getY());
			}
			
		}
	}

}
