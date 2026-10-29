package za.ac.cput.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService extends TokenService {

    private final Duration lifetime;

    public RefreshTokenService(TokenCodec codec,
                               @Value("${fitnova.security.jwt.refresh-days:7}") long days) {
        super(codec);
        if (days <= 0) {
            throw new IllegalArgumentException("refresh-days must be positive");
        }
        this.lifetime = Duration.ofDays(days);
    }

    @Override
    protected String type() {
        return "refresh";
    }

    @Override
    protected Duration lifetime() {
        return lifetime;
    }
}