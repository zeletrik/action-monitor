package hu.zeletrik.example.actionmonitor.web.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;

/**
 * Unit tests for {@link UserDTOtoResponseConverter}.
 */
public class UserDTOtoResponseConverterTest {

    private static final String USERNAME = "TEST";
    private static final String PASSWORD = "PASSWORD";
    private static final long ID = 1L;

    private UserDTOtoResponseConverter underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new UserDTOtoResponseConverter();
    }

    @Test(dataProvider = "convertProvider")
    public void convertShouldWork(UserDTO dto, UserResponse expected) {
        //given

        //when
        var actual = underTest.convert(dto);

        //then
        assertThat(actual, is(expected));
    }

    @DataProvider
    private Object[][] convertProvider() {
        var dto = UserDTO.builder().user_id(ID).username(USERNAME).password(PASSWORD).build();
        var response = UserResponse.builder().user_id(ID).username(USERNAME).build();

        var emptyDto = UserDTO.builder().build();
        var emptyResponse = UserResponse.builder().build();

        return new Object[][]{
                {dto, response},
                {emptyDto, emptyResponse}
        };
    }
}
