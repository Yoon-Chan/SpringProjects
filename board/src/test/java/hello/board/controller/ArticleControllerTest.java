package hello.board.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(controllers = ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    //테스트케이스에 있는 것은 직접 Autowired를 지정해야 함.
    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    //Disabled를 사용하면 build 시 Test도 확인을 하는 데 이 오류를 해결할 수 있음.
//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given

        //When
        mvc.perform(get("/articles"))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                //모델이 주입되어있는지 확인
                .andExpect(model().attributeExists("articles"));
        //Then
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //Given

        //When
        mvc.perform(get("/articles/1"))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                //모델이 주입되어있는지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        //Then
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //Given

        //When
        mvc.perform(get("/articles/search"))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
        //Then
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagView_thenReturnsArticleHashtagView() throws Exception {
        //Given

        //When
        mvc.perform(get("/articles/search-hashtag"))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));
        //Then
    }
}