package hello.board.controller;

import hello.board.domain.type.SearchType;
import hello.board.response.ArticleResponse;
import hello.board.response.ArticleWithCommentResponse;
import hello.board.service.ArticleService;
import hello.board.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<ArticleResponse> response = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), response.getTotalPages());
        map.addAttribute("articles", response);
        map.addAttribute("paginationBarNumbers", barNumbers);
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        return "articles/detail";
    }
}
