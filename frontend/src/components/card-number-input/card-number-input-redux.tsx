import * as React from "react";
import {Field} from "redux-form";
import {cardNumberFormatter} from "./card-number-input-utils";
import {CARD_NUMBER_DEFAULT_LABEL} from "./card-number-input-constants";
import {cardNumberInput} from "./card-number-input";

type TCardNumberInputReduxProps = {
    label?: string;
    name: string;
    required?: boolean;
};

export const cardNumberInputRedux = React.memo<TCardNumberInputReduxProps>(
    ({
        label,
        name,
        required
    }) => (
        <Field
            component={cardNumberInput}
            format={cardNumberFormatter}
            label={label || CARD_NUMBER_DEFAULT_LABEL}
            name={name}
            required={required}
        />
    )
);