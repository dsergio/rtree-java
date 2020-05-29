package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CloudRTreeTests {

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
	void CreateTree_Success() {
		// Arrange
		
		// Act
		try {
			CloudRTree tree = new CloudRTree("TestTree1", 4, 4, "InMemory");
		} catch (Exception e) {
			fail("Failed to create tree");
			e.printStackTrace();
		}
		
		// Assert
		assertTrue(true);
	}

}
