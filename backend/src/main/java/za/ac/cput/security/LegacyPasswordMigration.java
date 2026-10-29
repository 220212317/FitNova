package za.ac.cput.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.Account;
import za.ac.cput.repository.IAccountRepository;

@Component
public class LegacyPasswordMigration {

    private final IAccountRepository accounts;
    private final PasswordEncoder encoder;

    public LegacyPasswordMigration(IAccountRepository accounts, PasswordEncoder encoder) {
        this.accounts = accounts;
        this.encoder = encoder;
    }

    /**
     * Migrates all accounts that still have a plaintext password.
     * @return the number of accounts that were updated
     */
    public int migrate() {
        int count = 0;
        for (Account a : accounts.findAll()) {
            String pw = a.getPassword();
            if (pw == null || pw.isBlank()) {
                continue;                       // skip missing / blank
            }
            if (isBcrypt(pw)) {
                continue;                       // already hashed
            }
            // plaintext → re-hash and save
            accounts.save(new Account.Builder()
                    .copy(a)
                    .setPassword(encoder.encode(pw))
                    .build());
            count++;
        }
        return count;
    }

    /**
     * Returns true only for a real BCrypt hash ($2a$ / $2b$ / $2y$ + 60 characters).
     */
    public static boolean isBcrypt(String value) {
        if (value == null) return false;
        return (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"))
                && value.length() == 60;
    }
}