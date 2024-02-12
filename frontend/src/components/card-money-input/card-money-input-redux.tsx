import * as React from "react";
import {Field} from "redux-form";
import {cardMoneyInput} from "./card-money-input";
import {moneyFormatter} from "./card-money-input-utils";

type TCardMoneyInputReduxProps = {
    label?: string;
    name: string;
    required?: boolean;
};

export const cardMoneyInputRedux = React.memo<TCardMoneyInputReduxProps>(
    ({
        label,
        name,
        required
    }) => (
        <Field
            component={cardMoneyInput}
            format={moneyFormatter}
            label={label}
            name={name}
            required={required}
        />
    )
);