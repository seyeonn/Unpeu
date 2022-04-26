package com.unpeu.domain.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.unpeu.domain.entity.Board;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ApiModel("BoardGetRes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardGetRes {
    private Long userId;
    private Long boardId;
    private String title;
    private String content;
    private String category;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화 오류 해결
    private LocalDateTime createdAt;

//    private List<Comment> commentList;

    public BoardGetRes(Board board) {
        this.userId = board.getUser().getId();
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.createdAt = board.getCreatedAt();
    }

}
