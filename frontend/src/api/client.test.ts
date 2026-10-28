import { describe, it } from "vitest";

describe("api client", () => {
  it.todo("attaches the Bearer token when present");
  it.todo("omits the Authorization header without a token");
  it.todo("clears the token and dispatches fitnova:unauthorized on 401");
  it.todo("refreshes the token once and retries the request");
  it.todo("surfaces the server error message");
});
