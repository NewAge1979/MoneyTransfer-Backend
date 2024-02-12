import * as React from "react";
import {Input} from "@mui/material";
import {withBaseFieldAdapter} from "../with-base-field-adapter";
import type {TAdapterComponentProps} from "../with-base-field-adapter";
import {CARD_CVC_PLACEHOLDER} from "./card-cvc-input-constants";

const cardCVCInputComponent = React.memo<TAdapterComponentProps>(
    ({
         className,
         error,
         input,
         placeholder,
         required
    }) => (
        <Input
            className={className}
            error={error}
            placeholder={placeholder ? placeholder : CARD_CVC_PLACEHOLDER}
            required={required}
            {...input}
        />
    )
);

export const cardCVCInput = withBaseFieldAdapter(cardCVCInputComponent);