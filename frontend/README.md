# FitNova Frontend (aligned with backend)

React + Vite UI for the Spring Boot FitNova API (`context-path=/FitNova`, port `8080`).

## Backend contracts used

| Area | Backend |
|------|---------|
| Login | `POST /auth/login` → `{ accessToken, refreshToken, tokenType, userId, email, roles[] }` |
| Register | `POST /auth/register` → creates **MEMBER** only |
| Roles | `MEMBER` \| `TRAINER` \| `ADMIN` |
| Booking status | `CONFIRMED` \| `CANCELLED` \| `COMPLETED` |
| Slot status | `AVAILABLE` \| `BOOKED` \| `UNAVAILABLE` |
| Slots | `GET/POST/PUT /availability-slots/...` (create requires **TRAINER** primary role) |
| Bookings | `POST /booking/create`, `PUT /booking/update`, `GET /booking/findByMember/{id}` |
| Assign role | `POST /admin/roles/{userId}/assign` body `{ "role": "TRAINER" }` (**ADMIN**) |
| Create staff | `POST /admin/staff/create?role=TRAINER\|ADMIN` (**ADMIN**) |
| Lookups | Gender/race CRUD requires `lookup:manage` (admin) |

Public (no token): `POST /auth/login`, `/auth/register`, `/auth/refresh`, and `GET /gender/getAll`, `/race/getAll`.

## Booking flow (matches backend)

1. Trainer creates slot → **AVAILABLE**
2. Member books → booking **CONFIRMED**, slot **BOOKED**
3. Member cancels → booking **CANCELLED** (slot can return to **AVAILABLE**)

There is **no** PENDING / accept-reject step in the current backend enums.

## How to log in as admin

The backend **does not seed** a default admin. Options:

1. **SQL (first admin)** — after the app has created tables, insert an account + user + `user_role` with `role_id = 'ADMIN'`, using a BCrypt password hash.
2. **Once one admin exists** — use **Admin → Users → Create staff** (`POST /admin/staff/create?role=ADMIN`) or assign roles via `/admin/roles/{id}/assign`.

Public signup always creates **MEMBER**.

## Run

```bash
cd fitnova-frontend
npm install
npm run dev
```

Dev proxy: `/FitNova` → `http://localhost:8080`.

Ensure `JWT_SECRET` is set for the backend and MySQL `FitNovaDB` is reachable.
