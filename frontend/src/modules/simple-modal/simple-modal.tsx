import * as React from "react";
import {useSelector} from "react-redux";
import cn from "classnames";
import {Button, Dialog} from "@mui/material";
import {useCloseSimpleModal} from "./simple-modal-actions";
import {getSimpleModalIsOpen, getSimpleModalWithError, getSimpleModalData} from "./simple-modal-selectors";
import styles from "./simple-modal.module.scss";

export const simpleModal = React.memo(
    () => {
        const {message, title} = useSelector(getSimpleModalData);
        const isOpen = useSelector(getSimpleModalIsOpen);
        const withError = useSelector(getSimpleModalWithError);
        const closeModal = useCloseSimpleModal();
        const errorClassName = {[styles.error]: withError};
        return (
            <Dialog open={isOpen}>
                <div className={styles.body}>
                    <div>
                        <h3 className={cn(styles.title, errorClassName)}>{title}</h3>
                        <div className={styles.message}>{message}</div>
                    </div>
                    <Button className={cn(styles.button, errorClassName)} onClick={closeModal} variant="contained">Ok</Button>
                </div>
            </Dialog>
        );
    }
)