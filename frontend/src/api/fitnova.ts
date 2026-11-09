import { api } from "./client";
import { ensureId } from "./ids";
import type {
    Account,
    Address,
    AvailabilitySlot,
    Booking,
    Contact,
    Demographic,
    Gender,
    GymResource,
    NextOfKinContact,
    Profile,
    Race,
    TrainerSummary,
    User,
    UserRole,
} from "../types";

export const trainersApi = {
    getAll: () => api.get<TrainerSummary[]>("/trainers"),
};

export const usersApi = {
    getAll: () => api.get<User[]>("/user/getAll").catch(() => [] as User[]),
    /** Same as getAll but lets errors (e.g. 403) reach the caller. Used by admin screens. */
    getAllStrict: () => api.get<User[]>("/user/getAll"),
    getById: (id: string) => api.get<User>(`/user/read/${id}`),
    create: (user: User) =>
        api.post<User>("/user/create", {
            ...user,
            userId: ensureId(user.userId),
        }),
    update: (user: User) => api.put<User>("/user/update", user),
    delete: (id: string) => api.delete(`/user/delete/${id}`),
    findByName: (first: string, last: string) =>
        api.get<User[]>(
            `/user/findByName/${encodeURIComponent(first)}/${encodeURIComponent(last)}`
        ),
    searchByLastName: (last: string) =>
        api.get<User[]>(`/user/searchByLastName/${encodeURIComponent(last)}`),
};

export const accountsApi = {
    create: (account: Account) =>
        api.post<Account>("/account/create", {
            ...account,
            accountId: ensureId(account.accountId),
        }),
    update: (account: Account) => api.put<Account>("/account/update", account),
    delete: (id: string) => api.delete(`/account/delete/${id}`),
    getById: (id: string) => api.get<Account>(`/account/read/${id}`),
    getAll: () => api.get<Account[]>("/account/getAll").catch(() => [] as Account[]),
    findByEmail: (email: string) =>
        api.get<Account>(`/account/findByEmail/${encodeURIComponent(email)}`),
};

export const addressesApi = {
    create: (address: Address) =>
        api.post<Address>("/address/create", {
            ...address,
            addressId: ensureId(address.addressId),
        }),
    update: (address: Address) => api.put<Address>("/address/update", address),
    delete: (id: string) => api.delete(`/address/delete/${id}`),
    getById: (id: string) => api.get<Address>(`/address/read/${id}`),
    getAll: () => api.get<Address[]>("/address/getAll").catch(() => [] as Address[]),
};

export const contactsApi = {
    create: (contact: Contact) =>
        api.post<Contact>("/contact/create", {
            ...contact,
            contactId: ensureId(contact.contactId),
        }),
    update: (contact: Contact) => api.put<Contact>("/contact/update", contact),
    delete: (id: string) => api.delete(`/contact/delete/${id}`),
    getById: (id: string) => api.get<Contact>(`/contact/read/${id}`),
    getAll: () => api.get<Contact[]>("/contact/getAll").catch(() => [] as Contact[]),
};

export const demographicsApi = {
    create: (demographic: Demographic) =>
        api.post<Demographic>("/demographic/create", {
            ...demographic,
            demographyId: ensureId(demographic.demographyId),
        }),
    update: (demographic: Demographic) =>
        api.put<Demographic>("/demographic/update", demographic),
    delete: (id: string) => api.delete(`/demographic/delete/${id}`),
    getAll: () =>
        api.get<Demographic[]>("/demographic/getAll").catch(() => [] as Demographic[]),
};

export const nextOfKinApi = {
    getAll: () =>
        api
            .get<NextOfKinContact[]>("/nextofkincontact/getAll")
            .catch(() => [] as NextOfKinContact[]),
    create: (kin: NextOfKinContact) =>
        api.post<NextOfKinContact>("/nextofkincontact/create", {
            ...kin,
            nextOfKinContactId: ensureId(kin.nextOfKinContactId),
        }),
    update: (kin: NextOfKinContact) =>
        api.put<NextOfKinContact>("/nextofkincontact/update", kin),
    delete: (id: string) => api.delete(`/nextofkincontact/delete/${id}`),
    byUser: (userId: string) =>
        api.get<NextOfKinContact[]>(`/nextofkincontact/findByUser/${userId}`),
};

export const userRolesApi = {
    getAll: () =>
        api.get<UserRole[]>("/userrole/getAll").catch(() => [] as UserRole[]),
    create: (role: UserRole) =>
        api.post<UserRole>("/userrole/create", {
            ...role,
            userRoleId: ensureId(role.userRoleId),
        }),
    update: (role: UserRole) => api.put<UserRole>("/userrole/update", role),
    delete: (id: string) => api.delete(`/userrole/delete/${id}`),
    byUser: (userId: string) =>
        api.get<UserRole[]>(`/userrole/findByUser/${userId}`),
};

