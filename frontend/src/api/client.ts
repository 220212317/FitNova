/** Athi Sintiya (220212317) — FitNova API client */

import { clearStoredSession, readStoredSession } from "../auth/storage";

const API_BASE =
  (import.meta.env.VITE_API_BASE as string | undefined)?.replace(/\/$/, "") ||
  (import.meta.env.DEV ? "/FitNova" : "http://localhost:8080/FitNova");

export class ApiError extends Error {
  status: number;
  body: unknown;

  constructor(status: number, message: string, body?: unknown) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.body = body ?? null;
  }
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_BASE}${path.startsWith("/") ? path : `/${path}`}`;
  const headers: HeadersInit = {
    Accept: "application/json",
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };
  const accessToken = readStoredSession()?.accessToken;
  if (accessToken) {
    (headers as Record<string, string>).Authorization = `Bearer ${accessToken}`;
  }

  let res: Response;
  try {
    res = await fetch(url, { ...options, headers });
  } catch (networkErr) {
    const hint = networkErr instanceof Error ? networkErr.message : "Network error";
    throw new ApiError(
      0,
      `Cannot reach FitNova API at ${url}. Is the backend running on port 8080? (${hint})`
    );
  }

  if (res.status === 204) {
    return undefined as T;
  }

  const text = await res.text();
  let data: unknown = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!res.ok) {
    const msg =
      typeof data === "object" && data && "message" in data
        ? String((data as { message: string }).message)
        : typeof data === "string" && data.trim()
          ? data
          : res.statusText || `Request failed (${res.status})`;
    if (res.status === 401 && accessToken) {
      clearStoredSession();
      window.dispatchEvent(new Event("fitnova:unauthorized"));
    }
    throw new ApiError(res.status, msg, data);
  }

  return data as T;
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body?: unknown) =>
    request<T>(path, {
      method: "POST",
      body: body === undefined ? undefined : JSON.stringify(body),
    }),
  put: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "PUT", body: JSON.stringify(body) }),
  delete: (path: string) => request<void>(path, { method: "DELETE" }),
  deleteWithBody: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "DELETE", body: JSON.stringify(body) }),
};

export { API_BASE };
