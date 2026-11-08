/**Athi Sintiya (220212317) */
export type RoleType = "MEMBER" | "TRAINER" | "ADMIN";
export type BookingStatus = "CONFIRMED" | "CANCELLED" | "COMPLETED";
export type SlotStatus = "AVAILABLE" | "BOOKED" | "UNAVAILABLE";

export interface Account {
    accountId: string;
    email: string;
    password?: string;
    registrationDate?: string;
}

export interface Address {
    addressId: string;
    streetNumber?: string;
    streetName: string;
    suburbName?: string;
    city: string;
    postalCode?: string;
    province?: string;
    country: string;
}

export interface Contact {
    contactId: string;
    cellphoneNumber?: string;
    alternativeCellphoneNumber?: string;
    emailAddress?: string;
}

export interface Gender {
    genderId: string;
    description: string;
}

export interface Race {
    raceId: string;
    description: string;
}

export interface Demographic {
    demographyId: string;
    gender?: Gender;
    race?: Race;
}

export interface NextOfKinContact {
    nextOfKinContactId: string;
    firstName?: string;
    lastName?: string;
    relationship?: string;
    cellphoneNumber?: string;
    user?: User;
}

export interface UserRole {
    userRoleId: string;
    user?: User;
    roleId: RoleType;
    description?: string;
}

export interface User {
    userId: string;
    firstName: string;
    lastName: string;
    dateOfBirth?: string;
    account?: Account;
    demographic?: Demographic;
    address?: Address;
    contact?: Contact;
    nextOfKinContacts?: NextOfKinContact[];
    userRoles?: UserRole[];
}

export interface AvailabilitySlot {
    slotId: string;
    date: string;
    startTime: string;
    endTime: string;
    status: SlotStatus;
    trainer?: User;
}

export interface Booking {
    bookingId: string;
    bookingDateTime: string;
    status: BookingStatus;
    member?: User;
    slot?: AvailabilitySlot;
}
