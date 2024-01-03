import java.util.*;

public class MoveEvent implements Event {
  public final Train t; public final Station s1, s2;
  public MoveEvent(Train t, Station s1, Station s2) {
    this.t = t; this.s1 = s1; this.s2 = s2;
  }
  public boolean equals(Object o) {
    if (o instanceof MoveEvent e) {
      return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(t, s1, s2);
  }
  public String toString() {
    return "Train " + t + " moves from " + s1 + " to " + s2;
  }
  public List<String> toStringList() {
    return List.of(t.toString(), s1.toString(), s2.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    boolean contains = false;
    if (t.line == null) {
      throw new UnsupportedOperationException();
    }

    for (Line line: mbta.all_lines) {
      if (line.train == t) {
        contains = true;
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
        if (line.train.cur_station != s1) {
          System.out.println("error1");
          throw new UnsupportedOperationException();}
        if (!line.train.reverse) {
          if (line.stations.get(line.stations.indexOf(s1) + 1) != s2) {
            System.out.println("error2");
            throw new UnsupportedOperationException();}
        }
        else {if (line.stations.get(line.stations.indexOf(s1) - 1) != s2) {
          System.out.println("error3");
          throw new UnsupportedOperationException();}}
        line.next_station();
      }
    }
    if (!contains) {
      System.out.println("error4");
      throw new UnsupportedOperationException();}
  }
}
