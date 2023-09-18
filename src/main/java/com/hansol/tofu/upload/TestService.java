package com.hansol.tofu.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    public String president(Long clubId) {
        return "president";
    }

    public String manager(Long clubId) {
        return "manager";
    }

    public String member(Long clubId) {
        return "member";
    }

    public String others() {
        return "others";
    }
}
