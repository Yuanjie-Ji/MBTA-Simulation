import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    Train.mbta = mbta;
    Train.log = log;
    Passenger.mbta = mbta;
    Passenger.log = log;
    List<Thread> all_threads = new ArrayList<>();
    for (Passenger p : Passenger.all_passengers.values()) {
      Thread thread = new Thread(p);
      all_threads.add(thread);
      thread.start();
    }
    for (Train t : Train.all_trains.values()) {
      Thread thread = new Thread(t);
      all_threads.add(thread);
      thread.start();
    }

    for (Thread t : all_threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);
    System.out.println(mbta.all_lines);
    System.out.println(mbta.all_journeys);

    Log log = new Log();
    run_sim(mbta, log);
    System.out.println("simulation done");
    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}

