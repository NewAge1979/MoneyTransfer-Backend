import {COMMISSION_PERCENT} from "./card-info-page-constants";

const INIT_STATE = {commissionPercent: COMMISSION_PERCENT};

//@ts-ignore
export const cardInfoPageReducer = (state = INIT_STATE, {payload, type}) => {
    switch (type) {
        default: {
            return state;
        }
    }
};