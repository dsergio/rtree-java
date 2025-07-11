package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;

class CuboidBaseTests {

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
	void CuboidBase_RectangleContainsPoint_ReturnTrue() {
		// Arrange
		ILocationItem item = new LocationItem2D(5, 5, "test");
		IHyperRectangle rect = new Rectangle2D();
		rect.setDim1(0, 0);
		rect.setDim1(1, 0);
		rect.setDim2(0, 10);
		rect.setDim2(1, 10);
		
		// Act
		boolean contains = rect.containsPoint(item);
				
		// Assert
		assertEquals(true, contains);
	}
	
	@Disabled
	@Test
	void CuboidBase_RectangleContainsPoint_ReturnFalse() {
		// Arrange
		ILocationItem item = new LocationItem2D(50, 50, "test");
		IHyperRectangle rect = new Rectangle2D();
		rect.setDim1(0, 0);
		rect.setDim1(1, 0);
		rect.setDim2(0, 10);
		rect.setDim2(1, 10);
		
		// Act
		boolean contains = rect.containsPoint(item);
				
		// Assert
		assertEquals(false, contains);
	}

}
