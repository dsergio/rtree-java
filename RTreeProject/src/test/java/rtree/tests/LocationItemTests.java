package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.item.LocationItemBase;

class LocationItemTests {

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
	void test() {
		// Arrange
		ILocationItem item1 = new LocationItem2D(0, 0, "test");
		ILocationItem item2 = new LocationItem2D(10, 10, "test");
		
		// Act
		int area = LocationItemBase.space(item1, item2);
				
		// Assert
		assertEquals(100, area);
	}

}
