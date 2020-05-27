package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleTests {

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
	void Rectangle_Overlap_True() {
		// Arrange
		Rectangle r1 = new Rectangle(0, 10, 0, 10);
		Rectangle r2 = new Rectangle(5, 15, 5, 15);
		
		// Act
		boolean overlap = Rectangle.rectanglesOverlap(r1, r2);
				
		// Assert
		assertEquals(true, overlap);
	}
	
	@Test
	void Rectangle_Overlap_False() {
		// Arrange
		Rectangle r1 = new Rectangle(0, 10, 0, 10);
		Rectangle r2 = new Rectangle(20, 30, 20, 30);
		
		// Act
		boolean overlap = Rectangle.rectanglesOverlap(r1, r2);
				
		// Assert
		assertEquals(false, overlap);
	}
	
	@Test
	void Rectangle_Sum_CorrectValue() {
		// Arrange
		Rectangle r1 = new Rectangle(0, 10, 0, 10);
		Rectangle r2 = new Rectangle(20, 30, 20, 30);
		
		// Act
		Rectangle sum = Rectangle.sumRectangles(r1, r2);
				
		// Assert
		assertEquals(900, sum.getArea());
	}

}
