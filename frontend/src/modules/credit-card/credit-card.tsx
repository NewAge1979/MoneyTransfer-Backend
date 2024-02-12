import * as React from "react";
import {FormSection} from "redux-form";
import cn from "classnames";
import {cardNumberInput as CardNumberInput} from "../../components/card-number-input";
import {cardExpiresInput as CardExpiresInput} from "../../components/card-expires-input";
import {cardCVCInput as CardCVCInput} from "../../components/card-cvc-input";
import MasterCardLogo from "../../assets/MasterCard.png";
import MirLogo from "../../assets/MIR.png";
import VisaLogo from "../../assets/Visa.png";
import {ECreditCardForm} from "./credit-card-enum";
import styles from "./credit-card.module.scss";

type TCreditCardProps = {
    className?: string;
    sectionName: string;
    showOnlyNumber?: boolean;
};

export const creditCard = React.memo<TCreditCardProps>(
    ({
        className,
        sectionName,
        showOnlyNumber
    }) => {
        return (
            <div className={cn(styles['card'], className)}>
                <FormSection name={sectionName}>
                    <div className={styles['assets']}>
                        <img alt="Visa card logotype" className={styles['card-logo']} src={VisaLogo}/>
                        <img alt="Mastercard card logotype" className={styles['card-logo']} src={MasterCardLogo}/>
                        <img alt="Mir card logotype" className={cn(styles['card-logo'], styles['card-logo-mir'])} src={MirLogo}/>
                    </div>
                    <div className={styles['card-number']}>
                        <CardNumberInput name={ECreditCardForm.CardNumber} required />
                    </div>
                    {!showOnlyNumber && (
                        <div className={styles['card-bottom']}>
                            <CardExpiresInput name={ECreditCardForm.CardExpires} required />
                            <CardCVCInput name={ECreditCardForm.CardCVC} required />
                        </div>
                    )}
                </FormSection>
            </div>
        );
    }
);