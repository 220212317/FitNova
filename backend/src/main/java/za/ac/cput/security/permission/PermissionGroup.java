package za.ac.cput.security.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
//Lisakhanya Tshokolo 220239215

public final class PermissionGroup implements PermissionNode {
    private final String name;
    private final List<PermissionNode>  children = new ArrayList<>();
    public PermissionGroup(String name) {
        if(name==null || name.isBlank()){
            throw new IllegalArgumentException("Group name cannot be null or blank");
        }
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void add(PermissionNode node) {
        children.add(Objects.requireNonNull(node,"Permission node cannot be null"));
    }

    public boolean remove(PermissionNode node) {
        return children.remove(node);
    }

    public List<PermissionNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void accept(PermissionVisitor visitor) {
        Objects.requireNonNull(visitor, "Visitor cannot be null").visit(this);
    }

}
