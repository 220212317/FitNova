import { describe, it, expect } from 'vitest'
import { newId, ensureId } from './ids'

describe('newId', () => {
    it('generates a non-empty string', () => {
        const id = newId()
        expect(typeof id).toBe('string')
        expect(id.length).toBeGreaterThan(0)
    })

    it('generates unique values on repeated calls', () => {
        const id1 = newId()
        const id2 = newId()
        expect(id1).not.toBe(id2)
    })
})

describe('ensureId', () => {
    it('returns the trimmed value when a non-blank string is given', () => {
        expect(ensureId('  abc-123  ')).toBe('abc-123')
    })

    it('generates a new id when given undefined', () => {
        const id = ensureId(undefined)
        expect(id.length).toBeGreaterThan(0)
    })

    it('generates a new id when given null', () => {
        const id = ensureId(null)
        expect(id.length).toBeGreaterThan(0)
    })

    it('generates a new id when given an empty string', () => {
        const id = ensureId('')
        expect(id.length).toBeGreaterThan(0)
    })

    it('generates a new id when given a whitespace-only string', () => {
        const id = ensureId('   ')
        expect(id.length).toBeGreaterThan(0)
    })

    it('does not return the same id twice when falling back', () => {
        const id1 = ensureId('')
        const id2 = ensureId('')
        expect(id1).not.toBe(id2)
    })
})