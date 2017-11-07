import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by rishabh on 31/10/2017.
 */
public abstract class CacheSimulator {
  int m;
  int n;

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
    int size = 100;
    double hitRatios[] = new double[size];
    double missRates[] = new double[size];
    for (int i = 0; i < size; i++) {
      double time = 0;
      int hits = 0;
      int misses = 0;
      int arrivals = 0;
      while (arrivals <= 100000) {
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
    System.out.println();
    System.out.println("Mean: " + hitRatioStats.getMean());
    System.out.println("Confidence Interval: " + confidenceInterval(1.96D,
        hitRatioStats.getStdDev(), hitRatios.length));

    Statistics missRateStats = new Statistics(missRates);
    System.out.println("Miss Rate");
    System.out.println();
    System.out.println("Mean: " + missRateStats.getMean());
    System.out.println("Confidence Interval: " + confidenceInterval(1.96D,
        missRateStats.getStdDev(), missRates.length));
    System.out.println("-------------------");
  }

  double confidenceInterval(double confidenceLevel, double standardDeviation,
                            int size) {
    return confidenceLevel * standardDeviation / Math.sqrt(size);
  }


  abstract void processCacheMiss(int val);

}