export const bookingsApi = {
    getAll: () => api.get<Booking[]>("/booking/getAll").catch(() => [] as Booking[]),
    getById: (id: string) => api.get<Booking>(`/booking/read/${id}`),
    create: (booking: Booking) =>
        api.post<Booking>("/booking/create", {
            ...booking,
            bookingId: ensureId(booking.bookingId),
        }),
    update: (booking: Booking) => api.put<Booking>("/booking/update", booking),
    delete: (id: string) => api.delete(`/booking/delete/${id}`),
    byMember: (userId: string) =>
        api.get<Booking[]>(`/booking/findByMember/${userId}`),
    bySlot: (slotId: string) =>
        api.get<Booking[]>(`/booking/findBySlot/${slotId}`),
    byStatus: (status: string) =>
        api.get<Booking[]>(`/booking/findByStatus/${status}`),
};

/** Backend maps getAll to `/availability-slots/all` */
export const slotsApi = {
    getAll: () =>
        api
            .get<AvailabilitySlot[]>("/availability-slots/all")
            .catch(() =>
                api
                    .get<AvailabilitySlot[]>("/availability-slots/getAll")
                    .catch(() => [] as AvailabilitySlot[])
            ),
    getById: (id: string) =>
        api.get<AvailabilitySlot>(`/availability-slots/read/${id}`),
    create: (slot: AvailabilitySlot) =>
        api.post<AvailabilitySlot>("/availability-slots/create", {
            ...slot,
            slotId: ensureId(slot.slotId),
        }),
    update: (slot: AvailabilitySlot) =>
        api.put<AvailabilitySlot>("/availability-slots/update", slot),
    delete: (id: string) => api.delete(`/availability-slots/delete/${id}`),
};

export const lookupsApi = {
    genders: () =>
        api.get<Gender[]>("/gender/getAll").catch(() => [] as Gender[]),
    races: () => api.get<Race[]>("/race/getAll").catch(() => [] as Race[]),
    createGender: (gender: Gender) =>
        api.post<Gender>("/gender/create", {
            ...gender,
            genderId: ensureId(gender.genderId),
        }),
    updateGender: (gender: Gender) => api.put<Gender>("/gender/update", gender),
    deleteGender: (id: string) => api.delete(`/gender/delete/${id}`),
    createRace: (race: Race) =>
        api.post<Race>("/race/create", {
            ...race,
            raceId: ensureId(race.raceId),
        }),
    updateRace: (race: Race) => api.put<Race>("/race/update", race),
    deleteRace: (id: string) => api.delete(`/race/delete/${id}`),
};

/** Admin-only endpoints (SecurityConfig: /admin/** requires ROLE_ADMIN) */
export const adminApi = {
  /** POST /admin/roles/{userId}/assign  body: { role: "TRAINER" | "ADMIN" } */
  assignRole: (userId: string, role: "TRAINER" | "ADMIN" | "MEMBER") =>
    api.post<string>(`/admin/roles/${encodeURIComponent(userId)}/assign`, { role }),

  /** PUT /admin/roles/{userId}  body: { role } - makes `role` the user's only role. */
  setRole: (userId: string, role: "TRAINER" | "ADMIN" | "MEMBER") =>
    api.put<string>(`/admin/roles/${encodeURIComponent(userId)}`, { role }),

  /** DELETE /admin/roles/{userId}/revoke  body: { role } */
  revokeRole: (userId: string, role: "TRAINER" | "ADMIN" | "MEMBER") =>
    api.deleteWithBody<string>(`/admin/roles/${encodeURIComponent(userId)}/revoke`, { role }),

  /**
   * POST /admin/staff/create?role=TRAINER|ADMIN
   * Creates a new staff account (not a public self-signup).
   */
  createStaff: (
    body: {
      firstName: string;
      lastName: string;
      email: string;
      password: string;
      dateOfBirth: string;
    },
    role: "TRAINER" | "ADMIN"
  ) =>
    api.post<Record<string, unknown>>(
      `/admin/staff/create?role=${encodeURIComponent(role)}`,
      body
    ),
};

/** Reading: any signed-in user. Create / update / delete: ADMIN (resource:manage). */
export const resourcesApi = {
    getAll: () => api.get<GymResource[]>("/resource/getAll"),
    create: (resource: GymResource) =>
        api.post<GymResource>("/resource/create", {
            ...resource,
            resourceId: ensureId(resource.resourceId),
        }),
    update: (resource: GymResource) =>
        api.put<GymResource>("/resource/update", resource),
    delete: (id: string) => api.delete(`/resource/delete/${encodeURIComponent(id)}`),
};

export const profileApi = {
    get: () => api.get<Profile>("/profile/me"),
    update: (profile: Partial<Profile>) => api.put<Profile>("/profile/me", profile),
    /** The password confirms the deletion. */
    delete: (password: string) => api.deleteWithBody<void>("/profile/me", { password }),
};
