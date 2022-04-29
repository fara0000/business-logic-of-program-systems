package backend.config;

import backend.filters.JwtFilter;
import backend.models.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtFilter jwtFilter;

    private final AbstractJaasAuthenticationProvider jaasAuthenticationProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jaasAuthenticationProvider);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //enable CORS and disable CRSF
        httpSecurity.cors().configurationSource(corsConfigurationSource()).and().csrf().disable();
        // Set session management to stateless
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        // Set unauthorized requests exception handler
        httpSecurity.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );
                }
        )
                .and();
        // Set permissions on endpoints
        httpSecurity.authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers(HttpMethod.GET, "/").not().fullyAuthenticated()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers( "/pin-builder/**").hasRole(Role.USER.name())
                .antMatchers(ADMIN_ACCESS).hasRole(Role.ADMIN.name())
                //Доступ только для авторизованных пользователей
                .anyRequest().authenticated();
        httpSecurity.headers().frameOptions().sameOrigin();
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private static final String[] AUTH_WHITELIST = {
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
    };

    private static final String[] ADMIN_ACCESS = {
        "/admin/**"
    };

    private static final String[] USER_ACCESS = {
       "/pin-builder/**",
       "/pin-builder/find-board/**"
    };

    /**
     * Конфигарация CORS.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.setAlwaysUseFullPath(true);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
