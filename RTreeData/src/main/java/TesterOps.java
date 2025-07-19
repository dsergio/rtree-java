import rtree.item.RDouble;
import rtree.log.ILogger;
import rtree.log.LogLevel;
import rtree.log.LoggerStdOut;
import rtree.storage.DataStorageInMemory;
import rtree.storage.DataStorageMySQL;
import rtree.storage.DataStorageSqlite;
import rtree.storage.IDataStorage;
import rtree.storage.StorageType;
import rtree.tree.IRTree;
import rtree.tree.RTree;

public class TesterOps {

	public static void main(String[] args) {
		
		System.out.println("Starting TesterOps...");
		
		String inputTreeName = null;

		// configurations
		ILogger logger = new LoggerStdOut(LogLevel.DEV);
		StorageType cloudType = StorageType.SQLITE;
		cloudType = StorageType.MYSQL;
		
		logger.log("arg length: " + args.length);

		if (args.length < 1) {
			logger.log("Usage: java TesterOps [treeName]");
			return;
		}
		inputTreeName = args[0];
		
		logger.log("tree: " + inputTreeName);

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

		try {

			tree = new RTree<RDouble>(dataStorage, logger, inputTreeName, RDouble.class);
			tree.printTree();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		try {
			logger.log("deleting tree " + inputTreeName);
			tree.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
