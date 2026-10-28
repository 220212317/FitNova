import { describe, it } from "vitest";

describe("App routing", () => {
  it.todo("redirects unauthenticated users from /dashboard to /login");
  it.todo("redirects authenticated users from /login to /dashboard");
  it.todo("blocks a member from /lookups");
  it.todo("allows an admin on /lookups");
});
