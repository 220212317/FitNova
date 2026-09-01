/** Collins Shibambo (230093183) */
import { useState } from "react";
import type { Account, Address, Demographic } from "../../types";
import { ApiError } from "../../api/client";
import { ensureId } from "../../api/ids";
import { createUser } from "./userApi";
import AccountFields from "../account/AccountFields";
import AddressFields from "../account/AddressFields";
import ContactFields from "../contact/ContactFields";
import NextOfKinFields from "../contact/NextOfKinFields";
import DemographicFields from "../lookups/DemographicFields";
import {
    emptyContactValues,
    emptyNextOfKinValues,
    type ContactValues,
    type NextOfKinValues,
} from "../contact/contactTypes";

interface UserFormProps {
    onCreated?: () => void;
    onCancel: () => void;
}

export default function UserForm({ onCreated, onCancel }: UserFormProps) {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [account, setAccount] = useState<Account | null>(null);
    const [address, setAddress] = useState<Partial<Address> | null>(null);
    const [contactValues, setContactValues] = useState<ContactValues>(emptyContactValues);
    const [nextOfKinValues, setNextOfKinValues] = useState<NextOfKinValues>(emptyNextOfKinValues);
    const [genderId, setGenderId] = useState<string | null>(null);
    const [raceId, setRaceId] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);
    const accountReady = Boolean(account?.accountId);

    function hasNextOfKin(values: NextOfKinValues): boolean {
        return Boolean(
            values.firstName.trim() ||
            values.lastName.trim() ||
            values.relationship.trim() ||
            values.cellphoneNumber.trim()
        );
    }

    async function handleSubmit() {
        setError(null);
        setSuccess(false);

        if (!accountReady || !account) {
            setError("Create the account before submitting.");
            return;
        }
        if (!firstName.trim() || !lastName.trim()) {
            setError("First and last name are required.");
            return;
        }
        if (!address?.streetName?.trim() || !address?.city?.trim() || !address?.country?.trim()) {
            setError("Street name, city, and country are required.");
            return;
        }
        if (!contactValues.cellphoneNumber.trim() || !contactValues.emailAddress.trim()) {
            setError("Contact cellphone number and email are required.");
            return;
        }
        if (!genderId || !raceId) {
            setError("Select a gender and race.");
            return;
        }

        setSubmitting(true);

        try {
            const demographic: Demographic = {
                demographyId: ensureId(),
                gender: { genderId, description: "" },
                race: { raceId, description: "" },
            };

            const fullAddress: Address = {
                addressId: ensureId(address.addressId),
                streetNumber: address.streetNumber,
                streetName: address.streetName,
                suburbName: address.suburbName,
                city: address.city,
                postalCode: address.postalCode,
                province: address.province,
                country: address.country,
            };

            const contact = {
                contactId: ensureId(),
                cellphoneNumber: contactValues.cellphoneNumber,
                alternativeCellphoneNumber: contactValues.alternativeCellphoneNumber || undefined,
                emailAddress: contactValues.emailAddress,
            };

            const nextOfKinContacts = hasNextOfKin(nextOfKinValues)
                ? [
                    {
                        nextOfKinContactId: ensureId(),
                        firstName: nextOfKinValues.firstName,
                        lastName: nextOfKinValues.lastName,
                        relationship: nextOfKinValues.relationship,
                        cellphoneNumber: nextOfKinValues.cellphoneNumber,
                    },
                ]
                : [];

            await createUser({
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                dateOfBirth: dateOfBirth || undefined,
                account,
                demographic,
                address: fullAddress,
                contact,
                nextOfKinContacts,
            });

            setSuccess(true);
            setFirstName("");
            setLastName("");
            setDateOfBirth("");
            setAccount(null);
            setAddress(null);
            setContactValues(emptyContactValues);
            setNextOfKinValues(emptyNextOfKinValues);
            setGenderId(null);
            setRaceId(null);
            onCreated?.();
        } catch (err) {
            setError(err instanceof ApiError ? err.message : "Failed to create user.");
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <div>
            <h2>Create User</h2>

            {error && (
                <p role="alert" style={{ color: "red" }}>
                    {error}
                </p>
            )}
            {success && <p style={{ color: "green" }}>User created.</p>}

            <AccountFields account={account} onAccountChange={setAccount} />

            <fieldset disabled={!accountReady}>
                <legend>Personal Details</legend>

                <label htmlFor="user-first-name">First Name *</label>
                <input
                    id="user-first-name"
                    type="text"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                />

                <label htmlFor="user-last-name">Last Name *</label>
                <input
                    id="user-last-name"
                    type="text"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                />

                <label htmlFor="user-dob">Date of Birth</label>
                <input
                    id="user-dob"
                    type="date"
                    value={dateOfBirth}
                    onChange={(e) => setDateOfBirth(e.target.value)}
                />
            </fieldset>

            <AddressFields
                address={address}
                onAddressChange={setAddress}
                disabled={!accountReady}
            />

            <ContactFields
                values={contactValues}
                onChange={setContactValues}
                disabled={!accountReady}
            />

            <NextOfKinFields
                values={nextOfKinValues}
                onChange={setNextOfKinValues}
                disabled={!accountReady}
            />

            <DemographicFields
                genderId={genderId}
                raceId={raceId}
                onGenderIdChange={setGenderId}
                onRaceIdChange={setRaceId}
                disabled={!accountReady}
            />

            <div>
                <button type="button" onClick={onCancel} disabled={submitting}>
                    Cancel
                </button>
                <button type="button" onClick={handleSubmit} disabled={!accountReady || submitting}>
                    {submitting ? "Creating..." : "Create User"}
                </button>
            </div>
        </div>
    );
}