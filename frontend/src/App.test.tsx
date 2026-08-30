import { describe, it, expect } from 'vitest'
import { render, screen, within } from '@testing-library/react'
import App from './App'

describe('App', () => {
    it('renders without crashing', () => {
        render(<App />)
    })

    it('renders the FitNova brand', () => {
        render(<App />)
        expect(screen.getAllByText('FitNova').length).toBeGreaterThan(0)
    })

    it('renders the main navigation links', () => {
        render(<App />)
        const nav = screen.getByRole('navigation')
        expect(within(nav).getByRole('link', { name: 'Home' })).toBeInTheDocument()
        expect(within(nav).getByRole('link', { name: 'Booking' })).toBeInTheDocument()
        expect(within(nav).getByRole('link', { name: 'Lookup' })).toBeInTheDocument()
        expect(within(nav).getByRole('link', { name: 'Slots' })).toBeInTheDocument()
        expect(within(nav).getByRole('link', { name: 'Users' })).toBeInTheDocument()
    })

    it('renders the Get Started call-to-action button', () => {
        render(<App />)
        expect(screen.getByRole('button', { name: 'Get Started' })).toBeInTheDocument()
    })

    it('renders the hero heading', () => {
        render(<App />)
        expect(screen.getByRole('heading', { level: 1 })).toBeInTheDocument()
    })
})