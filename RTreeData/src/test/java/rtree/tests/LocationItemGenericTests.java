package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rtree.item.BoundingBox;
import rtree.item.ILocationItemGeneric;
import rtree.item.LocationItemBaseGeneric;
import rtree.item.LocationItemNDGeneric;
import rtree.item.RBoolean;
import rtree.item.RInteger;

class LocationItemGenericTests {

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
	void LocationItemGeneric_RTreeInteger_SpaceCorrect() {
		// Arrange
		ILocationItemGeneric<RInteger> item1 = new LocationItemNDGeneric<RInteger>(2);
		ILocationItemGeneric<RInteger> item2 = new LocationItemNDGeneric<RInteger>(2);
		
		RInteger x1 = new RInteger(0);
		RInteger y1 = new RInteger(0);
		
		RInteger x2 = new RInteger(10);
		RInteger y2 = new RInteger(10);
		
		item1.setDim(0, x1);
		item1.setDim(1, y1);
		
		item2.setDim(0, x2);
		item2.setDim(1, y2);
		
		// Act
		double space = LocationItemBaseGeneric.space(item1, item2);
				
		// Assert
		assertEquals(100, space);
	}
	
	@Test
	void LocationItemGeneric_RTreeBoolean_BoundingBoxSpaceCorrect() {
		// Arrange
		ILocationItemGeneric<RBoolean> item1 = new LocationItemNDGeneric<RBoolean>(3);
		ILocationItemGeneric<RBoolean> item2 = new LocationItemNDGeneric<RBoolean>(3);
		
		RBoolean x1 = new RBoolean(true);
		RBoolean y1 = new RBoolean(true);
		RBoolean z1 = new RBoolean(false);
		
		RBoolean x2 = new RBoolean(false);
		RBoolean y2 = new RBoolean(false);
		RBoolean z2 = new RBoolean(true);
		
		item1.setDim(0, x1);
		item1.setDim(1, y1);
		item1.setDim(2, z1);
		
		item2.setDim(0, x2);
		item2.setDim(1, y2);
		item2.setDim(2, z2);
		
		// Act
		BoundingBox box = LocationItemBaseGeneric.getBoundingBox(item1, item2);
		double boundingBoxspace = box.getBoxSpace();
				
		// Assert
		assertEquals(0, boundingBoxspace);
	}
	
	@Test
	void LocationItemGeneric_RTreeBoolean_BoundingBoxSpaceCorrect2() {
		// Arrange
		ILocationItemGeneric<RBoolean> item1 = new LocationItemNDGeneric<RBoolean>(3);
		ILocationItemGeneric<RBoolean> item2 = new LocationItemNDGeneric<RBoolean>(3);
		
		RBoolean x1 = new RBoolean(true);
		RBoolean y1 = new RBoolean(true);
		RBoolean z1 = new RBoolean(false);
		
		RBoolean x2 = new RBoolean(true);
		RBoolean y2 = new RBoolean(false);
		RBoolean z2 = new RBoolean(true);
		
		item1.setDim(0, x1);
		item1.setDim(1, y1);
		item1.setDim(2, z1);
		
		item2.setDim(0, x2);
		item2.setDim(1, y2);
		item2.setDim(2, z2);
		
		// Act
		BoundingBox box = LocationItemBaseGeneric.getBoundingBox(item1, item2);
		double boundingBoxspace = box.getBoxSpace();
		int boundingBoxDimensions = box.getBoxDimensions();
				
		// Assert
		assertEquals(1, boundingBoxspace);
		assertEquals(1, boundingBoxDimensions);
	}

}
