package hu.zeletrik.example.actionmonitor.service.conversion;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageDTOtoEntityConverter implements Converter<MessageDTO, MessageEntity> {

    @Override
    public MessageEntity convert(MessageDTO messageDTO) {
        var entity = new MessageEntity();
        entity.setFrom(messageDTO.getFrom());
        entity.setTo(messageDTO.getTo());
        entity.setText(messageDTO.getText());
        entity.setTime(messageDTO.getTime());
        return entity;
    }
}
