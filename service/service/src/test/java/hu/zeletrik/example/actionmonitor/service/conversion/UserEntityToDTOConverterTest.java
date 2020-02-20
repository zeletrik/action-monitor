package hu.zeletrik.example.actionmonitor.service.conversion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

/**
 * Unit tests for {@link UserEntityToDTOConverter}.
 */
public class UserEntityToDTOConverterTest {

    private static final String USERNAME = "TEST";
    private static final String PASSWORD = "PASSWORD";
    private static final long ID = 1L;

    private UserEntityToDTOConverter underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new UserEntityToDTOConverter();
    }

    @Test(dataProvider = "convertProvider")
    public void convertShouldWork(UserEntity entity, UserDTO expected) {
        //given

        //when
        var actual = underTest.convert(entity);

        //then
        assertThat(actual, is(expected));
    }


    @DataProvider
    private Object[][] convertProvider() {
        var entity = new UserEntity();
        entity.setUsername(USERNAME);
        entity.setPassword(PASSWORD);
        entity.setUser_id(ID);
        var dto = UserDTO.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .user_id(ID)
                .build();

        var emptyEntity = new UserEntity();
        var emptyDto = UserDTO.builder().build();

        var partialEntity = new UserEntity();
        partialEntity.setUsername(USERNAME);
        var partialDto = UserDTO.builder()
                .username(USERNAME)
                .build();

        return new Object[][]{
                {entity, dto},
                {emptyEntity, emptyDto},
                {partialEntity, partialDto},
        };
    }
}
