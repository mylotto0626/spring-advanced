package org.example.expert.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// 2-9 AOP
// - 어드민 사용자만 접근할 수 있는 특정 API에는 접근할 때마다 접근 로그를 기록 -> org.example.expert.log.AdminAccessLog.java
@RestController
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @DeleteMapping("/admin/comments/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        commentAdminService.deleteComment(commentId);
    }
}
