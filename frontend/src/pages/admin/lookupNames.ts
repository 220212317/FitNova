/*
 * lookupNames.ts
 * Author: Inga Plati
 * 230126634
 */

export interface LookupItem {
  id: string;
  description: string;
}

export const GENDER_PRESETS = ["Female", "Male", "Non-binary", "Prefer not to say"];
export const RACE_PRESETS = ["Black African", "Coloured", "Indian/Asian", "White", "Other"];

export const CUSTOM_OPTION = "__custom__";

export function cleanName(value: string): string {
  return value.trim().replace(/\s+/g, " ");
}

export function lookupKey(value: string): string {
  return value.toLowerCase().replace(/[^\p{L}\p{N}]/gu, "");
}

export function findDuplicate(
  items: LookupItem[],
  value: string,
  ignoreId?: string
): LookupItem | undefined {
  const key = lookupKey(value);
  if (!key) return undefined;
  return items.find((item) => item.id !== ignoreId && lookupKey(item.description) === key);
}

export function remainingPresets(presets: string[], items: LookupItem[]): string[] {
  return presets.filter((preset) => !findDuplicate(items, preset));
}

export function duplicateMessage(noun: string, existing: LookupItem): string {
  return `A ${noun} called "${existing.description}" already exists.`;
}
