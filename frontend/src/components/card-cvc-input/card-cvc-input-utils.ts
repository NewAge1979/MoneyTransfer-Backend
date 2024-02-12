import {CARD_CVC_LENGTH} from "./card-cvc-input-constants";

export const cardCVCFormatter = (value = '') => {
    const clearValue = value?.replace(/\D/g, '');
    return Array.from(clearValue).slice(0, CARD_CVC_LENGTH).join('');
}