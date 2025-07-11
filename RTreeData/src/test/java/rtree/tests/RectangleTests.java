package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;

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
	
	@Disabled
	@Test
	void Rectangle_SetGetDimensionValues_Success() {
		
		// Arrange
		Integer x1 = 0;
		Integer x2 = 10;
		Integer y1 = 0;
		Integer y2 = 10;
		IHyperRectangle r = new Rectangle2D(x1, x2, y1, y2);
		
		
		// Act
		Integer x1Observed = r.getDim1(0);
		Integer x2Observed = r.getDim2(0);
		Integer y1Observed = r.getDim1(1);
		Integer y2Observed = r.getDim2(1);
				
		// Assert
		assertEquals(x1, x1Observed);
		assertEquals(x2, x2Observed);
		assertEquals(y1, y1Observed);
		assertEquals(y2, y2Observed);
	}
	
	@Disabled
	@Test
	void Rectangle_SetGetDefaultDimensionValues_Success() {
		
		// Arrange
		Integer x1 = 0;
		Integer x2 = 0;
		Integer y1 = 0;
		Integer y2 = 0;
		IHyperRectangle r = new Rectangle2D();
		
		
		// Act
		Integer x1Observed = r.getDim1(0);
		Integer x2Observed = r.getDim2(0);
		Integer y1Observed = r.getDim1(1);
		Integer y2Observed = r.getDim2(1);
		
		System.out.println("x1Observed: " + x1Observed);
		System.out.println("x2Observed: " + x2Observed);
		System.out.println("y1Observed: " + y1Observed);
		System.out.println("y2Observed: " + y2Observed);
				
		// Assert
		assertEquals(x1, x1Observed);
		assertEquals(x2, x2Observed);
		assertEquals(y1, y1Observed);
		assertEquals(y2, y2Observed);
	}

	@Disabled
	@Test
	void Rectangle_Overlap_True() {
		// Arrange
		IHyperRectangle r1 = new Rectangle2D(0, 10, 0, 10);
		IHyperRectangle r2 = new Rectangle2D(5, 15, 5, 15);
		
		// Act
		boolean overlap = Rectangle2D.rectanglesOverlap2D(r1, r2);
				
		// Assert
		assertEquals(true, overlap);
	}
	
	@Disabled
	@Test
	void Rectangle_Overlap_False() {
		// Arrange
		IHyperRectangle r1 = new Rectangle2D(0, 10, 0, 10);
		IHyperRectangle r2 = new Rectangle2D(20, 30, 20, 30);
		
		// Act
		boolean overlap = Rectangle2D.rectanglesOverlap2D(r1, r2);
				
		// Assert
		assertEquals(false, overlap);
	}
	
	@Disabled
	@Test
	void Rectangle_Sum_CorrectValue() {
		// Arrange
		IHyperRectangle r1 = new Rectangle2D(0, 10, 0, 10);
		IHyperRectangle r2 = new Rectangle2D(20, 30, 20, 30);
		
		// Act
		IHyperRectangle sum = Rectangle2D.sumRectangles2D(r1, r2);
				
		// Assert
		assertEquals(900, sum.getSpace());
	}

}
