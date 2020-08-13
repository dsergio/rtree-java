[![Build Status](https://dev.azure.com/dsergio/rtree-java/_apis/build/status/dsergio-rtree-app%20-%20CI?branchName=master)](https://dev.azure.com/dsergio/rtree-java/_build/latest?definitionId=14&branchName=master)

# rtree-java

N-Dimensional R-Tree data structure using minimum bounding boxes to hold data in a balanced R-Tree structure with configurable storage and configurable split (insertion) algorithms. The structure allows for fast O(log<sub>M</sub>n) querying by boundaries. For example, one might want to query all landmarks on a map that fall within a geographical region. This structure is designed for such queries.

Properties of RTree:
* Depth-balanced
* Max number of children, and max number of leaf node Items
* Each branch node contains a rectangle and a set of children nodes
* Each leaf node contains a rectangle and a set of point data.
* Split Algorithm: The split algorithm determines how to split up the overflow of items or children when a node exceeds the max, which occurs recursively from the leaf node up to the root if required. This implementation uses quadradic split. The quadradic split in the leaf node calculates the worst combination of two items (largest combined rectangle). These two items are the seeds, and the other items are distributed to either of the two seeds in a way that minimizes the enlargement area. There are other flavors of split algorithms that could be explored in order to optimize performance.

## RTreeWeb-dotNet
* Configuration:  `config.properties` configuration file in the `RTreeWeb/src/main/resources` directory.
* URL: https://dsergio-rtree-app.azurewebsites.net/RTree/

## rtree-api-boot
* Spring Boot Rest API
* URL: https://dsergio-rtree-api-boot.azurewebsites.net/swagger-ui/index.html

## RTreeData

* pom.xml:
```
<!-- https://pkgs.dev.azure.com/dsergio/rtree-java/_packaging/rtree-java-feed/maven/v1 -->
<dependency>
  <groupId>dsergio</groupId>
  <artifactId>rtree</artifactId>
  <version>1.0.2</version>
</dependency>
```

### Storage Configurations

### MySQL
Configuration: `config.properties` configuration file in the `RTreeData/src/main/resources` directory, or environment variables.

```
MYSQL.user
MYSQL.password
MYSQL.host
MYSQL.database
```

### Sqlite
Configuration: `config.properties` configuration file in the `RTreeData/src/main/resources` directory.

```
SQLITE.dir = C:\\path\\to\\sqlite\\database
```

### AWS DynamoDB
The ProfileCredentialsProvider will return your [default] credential profile by reading from the credentials file located at `~/.aws/credentials` for Linux and Mac machines.

### Metadata Table
Example `rtree_metadata` table: 

|id|treeName|maxChildren|maxItems|treeType|N|minimums|maximums
|--|----------|----------|----------|-----|-----|----------|----------
|63|3D_TestTree_1|4|4|RInteger|3|[14,13,6]|[82,93,89]
|64|TestTree_3D|4|4|RDouble|3|[1.2,10.0,1.5]|[5.5,100.0,15.2]
|65|4D_TestTree_1|4|4|RInteger|4|[8,0,32,4]|[57,97,90,97]
|66|8D_TestTree_1|4|4|RInteger|8|[6,36,7,1,0,17,24,1]|[59,90,64,90,81,85,87,94]
|67|1D_TestTree_1|4|4|RInteger|1|[2]|[93]

### Data Table
Example `rtree_data` table: 

|nodeId|parent|rectangle|items|children
|----------|----------|----------|----------|----------
|3D_TestTree_1|NULL|{"z1":6,"y1":13,"z2":89,"x1":1,"y2":93,"x2":82}|[]|["f61f0cdf","89486fad","2251cc18","d93a0d78"]
|203205e8|3D_TestTree_1|{"z1":23,"y1":13,"z2":89,"x1":34,"y2":35,"x2":82}|[{"x":55,"y":23,"z":89,"type":"Mountain Gorilla"},{"x":63,"y":13,"z":23,"type":"Brown Bear"},{"x":82,"y":35,"z":24,"type":"Snapping Turtle"},{"x":34,"y":27,"z":85,"type":"Boykin Spaniel"}]|[]
|4D_TestTree_1|NULL|{"3_1":4,"3_2":98,"z1":13,"y1":0,"z2":90,"x1":8,"y2":97,"x2":57}|[]|["7abd64e7","dd421c7d","f5888401"]
|0829f348|4D_TestTree_1|{"3_1":4,"3_2":85,"z1":43,"y1":19,"z2":71,"x1":22,"y2":97,"x2":44}|[{"3":4,"x":32,"y":39,"z":71,"type":"Buffalo"},{"3":85,"x":44,"y":97,"z":71,"type":"White Tiger"},{"3":85,"x":32,"y":77,"z":57,"type":"Uakari"},{"3":52,"x":22,"y":19,"z":43,"type":"Monkey"}]|[]
|8D_TestTree_1|NULL|{"3_1":1,"3_2":90,"4_1":0,"4_2":90,"5_1":17,"5_2":91,"6_1":24,"6_2":87,"7_1":1,"7_2":94,"z1":2,"y1":22,"z2":76,"x1":6,"y2":90,"x2":59}|[]|["d68ecc6f","a7d50781","17ad9e40","f52d34ab"]

### Items Table
Example `rtree_items` table: 

|Id|N|location|type|treeType|properties
|----------|----------|----------|----------|----------|----------
|e4fc87d7-0e97-43fe-9727-e6cfcd57c7e9|2|{"x":199,"y":334}|Hedgehog|RInteger|{"property1": "propertyvalue1"}

## RTreeData GUI Usage (2D Java Swing Implementation)

The default max children count is 4. The default max item count is 4. If an RTree already exists, it has these set already, it will use the value in the metadata table (either DynamoDB or other storage configuration), not in the command line argument.  

Compile: `mvn package` or see above pom.xml


### Run GUI

`java -cp .\target\rtree-1.0.jar Tester gui [treeName] [optional maxChildren] [optional maxItems]`

e.g. `java -cp .\target\rtree-1.0.jar Tester gui tree1`

### Insert CLI and GUI

The CLI feature will read a data file (see example [wa_cities](https://github.com/dsergio/rtree-java/blob/master/wa_cities)) with each line in the form<br /><br />
`city name,state abbreviation;latitude,longitude`<br /><br />
and insert the item into the R-Tree.

`java -cp .\target\rtree-1.0.jar Tester cli [treeName] [inputFile] [number of inserts] [optional maxChildren] [optional maxItems]`

e.g. `java -cp .\target\rtree-1.0.jar Tester cli cloudtree28 ../wa_cities 100 8 8`

### Using the GUI

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
