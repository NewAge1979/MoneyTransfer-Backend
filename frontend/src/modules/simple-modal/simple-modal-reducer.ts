import {
    OPEN_SIMPLE_MODAL,
    CLOSE_SIMPLE_MODAL
} from "./simple-modal-actions";

const INIT_STATE = {
    isOpen: false,
    message: '',
    title: '',
    withError: false
};

// @ts-ignore
export const simpleModalReducer = (state = INIT_STATE, {payload, type}) => {
    switch (type) {
        case OPEN_SIMPLE_MODAL: {
            return {
                ...payload,
                isOpen: true
            };
        }
        case CLOSE_SIMPLE_MODAL: {
            return INIT_STATE;
        }
        default: {
            return state;
        }
    }
}