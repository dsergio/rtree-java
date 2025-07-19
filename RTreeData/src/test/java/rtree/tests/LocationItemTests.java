package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.item.BoundingBox;
import rtree.item.ILocationItem;
import rtree.item.LocationItemBase;
import rtree.item.LocationItem;
import rtree.item.RBoolean;
import rtree.item.RInteger;

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

//	@Disabled
	@Test
	void LocationItem_RTreeInteger_SpaceCorrect() {
		// Arrange
		ILocationItem<RInteger> item1 = new LocationItem<RInteger>(2);
		ILocationItem<RInteger> item2 = new LocationItem<RInteger>(2);
		
		RInteger x1 = new RInteger(0);
		RInteger y1 = new RInteger(0);
		
		RInteger x2 = new RInteger(10);
		RInteger y2 = new RInteger(10);
		
		item1.setDim(0, x1);
		item1.setDim(1, y1);
		
		item2.setDim(0, x2);
		item2.setDim(1, y2);
		
		// Act
		double space = LocationItemBase.space(item1, item2);
				
		// Assert
		assertEquals(100, space);
	}
	
//	@Disabled
	@Test
	void LocationItem_RTreeBoolean_BoundingBoxSpaceCorrect() {
		// Arrange
		ILocationItem<RBoolean> item1 = new LocationItem<RBoolean>(3);
		ILocationItem<RBoolean> item2 = new LocationItem<RBoolean>(3);
		
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
		BoundingBox box = LocationItemBase.getBoundingBox(item1, item2);
		double boundingBoxspace = box.getBoxSpace();
				
		// Assert
		assertEquals(0, boundingBoxspace);
	}
	
//	@Disabled
	@Test
	void LocationItem_RTreeBoolean_BoundingBoxSpaceCorrect2() {
		// Arrange
		ILocationItem<RBoolean> item1 = new LocationItem<RBoolean>(3);
		ILocationItem<RBoolean> item2 = new LocationItem<RBoolean>(3);
		
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
		BoundingBox box = LocationItemBase.getBoundingBox(item1, item2);
		double boundingBoxspace = box.getBoxSpace();
		int boundingBoxDimensions = box.getBoxDimensions();
				
		// Assert
		assertEquals(1, boundingBoxspace);
		assertEquals(1, boundingBoxDimensions);
	}

}
