package com.hansol.tofu.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    public String president() {
        return "president";
    }

    public String manager() {
        return "manager";
    }

    public String member() {
        return "member";
    }

    public String others() {
        return "others";
    }
}
