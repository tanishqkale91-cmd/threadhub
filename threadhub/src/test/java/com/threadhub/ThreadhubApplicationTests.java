package com.threadhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
})
class ThreadhubApplicationTests {

    @Test
    void contextLoads() {
    }

}
