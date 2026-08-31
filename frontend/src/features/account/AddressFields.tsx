/** Athi Sintiya (220212317) */
import type { Address } from '../../types';

interface AddressFieldsProps {
    address: Partial<Address> | null;  // ✅ Expects null, not undefined
    onAddressChange: (address: Address) => void;
    disabled?: boolean;
}

export default function AddressFields({
                                          address,
                                          onAddressChange,
                                          disabled = false,
                                      }: AddressFieldsProps) {
    // Since Address has @OneToOne with User and cascade ALL,
    // the address is created alongside the user. We just collect the data
    // and pass it up to the parent form.

    const streetNumber = address?.streetNumber ?? '';
    const streetName = address?.streetName ?? '';
    const suburbName = address?.suburbName ?? '';
    const city = address?.city ?? '';
    const postalCode = address?.postalCode ?? '';
    const province = address?.province ?? '';
    const country = address?.country ?? 'South Africa';

    function updateField<K extends keyof Address>(field: K, value: string) {
        // Build a complete Address-like object with all fields
        const updated: Address = {
            addressId: address?.addressId ?? '',
            streetNumber: field === 'streetNumber' ? value : streetNumber,
            streetName: field === 'streetName' ? value : streetName,
            suburbName: field === 'suburbName' ? value : suburbName,
            city: field === 'city' ? value : city,
            postalCode: field === 'postalCode' ? value : postalCode,
            province: field === 'province' ? value : province,
            country: field === 'country' ? value : country,
        };
        onAddressChange(updated);
    }

    return (
        <fieldset disabled={disabled} style={{ border: '1px solid #ddd', padding: '16px', borderRadius: '8px', marginBottom: '16px' }}>
            <legend style={{ fontWeight: 'bold', padding: '0 8px' }}>Address</legend>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-street-number" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Street Number
                </label>
                <input
                    id="address-street-number"
                    type="text"
                    value={streetNumber}
                    onChange={(e) => updateField('streetNumber', e.target.value)}
                    placeholder="123"
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-street-name" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Street Name *
                </label>
                <input
                    id="address-street-name"
                    type="text"
                    value={streetName}
                    onChange={(e) => updateField('streetName', e.target.value)}
                    placeholder="Main Street"
                    required
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-suburb" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Suburb
                </label>
                <input
                    id="address-suburb"
                    type="text"
                    value={suburbName}
                    onChange={(e) => updateField('suburbName', e.target.value)}
                    placeholder="Suburb"
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-city" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    City *
                </label>
                <input
                    id="address-city"
                    type="text"
                    value={city}
                    onChange={(e) => updateField('city', e.target.value)}
                    placeholder="Cape Town"
                    required
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-postal-code" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Postal Code
                </label>
                <input
                    id="address-postal-code"
                    type="text"
                    value={postalCode}
                    onChange={(e) => updateField('postalCode', e.target.value)}
                    placeholder="8001"
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>

            <div style={{ marginBottom: '12px' }}>
                <label htmlFor="address-province" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Province
                </label>
                <select
                    id="address-province"
                    value={province}
                    onChange={(e) => updateField('province', e.target.value)}
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                >
                    <option value="">Select province</option>
                    <option value="Eastern Cape">Eastern Cape</option>
                    <option value="Free State">Free State</option>
                    <option value="Gauteng">Gauteng</option>
                    <option value="KwaZulu-Natal">KwaZulu-Natal</option>
                    <option value="Limpopo">Limpopo</option>
                    <option value="Mpumalanga">Mpumalanga</option>
                    <option value="Northern Cape">Northern Cape</option>
                    <option value="North West">North West</option>
                    <option value="Western Cape">Western Cape</option>
                </select>
            </div>

            <div>
                <label htmlFor="address-country" style={{ display: 'block', marginBottom: '4px', fontWeight: 'bold' }}>
                    Country *
                </label>
                <input
                    id="address-country"
                    type="text"
                    value={country}
                    onChange={(e) => updateField('country', e.target.value)}
                    placeholder="South Africa"
                    required
                    style={{ width: '100%', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' }}
                />
            </div>
        </fieldset>
    );
}