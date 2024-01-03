import java.util.ArrayList;
import java.util.List;

public class Journey {

    public List<Station> stations = new ArrayList<>();

    public Passenger passenger = null;

    public Journey(Passenger passenger, List<String> stations) {
        this.passenger = passenger;
        for (String s : stations) {
            this.stations.add(Station.make(s));
        }
    }

}
