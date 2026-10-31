package za.ac.cput.security.permission;
//Lisakhanya Tshokolo 220239215

public interface PermissionNode {
    void accept(PermissionVisitor visitor);
}
