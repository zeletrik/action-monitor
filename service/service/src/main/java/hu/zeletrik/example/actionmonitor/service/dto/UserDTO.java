package hu.zeletrik.example.actionmonitor.service.dto;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString(exclude = "password")
public class UserDTO {

    private final Long user_id;
    private final String username;
    private final String password;

}
