
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class Train extends Entity implements Runnable {

  public static Map<String, Train> all_trains = new HashMap<>();

  public Line line = null;

  public boolean reverse = false;

  public Station cur_station = null;

  public Station next_station = null;

  public List<Passenger> all_passengers = new ArrayList<>();

  public static Log log = null;

  public static MBTA mbta = null;

  private Train(String name) { super(name); }

  public static Train make(String name) {
    // Change this method!
    if (all_trains.containsKey(name)) {
      return all_trains.get(name);
    }
    else {
      Train new_train = new Train(name);
      all_trains.put(name, new_train);
      return new_train;
    }
  }

  public Station get_next_station() {
    if (!this.reverse) {
      return line.stations.get(line.stations.indexOf(cur_station) + 1);
    }
    else {
      return line.stations.get(line.stations.indexOf(cur_station) - 1);
    }
  }

  public void run() {

    try {
      while (!mbta.all_journeys.isEmpty()) {
        Thread.sleep(500);
        if (!line.train.reverse) {
          if (line.stations.indexOf(line.train.cur_station) == line.stations.size() - 1) {
            line.train.reverse = true;
          }
        }
        else {
          if (line.stations.indexOf(line.train.cur_station) == 0) {
            line.train.reverse = false;
          }
        }
        next_station = get_next_station();
        next_station.lock.lock();
        while (next_station.cur_train != null) {
          next_station.condition.await();
        }
        cur_station.lock.lock();
        Station previous_station = cur_station;
        cur_station.cur_train = null;
        cur_station.condition.signalAll();
        log.train_moves(this, cur_station, next_station);
        previous_station.lock.unlock();
        cur_station = next_station;
        if (!line.train.reverse) {
          if (line.stations.indexOf(line.train.cur_station) == line.stations.size() - 1) {
            line.train.reverse = true;
          }
        }
        else {
          if (line.stations.indexOf(line.train.cur_station) == 0) {
            line.train.reverse = false;
          }
        }
        cur_station.cur_train = this;
        cur_station.condition.signalAll();
        cur_station.lock.unlock();
        next_station = get_next_station();
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

}
