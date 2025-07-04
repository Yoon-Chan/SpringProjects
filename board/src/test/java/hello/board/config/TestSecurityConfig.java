package hello.board.config;

import hello.board.repository.UserAccountRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @Bean
    public UserAccountRepository userAccountRepository() {
        return Mockito.mock(UserAccountRepository.class);
    }
}
