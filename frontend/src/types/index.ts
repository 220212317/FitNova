/** FitNova domain types — aligned with backend enums */

export type RoleType = "MEMBER" | "TRAINER" | "ADMIN";

/** Backend: za.ac.cput.domain.enums.BookingStatus */
export type BookingStatus = "CONFIRMED" | "CANCELLED" | "COMPLETED";

/** Backend: za.ac.cput.domain.enums.SlotStatus */
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

/** Backend AvailabilitySlot — no title/notes fields */
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
  /** ISO local: yyyy-MM-dd'T'HH:mm:ss */
  bookingDateTime: string;
  status: BookingStatus;
  member?: User;
  slot?: AvailabilitySlot;
}

/** Backend: za.ac.cput.domain.GymResource (category is serialised lower-case) */
export type GymResourceCategory = "manual" | "nutrition" | "tip";

export interface GymResource {
  resourceId: string;
  category: GymResourceCategory;
  title: string;
  summary?: string | null;
  body: string;
  tags: string[];
}

export type TrainerApplicationStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface TrainerApplication {
  applicationId: string;
  userId: string;
  user?: User;
  motivation?: string;
  experience?: string;
  certifications?: string;
  status: TrainerApplicationStatus;
  submittedAt: string;
  reviewedAt?: string;
  reviewedBy?: string;
  adminNotes?: string;
}

/** Backend: za.ac.cput.profile.ProfileResponse (GET/PUT /profile/me) */
export interface NextOfKinInfo {
  firstName: string;
  lastName: string;
  relationship: string;
  cellphoneNumber: string;
}

export interface Profile {
  userId: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  email: string;
  memberSince?: string | null;
  roles: string[];
  gender?: string | null;
  streetNumber?: string | null;
  streetName?: string | null;
  suburbName?: string | null;
  city?: string | null;
  postalCode?: string | null;
  province?: string | null;
  country?: string | null;
  cellphoneNumber?: string | null;
  alternativeCellphoneNumber?: string | null;
  nextOfKin?: NextOfKinInfo | null;
}
