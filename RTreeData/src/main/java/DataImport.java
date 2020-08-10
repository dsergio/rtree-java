import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rtree.item.LocationItem2D;
import rtree.log.ILogger;
import rtree.log.ILoggerPaint;
import rtree.log.LogLevel;
import rtree.log.LoggerPaint;
import rtree.log.LoggerStdOut;
import rtree.storage.DataStorageInMemory;
import rtree.storage.DataStorageMySQL;
import rtree.storage.DataStorageSqlite;
import rtree.storage.IDataStorage;
import rtree.tree.IRTree;
import rtree.tree.RTreeND;

public class DataImport {

	public static void main(String[] args) {

		String inputTreeName = null;
		String inputMaxChildren = null;
		String inputMaxItems = null;
		String inputFile = null;
		int numInserts = 0; // default to the whole list

		Map<String, String> cityNameLatLong = new HashMap<String, String>();
		Map<String, String> cityNameLatLongToInsert = new HashMap<String, String>();

		// configurations
		ILogger logger = new LoggerStdOut(LogLevel.PROD);
		ILoggerPaint paintLogger = new LoggerPaint(LogLevel.PROD);
		rtree.storage.StorageType cloudType = rtree.storage.StorageType.MYSQL;

		if (args.length < 5) {
			logger.log("Usage: java DataImport [treeName] [inputFile] [number of inserts] [maxChildren] [maxItems]");
			return;
		} else {
			inputTreeName = args[0];
			inputFile = args[1];
			numInserts = Integer.parseInt(args[2]);
			inputMaxChildren = args[3];
			inputMaxItems = args[4];
		}

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.matches("[a-zA-Z,-_\\s0-9]+;-?[0-9.]+\\s*,\\s*-?[0-9.]+")) {

					String type = line.split(";")[0];
					String latlong = line.split(";")[1];
					cityNameLatLong.put(type, latlong);

				}

			}
		} catch (FileNotFoundException e) {
			logger.log("File not found");
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IDataStorage dataStorage = null;

		switch (cloudType) {
		case MYSQL:
			try {
				dataStorage = new DataStorageMySQL(logger);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case INMEMORY:
			dataStorage = new DataStorageInMemory(logger);
			break;
		case DYNAMODB:
//			dataStorage = new DataStorageDynamoDB("us-west-2", logger, inputTreeName, 2); // use a static value for now
			break;
		case SQLITE:
			dataStorage = new DataStorageSqlite(logger);
			break;
		default:
			try {
				dataStorage = new DataStorageMySQL(logger);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		IRTree tree = null;

		int inputMaxChildrenInt = 0;
		int inputMaxItemsInt = 0;

		try {

			if (inputMaxChildren != null && inputMaxItems != null) {

				inputMaxChildrenInt = Integer.parseInt(inputMaxChildren);
				inputMaxItemsInt = Integer.parseInt(inputMaxItems);

				if (inputMaxChildrenInt < 2 || inputMaxChildrenInt > 10) {
					throw new IllegalArgumentException(
							"Invalid max children input. Value must be between 2 and 10 inclusive.");
				}
				if (inputMaxItemsInt < 2 || inputMaxItemsInt > 10) {
					throw new IllegalArgumentException(
							"Invalid max items input. Value must be between 2 and 10 inclusive.");
				}

				tree = new RTreeND(dataStorage, inputMaxChildrenInt, inputMaxItemsInt, logger, 2, inputTreeName);

			} else {

				tree = new RTreeND(dataStorage, logger, inputTreeName);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

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
//						logger.log("longZero: " + longZero + ", longMax: " + longMax);
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

				if (tree != null) {
					tree.insertType(newCity(latitude, longitude, key, latZero, latMax, longZero, longMax, mapSize));
					tree.updateRoot();
				}

				long curTime = System.currentTimeMillis();
				count++;

				if (tree != null) {
					performanceOutput += count + "\t" + size + "\t" + key + "\t" + (curTime - time) + "\t"
							+ (curTime - startTime) + "\t" + tree.numAdds() + "\t" + tree.numReads() + "\t"
							+ tree.numUpdates() + "\t" + tree.getAddTime() + "\t" + tree.getReadTime() + "\t"
							+ tree.getUpdateTime() + "\t" + "\n";
				}

//					logger.log(
//							"Inserted " + count + " of " + size + " (" + numInserts + ") total items (" + latitude + ", " + longitude + ") " + 
//							key + " current insert: " + (curTime - time) + "ms total so far: " + (curTime - startTime) + "ms total" + 
//							" adds: " + tree.numAdds() + " reads: " + tree.numReads() + " updates: " + tree.numUpdates() +
//							" add time: " + tree.getAddTime() + " read time: " + tree.getReadTime() + " update time: " + tree.getUpdateTime()
//					);
				time = System.currentTimeMillis();
			}

//				logger.log("COUNT\tSIZE\tKEY\tINSERT TIME (ms)\tTOTAL TIME (ms)\tADDS\tREADS\tUPDATES\tADD TOT (ms)\tREAD TOT (ms)\tUPDATE TOT (ms)\t\t");
			logger.log(performanceOutput);
//				
//				logger.log("\nlatZero: " + latZero + " latMax: " + latMax + " longZero: " + longZero + " longMax: " + longMax);
//				logger.log("Inserted the following: \n");
//				for (String key : cityNameLatLongToInsert.keySet()) {
//					logger.log(key + ": " + cityNameLatLongToInsert.get(key));
//				}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static LocationItem2D newCity(double latitude, double longitude, String name, double latZero, double latMax,
			double longZero, double longMax, int mapSize) {

		int xPos = (int) (((longitude - longZero) / (longMax - longZero)) * mapSize);

		int yPos = (int) ((1 - ((latitude - latZero) / (latMax - latZero))) * mapSize);

		LocationItem2D item = new LocationItem2D(xPos, yPos, name);
		return item;
	}

}
