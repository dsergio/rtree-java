# rtree-java


<p>Cloud-based R-Tree structure which can store spatial data (2D in this project) in a R-Tree structure in Amazon AWS DynamoDB, using Java. This project was created to demonstrate a simple cloud-based tree structure, however I am currently working on extending it to use any persistent storage (such as MySQL) via the strategy design pattern. This is in progress and far beyond the scope of the original assignment.</p>


<p>Properties of RTree:
<ul> 
	<li>Depth-balanced</li>
	<li>Max number of children, and max number of leaf node Items</li>
	<li>Each branch node contains a rectangle and a set of children nodes</li>
	<li>Each leaf node contains a rectangle and a set of point data.</li>
	<li>Split Algorithm: The split algorithm determines how to split up the overflow of items or children when a node exceeds the max, which occurs recursively from the leaf node up to the root if required. This implementation uses quadradic split. The quadradic split in the leaf node calculates the worst combination of two items (largest combined rectangle). These two items are the seeds, and the other items are distributed to either of the two seeds in a way that minimizes the enlargement area. There are other flavors of split algorithms that could be explored in order to optimize performance.</li>
</ul>
</p>


## Usage 
The default max children count is 4. The default max item count is 4. If an RTree already exists, it has these set already, it will use the value in the DynamoDB table, not in the command line argument.  
javac *.java 

## Run GUI 
java Tester gui [treeName] [optional maxChildren] [optional maxItems] 
E.g. Java Tester gui cloudtree27 

Insert CLI and GUI 
java Tester cli [treeName] [inputFile] [number of inserts] [optional maxChildren] [optional maxItems] 
E.g. Java Tester cli cloudtree28 ./wa_cities 100 8 8 
 
<p>
Using the GUI The GUI is based on swing Java Graphics using a BorderLayout. 
GUI Features • Show/Hide Tree Button: fetch the entire RTRee structure from DynamoDB if it is not cached. If it is cached, toggle between hiding and displaying the tree on the graph • Search Command Bar: Enter commands to interact with the RTree structure. E.g. o search [x] [y] [range] ▪ Search the CloudRTree data structure o set range [range] ▪ Set search range for right-click search o delete [x] [y] [type] ▪ Delete an item o print ▪ Print textual representation of tree to stdout.  • Click Interactions with Output: o Right-clicking will search the coordinates  o Left-clicking will insert a new LocationItem with the x and y value of the click location, and a type value set to a random animal name. • The left list view will display the LocationItems returned from a search 
</p>