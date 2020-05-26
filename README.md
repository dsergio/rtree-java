# rtree-java

<li>
<p>Cloud-based R-Tree structure which can store spatial data (2D in this project) in a R-Tree structure in Amazon AWS DynamoDB, using Java. This project was created to demonstrate a simple cloud-based tree structure, however I am currently working on extending it to use any persistent storage (such as MySQL) via the strategy design pattern. This is in progress and far beyond the scope of the original assignment.</p>
</li>
<li>
<p>Properties of RTree:
<ul> 
	<li>Depth-balanced</li>
	<li>Max number of children, and max number of leaf node Items</li>
	<li>Each branch node contains a rectangle and a set of children nodes</li>
	<li>Each leaf node contains a rectangle and a set of point data.</li>
	<li>Split Algorithm: The split algorithm determines how to split up the overflow of items or children when a node exceeds the max, which occurs recursively from the leaf node up to the root if required. This implementation uses quadradic split. The quadradic split in the leaf node calculates the worst combination of two items (largest combined rectangle). These two items are the seeds, and the other items are distributed to either of the two seeds in a way that minimizes the enlargement area. There are other flavors of split algorithms that could be explored in order to optimize performance.</li>
</ul>
</p>
</li>