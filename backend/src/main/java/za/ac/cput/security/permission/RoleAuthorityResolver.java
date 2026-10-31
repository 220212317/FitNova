package za.ac.cput.security.permission;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.RoleType;

import java.util.Collection;
import java.util.List;
//Lisakhanya Tshokolo 220239215

@Component
public class RoleAuthorityResolver {
    public Collection<SimpleGrantedAuthority> resolve(List<RoleType> roles) {

        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        return roles.stream()
                .filter(role -> role != null)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }
}
