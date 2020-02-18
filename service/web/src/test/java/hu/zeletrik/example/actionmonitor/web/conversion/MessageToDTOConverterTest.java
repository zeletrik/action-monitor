package hu.zeletrik.example.actionmonitor.web.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.service.dto.MessageDTO;
import hu.zeletrik.example.actionmonitor.web.socket.domain.IncomingMessage;

/**
 * Unit tests for {@link MessageToDTOConverter}.
 */
public class MessageToDTOConverterTest {

    private static final String TO = "TO_USER";
    private static final String FROM = "FROM_USER";
    private static final String TEXT = "MESSAGE";

    private MessageToDTOConverter underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new MessageToDTOConverter();
    }

    @Test(dataProvider = "convertProvider")
    public void convertShouldWork(IncomingMessage message, MessageDTO expected) {
        //given

        //when
        var actual = underTest.convert(message);

        //then
        assertThat(actual.getFrom(), is(expected.getFrom()));
        assertThat(actual.getTo(), is(expected.getTo()));
        assertThat(actual.getText(), is(expected.getText()));;
        assertThat(actual.getTime().truncatedTo(ChronoUnit.MINUTES), is(Instant.now().truncatedTo(ChronoUnit.MINUTES)));
    }

    @DataProvider
    private Object[][] convertProvider() {
        var msg = new IncomingMessage();
        msg.setFrom(FROM);
        msg.setText(TEXT);
        msg.setTo(TO);

        var dto =  MessageDTO.builder()
                .from(FROM)
                .to(TO)
                .text(TEXT)
                .build();

        var emptyMsg = new IncomingMessage();
        var emptyDto =  MessageDTO.builder()
                .build();

        return new Object[][]{
                {msg, dto},
                {emptyMsg, emptyDto}
        };
    }
}
