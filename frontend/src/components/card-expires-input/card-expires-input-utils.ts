import {
    CARD_EXPIRES_DELIMITER,
    CARD_EXPIRES_MONTH_START,
    CARD_EXPIRES_MONTH_END,
    CARD_EXPIRES_YEAR_START,
    CARD_EXPIRES_YEAR_END
} from "./card-expires-input-constants";

export const cardExpiresFormatter = (value = '') => {
    const clearValue = value?.replace(/\D/g, '');
    if (clearValue.length <= CARD_EXPIRES_MONTH_END) {
        return clearValue;
    }
    return [
        clearValue.slice(CARD_EXPIRES_MONTH_START, CARD_EXPIRES_MONTH_END),
        clearValue.slice(CARD_EXPIRES_YEAR_START, CARD_EXPIRES_YEAR_END)
    ].join(CARD_EXPIRES_DELIMITER);
}