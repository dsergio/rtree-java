import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtree.item.LocationItem2D;
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
import rtree.tree.RTree2D;
import rtree.tree.RTreeND;
import rtree.tree.generic.IRTreeGeneric;
import rtree.tree.generic.RTreeNDGeneric;

public class TesterGeo {

	public static void main(String[] args) {

		String inputTreeName = null;
		String inputMaxChildren = null;
		String inputMaxItems = null;
		
		Double latitudeMin = null;
		Double latitudeMax = null;
		Double longitudeMin = null;
		Double longitudeMax = null;

		// configurations
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		ILoggerPaint paintLogger = new LoggerPaint(LogLevel.DEV);
		rtree.storage.StorageType cloudType = rtree.storage.StorageType.SQLITE;

		if (args.length < 1) {
			logger.log("Usage: java TesterGeo [treeName] [optional maxChildren] [optional maxItems]");
			return;
		} else if (args.length == 3) {
			inputMaxChildren = args[1];
			inputMaxItems = args[2];
		}
		inputTreeName = args[0];

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

				tree = new RTreeNDGeneric<RDouble>(dataStorage, inputMaxChildrenInt, inputMaxItemsInt, logger, 2,
						inputTreeName, RDouble.class);

			} else {

				tree = new RTreeNDGeneric<RDouble>(dataStorage, logger, inputTreeName, RDouble.class);
			}
			
			List<RDouble> minimums = tree.getMin();
			List<RDouble> maximums = tree.getMax();
			
			latitudeMin = minimums.get(1).getData();
			latitudeMax = maximums.get(1).getData();
			longitudeMin = minimums.get(0).getData();
			longitudeMax = maximums.get(0).getData();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		TestPaintGeo testPaint = null;

		testPaint = new TestPaintGeo(tree, logger, paintLogger, longitudeMin, longitudeMax, latitudeMin, latitudeMax);
		testPaint.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

}
