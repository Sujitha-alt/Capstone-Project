# CHANGELOG (Backend)

- Verified security rules restrict `/api/v1/reports/**` to `ROLE_ADMIN` via `SecurityConfig`.
- Confirmed report endpoints exist in `ReportController` with PDF download routes:
  - `GET /api/v1/reports/bookings/pdf`
  - `GET /api/v1/reports/payments/pdf`
- No breaking changes to endpoints, payloads, or schema.
