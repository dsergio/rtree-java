package rtree.tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rtree.item.RInteger;
import rtree.rectangle.HyperRectangle;
import rtree.rectangle.IHyperRectangle;


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
	
//	@Disabled
	@Test
	void Rectangle_SetGetDimensionValues_Success() {
		
		// Arrange
		Integer x1 = 0;
		Integer x2 = 10;
		Integer y1 = 0;
		Integer y2 = 10;
		IHyperRectangle<RInteger> r = new HyperRectangle<>(2);
		
		r.setDim1(0, new RInteger(x1));
		r.setDim2(0, new RInteger(x2));
		r.setDim1(1, new RInteger(y1));
		r.setDim2(1, new RInteger(y2));
		
		
		// Act
		Integer x1Observed = r.getDim1(0).getData();
		Integer x2Observed = r.getDim2(0).getData();
		Integer y1Observed = r.getDim1(1).getData();
		Integer y2Observed = r.getDim2(1).getData();
				
		// Assert
		assertEquals(x1, x1Observed);
		assertEquals(x2, x2Observed);
		assertEquals(y1, y1Observed);
		assertEquals(y2, y2Observed);
	}
	
//	@Disabled
	@Test
	void Rectangle_SetGetDefaultDimensionValues_Success() {
		
		// Arrange
		Integer x1 = 0;
		Integer x2 = 0;
		Integer y1 = 0;
		Integer y2 = 0;
		IHyperRectangle<RInteger> r = new HyperRectangle<>(2);
		
		r.setDim1(0, new RInteger(x1));
		r.setDim2(0, new RInteger(x2));
		r.setDim1(1, new RInteger(y1));
		r.setDim2(1, new RInteger(y2));
		
		
		// Act
		Integer x1Observed = r.getDim1(0).getData();
		Integer x2Observed = r.getDim2(0).getData();
		Integer y1Observed = r.getDim1(1).getData();
		Integer y2Observed = r.getDim2(1).getData();
		
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

//	@Disabled
	@Test
	void Rectangle_Overlap_True() {
		// Arrange
		
		IHyperRectangle<RInteger> r1 = new HyperRectangle<>(2);
		IHyperRectangle<RInteger> r2 = new HyperRectangle<>(2);
		
		r1.setDim1(0, new RInteger(0));
		r1.setDim1(1, new RInteger(0));
		r1.setDim2(0, new RInteger(10));
		r1.setDim2(1, new RInteger(10));
		
		r2.setDim1(0, new RInteger(5));
		r2.setDim1(1, new RInteger(5));
		r2.setDim2(0, new RInteger(15));
		r2.setDim2(1, new RInteger(15));
		
		
		// Act
		boolean overlap = HyperRectangle.rectanglesOverlap(r1, r2);
				
		// Assert
		assertEquals(true, overlap);
	}
	
//	@Disabled
	@Test
	void Rectangle_Overlap_False() {
		// Arrange
		
		IHyperRectangle<RInteger> r1 = new HyperRectangle<>(2);
		IHyperRectangle<RInteger> r2 = new HyperRectangle<>(2);
		
		r1.setDim1(0, new RInteger(0));
		r1.setDim1(1, new RInteger(0));
		r1.setDim2(0, new RInteger(10));
		r1.setDim2(1, new RInteger(10));
		
		r2.setDim1(0, new RInteger(20));
		r2.setDim1(1, new RInteger(20));
		r2.setDim2(0, new RInteger(30));
		r2.setDim2(1, new RInteger(30));
		
		
		// Act
		boolean overlap = HyperRectangle.rectanglesOverlap(r1, r2);
				
		// Assert
		assertEquals(false, overlap);
	}
	
//	@Disabled
	@Test
	void Rectangle_Sum_CorrectValue() {
		// Arrange
		IHyperRectangle<RInteger> r1 = new HyperRectangle<>(2);
		
		r1.setDim1(0, new RInteger(0));
		r1.setDim1(1, new RInteger(0));
		r1.setDim2(0, new RInteger(10));
		r1.setDim2(1, new RInteger(10));
		IHyperRectangle<RInteger> r2 = new HyperRectangle<>(2);
		r2.setDim1(0, new RInteger(20));
		r2.setDim1(1, new RInteger(20));
		r2.setDim2(0, new RInteger(30));
		r2.setDim2(1, new RInteger(30));
		
		List<IHyperRectangle<RInteger>> rectangles = new ArrayList<>();
		rectangles.add(r1);
		rectangles.add(r2);
		
		// Act
		IHyperRectangle<RInteger> sum = HyperRectangle.sumRectangles(rectangles);
				
		// Assert
		assertEquals(900, sum.getSpace());
	}

}
