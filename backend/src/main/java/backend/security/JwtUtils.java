package backend.security;

import backend.entities.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final String JWT_SECRET = "VorovatPloho";
    private final int JWT_EXPIRATION_MS = 100000000;

    public String generateJwtToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getEmail())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS)).signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
//            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
//            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
//            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
//            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
//            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
