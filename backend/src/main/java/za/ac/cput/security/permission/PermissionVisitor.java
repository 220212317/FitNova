package za.ac.cput.security.permission;
//Lisakhanya Tshokolo 220239215

public interface PermissionVisitor {
    void visit(PermissionGroup group);
    void visit(PermissionLeaf leaf);
}
