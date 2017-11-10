import java.util.ArrayList;

public class RANDCacheSimulator extends CacheSimulator {
  ArrayList<Integer> cacheList;

  public RANDCacheSimulator(int m, int n) {
    super(m, n);
    this.initCacheList();
  }

  private void initCacheList() {
    cacheList = new ArrayList<Integer>();
    for (int i = 1; i <= m; i++) {
      cacheList.add(i);
    }
  }

  @Override
  void processCacheMiss(int val) {
    int index = (int) Math.floor(Math.random() * m);
    int toBeRemoved = cacheList.get(index);
    cache.remove(toBeRemoved);
    cacheList.remove(index);
    cache.add(val);
    cacheList.add(val);
  }

  public static void main(String[] args) {
    System.out.println("RAND: ");
    RANDCacheSimulator randSimulator;
    randSimulator = new RANDCacheSimulator(10, 1000);
    randSimulator.simulate();
    randSimulator = new RANDCacheSimulator(50, 1000);
    randSimulator.simulate();
    randSimulator = new RANDCacheSimulator(100, 1000);
    randSimulator.simulate();
  }
}
