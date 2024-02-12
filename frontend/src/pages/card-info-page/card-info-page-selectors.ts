import {getFormValues} from "redux-form";
import {createSelector} from "reselect";
import {formatByMoneyPattern} from "../../utils";
import {
    CARD_INFO_PAGE_FORM,
    CARD_INFO_PAGE_STORE_KEY
} from "./card-info-page-constants";
import type {TCreditCardInfoPageFormValues} from "./card-info-page-types";
import {ECardInfoFields} from "./card-info-page-enum";

const getCardInfoPageState = (state: any) => state[CARD_INFO_PAGE_STORE_KEY];
const getCardInfoPageFormValues = getFormValues(CARD_INFO_PAGE_FORM) as (state: any) => TCreditCardInfoPageFormValues;

export const getCommissionPercent = createSelector(
    getCardInfoPageState,
    (pageState): number => pageState.commissionPercent
);

export const getInputedMoney = createSelector(
    getCardInfoPageFormValues,
    (cardInfoPageFormValues: TCreditCardInfoPageFormValues): number => (
        Number(cardInfoPageFormValues?.[ECardInfoFields.Money]?.replace(/\D/g, '')) || 0
    )
);

export const getComputedCommission = createSelector(
    getInputedMoney,
    getCommissionPercent,
    (inputedMoney: number, commissionPercent: number): string => (
        formatByMoneyPattern(inputedMoney / 100 * commissionPercent, true)
    )
);