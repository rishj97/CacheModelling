import java.util.ArrayList;
import java.util.HashSet;

public abstract class CacheSimulator {
  int m;
  int n;
  final int numSimulations = 100;
  final int maxArrivals = 100000;
  double lambdaSum = 0;
  AliasMethod aliasMethod;
  HashSet<Integer> cache;


  public CacheSimulator(int m, int n) {
    this.m = m;
    this.n = n;
    this.cache = new HashSet<Integer>();
    init();
  }

  void init() {
    this.initCache();
    this.initCalculations();
    this.initAliasMethod();
  }

  void initAliasMethod() {
    ArrayList<Double> probabilities = new ArrayList<Double>();
    for (int i = 1; i <= n; i++) {
      probabilities.add(lambda(i) / lambdaSum);
    }
    aliasMethod = new AliasMethod(probabilities);
  }

  void initCalculations() {
    for (int i = 1; i <= n; i++) {
      this.lambdaSum += lambda(i);
    }
  }

  void initCache() {
    cache.clear();
    for (int i = 1; i <= m; i++) {
      cache.add(i);
    }
  }

  double lambda(int k) {
    return 1.0D / (double) k;
  }

  public double sampleArrivalTime() {
    double u = Math.random();
    return -1.0D * Math.log(1 - u) / lambdaSum;

  }

  void simulate() {

    double hitRatios[] = new double[numSimulations];
    double missRates[] = new double[numSimulations];
    for (int i = 0; i < numSimulations; i++) {
      double time = 0;
      int hits = 0;
      int misses = 0;
      int arrivals = 0;
      while (arrivals <= maxArrivals) {
        arrivals++;
        time += sampleArrivalTime();
        int request = aliasMethod.next() + 1;
        if (cache.contains(request)) {
          hits++;
        } else {
          misses++;
          processCacheMiss(request);
        }
      }
      hitRatios[i] = hits / (double) arrivals;
      missRates[i] = misses / time;
    }
    Statistics hitRatioStats = new Statistics(hitRatios);

    System.out.println("Hit Ratio");
    System.out.println("Mean: " + hitRatioStats.getMean());
    System.out.println("Confidence Interval: " + confidenceInterval(
        hitRatioStats.getStdDev(), hitRatios.length));

    Statistics missRateStats = new Statistics(missRates);
    System.out.println("Miss Rate");
    System.out.println("Mean: " + missRateStats.getMean());
    System.out.println("Confidence Interval: " + confidenceInterval(
        missRateStats.getStdDev(), missRates.length));
  }

  double confidenceInterval(double standardDeviation,
                            int size) {
    double confidenceValue = 1.96;    // For 95 percent confidence level
    return confidenceValue * standardDeviation / Math.sqrt(size);
  }


  abstract void processCacheMiss(int val);

}
