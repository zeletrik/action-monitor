package hu.zeletrik.example.actionmonitor.web.rest.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class UserResponse {

    private Long user_id;
    private String username;
}
