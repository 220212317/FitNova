package za.ac.cput.security.access;
//Lisakhanya Tshokolo 220239215

@FunctionalInterface
public interface AccessExpression {
    boolean allows(AccessContext accessContext, String resourceOwnerId);
}
