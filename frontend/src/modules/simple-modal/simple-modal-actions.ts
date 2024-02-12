import {useCallback} from "react";
import {useDispatch} from "react-redux";
const nameSpaceAction = (actionType: string): string => `@simple-modal/${actionType}`;

export const OPEN_SIMPLE_MODAL = nameSpaceAction('open-simple-modal')
export const openSimpleModalAction = (title: string, message: string, withError?: boolean) => ({
    payload: {
        message,
        title,
        withError
    },
    type: OPEN_SIMPLE_MODAL
});

export const CLOSE_SIMPLE_MODAL = nameSpaceAction('close-simple-modal')
const closeSimpleModalAction = () => ({type: CLOSE_SIMPLE_MODAL});

export const useCloseSimpleModal = () => {
    const dispatch = useDispatch();
    return useCallback(
        () => dispatch(closeSimpleModalAction()),
        [dispatch]
    );
}