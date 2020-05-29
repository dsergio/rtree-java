import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cloudrtree.CloudRTree;
import cloudrtree.LocationItem;

public class Tester {

	public static void main(String[] args) {
		
		boolean gui = true;
		boolean insertGui = true;
		
		String inputTreeName = null;
		String inputMaxChildren = null;
		String inputMaxItems = null;
		String inputFile = null;
		int numInserts = 0; // default to the whole list
		
		Map<String, String> cityNameLatLong = new HashMap<String, String>();
		Map<String, String> cityNameLatLongToInsert = new HashMap<String, String>();
		
		
		if (args.length < 2)  {
			System.out.println("Usage: java Tester gui [treeName] [optional maxChildren] [optional maxItems]");
			System.out.println("Usage: java Tester cli [treeName] [inputFile] [number of inserts] [optional maxChildren] [optional maxItems]");
			return;
		}
		
		if (args[0].equals("gui")) {
			
			if (args.length == 3) {
				inputMaxChildren = args[2];
				inputMaxItems = args[2];
			} else if (args.length == 4) {
				inputMaxChildren = args[2];
				inputMaxItems = args[3];
			}
			insertGui = false;
			
		} else if (args[0].equals("cli")) {
			if (args.length < 3) {
				System.out.println("Usage: java Tester gui [treeName] [optional maxChildren] [optional maxItems]");
				System.out.println("Usage: java Tester cli [treeName] [inputFile] [number of inserts] [optional maxChildren] [optional maxItems]");
			}
			inputFile = args[2];
			numInserts = Integer.parseInt(args[3]);
			
			if (args.length == 5) {
				inputMaxChildren = args[4];
				inputMaxItems = args[4];
			} else if (args.length == 6) {
				inputMaxChildren = args[4];
				inputMaxItems = args[5];
			}
			gui = false;
			
			try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	line = line.trim();
			    	if (line.matches("[a-zA-Z,-_\\s0-9]+;-?[0-9.]+\\s*,\\s*-?[0-9.]+")) {
			    		
			    		String type = line.split(";")[0];
				    	String latlong =  line.split(";")[1];
				    	cityNameLatLong.put(type, latlong);
			    		
			    	}
			    	
			    	
			    }
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		inputTreeName = args[1];
		
		
		
//		CloudRTree tree = new CloudRTree("animal_rtree1", 4, 4);
//		CloudRTree tree = new CloudRTree("cloudtree20", 10, 10);
//		CloudRTree tree = new CloudRTree("cloudtree21", 4, 4);
//		CloudRTree tree = new CloudRTree("cloudtree22", 10, 10);
		
		CloudRTree tree = null;
		
		try {
			if (inputMaxChildren != null && inputMaxItems != null) {
				tree = new CloudRTree(inputTreeName, Integer.parseInt(inputMaxChildren), Integer.parseInt(inputMaxItems));
			} else {
				tree = new CloudRTree(inputTreeName);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		if (tree.getMaxChildrenVar() == -1) {
			System.out.println("Tree meta data not set");
			System.exit(0);
		}
		
		
		
		
//		insertGui = false;
		
		
		TestPaint testPaint = null;
		
		if (insertGui) {
			
			testPaint = new TestPaint(tree, true);
			testPaint.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
			
		}
		
		
//		gui = false;
		
		if (gui) {
			
			testPaint = new TestPaint(tree);
			testPaint.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
//			tree.printTree();
			
		} else {
			
			int mapSize = 700;
			double latZero = 49.112647;
			double latMax = 45.748839;
			double longZero = -124.118847;
			double longMax = -116.794380;
			
			int count = 0;
			int size = cityNameLatLong.size();
			if (numInserts == 0) {
				numInserts = size;
			}
			
			try {
				
				int insertCount = 0;
				for (String key : cityNameLatLong.keySet()) {
					
					
					if (insertCount < numInserts) {
						insertCount++;
						
						String latLong = cityNameLatLong.get(key);
						double latitude = Double.parseDouble(latLong.split(",")[0]);
						double longitude = Double.parseDouble(latLong.split(",")[1]);
						
						if (latitude < latZero) {
							latZero = latitude;
						}
						if (latitude > latMax) {
							latMax = latitude;
						}
						if (longitude < longZero) {
							longZero = longitude;
						}
						if (longitude > longMax) {
							longMax = longitude;
						}
						System.out.println("longZero: " + longZero + ", longMax: " + longMax);
						cityNameLatLongToInsert.put(key, cityNameLatLong.get(key));
						
					}
				}
				double padding = 0;
				longZero -= padding;
				longMax += padding;
				latZero -= padding;
				latMax += padding;
						
				long time = System.currentTimeMillis();
				long startTime = time;
				String performanceOutput = "";
				
				
				for (String key : cityNameLatLongToInsert.keySet()) {
						
					String latLong = cityNameLatLongToInsert.get(key);
					double latitude = Double.parseDouble(latLong.split(",")[0]);
					double longitude = Double.parseDouble(latLong.split(",")[1]);
					
					
					tree.insertType(newCity(latitude, longitude, key, latZero, latMax, longZero, longMax, mapSize));
					tree.updateRoot();
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					if (insertGui) {
						testPaint.repaint();
					}
					
					long curTime = System.currentTimeMillis();
					count++;
					performanceOutput += 
						count + "\t" + size + "\t" + key + "\t" + (curTime - time) + "\t" + (curTime - startTime) + "\t" + 
						tree.dynamoAdds() + "\t" + tree.numReads() + "\t" + tree.numUpdates() + "\t" + 
						tree.getAddTime() + "\t" + tree.getReadTime() + "\t" + tree.getUpdateTime() + "\t" + 
						tree.getLeafNodeSplit() + "\t" + tree.getBranchSplit() + 
						"\n";
								
					
					System.out.println(
							"Inserted " + count + " of " + size + " (" + numInserts + ") total items (" + latitude + ", " + longitude + ") " + 
							key + " current insert: " + (curTime - time) + "ms total so far: " + (curTime - startTime) + "ms total" + 
							" dynamoDB adds: " + tree.dynamoAdds() + " dynamo reads: " + tree.numReads() + " dynamo updates: " + tree.numUpdates() +
							" dynamoDB add time: " + tree.getAddTime() + " dynamo read time: " + tree.getReadTime() + " dynamo update time: " + tree.getUpdateTime() +
							" leafnode split? " + tree.getLeafNodeSplit() + " branchsplit? " + tree.getBranchSplit()
					);
					time = System.currentTimeMillis();
				}
					
				System.out.println("COUNT\tSIZE\tKEY\tINSERT TIME (ms)\tTOTAL TIME (ms)\tDYNAMO ADDS\tDYNAMO READS\tDYNAMO UPDATES\tDYNAMO ADD TOT (ms)\tDYNAMO READ TOT (ms)\tDYNAMO UPDATE TOT (ms)\tLEAF SPLIT\tBRANCH SPLIT");
				System.out.println(performanceOutput);
				
				System.out.println("\nlatZero: " + latZero + " latMax: " + latMax + " longZero: " + longZero + " longMax: " + longMax);
				System.out.println("Inserted the following: \n");
				for (String key : cityNameLatLongToInsert.keySet()) {
					System.out.println(key + ": " + cityNameLatLongToInsert.get(key));
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static LocationItem newCity(double latitude, double longitude, String name, double latZero, double latMax, double longZero, double longMax, int mapSize) {
		
		
		int xPos = (int) ( (          (longitude - longZero)   /  (longMax - longZero)   ) * mapSize);
		
		int yPos = (int) ( (      1 - ((latitude - latZero)    /  (latMax - latZero))     ) * mapSize);
		
		LocationItem item = new LocationItem(xPos, yPos, name);
		return item;
	}
	
}
