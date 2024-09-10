package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    // 레벨 2-5 테스트 연습
    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        // 인자 순서가 이상. 1원래 비밀번호와 2암호화된 비밀번호 비교
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
    }
}
