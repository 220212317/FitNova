package za.ac.cput.security.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.RoleType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//Lisakhanya Tshokolo 220239215

/**
 * Turns a user's roles into Spring authorities: the role itself (ROLE_ADMIN) plus every
 * permission that role carries in {@link RolePermissionTree} (user:manage, slot:manage, ...).
 * SecurityConfig relies on those permission authorities.
 */
@Component
public class RoleAuthorityResolver {

    private final RolePermissionTree permissionTree;
    private final AuthorityFlyweightFactory flyweightFactory;

    @Autowired
    public RoleAuthorityResolver(RolePermissionTree permissionTree,
                                 AuthorityFlyweightFactory flyweightFactory) {
        this.permissionTree = permissionTree;
        this.flyweightFactory = flyweightFactory;
    }

    public RoleAuthorityResolver() {
        this(new RolePermissionTree(), new AuthorityFlyweightFactory());
    }

    public Collection<SimpleGrantedAuthority> resolve(List<RoleType> roles) {

        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        Map<String, SimpleGrantedAuthority> authorities = new LinkedHashMap<>();

        for (RoleType role : roles) {
            if (role == null) {
                continue;
            }
            authorities.putIfAbsent("ROLE_" + role.name(),
                    flyweightFactory.getAuthority("ROLE_" + role.name()));

            PermissionGroup permissions = permissionTree.getPermissions(role);
            if (permissions != null) {
                AuthorityCollector collector = new AuthorityCollector(flyweightFactory);
                permissions.accept(collector);
                for (SimpleGrantedAuthority authority : collector.getAuthorities()) {
                    authorities.putIfAbsent(authority.getAuthority(), authority);
                }
            }
        }

        return List.copyOf(authorities.values());
    }
}
