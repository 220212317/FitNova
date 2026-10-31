package za.ac.cput.security.permission;

import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.RoleType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

//Lisakhanya Tshokolo 220239215

@Component
public class RolePermissionTree {

        private final Map<RoleType, PermissionGroup> rolePermissions =
                new EnumMap<>(RoleType.class);

        public RolePermissionTree() {
            buildPermissionTree();
        }

        private void buildPermissionTree() {

            PermissionGroup memberPermissions =
                    new PermissionGroup("MEMBER_PERMISSIONS");

            memberPermissions.add(new PermissionLeaf("member:read"));
            memberPermissions.add(new PermissionLeaf("booking:read"));
            memberPermissions.add(new PermissionLeaf("booking:create"));
            memberPermissions.add(new PermissionLeaf("profile:read"));

            PermissionGroup trainerPermissions =
                    new PermissionGroup("TRAINER_PERMISSIONS");

            trainerPermissions.add(new PermissionLeaf("member:read"));
            trainerPermissions.add(new PermissionLeaf("booking:read"));
            trainerPermissions.add(new PermissionLeaf("slot:manage"));
            trainerPermissions.add(new PermissionLeaf("profile:read"));

            PermissionGroup adminPermissions =
                    new PermissionGroup("ADMIN_PERMISSIONS");

            adminPermissions.add(new PermissionLeaf("member:read"));
            adminPermissions.add(new PermissionLeaf("booking:read"));
            adminPermissions.add(new PermissionLeaf("booking:create"));
            adminPermissions.add(new PermissionLeaf("profile:read"));
            adminPermissions.add(new PermissionLeaf("slot:manage"));
            adminPermissions.add(new PermissionLeaf("lookup:manage"));
            adminPermissions.add(new PermissionLeaf("user:manage"));

            rolePermissions.put(RoleType.MEMBER, memberPermissions);
            rolePermissions.put(RoleType.TRAINER, trainerPermissions);
            rolePermissions.put(RoleType.ADMIN, adminPermissions);
        }

        public PermissionGroup getPermissions(RoleType role) {
            return rolePermissions.get(role);
        }

        public Map<RoleType, PermissionGroup> getAllPermissions() {
            return Collections.unmodifiableMap(rolePermissions);
        }
    }