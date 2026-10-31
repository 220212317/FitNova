package za.ac.cput.security.permission;

import org.springframework.stereotype.Component;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
//Lisakhanya Tshokolo 220239215

@Component
public class AuthorityFlyweightFactory {

        private final ConcurrentMap<String, SimpleGrantedAuthority> cache =
                new ConcurrentHashMap<>();

        public SimpleGrantedAuthority getAuthority(String authority) {
            if (authority == null || authority.isBlank()) {
                throw new IllegalArgumentException(
                        "Authority cannot be null or blank");
            }

            return cache.computeIfAbsent(
                    authority,
                    SimpleGrantedAuthority::new);
        }

        public int getCachedAuthorityCount() {
            return cache.size();
        }
    }
