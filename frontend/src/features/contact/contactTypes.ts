/** TODO — Lisakhanya Tshokolo (220239215) */
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

export interface NextOfKinValues {
    firstName: string;
    lastName: string;
    relationship: string;
    cellphoneNumber: string;
    userId?: string;
}

export const emptyNextOfKinValues: NextOfKinValues = {
    firstName: "",
    lastName: "",
    relationship: "",
    cellphoneNumber: "",
};

export interface NextOfKinFieldErrors {
    firstName?: string;
    lastName?: string;
    relationship?: string;
    cellphoneNumber?: string;
}