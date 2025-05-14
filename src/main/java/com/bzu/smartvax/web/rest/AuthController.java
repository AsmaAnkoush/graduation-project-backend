package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<Users> userOpt = usersRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            Users user = userOpt.get();

            // ✅ حفظ المستخدم في الجلسة
            session.setAttribute("user", user);

            // ✅ إعداد صلاحية المستخدم كما هي من جدول users (مثل: "PARENT")
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole())) // لا تضيف ROLE_
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(Map.of("username", user.getUsername(), "role", user.getRole()));
        } else {
            return ResponseEntity.status(401).body("اسم المستخدم أو كلمة المرور غير صحيحة");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("تم تسجيل الخروج");
    }

    @GetMapping("/session-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("لا يوجد مستخدم في الجلسة");
        }

        // 🔍 للتأكد من وجود الصلاحية في السيكيورتي كونتكست
        System.out.println("🔐 Authenticated role: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return ResponseEntity.ok(Map.of("username", user.getUsername(), "role", user.getRole()));
    }
}
