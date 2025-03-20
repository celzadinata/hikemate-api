package com.enigmacamp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @JsonAlias("hiker_id")
    private String hikerId;

    @JsonAlias("ranger_pic_id")
    private String rangerId;

    @JsonAlias("mountain_id")
    private String mountainId;

    @JsonAlias("start_date")
    private String startDate;

    @JsonAlias("end_date")
    private String endDate;

    @JsonAlias("route_id")
    private String routeId;
}
