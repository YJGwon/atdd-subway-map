package wooteco.subway.service;

import java.util.List;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;

public class StationService {

    public Station createStation(String name) {
        List<Station> stations = StationDao.findAll();
        boolean isDuplicated = stations.stream()
            .anyMatch(station -> station.getName().equals(name));
        if (isDuplicated) {
            throw new IllegalArgumentException("중복!");
        }
        Station station = new Station(name);
        return StationDao.save(station);
    }

}
