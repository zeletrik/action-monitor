package hu.zeletrik.example.actionmonitor.web.rest.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {
    private String username;
    private String password;
}
