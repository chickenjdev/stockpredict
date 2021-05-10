package predict.stock.notification;

import java.util.Arrays;

public enum NotificationType {

    NOTI_DETAIL(1),
    NOTI_TRANSACTION(2),
    NOTI_NEW_spER(3),
    NOTI_ADVERTISE(4),
    NOTI_MONEY_REQUEST(5),
    NOTI_UPDATE_INFO(6),
    NOTI_NOTICE(7),
    NOTI_DEPOSIT_WITHDRAW_AT_PLACE(8),
    NOTI_GENERIC(9),
    NOTI_STUDENT(10),
    NOTI_123PHIM(11),
    NOTI_PAY_ONE_BILL(12),
    NOTI_SERVICE_ONE_BILL(13),
    NOTI_QR_CODE(14),
    NOTI_CASH_MONEY(15),
    NOTI_WITHDRAW_MONEY(16),
    NOTI_FIND_LOCATION(17),
    NOTI_BORROW_MONEY(18),
    NOTI_LIXI_VIEW(19),
    NOTI_LIXI_CREATE(20),
    NOTI_VOUCHER_VIEW(21),
    NOTI_VOUCHER_CREATE(22),
    NOTI_TOPUP(23),
    NOTI_SEND_MONEY(24),
    NOTI_GIFT_RECEIVE(25),
    NOTI_GIFT_REPLY(26),
    NOTI_MAP_WALLET(27),
    REMIND_EXPIRED(28),
    POPUP_INFORMATION(29),
    NOTI_SILENT(30),
    NOTI_UNMAP_WALLET(31),
    NOTI_RECEIVE_MONEY(32),
    NOTI_RECEIVE_MONEY_NONBANK(205),
    NOTI_RECEIVE_LUCKY_MONEY_NONBANK(206),
    NOTI_PHONE_CARD(33),
    NOTI_REQUEST_LIXI_SENDER(34),
    NOTI_REQUEST_LIXI_RECEIVER(35),
    NOTI_LIXI_THANKS(36),
    NOTI_GIFT_SEND(37),
    NOTI_CONFIRM_EMAIL(38),
    NOTI_BILL_REMIND(39),
    NOTI_LINK_FIM_PLUS(40),
    NOTI_NEW_spER_AND_FRIEND(41),
    NOTI_LUCKY_MONEY_CREATE(42),
    NOTI_RECEIVE_LUCKY_MONEY(43),
    NOTI_LINK_FIM_PLUS_SUCCESS(44),
    NOTI_LUCKY_MONEY_THANKS(45),
    NOTI_RECEIVED_LIXI_CODE(46),
    POPUP_CASH_BACK_LEVEL_UP(49),
    NOTI_CASHBACK_TRANS(50),
    NOTI_PIG_GOD(52),
    NOTI_SYNC_CASH_BACK(55),
    NOTI_REMIND_AUTO_DEBIT(56),
    NOTI_SYNC_TRAN_PAYMENT(57),
    NOTI_SYNC_TRAN_CASHIN(58),
    NOTI_SYNC_TRAN_CASHOUT(59),
    NOTI_SYNC_TRAN_TRANSFER(60),
    NOTI_SYNC_TRAN_CASH_BACK(61),
    GET_LAST_DAY_GIFT(66),
    CONVERT_TICKET(67),
    ROB_BOX(69),
    NOTI_GIVE_GIFT(80),
    REQUEST_MONEY_LIST(70),
    NOTI_AGENT_IDENTIFY(101),
    BANNER(115),
    NOTI_X_BANNER_BILLPAY(989),
    NFC_TOPUP(999),
    NOTI_DEAL_PACAKAGE(1000),
    GIFT_VOUCHER_RECEIVE(1100),
    NOTI_GIVE_POINT(1101),
    NOTI_GIVE_POINT_SILVER(1102),
    NOTI_GIVE_POINT_GOLD(1103),
    NOTI_VOUCHER_CASHBACK_sp(1502),
    CONVERT_VOUCHER(1911),
    NOTI_CHAT_GROUP(1988),
    NOTI_CHAT_PERSONAL(1987),
    NOTI_CHAT_PERSONAL_CORE(1989),
    NOTI_CHAT_OPEN_ROOM(1990),
    REGISTER_EVENT_POWER(2000),
    REMIND_TOPUP_DATA(2001),
    REMIND_UNPAID_BILL_T7(2002),
    NOTI_RECEIVE_LIXI(2020),
    NOTI_RATING_APP(2040),
    NOTI_POPUP_AGENT_NOTRANS(3000),
    POPUP_INFORMATION_NEW(3003),
    POPUP_UP_POINT_SILVER(11021),
    POPUP_UP_POINT_GOLD(11031),
    NOTI_SYNCH_BILL(190830),
    POPUP_BLOCK_NONBANK_RECEIVER(9001),
    NOTI_REMIND_NONBANK_RECEIVER_MAPBANK(9002),
    NOTI_ROUTE_TO_AI(9696),
    UNSUPPORTED_VALUE(-1),
    NOTI_PRIVATE_CHAT(1989),
    TT23_CREDITOR_TRANSFER_NONKYC(207),
    REMIND_AFTER_SERVICE_AVAILABLE(208),
    NOTI_PIG_ANNOUNCEMENT(4004);


    private int value;

    NotificationType(int value) {
        this.value = value;
    }

    public static NotificationType parse(int value) {
        NotificationType[] values = NotificationType.values();
        return Arrays.stream(values).filter(t -> t.getValue() == value).findFirst().orElse(UNSUPPORTED_VALUE);
    }

    public int getValue() {
        return value;
    }
}