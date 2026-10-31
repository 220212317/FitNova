package za.ac.cput.security.permission;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
//Lisakhanya Tshokolo 220239215

public class AuthorityCollector implements PermissionVisitor {
        private final AuthorityFlyweightFactory factory;

        private final Map<String, SimpleGrantedAuthority> authorities =
                new LinkedHashMap<>();

        public AuthorityCollector(AuthorityFlyweightFactory factory) {
            this.factory = Objects.requireNonNull(
                    factory, "Factory cannot be null");
        }

        @Override
        public void visit(PermissionGroup group) {
            for (PermissionNode child : group.getChildren()) {
                child.accept(this);
            }
        }

        @Override
        public void visit(PermissionLeaf leaf) {
            String authority = leaf.name();

            SimpleGrantedAuthority grantedAuthority =
                    factory.getAuthority(authority);

            authorities.putIfAbsent(authority, grantedAuthority);
        }

        public Collection<SimpleGrantedAuthority> getAuthorities() {
            return java.util.List.copyOf(authorities.values());
        }

        public void clear() {
            authorities.clear();
        }
    }