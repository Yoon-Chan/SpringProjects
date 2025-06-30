package hello.board.repository;

import hello.board.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

//인터페이스로 구현하고 JpaRepository는 extends로 상속받기
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
