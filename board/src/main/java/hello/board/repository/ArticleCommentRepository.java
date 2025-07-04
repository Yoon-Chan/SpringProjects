package hello.board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import hello.board.domain.ArticleComment;
import hello.board.domain.QArticle;
import hello.board.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);
    //사용자 id도 같이 확이해서 삭제를 진행
    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        //현재 QuerydslPredicateExecutor을 통해서 모든 검색을 하게 되는데,
        //선택적으로 검색을 하고 싶을 때 사용
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content,root.createdAt, root.createdBy);
        //검색파라미터를 하나만 받음
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
