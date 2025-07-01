package hello.board.repository;

import hello.board.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//인터페이스로 구현하고 JpaRepository는 extends로 상속받기
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
