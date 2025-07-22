package com.chan.sns.service;

import com.chan.sns.exception.ErrorCode;
import com.chan.sns.exception.SnsApplicationException;
import com.chan.sns.fixture.UserEntityFixture;
import com.chan.sns.model.entity.UserEntity;
import com.chan.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @MockitoBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {
        String username = "userName";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(username, password, 1));

        assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우() {
        String username = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(username, password));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_NAME);
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {
        String username = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);
        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는경우() {
        String username = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());


        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(username, password));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void 로그인시_패스워드가_틀린_경우() {
        String username = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(username, password, 1);

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(username, wrongPassword));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }
}
