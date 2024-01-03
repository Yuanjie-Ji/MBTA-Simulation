import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Passenger extends Entity implements Runnable {

  public static Map<String, Passenger> all_passengers = new HashMap<>();

  public Train cur_train = null;

  public Journey journey = null;

  public Station cur_station = null;

  public Station next_station = null;

  public static Log log = null;

  public static MBTA mbta = null;

  public int journey_index = 0;


  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if (all_passengers.containsKey(name)) {
      return all_passengers.get(name);
    }
    else {
      Passenger new_passenger = new Passenger(name);
      all_passengers.put(name, new_passenger);
      return new_passenger;
    }
  }

  public Station get_cur_station() {
    if (cur_train != null) {
      return cur_train.cur_station;
    }
    return cur_station;
  }

  public boolean should_board(Station station) {
    Train train = station.cur_train;
    if (train == null) {
      return false;
    }
    if (journey_index == journey.stations.size() - 1) {
      return false;
    }
    Station next_station = journey.stations.get(journey_index + 1);
    return train.line.stations.contains(next_station);
  }

  public void run() {
    try {
      while (journey_index != journey.stations.size() - 1) {
        if (cur_train == null) {
          Station cur_station = get_cur_station();
          cur_station.lock.lock();
          cur_station.condition.await();
          if (should_board(get_cur_station())) {
            cur_train = cur_station.cur_train;
            log.passenger_boards(this, cur_train, cur_station);
            cur_station.lock.unlock();
            Station next_station = journey.stations.get(journey_index + 1);
            next_station.lock.lock();
            next_station.condition.await();
          }
          else {cur_station.lock.unlock();}
        }
        else {
          if (cur_train.cur_station == journey.stations.get(journey_index + 1)) {
            journey_index++;
            cur_station = journey.stations.get(journey_index);
            log.passenger_deboards(this, cur_train, cur_station);
            cur_train = null;
            cur_station.lock.unlock();
          }
          else {
            journey.stations.get(journey_index + 1).condition.await();
          }
        }
      }
      mbta.all_journeys.remove(journey);
      System.out.println(mbta.all_journeys);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

