import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.LocationItem2D;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.item.generic.RDouble;
import rtree.log.ILogger;
import rtree.log.ILoggerPaint;
import rtree.log.LogLevel;
import rtree.log.LoggerPaint;
import rtree.log.LoggerStdOut;
import rtree.storage.DataStorageInMemory;
import rtree.storage.DataStorageMySQL;
import rtree.storage.DataStorageSqlite;
import rtree.storage.IDataStorage;
import rtree.storage.generic.DataStorageInMemoryGeneric;
import rtree.storage.generic.DataStorageMySQLGeneric;
import rtree.storage.generic.DataStorageSqliteGeneric;
import rtree.storage.generic.IDataStorageGeneric;
import rtree.tree.IRTree;
import rtree.tree.RTreeND;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.RTreeNDGeneric;

public class DataImport {

	public static void main(String[] args) {

		String inputTreeName = null;
		String inputMaxChildren = null;
		String inputMaxItems = null;
		String inputFile = null;
		
		int numInserts = 0; // default to the whole list

		List<ILocationItemGeneric<RDouble>> citiesToInsert = new ArrayList<ILocationItemGeneric<RDouble>>();

		// configurations
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		rtree.storage.StorageType cloudType = rtree.storage.StorageType.SQLITE;

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
			
			String line = br.readLine(); // header
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.matches("[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*")) {
					
					String[] arr = line.split(",");
					
					String country = arr[0];
					String type = arr[1];
					String city = type;
					String population = arr[4];
					String latitude = arr[5];
					String longitude = arr[6];
					
					ILocationItemGeneric<RDouble> locationItem = new LocationItemNDGeneric<RDouble>(2);
					locationItem.setType(type);
					locationItem.setProperty("country", country);
					locationItem.setProperty("city", city);
					locationItem.setProperty("population", population);
					locationItem.setDim(0, new RDouble(Double.parseDouble(longitude)));
					locationItem.setDim(1, new RDouble(Double.parseDouble(latitude)));
					
					citiesToInsert.add(locationItem);

				}

			}
		} catch (FileNotFoundException e) {
			logger.log("File not found");
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IDataStorageGeneric<RDouble> dataStorage = null;

		switch (cloudType) {
		case MYSQL:
			try {
				dataStorage = new DataStorageMySQLGeneric<RDouble>(logger, RDouble.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case INMEMORY:
			dataStorage = new DataStorageInMemoryGeneric<RDouble>(logger, RDouble.class);
			break;
		case DYNAMODB:
//			dataStorage = new DataStorageDynamoDB("us-west-2", logger, inputTreeName, 2); // use a static value for now
			break;
		case SQLITE:
			dataStorage = new DataStorageSqliteGeneric<RDouble>(logger, RDouble.class);
			break;
		default:
			try {
				dataStorage = new DataStorageMySQLGeneric<RDouble>(logger, RDouble.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		IRTreeGeneric<RDouble> tree = null;

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

				tree = new RTreeNDGeneric<RDouble>(dataStorage, inputMaxChildrenInt, inputMaxItemsInt, logger, 2, inputTreeName, RDouble.class);

			} else {

				tree = new RTreeNDGeneric<RDouble>(dataStorage, logger, inputTreeName, RDouble.class);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		int size = citiesToInsert.size();
		if (numInserts == 0) {
			numInserts = size;
		}
		
		System.out.println("numInserts: " + numInserts);

		try {

			int insertCount = 0;
			for (ILocationItemGeneric<RDouble> item : citiesToInsert) {

				if (insertCount < numInserts && tree != null) {
					insertCount++;

					tree.insertType(item);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
