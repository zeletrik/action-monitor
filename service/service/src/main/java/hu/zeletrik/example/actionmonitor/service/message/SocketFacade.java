package hu.zeletrik.example.actionmonitor.service.message;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.data.repository.MessageRepository;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SocketFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketFacade.class);
    public static final String SECURED_CHAT_SPECIFIC_USER = "/secured/user/queue/specific-user";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final ConversionService conversionService;

    public SocketFacade(SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository,
                        ConversionService conversionService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.conversionService = conversionService;
    }

    /**
     * Sends the message to the destination and calls the repository to save it.
     *
     */
    public void processMessage(MessageDTO message) {
        LOGGER.info("Processing message, sending to user={}", message.getTo());
        try {
            simpMessagingTemplate.convertAndSendToUser(message.getTo(), SECURED_CHAT_SPECIFIC_USER, message);
            var entity = conversionService.convert(message, MessageEntity.class);
            var saved = messageRepository.save(entity);
            messageRepository.findById(saved.getId()).ifPresentOrElse(
                    msg -> LOGGER.info("Message sent and saved successfully"),
                    () ->  LOGGER.warn("Message cannot be persisted"));

        } catch (MessagingException ex) {
            LOGGER.error("Error during sending message to user={} , ex={}", message.getTo(), ex.getFailedMessage());
        }
    }
}
