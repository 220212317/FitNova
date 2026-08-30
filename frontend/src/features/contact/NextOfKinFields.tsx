/** TODO — Lisakhanya Tshokolo (220239215) */
import React from "react";
import type {
    NextOfKinValues,
    NextOfKinFieldErrors,
} from "./contactTypes";

interface NextOfKinFieldsProps {
    values: NextOfKinValues;
    onChange: (values: NextOfKinValues) => void;
    errors?: NextOfKinFieldErrors;
    disabled?: boolean;
}

const NextOfKinFields: React.FC<NextOfKinFieldsProps> = ({
                                                             values,
                                                             onChange,
                                                             errors,
                                                             disabled = false,
                                                         }) => {
    const handleChange =
        (field: keyof NextOfKinValues) =>
            (e: React.ChangeEvent<HTMLInputElement>) => {
                onChange({
                    ...values,
                    [field]: e.target.value,
                });
            };

    return (
        <div className="fn-field-group">
            <div className="fn-field">
                <label htmlFor="nok-firstName">
                    First name
                </label>

                <input
                    id="nok-firstName"
                    name="firstName"
                    type="text"
                    value={values.firstName}
                    onChange={handleChange("firstName")}
                    disabled={disabled}
                    required
                    aria-invalid={!!errors?.firstName}
                />

                {errors?.firstName && (
                    <small className="fn-field-error">
                        {errors.firstName}
                    </small>
                )}
            </div>

            <div className="fn-field">
                <label htmlFor="nok-lastName">
                    Last name
                </label>

                <input
                    id="nok-lastName"
                    name="lastName"
                    type="text"
                    value={values.lastName}
                    onChange={handleChange("lastName")}
                    disabled={disabled}
                    required
                    aria-invalid={!!errors?.lastName}
                />

                {errors?.lastName && (
                    <small className="fn-field-error">
                        {errors.lastName}
                    </small>
                )}
            </div>

            <div className="fn-field">
                <label htmlFor="nok-relationship">
                    Relationship
                </label>

                <input
                    id="nok-relationship"
                    name="relationship"
                    type="text"
                    value={values.relationship}
                    onChange={handleChange("relationship")}
                    disabled={disabled}
                    required
                    aria-invalid={!!errors?.relationship}
                />

                {errors?.relationship && (
                    <small className="fn-field-error">
                        {errors.relationship}
                    </small>
                )}
            </div>

            <div className="fn-field">
                <label htmlFor="nok-cell">
                    Cell number
                </label>

                <input
                    id="nok-cell"
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
                    <small className="fn-field-error">
                        {errors.cellphoneNumber}
                    </small>
                )}
            </div>
        </div>
    );
};

export default NextOfKinFields;