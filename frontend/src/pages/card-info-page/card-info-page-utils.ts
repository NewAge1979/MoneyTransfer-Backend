import {ECreditCardForm} from "../../modules/credit-card";
import {
    CARD_EXPIRES_DELIMITER,
    CARD_EXPIRES_MONTH_LENGTH,
    CARD_EXPIRES_YEAR_LENGTH
} from "../../components/card-expires-input";
import {CARD_CVC_LENGTH} from "../../components/card-cvc-input";
import {CARD_NUMBER_LENGTH, FIRST_MONTH, LAST_MONTH, RUR_CODE, RUR_SIGN} from "../../constants";
import type {
    TTransferPostData,
    TConfirmOperationPostData
} from "../../services";
import type {TCreditCardInfoPageFormValues} from "./card-info-page-types";
import {ECardInfoFields} from "./card-info-page-enum";

type TBaseFieldValidator = (value: string | undefined, person?: string) => string | void;

const validateCardNumber: TBaseFieldValidator = (cardNumber, person) => {
    if (!cardNumber || !cardNumber.trim()) {
        return `Номер карты ${person} обязателен.`;
    }
    if (cardNumber.replace(/\D/g, '').length !== CARD_NUMBER_LENGTH) {
        return `Номер карты ${person} должен быть длиной ${CARD_NUMBER_LENGTH} символов.`;
    }
}

const validateCardExpires: TBaseFieldValidator = (cardExpires, person) => {
    if (!cardExpires || !cardExpires.trim()) {
        return `Срок действия карты ${person} обязателен.`;
    }
    const [month, year] = cardExpires.split(CARD_EXPIRES_DELIMITER);
    if (!month || month.length < CARD_EXPIRES_MONTH_LENGTH) {
        return `Месяц действия карты ${person} обязателен и должен состоять из ${CARD_EXPIRES_MONTH_LENGTH} цифр.`;
    }
    if (!year || year.length < CARD_EXPIRES_YEAR_LENGTH) {
        return `Год действия карты ${person} обязателен и должен состоять из ${CARD_EXPIRES_YEAR_LENGTH} цифр.`;
    }
    const dateNow = new Date();
    const curMonth = dateNow.getMonth() + 1;
    const curYear2 = dateNow.getFullYear().toString().slice(2);
    if (Number(month) < FIRST_MONTH) {
        return `Месяц не может быть меньше ${FIRST_MONTH}.`;
    }
    if (Number(month) > LAST_MONTH) {
        return `Месяц не может быть больше ${LAST_MONTH}.`;
    }
    const monthIsExpires = Number(month) < Number(curMonth);
    const yearIsExpires = Number(year) < Number(curYear2);
    const yearIsEquals = Number(year) === Number(curYear2);
    if (yearIsExpires || (yearIsEquals && monthIsExpires)) {
        return `Срок действия карты ${person} истек.`;
    }
}

const validateCardCVC: TBaseFieldValidator = (cardCVC, person) => {
    if (!cardCVC || !cardCVC.trim()) {
        return `CVC/CVC2 код карты ${person} обязателен.`;
    }
    if (cardCVC.replace(/\D/g, '').length !== CARD_CVC_LENGTH) {
        return `CVC/CVC2 код карты ${person} должен быть длиной ${CARD_CVC_LENGTH} символа.`;
    }
}

const validateMoney: TBaseFieldValidator = (money) => {
    if (!money || !money.trim()) {
        return `Неободимо указать сумму перевода.`;
    }
    const clearMoney = money.replace(new RegExp(`[s${RUR_SIGN}]`, 'g'), '');
    const clearMoneyCount = parseFloat(clearMoney);
    if (!clearMoneyCount || clearMoneyCount === 0) {
        return `Неободимо указать сумма перевода.`;
    }
}

export const validateCardInfoForm = (values: TCreditCardInfoPageFormValues) => {
    const errors = {};
    const fromErrors = {};
    const toErrors = {};

    const {from, to, money} = values;

    const senderName = 'отправителя';
    const cardNumberFromError = validateCardNumber(from?.[ECreditCardForm.CardNumber], senderName);
    if (cardNumberFromError) {
        Object.assign(fromErrors, {[ECreditCardForm.CardNumber]: cardNumberFromError});
    }
    const cardExpiresFromErrors = validateCardExpires(from?.[ECreditCardForm.CardExpires], senderName);
    if (cardExpiresFromErrors) {
        Object.assign(fromErrors, {[ECreditCardForm.CardExpires]: cardExpiresFromErrors});
    }
    const cardCVCFromErrors = validateCardCVC(from?.[ECreditCardForm.CardCVC], senderName);
    if (cardCVCFromErrors) {
        Object.assign(fromErrors, {[ECreditCardForm.CardCVC]: cardCVCFromErrors});
    }

    const receiveName = 'получателя';
    const cardNumberToError = validateCardNumber(to?.[ECreditCardForm.CardNumber], receiveName);
    if (cardNumberToError) {
        Object.assign(toErrors, {[ECreditCardForm.CardNumber]: cardNumberToError});
    }

    if (Object.keys(fromErrors).length > 0) {
        Object.assign(errors, {[ECardInfoFields.From]: fromErrors});
    }
    if (Object.keys(toErrors).length > 0) {
        Object.assign(errors, {[ECardInfoFields.To]: toErrors});
    }
    const moneyErrors = validateMoney(money);
    if (moneyErrors) {
        Object.assign(errors, {[ECardInfoFields.Money]: moneyErrors});
    }

    return errors;
}

export const prepareFormValuesToSendTransfer = (values: TCreditCardInfoPageFormValues): TTransferPostData => ({
    cardFromNumber: values[ECardInfoFields.From][ECreditCardForm.CardNumber].replace(/\D/g, ''),
    cardToNumber: values[ECardInfoFields.To][ECreditCardForm.CardNumber].replace(/\D/g, ''),
    cardFromCVV: values[ECardInfoFields.From][ECreditCardForm.CardCVC] as string,
    cardFromValidTill: values[ECardInfoFields.From][ECreditCardForm.CardExpires] as string,
    amount: {
        currency: RUR_CODE,
        value: Number(values[ECardInfoFields.Money].replace(/\D/g, ''))
    }
})

export const prepareOperationIdToSendConfirmation = (operationId: string): TConfirmOperationPostData => ({
    code: '0000',
    operationId
})