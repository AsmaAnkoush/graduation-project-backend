package com.bzu.smartvax.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.bzu.smartvax.web.filter.SpaWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers(
                        mvc.pattern("/index.html"),
                        mvc.pattern("/*.js"),
                        mvc.pattern("/*.txt"),
                        mvc.pattern("/*.json"),
                        mvc.pattern("/*.map"),
                        mvc.pattern("/*.css")
                    )
                    .permitAll()
                    .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/i18n/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/content/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/swagger-ui/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/login"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/register-parent"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/validate-child-info"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/appointments/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/health-workers/by-user/**"))
                    .permitAll()
                    .requestMatchers("/api/schedule-vaccinations", "/api/schedule-vaccinations/**")
                    .permitAll()
                    .requestMatchers("/api/vaccinations", "/api/vaccinations/**")
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/appointments/parent-id/by-user/**"))
                    .permitAll()
                    .requestMatchers("/api/feedbacks", "/api/feedbacks/**")
                    .permitAll()
                    .requestMatchers("/api/parent-account")
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/children/by-parent/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/children/**"))
                    .permitAll()
                    .requestMatchers("/api/vaccination-centers", "/api/vaccination-centers/**")
                    .permitAll()
                    .requestMatchers("/api/vaccine-types", "/api/vaccine-types/**")
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/health"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/health/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/growth-analysis"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/growth-analysis/**"))
                    .permitAll()
                    .requestMatchers("/api/additional-vaccine-child", "/api/additional-vaccine-child/**")
                    .permitAll()
                    .requestMatchers("/api/additional-vaccines", "/api/additional-vaccines/**")
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/info"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/prometheus"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/**"))
                    .authenticated()
                    .requestMatchers(mvc.pattern("/api/health-worker-account"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/child-profile"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/update-child-profile"))
                    .permitAll()
                    .requestMatchers("/api/children/profile/**")
                    .permitAll()

                    .requestMatchers(mvc.pattern("/api/ai/ask-vaccine-bot"))

                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/reminders/by-parent/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/reminders/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/app/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/app/**")).permitAll()
                    .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
