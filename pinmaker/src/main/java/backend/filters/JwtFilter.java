package backend.filters;
//
import backend.entities.User;
import backend.repositories.UserRepository;
import backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION = "Authorization";
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getTokenFromRequest(request);
        if (jwt != null && jwtUtil.validateToken(jwt)) {
            User user = userRepository.findUserByEmail(jwtUtil.getWordForToken(jwt));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                    null,  AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_".concat(
                    user.getRole().name())));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

//    private String parseJwt(HttpServletRequest request) {
//        String headerName = "Authorization";
//        String prefix = "Bearer ";
//        String headerAuth = request.getHeader(headerName);
//
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(prefix)) {
//            return headerAuth.substring(prefix.length());
//        }
////        log.debug("Invalid header name or auth prefix for request to {}", request.getRequestURI());
//        return null;
//    }
}
