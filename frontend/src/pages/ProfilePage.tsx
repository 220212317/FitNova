
import { useEffect, useState, type FormEvent } from "react";
import { api } from "../api/client";
import { useAuth } from "../auth/AuthContext";
import "./ProfilePage.css";

interface UserProfile {
    userId: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string | null;
    [key: string]: unknown;
}

interface ProfileForm {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
}

export default function ProfilePage() {
    const { user } = useAuth();

    const [profile, setProfile] = useState<UserProfile | null>(null);

    const [formData, setFormData] = useState<ProfileForm>({
        firstName: "",
        lastName: "",
        dateOfBirth: "",
    });

    const [isEditing, setIsEditing] = useState(false);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    // Load the logged-in user's profile.
    useEffect(() => {
        const loadProfile = async () => {
            if (!user?.userId) {
                setError("You must be logged in to view your profile.");
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                setError("");

                const response = await api.get<UserProfile>(
                    `/user/read/${encodeURIComponent(user.userId)}`
                );

                const data = response.data;

                // Confirm that the returned profile belongs to this user.
                if (String(data.userId) !== String(user.userId)) {
                    throw new Error("You cannot access another user's profile.");
                }

                setProfile(data);

                setFormData({
                    firstName: data.firstName ?? "",
                    lastName: data.lastName ?? "",
                    dateOfBirth: data.dateOfBirth ?? "",
                });

            } catch (err: any) {
                setError(
                    err.response?.data?.message ||
                    err.message ||
                    "Unable to load your profile."
                );
            } finally {
                setLoading(false);
            }
        };

        loadProfile();
    }, [user?.userId]);

    // Update form values.
    const handleChange = (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value,
        }));
    };

    // Validate form information.
    const validateForm = (): string => {
        if (!formData.firstName.trim()) {
            return "First name is required.";
        }

        if (!formData.lastName.trim()) {
            return "Last name is required.";
        }

        if (formData.firstName.trim().length > 50) {
            return "First name cannot exceed 50 characters.";
        }

        if (formData.lastName.trim().length > 50) {
            return "Last name cannot exceed 50 characters.";
        }

        if (!/^[\p{L}\p{M}\s'-]+$/u.test(formData.firstName.trim())) {
            return "First name contains invalid characters.";
        }

        if (!/^[\p{L}\p{M}\s'-]+$/u.test(formData.lastName.trim())) {
            return "Last name contains invalid characters.";
        }

        if (formData.dateOfBirth) {
            const date = new Date(
                `${formData.dateOfBirth}T00:00:00`
            );

            if (Number.isNaN(date.getTime())) {
                return "Please enter a valid date of birth.";
            }

            if (date > new Date()) {
                return "Date of birth cannot be in the future.";
            }
        }

        return "";
    };

    // Save profile changes.
    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!profile || !user) {
            return;
        }

        setError("");
        setSuccess("");

        const validationError = validateForm();

        if (validationError) {
            setError(validationError);
            return;
        }

        try {
            setSaving(true);

            // Preserve the original ID and related entity data.
            // Only the permitted personal fields are changed.
            const updatedProfile = {
                ...profile,
                userId: profile.userId,
                firstName: formData.firstName.trim(),
                lastName: formData.lastName.trim(),
                dateOfBirth: formData.dateOfBirth || null,
            };

            const response = await api.put<UserProfile>(
                "/user/update",
                updatedProfile
            );

            const data = response.data;

            if (String(data.userId) !== String(user.userId)) {
                throw new Error("The returned profile ID does not match your account.");
            }

            setProfile(data);

            setFormData({
                firstName: data.firstName ?? "",
                lastName: data.lastName ?? "",
                dateOfBirth: data.dateOfBirth ?? "",
            });

            setIsEditing(false);
            setSuccess("Your profile has been updated successfully.");

        } catch (err: any) {
            setError(
                err.response?.data?.message ||
                err.message ||
                "Unable to save your profile. Please try again."
            );
        } finally {
            setSaving(false);
        }
    };

    // Cancel editing and restore original values.
    const handleCancel = () => {
        if (!profile) return;

        setFormData({
            firstName: profile.firstName ?? "",
            lastName: profile.lastName ?? "",
            dateOfBirth: profile.dateOfBirth ?? "",
        });

        setIsEditing(false);
        setError("");
        setSuccess("");
    };

    if (loading) {
        return (
            <div className="profile-loading">
                Loading your profile...
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="profile-message error-message">
                {error || "Profile information could not be found."}
            </div>
        );
    }

    return (
        <div className="profile-page">

            {/* Page heading */}
            <div className="profile-header">
                <div>
                    <h2>My Profile</h2>
                    <p>View and manage your personal information.</p>
                </div>

                {!isEditing && (
                    <button
                        className="edit-profile-btn"
                        onClick={() => {
                            setIsEditing(true);
                            setError("");
                            setSuccess("");
                        }}
                    >
                        Edit Profile
                    </button>
                )}
            </div>

            {/* Success message */}
            {success && (
                <div className="profile-message success-message">
                    {success}
                </div>
            )}

            {/* Error message */}
            {error && (
                <div className="profile-message error-message">
                    {error}
                </div>
            )}

            {/* Profile card */}
            <div className="profile-card">

                <div className="profile-card-header">
                    <div className="profile-avatar">
                        {profile.firstName.charAt(0).toUpperCase()}
                    </div>

                    <div>
                        <h3>
                            {profile.firstName} {profile.lastName}
                        </h3>
                        <p>{user?.email}</p>
                        <span className="profile-role">
                            {user?.role}
                        </span>
                    </div>
                </div>

                <form onSubmit={handleSubmit}>

                    <div className="profile-section">
                        <h3>Personal Information</h3>

                        <div className="profile-form-grid">

                            <div className="profile-field">
                                <label htmlFor="firstName">
                                    First Name
                                </label>

                                <input
                                    id="firstName"
                                    type="text"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    disabled={!isEditing || saving}
                                    maxLength={50}
                                    required
                                />
                            </div>

                            <div className="profile-field">
                                <label htmlFor="lastName">
                                    Last Name
                                </label>

                                <input
                                    id="lastName"
                                    type="text"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    disabled={!isEditing || saving}
                                    maxLength={50}
                                    required
                                />
                            </div>

                            <div className="profile-field">
                                <label htmlFor="email">
                                    Email Address
                                </label>

                                <input
                                    id="email"
                                    type="email"
                                    value={user?.email ?? ""}
                                    disabled
                                />

                                <small>Email is managed through your account.</small>
                            </div>

                            <div className="profile-field">
                                <label htmlFor="dateOfBirth">
                                    Date of Birth
                                </label>

                                <input
                                    id="dateOfBirth"
                                    type="date"
                                    name="dateOfBirth"
                                    value={formData.dateOfBirth}
                                    onChange={handleChange}
                                    disabled={!isEditing || saving}
                                    max={new Date().toISOString().split("T")[0]}
                                />
                            </div>

                            <div className="profile-field">
                                <label htmlFor="userId">
                                    User ID
                                </label>

                                <input
                                    id="userId"
                                    type="text"
                                    value={profile.userId}
                                    disabled
                                />

                                <small>Your user ID cannot be changed.</small>
                            </div>

                        </div>
                    </div>

                    {/* Form actions */}
                    {isEditing && (
                        <div className="profile-actions">

                            <button
                                type="button"
                                className="cancel-profile-btn"
                                onClick={handleCancel}
                                disabled={saving}
                            >
                                Cancel
                            </button>

                            <button
                                type="submit"
                                className="save-profile-btn"
                                disabled={saving}
                            >
                                {saving ? "Saving..." : "Save Changes"}
                            </button>

                        </div>
                    )}

                </form>
            </div>
        </div>
    );
}
