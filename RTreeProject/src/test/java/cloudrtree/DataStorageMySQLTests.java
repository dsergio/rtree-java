package cloudrtree;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataStorageMySQLTests {

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
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		
		// Act
		try {
			
			DataStorageMySQL dbAccessMySQL = new DataStorageMySQL(logger);
		} catch (Exception e) {
			fail("Failed to create database access object");
			e.printStackTrace();
		}
				
		// Assert
		assertTrue(true);
	}
	
	@Test
	void CreateDBAccess_IllegalTreeName_ThrowsIllegalArgumentException() {
		// Arrange
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		
		// Act Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			DataStorageMySQL dbAccessMySQL = new DataStorageMySQL(logger);
			dbAccessMySQL.initializeStorage("");
		});
		
	}

}
