# End-to-end integration checklist

This checklist covers the member, trainer, and admin journeys tracked by issue
#232. Run it after the authentication endpoints and representative accounts are
available in the target environment.

## Automated checks

From `backend`:

```powershell
mvn test
```

From `frontend`:

```powershell
npm test -- --run
npm run lint
npm run build
```

## Member journey

1. Register and sign in as a member.
2. Open `/trainers` and confirm only trainer names and IDs are returned.
3. Select an available trainer, create a valid booking, and confirm it appears
   in the member schedule.
4. Attempt an invalid or conflicting booking and confirm the API error is shown
   without changing the schedule.

## Trainer journey

1. Sign in as a trainer and open the trainer dashboard.
2. Review assigned members and scheduled sessions.
3. Update an allowed session and confirm the member view reflects the change.
4. Confirm member-only and admin-only routes are denied.

## Admin journey

1. Sign in as an administrator.
2. Review account and role management pages.
3. Perform an allowed role or account update and confirm the new authorization
   takes effect after refreshing the session.
4. Confirm audit output distinguishes successful and failed authentication
   events without logging credentials or tokens.

## Release gate

- No broken routes or unexpected browser console errors.
- Unauthorized requests return `401` or `403` and do not expose private data.
- Booking validation is enforced by the backend, not only by the UI.
- UI state matches the persisted backend state after a refresh.
- Backend and frontend automated checks pass in CI.

The login and registration steps depend on the authentication services tracked
separately by issues #202 and #203. Until those services are implemented and an
environment is seeded with member, trainer, and admin accounts, the manual
journeys above cannot be signed off end to end.
