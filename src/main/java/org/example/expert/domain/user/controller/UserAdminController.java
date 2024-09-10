package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 2-9 AOP
// - 어드민 사용자만 접근할 수 있는 특정 API에는 접근할 때마다 접근 로그를 기록 -> org.example.expert.log.AdminAccessLog.java
@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(@PathVariable long userId, @RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
