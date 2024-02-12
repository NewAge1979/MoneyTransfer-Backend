import * as React from "react";
import {Field} from "redux-form";
import {CARD_CVC_DEFAULT_LABEL} from "./card-cvc-input-constants";
import {cardCVCFormatter} from "./card-cvc-input-utils";
import {cardCVCInput} from "./card-cvc-input";

type TCardCVCInputReduxProps = {
    label?: string;
    name: string;
    required?: boolean;
};

export const cardCVCInputRedux = React.memo<TCardCVCInputReduxProps>(
    ({
        label,
        name,
        required
    }) => (
        <Field
            component={cardCVCInput}
            format={cardCVCFormatter}
            label={label || CARD_CVC_DEFAULT_LABEL}
            name={name}
            required={required}
        />
    )
);