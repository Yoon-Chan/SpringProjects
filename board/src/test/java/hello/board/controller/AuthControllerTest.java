package hello.board.controller;

import hello.board.config.SecurityConfig;
import hello.board.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 인증")
@Import(TestSecurityConfig.class)
@WebMvcTest(AuthControllerTest.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("[view][GET] 로그인 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenTryingToLoginIn_thenReturnsLoginView() throws Exception {
        //Given

        //When
        mvc.perform(get("/login"))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
        //Then
    }

}
