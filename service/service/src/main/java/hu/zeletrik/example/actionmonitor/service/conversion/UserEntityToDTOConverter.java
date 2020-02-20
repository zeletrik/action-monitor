package hu.zeletrik.example.actionmonitor.service.conversion;

import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOConverter implements Converter<UserEntity, UserDTO> {

    @Override
    public UserDTO convert(UserEntity userEntity) {
        return UserDTO.builder()
                .user_id(userEntity.getUser_id())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }
}
