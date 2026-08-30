/** TODO — Lisakhanya Tshokolo (220239215) */
import React from "react";

export interface ContactValues {
    cellphoneNumber: string;
    alternativeCellphoneNumber: string;
    emailAddress: string;
}

export const emptyContactValues: ContactValues = {
    cellphoneNumber: "",
    alternativeCellphoneNumber: "",
    emailAddress: "",
};

export interface ContactFieldErrors {
    cellphoneNumber?: string;
    alternativeCellphoneNumber?: string;
    emailAddress?: string;
}

interface ContactFieldsProps {
    values: ContactValues;
    onChange: (values: ContactValues) => void;
    errors?: ContactFieldErrors;
    disabled?: boolean;
    /** Prefix for input ids so this component can be rendered more than once on a page. */
    idPrefix?: string;
}

const ContactFields: React.FC<ContactFieldsProps> = ({
                                                         values,
                                                         onChange,
                                                         errors,
                                                         disabled = false,
                                                         idPrefix = "contact",
                                                     }) => {
    const handleChange =
        (field: keyof ContactValues) => (e: React.ChangeEvent<HTMLInputElement>) => {
            onChange({...values, [field]: e.target.value});
        };

    return (
        <div className="fn-field-group">
            <div className="fn-field">
                <label htmlFor={`${idPrefix}-cellphoneNumber`}>Cell number</label>
                <input
                    id={`${idPrefix}-cellphoneNumber`}
                    name="cellphoneNumber"
                    type="tel"
                    autoComplete="tel"
                    value={values.cellphoneNumber}
                    onChange={handleChange("cellphoneNumber")}
                    disabled={disabled}
                    required
                    aria-invalid={!!errors?.cellphoneNumber}
                />
                {errors?.cellphoneNumber && (
                    <small className="fn-field-error">{errors.cellphoneNumber}</small>
                )}
            </div>

            <div className="fn-field">
                <label htmlFor={`${idPrefix}-alternativeCellphoneNumber`}>Alternative number</label>
                <input
                    id={`${idPrefix}-alternativeCellphoneNumber`}
                    name="alternativeCellphoneNumber"
                    type="tel"
                    autoComplete="tel"
                    value={values.alternativeCellphoneNumber}
                    onChange={handleChange("alternativeCellphoneNumber")}
                    disabled={disabled}
                    aria-invalid={!!errors?.alternativeCellphoneNumber}
                />
                {errors?.alternativeCellphoneNumber && (
                    <small className="fn-field-error">{errors.alternativeCellphoneNumber}</small>
                )}
            </div>

            <div className="fn-field">
                <label htmlFor={`${idPrefix}-emailAddress`}>Email</label>
                <input
                    id={`${idPrefix}-emailAddress`}
                    name="emailAddress"
                    type="email"
                    autoComplete="email"
                    value={values.emailAddress}
                    onChange={handleChange("emailAddress")}
                    disabled={disabled}
                    required
                    aria-invalid={!!errors?.emailAddress}
                />
                {errors?.emailAddress && (
                    <small className="fn-field-error">{errors.emailAddress}</small>
                )}
            </div>
        </div>
    );
};

export default ContactFields;