package za.ac.cput.security.command;

import org.springframework.stereotype.Component;
//Lisakhanya Tshokolo 220239215

@Component
public class RoleCommandInvoker {
        public void execute(RoleCommand command) {

            if (command == null) {
                throw new IllegalArgumentException("Role command cannot be null.");
            }

            command.execute();
        }
    }