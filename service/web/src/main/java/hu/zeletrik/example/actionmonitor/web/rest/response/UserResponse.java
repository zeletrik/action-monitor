package hu.zeletrik.example.actionmonitor.web.rest.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    private Long user_id;
    private String username;
}
