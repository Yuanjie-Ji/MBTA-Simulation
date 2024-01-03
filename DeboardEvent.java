import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
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
              System.out.println("error9");
              throw new UnsupportedOperationException();
            }
            if (!line.stations.contains(s)) {
              System.out.println("error10");
              throw new UnsupportedOperationException();
            }
            if (!line.train.all_passengers.contains(p)) {
              System.out.println("error11");
              throw new UnsupportedOperationException();
            }
            if (line.train.cur_station != s) {
              System.out.println("error12");
              throw new UnsupportedOperationException();
            }
            line.train.all_passengers.remove(p);
            p.cur_station = s;
            p.cur_train = null;
          }
        }
      }
    }
    if (!contains) {
      System.out.println("error13");
      throw new UnsupportedOperationException();}
  }
}
