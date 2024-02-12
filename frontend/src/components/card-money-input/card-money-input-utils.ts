import {formatByMoneyPattern} from "../../utils";

export const moneyFormatter = (value: string = ''): string => {
    const clearValue = Number(value.replace(/\D/g, ''));
    const formattedValue = formatByMoneyPattern(clearValue);

    return formattedValue.trim();
}