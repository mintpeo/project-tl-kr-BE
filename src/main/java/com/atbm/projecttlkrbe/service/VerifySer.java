package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.VerifyEmailReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.Verify;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.VerifyRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VerifySer {
    private final VerifyRep rep;
    private final MailSer mailSer;
    private final AuthRep authRep;
    private final PasswordEncoder passwordEncoder;

    // Verify Code
    public AuthRes verifyCode(VerifyEmailReq req) {
        Auth auth = authRep.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("Not found email: " + req.getEmail()));
        Verify verify = rep.findFirstByAuthIdOrderByIdDesc(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));

        if (verify.isStatus()) throw new RuntimeException("Code has already been used");
        if (verify.getExpiredAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Code has expired");
        if (!passwordEncoder.matches(req.getCode(), verify.getCode())) throw new RuntimeException("Wrong code");

        verify.setStatus(true);
        rep.save(verify);

        auth.setEnabled(true);
        authRep.save(auth);

        AuthRes res = new AuthRes();
        res.setEmail(auth.getEmail());
        return res;
    }

    // Save code
    public void saveCode(Auth auth) {
        int code = ThreadLocalRandom.current().nextInt(1000, 10000);

        Verify verify = new Verify();
        verify.setAuth(auth);
        verify.setCode(passwordEncoder.encode(String.valueOf(code)));
        verify.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        verify.setStatus(false);

        rep.save(verify);
        sendVerifyMail(auth.getEmail(), code);
    }

    // Send Verify Email
    private void sendVerifyMail(String email, int code) {
        String subject = "Verify Mail";
        String text = "Your verification code: " + code;
        mailSer.sendMail(email, subject, text);
    }
}
