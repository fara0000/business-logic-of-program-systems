package backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;

import static java.lang.String.format;

public enum Role implements GrantedAuthority  {
    USER, // 0
    ADMIN; // 1

    @Override
    public String getAuthority() {
        return name();
    }
}
