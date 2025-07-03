package hello.board.controller;

import hello.board.config.SecurityConfig;
import hello.board.dto.ArticleWithCommentsDto;
import hello.board.dto.UserAccountDto;
import hello.board.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(controllers = ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    //@MockBean은 스프링 3.4.0부터 deprecated됨
    //테스트 클래스의 필드에만 적용 가능하는 @MockitoBean을 사용하자여 대체하기
    @MockitoBean
    private ArticleService articleService;

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
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
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
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    //    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWihCommentsDto());

        //When
        mvc.perform(get("/articles/" + articleId))
                //정상 호출이 되었는지
                .andExpect(status().isOk())
                //contentType이 Text형식인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                //모델이 주입되어있는지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        //Then
        then(articleService).should().getArticle(articleId);
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

    private ArticleWithCommentsDto createArticleWihCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "chan",
                LocalDateTime.now(),
                "chan"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "chan",
                "pw",
                "chan@email.com",
                "Chan",
                "memo",
                LocalDateTime.now(),
                "chan",
                LocalDateTime.now(),
                "chan"
        );
    }
}