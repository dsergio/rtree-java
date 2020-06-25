package rtree.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.tree.DepRTreeNode;

/**
 * 
 * @author David Sergio
 *
 */
class DepRTreeNodeTests {

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
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		
		DepRTreeNode node1 = new DepRTreeNode(node1Id, null, null, null, logger);
		DepRTreeNode node2 = new DepRTreeNode(node2Id, null, null, null, logger);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.getNodeId());
		newChildren.add(node2.getNodeId());
		
		// Act
		DepRTreeNode node = new DepRTreeNode(nodeId, null, null, null, logger);
		node.setChildren(newChildren);
		
		
		// Assert
		assertEquals(2, node.getChildren().size());
		
	}
	
	

}
