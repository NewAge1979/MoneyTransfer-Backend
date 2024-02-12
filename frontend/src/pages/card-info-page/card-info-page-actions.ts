import axios from "axios";
import {FormAction, reset, SubmissionError} from "redux-form";
import {
    DEFAULT_ERR_MSG,
    DEFAULT_ERR_TITLE,
    DEFAULT_OK_MSG,
    DEFAULT_OK_TITLE,
    openSimpleModalAction
} from "../../modules/simple-modal";
import type {
    TTransferPostDataResponse,
    TConfirmOperationPostDataResponse
} from "../../services";
import {
    TRANSFER_URL,
    CONFIRM_OPERATION_URL
} from "../../services";
import {CARD_INFO_PAGE_FORM} from "./card-info-page-constants";
import {
    prepareFormValuesToSendTransfer,
    prepareOperationIdToSendConfirmation,
    validateCardInfoForm
} from "./card-info-page-utils";
import type {TCreditCardInfoPageFormValues} from "./card-info-page-types";

export const onSubmitHandler = async (values: TCreditCardInfoPageFormValues, dispatch: (arg0: FormAction) => void) => {
    const errors = validateCardInfoForm(values);

    if (Object.keys(errors).length > 0) {
        throw new SubmissionError(errors)
    }

    try {
        const dataForTransfer = prepareFormValuesToSendTransfer(values);
        const transferResponse = await axios.post<TTransferPostDataResponse>(TRANSFER_URL, dataForTransfer);
        const dataForConfirmation = prepareOperationIdToSendConfirmation(transferResponse?.data?.operationId);
        await axios.post<TConfirmOperationPostDataResponse>(CONFIRM_OPERATION_URL, dataForConfirmation);

        dispatch(
            openSimpleModalAction(DEFAULT_OK_TITLE, DEFAULT_OK_MSG)
        );
        dispatch(reset(CARD_INFO_PAGE_FORM))
    } catch (error) {
        // @ts-ignore
        const errMsg = error?.response?.data?.message;
        dispatch(
            //openSimpleModalAction(DEFAULT_ERR_TITLE, error?.response?.data?.message || DEFAULT_ERR_MSG, true)
            openSimpleModalAction(DEFAULT_ERR_TITLE, errMsg || DEFAULT_ERR_MSG, true)
        );
    }
};