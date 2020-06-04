package cloudrtree;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloudrtree.ILogger.LogLevel;

class RTreeNodeTests {

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
	void CreateNode_SetChildren() {
		// Arrange
		String node1Id = UUID.randomUUID().toString();
		String node2Id = UUID.randomUUID().toString();
		String nodeId = UUID.randomUUID().toString();
		ILogger logger = new StdOutLogger(LogLevel.DEV);
		
		RTreeNode node1 = new RTreeNode(node1Id, null, null, null, logger);
		RTreeNode node2 = new RTreeNode(node2Id, null, null, null, logger);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.nodeId);
		newChildren.add(node2.nodeId);
		
		// Act
		RTreeNode node = new RTreeNode(nodeId, "", "", null, logger);
		node.setChildren(newChildren);
		
		
		// Assert
		assertEquals(2, node.getChildren().size());
		
	}

}