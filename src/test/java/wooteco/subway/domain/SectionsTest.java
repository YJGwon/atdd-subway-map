package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private final Station upTermination = new Station(1L, "상행종점역");
    private final Station downTermination = new Station(2L, "하행종점역");
    private Sections sections;

    @BeforeEach
    void setUp() {
        Section section = new Section(upTermination, downTermination, 10);
        sections = new Sections(List.of(section));
    }

    @DisplayName("추가하는 구간의 하행 종점이 노선의 상행 종점이면 첫 구간으로 추가된다")
    @Test
    void add_first() {
        Station station = new Station(3L, "새로운역");
        Section section = new Section(station, upTermination, 5);
        sections.add(section);

        assertThat(sections.getAllStations().get(0)).isEqualTo(station);
    }

    @DisplayName("추가하는 구간의 상행 종점이 노선의 하행 종점이면 마지막 구간으로 추가된다")
    @Test
    void add_last() {
        Station station = new Station(3L, "새로운역");
        Section section = new Section(downTermination, station, 5);
        sections.add(section);

        List<Station> allStations = sections.getAllStations();
        assertThat(allStations.get(allStations.size() - 1)).isEqualTo(station);
    }

    @DisplayName("추가하는 구간의 양 방향 종점이 모두 노선에 존재하지 않으면 예외가 발생한다.")
    @Test
    void add_no_station() {
        Station station1 = new Station(3L, "새로운역");
        Station station2 = new Station(4L, "또다른역");
        Section section = new Section(station1, station2, 3);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sections.add(section))
                .withMessageContaining("존재하지 않아");
    }
}
