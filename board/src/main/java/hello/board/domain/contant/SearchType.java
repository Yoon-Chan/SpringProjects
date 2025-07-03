package hello.board.domain.contant;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("제목"), ID("유저 ID"), CONTENT("본문"), NICKNAME("닉네임"), HASHTAG("해시태그");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
