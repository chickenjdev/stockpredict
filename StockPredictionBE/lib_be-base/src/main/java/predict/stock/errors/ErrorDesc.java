package predict.stock.errors;

public enum ErrorDesc {
    SUCCESS("Giao dịch thành công", 0),
    NOT_HAVE_BANK_ACCOUNT("Chưa liên kết tài khoản ngân hàng",10),
    INVALID_TRAN_COMMIT_ROLLBACK_PARAM ("Tham số hoàn tiền không đúng",-14),
    CONNECTOR_ERROR("Lỗi kết nối",-80),
    SERVICE_MAINTAIN ("Hệ thống bảo trì",-91),
    IN_PROCESS("Giao dịch đang chờ xử lý",9000),
    BAD_FORMAT ("Tham số không đúng",1),
    TARGET_IS_NOT_SPECIAL ("Mục tiêu không tồn tại",47),
    OPERATION_NOT_SUPPORTED("Phương thức không hỗ trợ",68),
    QUOTE_SUCCESS("Trích dẫn thành công",600),
    SYSTEM_ERROR("Lỗi hệ thống",1006),
    CANNOT_SEND_QUEUE_SERVER("Không thể gửi vào hàng chờ",545);

    private final String desc;
    private final int val;

    ErrorDesc(String desc, int val){
        this.val = val;
        this.desc = desc;
    }

    private int getVal() {
        return val;
    }

    public static String getDescByVal(int val){
        ErrorDesc[] val1 = values();

        for (ErrorDesc errorDesc : val1) {
            if (errorDesc.getVal() == val) {
                return errorDesc.desc;
            }
        }
        return SYSTEM_ERROR.desc;
    }
}
