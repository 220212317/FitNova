/*
 * AdminStaffController.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.RoleType;

import java.util.Map;

@RestController
@RequestMapping("/admin/staff")
public class AdminStaffController {

    private final RegistrationService registrationService;

    public AdminStaffController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody RegisterRequest request,
                                                      @RequestParam RoleType role) {
        User created = registrationService.registerStaff(request, role);
        return new ResponseEntity<>(AuthController.summary(created), HttpStatus.CREATED);
    }
}
