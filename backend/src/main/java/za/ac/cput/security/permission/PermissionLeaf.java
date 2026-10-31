package za.ac.cput.security.permission;
//Lisakhanya Tshokolo 220239215

public record PermissionLeaf(String name) implements PermissionNode{
    public PermissionLeaf {
        if(name== null || name.isBlank()){
            throw new IllegalArgumentException("Permission name cannot be null or blank");
        }
    }
    @Override
    public void accept(PermissionVisitor visitor) {
        if(visitor == null){
            throw new IllegalArgumentException("Permission Visitor cannot be null");
        }
        visitor.visit(this);
    }
}
