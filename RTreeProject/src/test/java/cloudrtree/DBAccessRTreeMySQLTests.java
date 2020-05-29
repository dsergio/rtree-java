package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBAccessRTreeMySQLTests {

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
	void CreateDBAccess_Success() {
		// Arrange
		
		// Act
		try {
			DBAccessRTreeMySQL dbAccessMySQL = new DBAccessRTreeMySQL();
		} catch (Exception e) {
			fail("Failed to create database access object");
			e.printStackTrace();
		}
				
		// Assert
		assertTrue(true);
	}

}
