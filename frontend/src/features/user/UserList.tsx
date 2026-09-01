/** Collins Shibambo (230093183) */
import type { User } from "../../types";

interface UserListProps {
    users: User[];
    onDelete: (user: User) => void;
    onAssignRole: (user: User) => void;
}

export default function UserList({ users, onDelete, onAssignRole }: UserListProps) {
    if (users.length === 0) {
        return <p>No users found.</p>;
    }

    return (
        <table>
            <thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Date of Birth</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            {users.map((user) => (
                <tr key={user.userId}>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.dateOfBirth}</td>
                    <td>
                        <button type="button" onClick={() => onAssignRole(user)}>
                            Assign Role
                        </button>
                    </td>
                    <td>
                        <button type="button" onClick={() => onDelete(user)}>
                            Delete
                        </button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}