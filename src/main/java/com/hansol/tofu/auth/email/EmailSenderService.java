package com.hansol.tofu.auth.email;

import com.hansol.tofu.auth.email.dto.EmailRequestDTO;

public interface EmailSenderService {

    void sendEmail(EmailRequestDTO emailRequestDTO);
}
