# rtree-java


Cloud-based R-Tree structure which can store spatial data (2D in this project) in a R-Tree structure in persistent storage, using Java. This project was created to demonstrate a simple cloud-based tree structure in AWS DynamoDB, however I am currently working on extending it to use any persistent storage (such as MySQL) via the strategy design pattern. This is in progress and far beyond the scope of the original assignment.


Properties of RTree:
* Depth-balanced
* Max number of children, and max number of leaf node Items
* Each branch node contains a rectangle and a set of children nodes
* Each leaf node contains a rectangle and a set of point data.
* Split Algorithm: The split algorithm determines how to split up the overflow of items or children when a node exceeds the max, which occurs recursively from the leaf node up to the root if required. This implementation uses quadradic split. The quadradic split in the leaf node calculates the worst combination of two items (largest combined rectangle). These two items are the seeds, and the other items are distributed to either of the two seeds in a way that minimizes the enlargement area. There are other flavors of split algorithms that could be explored in order to optimize performance.

## Storage Configuration

### MySQL
This application reads a creds.txt file 2 directories up from the launch directory:
>host=[host]

>user=[user]

>password=[password]

>database=[database]

### AWS DynamoDB
The ProfileCredentialsProvider will return your [default] credential profile by reading from the credentials file located at `~/.aws/credentials` for Linux and Mac machines.

### Metadata Table
TBD

## Usage

The default max children count is 4. The default max item count is 4. If an RTree already exists, it has these set already, it will use the value in the metadata table (either DynamoDB or other storage configuration), not in the command line argument.  

To Compile: `mvn package`


## Run GUI 

`java Tester gui [treeName] [optional maxChildren] [optional maxItems]`

e.g. `Java Tester gui tree1`

## Insert CLI and GUI 

`java Tester cli [treeName] [inputFile] [number of inserts] [optional maxChildren] [optional maxItems]`

e.g. `Java Tester cli cloudtree28 ./wa_cities 100 8 8`

## Using the GUI

The GUI is based on swing Java Graphics using a BorderLayout. 
GUI Features
* Show/Hide Tree Button: fetch the entire RTree structure from persistent storage if it is not cached. If it is cached, toggle between hiding and displaying the tree on the graph
* Search Command Bar: Enter commands to interact with the RTree structure. 
	* Search the CloudRTree data structure 
		* `search [x] [y] [range]`
	* Set search range for right-click search
		* `set range [range]`
	* Delete an item
		* `delete [x] [y] [type]`
	* Print textual representation of tree to stdout.
		* `print`
* Click Interactions with Output:
	* Right-clicking will search the coordinates
	* Left-clicking will insert a new LocationItem with the x and y value of the click location, and a type value set to a random animal name.
* The left list view will display the LocationItems returned from a search 
