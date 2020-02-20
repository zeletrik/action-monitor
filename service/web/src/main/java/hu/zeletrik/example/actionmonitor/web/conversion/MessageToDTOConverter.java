package hu.zeletrik.example.actionmonitor.web.conversion;

import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import hu.zeletrik.example.actionmonitor.web.socket.domain.IncomingMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageToDTOConverter implements Converter<IncomingMessage, MessageDTO> {

    @Override
    public MessageDTO convert(IncomingMessage message) {
        return MessageDTO.builder()
                .from(message.getFrom())
                .to(message.getTo())
                .text(message.getText())
                .time(Instant.now())
                .build();
    }
}
