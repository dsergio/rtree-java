package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.item.LocationItem2D;
import rtree.item.LocationItemBase;
import rtree.item.LocationItemND;

class LocationItemBaseTests {

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

	@Disabled
	@Test
	void LocationItemBase_2D_Space() {
		// Arrange
		LocationItemBase item1 = new LocationItem2D(0, 0, "test");
		LocationItemBase item2 = new LocationItem2D(10, 10, "test");
		
		// Act
		int space = LocationItemBase.space(item1, item2);
				
		// Assert
		assertEquals(100, space);
	}
	
	@Disabled
	@Test
	void LocationItemBase_3D_Space() {
		// Arrange
		LocationItemBase item1 = new LocationItemND(3);
		LocationItemBase item2 = new LocationItemND(3);
		item1.setDim(0, 0);
		item1.setDim(1, 0);
		item1.setDim(2, 0);
		
		item2.setDim(0, 10);
		item2.setDim(1, 10);
		item2.setDim(2, 10);
		
		// Act
		int space = LocationItemBase.space(item1, item2);
				
		// Assert
		assertEquals(1000, space);
	}

}
