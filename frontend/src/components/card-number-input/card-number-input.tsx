import * as React from "react";
import cn from "classnames";
import {Input} from "@mui/material";
import {withBaseFieldAdapter} from "../with-base-field-adapter";
import type {TAdapterComponentProps} from "../with-base-field-adapter";
import {CARD_NUMBER_PLACEHOLDER} from "./card-number-input-constants";
import styles from "./card-number-input.module.scss";

const cardNumberInputComponent = React.memo<TAdapterComponentProps>(
    ({
        className,
        error,
        input,
        placeholder,
        required
    }) => (
        <Input
            className={cn(styles['full-width'], className)}
            error={error}
            placeholder={placeholder ? placeholder : CARD_NUMBER_PLACEHOLDER}
            required={required}
            {...input}
        />
    )
);

export const cardNumberInput = withBaseFieldAdapter(cardNumberInputComponent);