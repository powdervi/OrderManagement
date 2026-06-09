package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrackingTimelineRes {
    private List<TrackingEventRes> events;
}
