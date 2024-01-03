import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MBTA {

  public List<Line> all_lines = new ArrayList<>();
  public List<Journey> all_journeys = new ArrayList<>();

  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    Line new_line = new Line(name, stations);
    Train new_train = Train.make(name);
    new_train.cur_station = new_line.stations.get(0);
    new_line.stations.get(0).cur_train = new_train;
    new_line.train = new_train;
    new_line.train.line = new_line;
    all_lines.add(new_line);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    Passenger new_passenger = Passenger.make(name);
    Journey new_journey = new Journey(new_passenger, stations);
    new_passenger.journey = new_journey;
    new_passenger.cur_station = new_journey.stations.get(0);
    all_journeys.add(new_journey);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    for (Line l : all_lines) {
      if (l.train.cur_station != l.stations.get(0)) {
        System.out.println("error14");
        throw new UnsupportedOperationException();
      }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    for (Journey j : all_journeys) {
      if (j.passenger.cur_station != j.stations.get(j.stations.size() - 1)) {
        System.out.println("error15");
        throw new UnsupportedOperationException();
      }
    }
  }


  // reset to an empty simulation
  public void reset() {
    all_lines.clear();
    all_journeys.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    Gson gson = new Gson();
    try {
      Reader reader = Files.newBufferedReader(Paths.get(filename));
      C temp = gson.fromJson(reader, C.class);
      System.out.println(temp.lines);
      System.out.println(temp.trips);
      for (String k_1 : temp.lines.keySet()) {
        this.addLine(k_1, temp.lines.get(k_1));
      }
      for (String k_2 : temp.trips.keySet()) {
        this.addJourney(k_2, temp.trips.get(k_2));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

