package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@Test
	void LocationItemBase_2D_Space() {
		// Arrange
		LocationItemBase item1 = new LocationItem(0, 0, "test");
		LocationItemBase item2 = new LocationItem(10, 10, "test");
		
		// Act
		int space = LocationItemBase.space(item1, item2);
				
		// Assert
		assertEquals(100, space);
	}
	
	@Test
	void LocationItemBase_3D_Space() {
		// Arrange
		LocationItemBase item1 = new LocationItem3D();
		LocationItemBase item2 = new LocationItem3D();
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
