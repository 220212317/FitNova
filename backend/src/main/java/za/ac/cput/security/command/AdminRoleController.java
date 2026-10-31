package za.ac.cput.security.command;
import jakarta.persistence.EntityManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.service.IUserRoleService;
import org.springframework.web.bind.annotation.*;

//Lisakhanya Tshokolo 220239215

@RestController
@RequestMapping("/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {
        private final IUserRoleService userRoleService;
        private final EntityManager entityManager;
        private final RoleCommandInvoker commandInvoker;

        public AdminRoleController(
                IUserRoleService userRoleService,
                EntityManager entityManager,
                RoleCommandInvoker commandInvoker
        ) {
            this.userRoleService = userRoleService;
            this.entityManager = entityManager;
            this.commandInvoker = commandInvoker;
        }

        /**
         * Request body for role assignment and revocation.
         */
        public record RoleRequest(RoleType role) {
        }

        /**
         * Assigns a role to a user.
         *
         * POST /admin/roles/{userId}/assign
         */
        @PostMapping("/{userId}/assign")
        public ResponseEntity<String> assignRole(
                @PathVariable String userId,
                @RequestBody RoleRequest request
        ) {

            if (request == null || request.role() == null) {
                return ResponseEntity.badRequest()
                        .body("A valid role is required.");
            }

            RoleCommand command = new AssignRoleCommand(
                    userRoleService,
                    entityManager,
                    userId,
                    request.role()
            );

            commandInvoker.execute(command);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Role " + request.role()
                            + " successfully assigned to user " + userId);
        }

        /**
         * Revokes a role from a user.
         *
         * DELETE /admin/roles/{userId}/revoke
         */
        @DeleteMapping("/{userId}/revoke")
        public ResponseEntity<String> revokeRole(
                @PathVariable String userId,
                @RequestBody RoleRequest request
        ) {

            if (request == null || request.role() == null) {
                return ResponseEntity.badRequest()
                        .body("A valid role is required.");
            }

            RoleCommand command = new RevokeRoleCommand(
                    userRoleService,
                    userId,
                    request.role()
            );

            commandInvoker.execute(command);

            return ResponseEntity.ok(
                    "Role " + request.role()
                            + " successfully revoked from user " + userId
            );
        }
    }