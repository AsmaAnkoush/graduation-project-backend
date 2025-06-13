package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
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

        try {
            Users user = usersRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println(":--------------------------------------");
            System.out.println("🔵 محاولة تسجيل الدخول:");
            System.out.println("👤 Username: " + username);
            System.out.println("🔑 Raw Password (input): " + password);
            System.out.println(":--------------------------------------");
            System.out.println("user.getusername  : " + user.getUsername());
            System.out.println("user.getPassword() : " + user.getPassword());
            System.out.println(":--------------------------------------");
            System.out.println(":::::" + passwordEncoder.matches(password, user.getPassword()));
            System.out.println(":encode entered pass::::" + passwordEncoder.encode(password));

            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null,
                    List.of(new SimpleGrantedAuthority(user.getRole()))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return ResponseEntity.ok(
                    Map.of(
                        "id",
                        user.getId(), // أضف هذا السطر
                        "username",
                        user.getUsername(),
                        "role",
                        user.getRole(),
                        "referenceId",
                        user.getReferenceId()
                    )
                );
            } else {
                return ResponseEntity.status(401).body("اسم المستخدم أو كلمة المرور غير صحيحة");
            }
        } catch (RuntimeException e) {
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

        System.out.println("🔐 Authenticated role: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return ResponseEntity.ok(Map.of("username", user.getUsername(), "role", user.getRole()));
    }
}
