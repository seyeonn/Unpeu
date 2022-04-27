package com.unpeu.domain.entity;


import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "카테고리를 입력해주세요")
    private String category;

    @NotNull(message = "제목을 입력해주세요")
    @Column(length = 50)
    private String title;

    @NotNull(message = "내용을 입력해주세요")
    private String content;

    @NotNull
    private String createdAt;

    // 게시글을 삭제하면 달려있는 댓글 모두 삭제
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @Builder
    public Board(User user, String category, String title, String content) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
    }

    @PrePersist
    public void onPrePersist(){
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ==== 비즈니스 로직 ====
    public void updateBoardInfo(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

}
