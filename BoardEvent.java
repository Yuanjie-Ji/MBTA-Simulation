import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    boolean contains = false;
    for (Line line: mbta.all_lines) {
      if (line.train == t) {
        for (Journey journey : mbta.all_journeys) {
          if (journey.passenger == p) {
            contains = true;
            if (!p.journey.stations.contains(s)) {
              System.out.println("error5");
              throw new UnsupportedOperationException();
            }
            if (!line.stations.contains(s)) {
              System.out.println("error6");
              throw new UnsupportedOperationException();
            }
            if (line.train.all_passengers.contains(p)) {
              System.out.println("error7");
            }
            line.train.all_passengers.add(p);
            p.cur_station = s;
            p.cur_train = t;
          }
        }
      }
    }
    if (!contains) {
      System.out.println("error8");
      throw new UnsupportedOperationException();}
  }
}
