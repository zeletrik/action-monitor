package hu.zeletrik.example.actionmonitor.service.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ServiceResponse<T> {

    private boolean success;
    private T body;
}
