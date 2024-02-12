import * as React from "react";
import {Input} from "@mui/material";
import {withBaseFieldAdapter} from "../with-base-field-adapter";
import type {TAdapterComponentProps} from "../with-base-field-adapter";
import {CARD_EXPIRES_PLACEHOLDER} from "./card-expires-input-constants";

const cardExpiresInputComponent = React.memo<TAdapterComponentProps>(
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
            placeholder={placeholder ? placeholder : CARD_EXPIRES_PLACEHOLDER}
            required={required}
            {...input}
        />
    )
);

export const cardExpiresInput = withBaseFieldAdapter(cardExpiresInputComponent);