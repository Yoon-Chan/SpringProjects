package hello.board.controller;

import hello.board.config.SecurityConfig;
import hello.board.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void givenNothing_whenRequestingRootPage_thenReturnsToArticlesPage() throws Exception {
        //Given

        //When

        //Then
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}