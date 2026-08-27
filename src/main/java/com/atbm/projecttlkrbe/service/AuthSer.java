package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AuthChangePassReq;
import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.request.AuthResetPassReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.dto.response.UserRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.ResetPass;
import com.atbm.projecttlkrbe.model.Role;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.ResetPassRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthSer {
    private final AuthRep rep;
    private final UserRep userRep;
    private final UserSer userSer;
    private final VerifySer verifySer;
    private final ResetPassRep resetPassRep;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Login with Google
    // Get JSESSIONID => API User for Google
    public OAuth2User loginGoogle(OAuth2User user) {
        return user;
    }

    // Get JSESSIONID => API User for custom
    public UserRes loadUserGoogle(OAuth2User user) {
        OAuth2User o = loginGoogle(user); // API User Google
        String email = o.getAttribute("email");
        String name = o.getAttribute("name");
        String password = "Google_API";

        // Check email exists
        Optional<Auth> authOpt = rep.findByEmail(email);
        Auth auth;
        if (authOpt.isEmpty()) {
            // Save Auth
            Auth newAuth = new Auth();
            newAuth.setEmail(email);
            newAuth.setPassword(passwordEncoder.encode(password));
            newAuth.setCreatedAt(LocalDateTime.now());
            newAuth.setEnabled(true);
            newAuth.setRole(Role.USER);
            newAuth.setGoogle(true);
            auth = rep.save(newAuth);

            // Save User
            User newUser = new User();
            newUser.setAuth(auth);
            newUser.setFullName(name);
            userRep.save(newUser);
        } else {
            auth = authOpt.get();
        }

        User userRes = userRep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));
        return userSer.responseUser(auth, userRes);
    }

    // Reset Password
    public AuthRes resetPass(AuthResetPassReq req) {
        String email = req.getEmail();
        String password = req.getPassword();

        Auth auth = rep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        ResetPass r = resetPassRep.findFirstByAuthIdOrderByIdDesc(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));
        if (r.getExpiredAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Link has expired");

        auth.setPassword(passwordEncoder.encode(password));
        rep.save(auth);

        AuthRes res = new AuthRes();
        res.setEmail(email);
        return res;
    }

    // Sign Up
    public AuthRes signUp(AuthReq authReq) {
        Optional<Auth> authEmail = rep.findByEmail(authReq.getEmail());
        if (authEmail.isPresent()) { // exists
            Auth existingAuth = authEmail.get();

            if (existingAuth.isEnabled()) { // enabled = true
                throw new RuntimeException("Email already exists");
            } else {
                verifySer.saveCode(existingAuth);

                AuthRes res = new AuthRes();
                res.setEmail(existingAuth.getEmail());
                return res;
            }
        }

        // Save Auth
        Auth auth = new Auth();
        auth.setEmail(authReq.getEmail());
        auth.setPassword(passwordEncoder.encode(authReq.getPassword()));
        auth.setCreatedAt(LocalDateTime.now());
        auth.setEnabled(false);
        auth.setRole(Role.USER);
        auth.setGoogle(false);
        Auth saveAuth = rep.save(auth);

        // Save User
        User newUser = new User();
        newUser.setAuth(auth);
        newUser.setFullName(authReq.getName());
        userRep.save(newUser);

        // Save Code
        verifySer.saveCode(saveAuth);

        // Response
        AuthRes res = new AuthRes();
        res.setEmail(saveAuth.getEmail());
        return res;
    }

    // Login
    public UserRes loginWithSession(AuthReq authReq, HttpServletRequest request) {
        String email = authReq.getEmail();
        String password = authReq.getPassword();

        Auth auth = rep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        if (!auth.isEnabled()) throw new RuntimeException("Auth is not enabled");

        // Set up auth context
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // JSESSIONID
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        User user = userRep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));

        // Success
        return userSer.responseUser(auth, user);
    }

    public Authentication loadUserLogin(Authentication authentication) {
        return authentication;
    }
}