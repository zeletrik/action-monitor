package hu.zeletrik.example.actionmonitor.service.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.data.entity.MessageEntity;
import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import hu.zeletrik.example.actionmonitor.service.security.exception.NoUserFoundException;

/**
 * Unit tests for {@link MessageDTOtoEntityConverter}.
 */
public class MessageDTOtoEntityConverterTest {

    private static final String TO = "TO_USER";
    private static final long TO_ID = 1L;
    private static final String FROM = "FROM_USER";
    private static final long FROM_ID = 2L;
    private static final String TEXT = "MESSAGE";
    private static final Instant TIME = Instant.now();
    private final MessageEntity ENTITY = new MessageEntity();

    private final MessageDTO DTO = MessageDTO.builder()
            .from(FROM)
            .to(TO)
            .text(TEXT)
            .time(TIME)
            .build();

    @Mock
    private UserRepository userRepositoryMock;

    private MessageDTOtoEntityConverter underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new MessageDTOtoEntityConverter(userRepositoryMock);

        ENTITY.setFrom(FROM_ID);
        ENTITY.setTo(TO_ID);
        ENTITY.setText(TEXT);
        ENTITY.setTime(TIME);
    }

    @Test
    public void convertShouldWork() {
        //given
        var fromEntity = new UserEntity();
        fromEntity.setUser_id(FROM_ID);

        var toEntity = new UserEntity();
        toEntity.setUser_id(TO_ID);

        given(userRepositoryMock.findByUsername(FROM)).willReturn(Optional.of(fromEntity));
        given(userRepositoryMock.findByUsername(TO)).willReturn(Optional.of(toEntity));

        //when
        var actual = underTest.convert(DTO);

        //then
        assertThat(actual, is(ENTITY));
        verify(userRepositoryMock).findByUsername(FROM);
        verify(userRepositoryMock).findByUsername(TO);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test(expectedExceptions = NoUserFoundException.class)
    public void convertShouldFailWhenFromNotFound() {
        //given
        given(userRepositoryMock.findByUsername(FROM)).willReturn(Optional.empty());

        //when
        var actual = underTest.convert(DTO);

        //then
        assertThat(actual, is(ENTITY));
        verify(userRepositoryMock).findByUsername(FROM);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test(expectedExceptions = NoUserFoundException.class)
    public void convertShouldFailWhenToNotFound() {
        //given
        var fromEntity = new UserEntity();
        fromEntity.setUser_id(FROM_ID);

        given(userRepositoryMock.findByUsername(FROM)).willReturn(Optional.of(fromEntity));
        given(userRepositoryMock.findByUsername(TO)).willReturn(Optional.empty());

        //when
        var actual = underTest.convert(DTO);

        //then
        assertThat(actual, is(ENTITY));
        verify(userRepositoryMock).findByUsername(FROM);
        verify(userRepositoryMock).findByUsername(TO);
        verifyNoMoreInteractions(userRepositoryMock);
    }
}
