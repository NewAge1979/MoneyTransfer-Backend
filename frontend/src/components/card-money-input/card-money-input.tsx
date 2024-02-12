import * as React from "react";
import cn from "classnames";
import {InputAdornment, TextField} from "@mui/material";
import {RUR_SIGN} from "../../constants";
import {withBaseFieldAdapter} from "../with-base-field-adapter";
import type {TAdapterComponentProps} from "../with-base-field-adapter";
import styles from "./card-money-input.module.scss";

const cardMoneyInputProps = {
    endAdornment: <InputAdornment className={styles['currency-sign']} position="start">{RUR_SIGN}</InputAdornment>
};

const cardMoneyInputComponent = React.memo<TAdapterComponentProps>(
    ({
        className,
        error,
        input,
        placeholder,
        required
    }) => (
        <TextField
            className={cn(styles['money-input'], className, {[styles['not-empty']]: Number(input?.value) !== 0})}
            error={error}
            InputProps={cardMoneyInputProps}
            placeholder={placeholder}
            required={required}
            {...input}
        />
    )
);

export const cardMoneyInput = withBaseFieldAdapter(cardMoneyInputComponent);