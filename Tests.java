import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class Tests {
  @Test public void testPass() {
    assertTrue("true should be true", true);
  }

  @Test public void test_1() {
    MBTA mbta_1 = new MBTA();
    Log log_1 = new Log();
    Train train_1 = Train.make("line1");
    Train train_2 = Train.make("line2");
    Passenger p_1 = Passenger.make("p1");
    Passenger p_2 = Passenger.make("p2");
    Station s_1 = Station.make("s1");
    Station s_2 = Station.make("s2");
    Station s_3 = Station.make("s3");
    Station s_4 = Station.make("s4");
    Station s_5 = Station.make("s5");
    List<String> l_1 = List.of("s1", "s2", "s3", "s4", "s5");
    mbta_1.addLine("line1", l_1);
    log_1.passenger_boards(p_1, train_1, s_1);
    log_1.train_moves(train_1, s_1, s_2);
    log_1.train_moves(train_1, s_2, s_3);
    log_1.passenger_boards(p_2, train_1, s_3);
    log_1.train_moves(train_1, s_3, s_4);
    log_1.passenger_deboards(p_1, train_1, s_4);
    log_1.train_moves(train_1, s_4, s_5);
    log_1.passenger_deboards(p_2, train_1, s_5);
    Verify.verify(mbta_1, log_1);
  }


}
