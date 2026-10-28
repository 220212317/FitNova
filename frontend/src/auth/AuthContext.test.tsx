import { describe, it } from "vitest";

describe("AuthContext", () => {
  it.todo("is unauthenticated by default");
  it.todo("login stores the token and sets the user");
  it.todo("logout clears the token and the user");
  it.todo("useAuth throws outside AuthProvider");
  it.todo("restores the session from sessionStorage on reload");
  it.todo("logs out when fitnova:unauthorized fires");
});
