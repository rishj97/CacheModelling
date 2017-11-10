import java.util.LinkedList;
import java.util.Queue;

public class FIFOCacheSimulator extends CacheSimulator {
  Queue<Integer> cacheQueue;

  public FIFOCacheSimulator(int m, int n) {
    super(m, n);
    this.initCacheQueue();
  }

  private void initCacheQueue() {
    cacheQueue = new LinkedList<Integer>();
    for (int i = 1; i <= m; i++) {
      cacheQueue.add(i);
    }
  }

  @Override
  void processCacheMiss(int val) {
    int toBeRemoved = cacheQueue.remove();
    cache.remove(toBeRemoved);
    cacheQueue.add(val);
    cache.add(val);
  }

  public static void main(String[] args) {
    System.out.println("FIFO: ");
    FIFOCacheSimulator fifoSimulator;
    fifoSimulator = new FIFOCacheSimulator(10, 1000);
    fifoSimulator.simulate();
    fifoSimulator = new FIFOCacheSimulator(50, 1000);
    fifoSimulator.simulate();
    fifoSimulator = new FIFOCacheSimulator(100, 1000);
    fifoSimulator.simulate();
  }
}
