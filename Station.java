import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station extends Entity {

  public static Map<String, Station> all_stations = new HashMap<>();

  public Train cur_train = null;

  public Lock lock = new ReentrantLock();

  public Condition condition = lock.newCondition();

  private Station(String name) { super(name); }

  public static Station make(String name) {

    if (all_stations.containsKey(name)) {
      return all_stations.get(name);
    }
    else {
      Station new_station = new Station(name);
      all_stations.put(name, new_station);
      return new_station;
    }
  }
}
