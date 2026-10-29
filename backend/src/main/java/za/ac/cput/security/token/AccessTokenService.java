package za.ac.cput.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AccessTokenService extends TokenService {

    private final Duration lifetime;

    public AccessTokenService(TokenCodec codec,
                              @Value("${fitnova.security.jwt.access-minutes:15}") long minutes) {
        super(codec);
        if (minutes <= 0) {
            throw new IllegalArgumentException("access-minutes must be positive");
        }
        this.lifetime = Duration.ofMinutes(minutes);
    }

    @Override
    protected String type() {
        return "access";
    }

    @Override
    protected Duration lifetime() {
        return lifetime;
    }
}