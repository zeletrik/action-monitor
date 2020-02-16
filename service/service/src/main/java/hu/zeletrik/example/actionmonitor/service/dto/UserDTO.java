package hu.zeletrik.example.actionmonitor.service.dto;

import lombok.*;

@Builder
@EqualsAndHashCode
@ToString(exclude = "password")
@Getter
public class UserDTO {

    private final Long user_id;
    private final String username;
    private final String password;

}
