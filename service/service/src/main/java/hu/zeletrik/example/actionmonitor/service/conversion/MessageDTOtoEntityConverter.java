package hu.zeletrik.example.actionmonitor.service.conversion;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageDTOtoEntityConverter implements Converter<MessageDTO, MessageEntity> {

    private final UserRepository userRepository;

    @Lazy
    public MessageDTOtoEntityConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MessageEntity convert(MessageDTO messageDTO) {
        var fromId = userRepository.findByUsername(messageDTO.getFrom()).getUser_id();
        var toId = userRepository.findByUsername(messageDTO.getTo()).getUser_id();

        var entity = new MessageEntity();
        entity.setFrom(fromId);
        entity.setTo(toId);
        entity.setText(messageDTO.getText());
        entity.setTime(messageDTO.getTime());
        return entity;
    }
}
