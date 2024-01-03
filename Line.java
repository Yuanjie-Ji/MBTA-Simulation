import java.util.ArrayList;
import java.util.List;

public class Line {

    public String name = null;

    public List<Station> stations = new ArrayList<>();

    public Train train = null;

    public Line(String name, List<String> stations) {
        this.name = name;
        for (String s : stations) {
            this.stations.add(Station.make(s));
        }
        this.train = Train.make(name);
        train.cur_station = this.stations.get(0);
        this.stations.get(0).cur_train = this.train;
    }

    public void next_station() {
        if (!train.reverse) {
            if (this.stations.get(this.stations.indexOf(train.cur_station) + 1) != null) {}
            train.cur_station = this.stations.get(this.stations.indexOf(train.cur_station) + 1);
        }
        else {
            if (this.stations.get(this.stations.indexOf(train.cur_station) - 1) != null) {}
            train.cur_station = this.stations.get(this.stations.indexOf(train.cur_station) - 1);
        }
        train.cur_station.cur_train = train;
    }


}
