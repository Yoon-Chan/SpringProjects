package hello.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    //@CreatedBy, @LastModifiedBy를 쓸 때 어떤 값을 넣어야할지 하는 빈
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("chan"); //스프링 시큐ㅣ티로 인증 기능을 붙일 때 수정
    }
}