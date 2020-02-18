package hu.zeletrik.example.actionmonitor.web.socket;

import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import hu.zeletrik.example.actionmonitor.service.message.SocketFacade;
import hu.zeletrik.example.actionmonitor.web.socket.domain.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class SocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketController.class);

    public static final String SECURED_CHAT_ROOM = "/secured/room";

    private final SocketFacade socketFacade;
    private final ConversionService conversionService;

    public SocketController(SocketFacade socketFacade, ConversionService conversionService) {
        this.socketFacade = socketFacade;
        this.conversionService = conversionService;
    }

    /**
     * Example of sending message to specific user using 'convertAndSendToUser()' and '/queue'
     */
    @MessageMapping(SECURED_CHAT_ROOM)
    public void sendSpecific(@Payload IncomingMessage message, Principal user, @Header("simpSessionId") String sessionId) {
        LOGGER.info("Send message to={}, message={}, simpleSession={}", message.getTo(), message.getText(), sessionId);

        var dto = conversionService.convert(message, MessageDTO.class);

        socketFacade.processMessage(dto);
    }
}
