package hu.zeletrik.example.actionmonitor.service.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class MessageDTO {

    private String from;
    private String to;
    private String text;
    private Instant time;
}
