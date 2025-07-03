package hello.board.controller;

import hello.board.dto.request.ArticleRequest;
import hello.board.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;


    @PostMapping("/new")
    public String postNewArticle(ArticleRequest articleRequest) {
        return "";
    }
}
