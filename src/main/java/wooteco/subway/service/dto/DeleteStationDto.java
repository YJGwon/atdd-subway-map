package wooteco.subway.service.dto;

import javax.validation.constraints.NotNull;

public class DeleteStationDto {

    @NotNull
    private final Long lineId;
    @NotNull
    private final Long stationId;

    public DeleteStationDto(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
