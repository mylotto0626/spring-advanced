package org.example.expert.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

// 2-9 AOP
// - 어드민 사용자만 접근할 수 있는 특정 API에는 접근할 때마다 접근 로그를 기록 -> org.example.expert.log.AdminAccessLog.java
// @Aspect로 선언하여 AOP 동작을 설정
@Aspect
@Component
@Slf4j
public class AdminAccessLog {
    // @Before 어노테이션을 사용해 접근 로그를 기록
    @Before("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void logAdminAccess(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String userId = request.getAttribute("userId").toString();
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        log.info("Admin access log: User ID = {}, Request URL = {}, Request Time = {}", userId, requestUrl, requestTime);
    }
}
