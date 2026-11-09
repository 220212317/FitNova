package za.ac.cput.security.directory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IUserRoleRepository;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainers")
public class TrainerDirectoryController {
    private final IUserRoleRepository userRoles;

    public TrainerDirectoryController(IUserRoleRepository userRoles) {
        this.userRoles = userRoles;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public List<TrainerSummary> getAll() {
        Map<String, User> uniqueTrainers = new LinkedHashMap<>();
        userRoles.findByRoleId(RoleType.TRAINER).forEach(role -> {
            User user = role.getUser();
            if (user != null && user.getUserId() != null) uniqueTrainers.putIfAbsent(user.getUserId(), user);
        });

        return uniqueTrainers.values().stream()
                .map(TrainerSummary::from)
                .sorted(Comparator.comparing(TrainerSummary::lastName,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(TrainerSummary::firstName,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
    }
}
