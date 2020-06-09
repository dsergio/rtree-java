package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@Test
	void CuboidBase_RectangleContainsPoint_ReturnTrue() {
		// Arrange
		ILocationItem item = new LocationItem(5, 5, "test");
		IHyperRectangle rect = new Rectangle();
		rect.setDim1(0, 0);
		rect.setDim1(1, 0);
		rect.setDim2(0, 10);
		rect.setDim2(1, 10);
		
		// Act
		boolean contains = rect.containsPoint(item);
				
		// Assert
		assertEquals(true, contains);
	}
	
	@Test
	void CuboidBase_RectangleContainsPoint_ReturnFalse() {
		// Arrange
		ILocationItem item = new LocationItem(50, 50, "test");
		IHyperRectangle rect = new Rectangle();
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
