import { useState } from "react";

export default function SignUpPage() {
    const [step] = useState<1 | 2>(1);

    // TODO: step 1 = email + password, step 2 = profile, then redirect to /login
    return <div>Sign up - step {step} of 2</div>;
}
