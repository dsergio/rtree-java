package rtree.item;

/**
 * TODO this should be a singleton, and it should read the distanceMatrix and rank from a file
 * This class represents a nominal quality type with predefined values and distances.
 *
 */
public class RNominalQuality extends RNominal implements IRType<RNominalQuality>{

	/**
	 * Constructor that initializes the nominal quality with the given data.
	 * @param data the data to initialize the nominal quality
	 */
	public RNominalQuality(Object data) {
		super(data);
		init();
	}
	
	private void init() {
		
		/*
		 * 
		 * These should be read from a file
		 * This is just for proof of concept
		 * 
		 */
		
		addValue("good");
		addValue("bad");
		addValue("poor");
		addValue("excellent");
		addValue("ok");
		addValue("horrible");
		
		addValueDistance("good", "good", 0);
		addValueDistance("good", "bad", 10);
		addValueDistance("good", "poor", 10);
		addValueDistance("good", "excellent", 1);
		addValueDistance("good", "ok", 5);
		addValueDistance("good", "horrible", 100);
		
		addValueDistance("bad", "bad", 0);
		addValueDistance("bad", "poor", 1);
		addValueDistance("bad", "excellent", 50);
		addValueDistance("bad", "ok", 5);
		addValueDistance("bad", "horrible", 50);
		
		addValueDistance("poor", "poor", 0);
		addValueDistance("poor", "excellent", 100);
		addValueDistance("poor", "ok", 20);
		addValueDistance("poor", "horrible", 10);
		
		addValueDistance("excellent", "excellent", 0);
		addValueDistance("excellent", "ok", 50);
		addValueDistance("excellent", "horrible", 200);
		
		addValueDistance("ok", "ok", 0);
		addValueDistance("ok", "horrible", 50);
		
		addValueDistance("horrible", "horrible", 0);
		
		setRank("good", 2);
		setRank("bad", 4);
		setRank("poor", 5);
		setRank("excellent", 1);
		setRank("ok", 3);
		setRank("horrible", 6);
	}
	
	public RNominalQuality() {
		super();
		init();
	}

	@Override
	public double distanceTo(RNominalQuality o) {
		return distanceMatrix[values.indexOf(data)][o.values.indexOf(o.getData())];
	}

	@Override
	public int compareTo(RNominalQuality o) {
		if (rank.get(data) < rank.get(o.getData())) {
			return -1;
		}
		if (rank.get(data) > rank.get(o.getData())) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean hasValueRange() {
		return true;
	}

	@Override
	public RNominalQuality getValueMin() {
		return null;
	}

	@Override
	public RNominalQuality getValueMax() {
		return null;
	}

	@Override
	public boolean hasDiscreteValues() {
		return true;
	}
}
