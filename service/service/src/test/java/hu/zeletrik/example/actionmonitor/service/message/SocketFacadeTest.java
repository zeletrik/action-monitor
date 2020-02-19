package hu.zeletrik.example.actionmonitor.service.message;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.data.repository.MessageRepository;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;

/**
 * Unit tests for {@link SocketFacade}.
 */
public class SocketFacadeTest {

    public static final String SECURED_CHAT_SPECIFIC_USER = "/secured/user/queue/specific-user";

    private static final String TO = "TO_USER";
    private static final String FROM = "FROM_USER";
    private static final String TEXT = "MESSAGE";
    private static final Instant TIME = Instant.now();

    @Mock
    private SimpMessagingTemplate simpMessagingTemplateMock;
    @Mock
    private MessageRepository messageRepositoryMock;
    @Mock
    private ConversionService conversionServiceMock;

    private SocketFacade underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new SocketFacade(simpMessagingTemplateMock, messageRepositoryMock, conversionServiceMock);
    }

    @Test
    public void processMessageShouldWork() {
        //given
        var dto = MessageDTO.builder()
                .to(TO)
                .from(FROM)
                .text(TEXT)
                .time(TIME)
                .build();

        var entity = createEntity();

        doNothing().when(simpMessagingTemplateMock).convertAndSendToUser(TO, SECURED_CHAT_SPECIFIC_USER, dto);
        given(conversionServiceMock.convert(dto, MessageEntity.class)).willReturn(entity);
        given(messageRepositoryMock.save(entity)).willReturn(entity);
        given(messageRepositoryMock.findById(entity.getId())).willReturn(Optional.of(entity));

        //when
        underTest.processMessage(dto);

        //then
        verify(simpMessagingTemplateMock).convertAndSendToUser(TO, SECURED_CHAT_SPECIFIC_USER, dto);
        verify(conversionServiceMock).convert(dto, MessageEntity.class);
        verify(messageRepositoryMock).save(entity);
        verify(messageRepositoryMock).findById(entity.getId());
        verifyNoMoreInteractions(simpMessagingTemplateMock, conversionServiceMock, messageRepositoryMock);
    }

    @Test
    public void processMessageShouldNotWorkWhenSendFail() {
        //given
        var dto = MessageDTO.builder()
                .to(TO)
                .from(FROM)
                .text(TEXT)
                .time(TIME)
                .build();

        doThrow(MessagingException.class).when(simpMessagingTemplateMock).convertAndSendToUser(TO, SECURED_CHAT_SPECIFIC_USER, dto);

        //when
        underTest.processMessage(dto);

        //then
        verify(simpMessagingTemplateMock).convertAndSendToUser(TO, SECURED_CHAT_SPECIFIC_USER, dto);
        verifyNoMoreInteractions(simpMessagingTemplateMock, conversionServiceMock, messageRepositoryMock);
    }

    private MessageEntity createEntity() {
        var entity = new MessageEntity();
        entity.setTo(1L);
        entity.setFrom(2L);
        entity.setText(TEXT);
        entity.setTime(TIME);

        return entity;
    }

}
