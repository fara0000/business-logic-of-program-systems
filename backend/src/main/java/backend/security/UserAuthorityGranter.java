package backend.security;

import backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
public class UserAuthorityGranter implements AuthorityGranter {

    private final UserRepository userRepository;

    @Override
    public Set<String> grant(Principal principal) {
        String role = String.valueOf(userRepository.findRoleByEmail(principal.getName()));
        return Collections.singleton(role);
    }
}
