package predict.stock.utils;

public class KeyUtils {

    public static class Common {
        public static final String SUB_SERVICES = "SUB_SERVICES";
        public static final String ACCOUNTS = "ACCOUNTS";
        public static final String SUB_SERVICE = "SUB_SERVICE";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String TOKEN = "TOKEN";
        public static final String TOKEN_RESPONSE = "TOKEN_RESPONSE";
        public static final String NOTIFICATION = "NOTIFICATION";
        public static final String MSG_TYPE = "msgType";
        public static final String sp_MSG = "spMsg";
        public static final String REQUEST_ID_BACKEND = "REQUEST_ID_BACKEND";
        public static final String PAY_BILL_PARTNER_SUCCESS = "PAY_BILL_PARTNER_SUCCESS";
        public static final String WAREHOUSE_RATE_LIST = "WAREHOUSE_RATE_LIST";
        public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
        public static final String STATUS_CODE = "statusCode";
        public static final String ERROR = "error";
        public static final String DATA = "data";
        public static final String CONNECTOR_ERROR_PARTNER = "Connection Partner Error!!";
        public static final String CHECKSTATUS_RESPONSE = "CHECKSTATUS_RESPONSE";
        public static final String GET_TOKEN = "GET_TOKEN";
        public static final String REQUEST = "request";
        public static final String USER_ID = "userId";
        public static final String REQUEST_ID = "requestId";
        public static final String REF1 = "reference1";
        public static final String REF2 = "reference2";
        public static final String TOTAL_AMOUNT = "totalAmount";
        public static final String DEBITOR = "debitor";
        public static final String DEBITOR_NAME = "debitorName";
        public static final String EXTRAS = "extras";
        public static final String RESULT_CODE = "resultCode";
        public static final String RESULT_MSG = "resultMessage";
        public static final String MSG_KAFKA = "msgKafka";
        public static final String TOP_UP = "TOP_UP";
        public static final String CHECK_STATUS = "CHECK_STATUS";
        public static final String DETAILS = "details";
        public static final String PREPAID = "prepaid";
    }


    public static class Core {
        public static final String LOCKED_ID = "LOCKED_ID";
        public static final String TRANS_WVP = "TRANS_WVP";
        public static final String TRANS_TOPUP = "TRANS_TOPUP";
        public static final String TRANS_CO = "TRANS_CO";
        public static final String TRANS_RB = "TRANS_RB";
        public static final String sp_BALANCE = "sp_BALANCE";
        public static final String PREPAID_IDS = "PREPAID_IDS";
        public static final String PREPAID_AMOUNT = "PREPAID_AMOUNT";
        public static final String DENY_BONUS = "deny_bonus";
        public static final String AI_DENY_BONUS = "ai_deny_bonus";
        public static final String GAMIFICATION = "GAMIFICATION";
        public static final String TAG_NAME = "tag_name";
        public static final String AI_RESPONSE = "ai_response";
    }

    public static class MultiBill {
        public static final String REDIS_PREFIX = "TELCO_MB_";
    }

    public static class Notification {
        public static final String BODY = "body";
        public static final String CAPTION = "caption";
        public static final String TYPE = "type";
        public static final String RECEIVER_NUMBER = "receiverNumber";
        public static final String EXTRA = "extra";
        public static final String CATEGORY = "category";
        public static final String SERVICE_NAME = "serviceName";
        public static final String CLASS = "_class";
        public static final String USER = "user";
        public static final String CMD_ID = "cmdId";
        public static final String TIME = "time";
        public static final String ID = "ID";
    }

    public static class Metrics {
        public static final String NAME_CALL_PARTNER = "call_partner";
        public static final String NAME_CALL_AI = "call_ai";
        public static final String TAG_NAME_GROUP = "group_name";
        public static final String NAME_WORKFLOW = "workflow";
        public static final String NAME_TASK = "task";

        public static final String TAG_RESULT = "result";
        public static final String TAG_PARTNER_RESULT = "partner_result";
        public static final String TAG_SERVICE_ID = "serviceId";
        public static final String TAG_ERROR_CODE = "error_code";
        public static final String TAG_WORKFLOW_NAME = "flow_name";
        public static final String TAG_TASK_NAME = "task_name";
    }

    public static class KeyExtra {
        public static final String KEY_VALUE_KAFKA = "VALUE_KAFKA";
        public static final String AUTH_JSON_SOCIAL = "AUTH_JSON_SOCIAL";
        public static final String PUBSUB_PROJECT = "PUBSUB_PROJECT";
        public static final String PUBSUB_TOPIC = "PUBSUB_TOPIC";
        public static final String CHAT_SHOOTER = "CHAT_SHOOTER";
    }

    public static class Api {
        public static final String TOPUP_PATH = "/topup";
        public static final String SWITCH_WAREHOUSE_PATH = TOPUP_PATH + "/switchWarehouse";
        public static final String GET_CONFIG_PATH = TOPUP_PATH + "/getConfig";
        public static final String TRAN_HIS_PATH = TOPUP_PATH + "/tranHis";
    }

    public static class SystemInfo {
        public static String IP_ADDRESS;
        public static String MODULE_NAME;
        public static boolean ENABLE_METRICS;

        static {
            try {
                IP_ADDRESS = Utils.isNullOrEmpty(System.getProperty("sp.topup.ip")) ?
                        System.getProperty("sp.topup.host") : System.getProperty("sp.topup.ip");
                if (Utils.isNullOrEmpty(IP_ADDRESS)) {
                    IP_ADDRESS = "localhost";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            MODULE_NAME = System.getProperty("moduleName") == null ? "default" : System.getProperty("moduleName");
        }
    }
}
