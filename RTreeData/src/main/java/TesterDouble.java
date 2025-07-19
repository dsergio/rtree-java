import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rtree.item.RDouble;
import rtree.log.ILogger;
import rtree.log.ILoggerPaint;
import rtree.log.LogLevel;
import rtree.log.LoggerPaint;
import rtree.log.LoggerStdOut;
import rtree.storage.DataStorageInMemory;
import rtree.storage.DataStorageMySQL;
import rtree.storage.DataStorageSqlite;
import rtree.storage.IDataStorage;
import rtree.storage.StorageType;
import rtree.tree.IRTree;
import rtree.tree.RTree;

public class TesterDouble {

	public static void main(String[] args) {

		String inputTreeName = null;
		String inputMaxChildren = null;
		String inputMaxItems = null;

		// configurations
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		ILoggerPaint paintLogger = new LoggerPaint(LogLevel.DEV);
		StorageType cloudType = StorageType.SQLITE;
		cloudType = StorageType.MYSQL;

		if (args.length < 1) {
			logger.log("Usage: java TesterDouble [treeName] [optional maxChildren] [optional maxItems]");
			return;
		} else if (args.length == 3) {
			inputMaxChildren = args[1];
			inputMaxItems = args[2];
		}
		inputTreeName = args[0];

		IDataStorage<RDouble> dataStorage = null;

		switch (cloudType) {
		case MYSQL:
			try {
				dataStorage = new DataStorageMySQL<RDouble>(logger, RDouble.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case INMEMORY:
			dataStorage = new DataStorageInMemory<RDouble>(logger, RDouble.class);
			break;
		case DYNAMODB:
//			dataStorage = new DataStorageDynamoDB("us-west-2", logger, inputTreeName, 2); // use a static value for now
			break;
		case SQLITE:
			dataStorage = new DataStorageSqlite<RDouble>(logger, RDouble.class);
			break;
		default:
			try {
				dataStorage = new DataStorageMySQL<RDouble>(logger, RDouble.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		IRTree<RDouble> tree = null;

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

				tree = new RTree<RDouble>(dataStorage, inputMaxChildrenInt, inputMaxItemsInt, logger, 2,
						inputTreeName, RDouble.class);

			} else {

				tree = new RTree<RDouble>(dataStorage, logger, inputTreeName, RDouble.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		TestPaintDouble testPaint = null;

		testPaint = new TestPaintDouble(tree, logger, paintLogger);
		testPaint.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

}
