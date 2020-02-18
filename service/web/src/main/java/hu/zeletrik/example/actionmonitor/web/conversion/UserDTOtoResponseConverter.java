package hu.zeletrik.example.actionmonitor.web.conversion;

import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOtoResponseConverter implements Converter<UserDTO, UserResponse> {

    @Override
    public UserResponse convert(UserDTO userDTO) {
        return UserResponse.builder()
                .user_id(userDTO.getUser_id())
                .username(userDTO.getUsername())
                .build();
    }
}
