package cloudrtree;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CloudRTreeNodeTests {

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
		
		CloudRTreeNode node1 = new CloudRTreeNode(node1Id, null, null, null);
		CloudRTreeNode node2 = new CloudRTreeNode(node2Id, null, null, null);
		ArrayList<String> newChildren = new ArrayList<String>();
		newChildren.add(node1.nodeId);
		newChildren.add(node2.nodeId);
		
		// Act
		CloudRTreeNode node = new CloudRTreeNode(nodeId, "", "", null);
		node.setChildren(newChildren);
		
		
		// Assert
		assertEquals(2, node.getChildren().size());
		
	}

}
