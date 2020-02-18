package hu.zeletrik.example.actionmonitor.service.conversion;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import hu.zeletrik.example.actionmonitor.service.security.exception.NoUserFoundException;

@Component
public class MessageDTOtoEntityConverter implements Converter<MessageDTO, MessageEntity> {

    private final UserRepository userRepository;

    @Lazy
    public MessageDTOtoEntityConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MessageEntity convert(MessageDTO messageDTO) throws NoUserFoundException{

        var fromId = userRepository.findByUsername(messageDTO.getFrom())
                .map(UserEntity::getUser_id)
                .orElseThrow(() -> new NoUserFoundException("No user found in the database for name=" + messageDTO.getFrom()));

        var toId = userRepository.findByUsername(messageDTO.getTo())
                .map(UserEntity::getUser_id)
                .orElseThrow(() -> new NoUserFoundException("No user found in the database for name=" + messageDTO.getTo()));

        final var entity = new MessageEntity();
        entity.setFrom(fromId);
        entity.setTo(toId);
        entity.setText(messageDTO.getText());
        entity.setTime(messageDTO.getTime());
        return entity;
    }
}
