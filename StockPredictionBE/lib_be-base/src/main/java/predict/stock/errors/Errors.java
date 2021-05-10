package predict.stock.errors;

public class Errors {
    public static final int SUCCESS = 0;
    public static final int INVALID_MSG = -2;
    public static final int OTP_NOT_CORRECT = -16;
    public static final int OTP_EXPIRED = -17;
    public static final int OTP_LOCKED = -18;
    public static final int BILLPAY_WRONG_ACCOUNTID = -19;
    public static final int NOT_HAVE_BANK_ACCOUNT = -21;
    public static final int CUSTOM_ERROR = -22;
    public static final int NOT_SUPPORT = -25;
    public static final int ALREADY_OWNED = -29;
    public static final int CANNOT_GET_FEE = -32;
    public static final int INVALID_GIFT = -37;
    public static final int REMOVE_GIFT_RULE = -42;
    public static final int UNAVAILABLE_GIFT = -43;
    public static final int FAIL_CONDITION_CHECK_PREPAID = -44;
    public static final int INVALID_BANK_ID = -38;
    public static final int INVALID_FILM_PARAMS = -39;
    public static final int INVALID_TRAN_LOCK_PARAM = -40;
    public static final int INVALID_TRAN_COMMIT_ROLLBACK_PARAM = -41;
    public static final int INVALID_PARTNER_CODE = -53;
    public static final int INVALID_PARTNER = -54;
    public static final int CHECK_RESET_PIN_ERROR = -57;

    public static int mapCode(int res) {
        switch (res) {
            case 1180:
                return Errors.NOT_HAVE_BANK_ACCOUNT;
            default:
                return res;
        }
    }

}
