package pl.chrapatij.moneytransfer;

import pl.chrapatij.moneytransfer.model.*;

public class DataForTest {
    public static final Double COMMISSION_PERCENT = 0.01;

    public static final String CARD1_NUM = "1111222233334444";
    public static final String CARD1_VAL = "09/25";
    public static final String CARD1_CVV = "804";
    public static final Card CARD1 = new Card(CARD1_NUM, CARD1_VAL, CARD1_CVV, false, 10000, 0, "RUR");

    public static final String CARD2_NUM = "2222333344445555";
    public static final String CARD2_VAL = "04/27";
    public static final String CARD2_CVV = "567";
    public static final Card CARD2 = new Card(CARD2_NUM, CARD2_VAL, CARD2_CVV, true, 15000, 5000, "RUR");

    public static final String ERROR_NUM = "0000000000000000";
    public static final String ERROR_VAL = "04/27";
    public static final String ERROR_CVV = "567";

    public static final TransferRequestBody TRB1 = new TransferRequestBody(CARD1_NUM, CARD1_VAL, CARD1_CVV, CARD2_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB2 = new TransferRequestBody(CARD1_NUM, CARD1_VAL, CARD1_CVV, CARD1_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB3 = new TransferRequestBody(ERROR_NUM, CARD1_VAL, CARD1_CVV, CARD1_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB4 = new TransferRequestBody(CARD1_NUM, CARD1_VAL, CARD1_CVV, ERROR_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB5 = new TransferRequestBody(CARD1_NUM, ERROR_VAL, CARD1_CVV, CARD2_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB6 = new TransferRequestBody(CARD1_NUM, CARD1_VAL, ERROR_CVV, CARD2_NUM, new Amount(5000, "RUR"));
    public static final TransferRequestBody TRB7 = new TransferRequestBody(CARD1_NUM, CARD1_VAL, CARD1_CVV, CARD2_NUM, new Amount(20000, "RUR"));
}