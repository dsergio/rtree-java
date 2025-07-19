package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.item.ILocationItem;
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.rectangle.HyperRectangle;
import rtree.rectangle.IHyperRectangle;

class HyperRectangleTests {

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
	void HyperRectangleContainsPoint_ReturnTrue() {
		// Arrange
		ILocationItem<RDouble> item = new LocationItem<RDouble>(2, "test");
		item.setDim(0, new RDouble(5.0));
		item.setDim(1, new RDouble(5.0));
		IHyperRectangle<RDouble> rect = new HyperRectangle<RDouble>(2);
		rect.setDim1(0, new RDouble(0.0));
		rect.setDim1(1, new RDouble(0.0));
		rect.setDim2(0, new RDouble(10.0));
		rect.setDim2(1, new RDouble(10.0));
		
		// Act
		boolean contains = rect.containsPoint(item);
				
		// Assert
		assertEquals(true, contains);
	}
	
//	@Disabled
	@Test
	void HyperRectangleContainsPoint_ReturnFalse() {
		// Arrange
		ILocationItem<RDouble> item = new LocationItem<RDouble>(2, "test");
		item.setDim(0, new RDouble(50.0));
		item.setDim(1, new RDouble(50.0));
		IHyperRectangle<RDouble> rect = new HyperRectangle<RDouble>(2);
		rect.setDim1(0, new RDouble(0.0));
		rect.setDim1(1, new RDouble(0.0));
		rect.setDim2(0, new RDouble(10.0));
		rect.setDim2(1, new RDouble(10.0));
		
		// Act
		boolean contains = rect.containsPoint(item);
				
		// Assert
		assertEquals(false, contains);
	}

}
