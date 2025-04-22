package net.dvdplay.base;

public class DvdplayBase {
   public static final int SERVER_CMD_ID_GET_GENRE = 1;
   public static final int SERVER_CMD_ID_GET_TITLE_DETAIL = 2;
   public static final int SERVER_CMD_ID_GET_GROUP_CODE = 3;
   public static final int SERVER_CMD_ID_GET_TRANSLATION = 4;
   public static final int SERVER_CMD_ID_GET_RATING = 5;
   public static final int SERVER_CMD_ID_GET_RATING_SYSTEM = 6;
   public static final int SERVER_CMD_ID_GET_REGULAR_PRICING = 7;
   public static final int SERVER_CMD_ID_GET_SPECIAL_PRICING = 8;
   public static final int SERVER_CMD_ID_GET_STATIC_PLAYLIST = 9;
   public static final int SERVER_CMD_ID_GET_LOCALE = 10;
   public static final int SERVER_CMD_ID_GET_MEDIA_PLAYLIST = 11;
   public static final int SERVER_CMD_ID_GET_AEM_PROPERTIES = 12;
   public static final int SERVER_CMD_ID_GET_DISC_DETAIL = 13;
   public static final int SERVER_CMD_ID_GET_PAYMENT_CARD = 14;
   public static final int SERVER_CMD_ID_GET_TITLE_TYPE = 15;
   public static final int SERVER_CMD_ID_GET_TITLE_TYPE_CAP = 16;
   public static final int SERVER_CMD_ID_GET_SECURITY = 17;
   public static final int SERVER_CMD_ID_GET_PRIVILEGES = 18;
   public static final int SERVER_CMD_ID_GET_BAD_SLOT = 19;
   public static final int SERVER_CMD_ID_GET_SLOT_OFFSET = 20;
   public static final int SERVER_CMD_ID_GET_ALL_DATA = 21;
   public static final int SERVER_CMD_ID_SEND_LOGS = 22;
   public static final int SERVER_CMD_ID_PURGE_TITLE_DETAIL_RECORDS = 23;
   public static final int SERVER_CMD_ID_PURGE_DISC_DETAIL_RECORDS = 24;
   public static final int SERVER_CMD_ID_DEBUG_MODE_ON = 25;
   public static final int SERVER_CMD_ID_DEBUG_MODE_OFF = 26;
   public static final int SERVER_CMD_ID_DOWNLOAD_FILE = 27;
   public static final int SERVER_CMD_ID_RESTART = 28;
   public static final int SERVER_CMD_ID_SEND_QUEUE = 29;
   public static final int SERVER_CMD_ID_RESET_SYNC_DATE = 30;
   public static final int SERVER_CMD_ID_DISABLE_RENTALS = 31;
   public static final int SERVER_CMD_ID_DISABLE_RETURNS = 32;
   public static final int SERVER_CMD_ID_ENABLE_RENTALS = 33;
   public static final int SERVER_CMD_ID_ENABLE_RETURNS = 34;
   public static final int SERVER_CMD_ID_CLEAR_LOCK_FILES = 36;
   public static final int SERVER_CMD_ID_RESET_STATUS = 37;
   public static final int SERVER_CMD_ID_GET_ABOUT_FRANCHISE = 38;
   public static final int SERVER_CMD_ID_REINIT_HARDWARE = 39;
   public static final int SERVER_CMD_ID_PURGE_MEDIA_DETAIL_RECORD = 40;
   public static final int SERVER_CMD_ID_RESET_MEDIA_DETAIL_RECORD_IMAGE = 41;
   public static final int SERVER_CMD_ID_RESET_MEDIA_DETAIL_RECORD_MEDIA = 42;
   public static final int SERVER_CMD_ID_GET_POLL = 43;
   public static final int SERVER_CMD_ID_UPLOAD_SLOT = 44;
   public static final int SERVER_CMD_ID_UPDLOAD_SERVO = 45;
   public static final int SERVER_CMD_ID_URGENT_SHUTDOWN_COMPUTER = 46;
   public static final int SERVER_CMD_ID_URGENT_RESTART_COMPUTER = 47;
   public static final int SERVER_CMD_ID_STOP_STROKING_WATCHDOG = 48;
   public static final int SERVER_CMD_ID_PURGE_QUEUE_JOB = 49;
   public static final int SERVER_CMD_ID_REBUILD_QUEUE = 50;
   public static final int SERVER_CMD_ID_UPLOAD_FILE = 51;
   public static final int DISC_STATUS_NULL = 0;
   public static final int DISC_STATUS_RENTED = 1;
   public static final int DISC_STATUS_SOLD = 2;
   public static final int DISC_STATUS_IN = 3;
   public static final int DISC_STATUS_NOT_DISPENSED = 4;
   public static final int DISC_STATUS_UNKNOWN = 5;
   public static final int DISC_STATUS_REMOVED = 6;
   public static final int DISC_STATUS_AUTHORIZATION = 7;
   public static final int DISC_STATUS_MISSING = 8;
   public static final int DISC_STATUS_FOUND = 9;
   public static final int DISC_STATUS_MAX_OUT = 10;
   public static final int DISC_STATUS_SPECIAL_RETURN = 11;
   public static final int TITLE_TYPE_CAP_TYPE_ID_FIRST_TIME_USER = 1;
   public static final int TITLE_TYPE_CAP_TYPE_ID_RETURN_USER = 2;
   public static final int TITLE_TYPE_CAP_TYPE_ID_MAX_CHARGE = 3;
   public static final int ACTIVE_TITLES = 1;
   public static final int INACTIVE_TITLES = 2;
   public static final int ALL_TITLES = 3;
   public static final int TITLE_QUERY = 4;
   public static final int ACTIVE_DISCS = 1;
   public static final int INACTIVE_DISCS = 2;
   public static final int ALL_DISCS = 3;
   public static final int AEM_DAILY_REPORT = 1;
   public static final int USED_ONCE_PROMO_REPORT = 2;
   public static final int REUSABLE_PROMO_REPORT = 3;
   public static final int DISC_LIST_REPORT = 4;
   public static final int CUSTOMER_LIST_REPORT = 5;
   public static final int TITLE_LIST_REPORT = 6;
   public static final int AGGREGATED_DAILY_REPORT = 7;
   public static final int ACTIVE_STATS_REPORT = 8;
   public static final int INACTIVE_STATS_REPORT = 9;
   public static final int LATE_DISC_REPORT = 10;
   public static final int MAX_PERIOD_DISC_REPORT = 11;
   public static final int SPECIAL_RETURN_DISC_REPORT = 12;
   public static final int PROMO_LIST_REPORT = 13;
   public static final int PROMO_STATS_REPORT = 14;
   public static final int PROMO_STATS_CSV_REPORT = 15;
   public static final String TOP_LINK_BG_COLOR = "#F1F1F1";
   public static final String TOP_LINK_DIVIDER_COLOR = "#AAAAAA";
   public static final String TOP_LINK_DIVIDER_LIGHT_COLOR = "#CCCCCC";
   public static final String HEADER_BANNER_BG_COLOR = "#9999cc";
   public static final String MARKED_FOR_REMOVAL_BG_COLOR = "#F1F1F1";
   public static final String WARNING_COLOR = "#FF6600";
   public static final String DEFAULT_TITLE_LIST = "[0-9]";
   public static final String NUMBER_STRING_QUOTE = "0-9";
   public static final String ACTIVE_TITLES_STRING = "Active Titles";
   public static final String INACTIVE_TITLES_STRING = "Inactive Titles";
   public static final String ALL_TITLES_STRING = "All Titles";
   public static final String ALL_TITLES_WORD = "All";
   public static final String[] CHARS = new String[]{
      "0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "All"
   };
   public static final String[] DISC_CODE_STATUS_NAMES = new String[]{
      "", "Rented", "Sold", "In", "Not Dispensed", "Unknown", "Removed", "Authorization", "Missing", "Found", "Max Period", "Special Return"
   };
   public static final String UNKNOWN_TITLE = "[unknown]";
   public static final int OPERATION_RENT = 1;
   public static final int OPERATION_SALE = 2;
   public static final int OPERATION_RETURN = 3;
   public static final int OPERATION_REMOVE = 4;
   public static final int OPERATION_MISSING = 5;
   public static final int OPERATION_FOUND = 6;
   public static final int TAX_RATE_DECIMAL_PLACES = 4;
   public static final int MAX_SHOPPING_CART_ITEMS = 4;
   public static final int EMAIL_MAX_LENGTH = 50;
   public static final String[] PROCESS_PAYMENT_PARAMITEM_FIELD_NAMES = new String[]{"Id", "ParamValue"};
   public static final String[] PROCESS_PAYMENT_PARAMITEM_FIELD_TYPES = new String[]{"int", "String"};
   public static final String[] PROCESS_PAYMENT_LINEITEM_FIELD_NAMES = new String[]{"LineItemId", "DiscDetailId", "Price", "OperationId", "DueTime"};
   public static final String[] PROCESS_PAYMENT_LINEITEM_FIELD_TYPES = new String[]{"int", "int", "BigDecimal", "int", "Date"};
   public static final String[] PROCESS_PAYMENT_PROMOITEM_FIELD_NAMES = new String[]{"PromoCode", "PromoValue"};
   public static final String[] PROCESS_PAYMNET_PROMOITEM_FIELD_TYPES = new String[]{"String", "BigDecimal"};
   public static final String[] PROCESS_PAYMNET_TRACKITEM_FIELD_NAMES = new String[]{"Track"};
   public static final String[] PROCESS_PAYMNET_TRACKITEM_FIELD_TYPES = new String[]{"String"};
   public static final String[] DISC_RETURN_DISCITEM_FIELD_NAMES = new String[]{"DiscCode", "GroupCode", "Slot", "DTUpdated"};
   public static final String[] DISC_RETURN_DISCITEM_FIELD_TYPES = new String[]{"String", "String", "int", "Date"};
   public static final String[] CONFIRM_DISPENSE_LINEITEM_FIELD_NAMES = new String[]{"LineItemId", "DiscDetailId", "DispensedDiscDetailId"};
   public static final String[] CONFIRM_DISPENSE_LINEITEM_FIELD_TYPES = new String[]{"int", "String", "String"};
   public static final String[] CONFIRM_DISPENSE_POLLITEM_FIELD_NAMES = new String[]{"PollId", "Response"};
   public static final String[] CONFIRM_DISPENSE_POLLITEM_FIELD_TYPES = new String[]{"int", "int"};
   public static final String[] DISC_REMOVE_DISCITEM_FIELD_NAMES = new String[]{"DiscDetailId", "DiscCode", "GroupCode", "DTUpdated"};
   public static final String[] DISC_REMOVE_DISCITEM_FIELD_TYPES = new String[]{"int", "String", "String", "Date"};
   public static final String[] PING_ACK_FIELD_NAMES = new String[]{"SequenceId", "CmdAck"};
   public static final String[] PING_ACK_FIELD_TYPES = new String[]{"int", "int"};
   public static final int AEM_HARDWARE_ERROR = 1;
   public static final int AEM_DATA_ERROR = 2;
   public static final int AEM_QUEUE_ERROR = 4;
   public static final int AEM_INVENTORY_ERROR = 8;
   public static final int AEM_PERSISTENCE_ERROR = 16;
   public static final int AEM_INTERNAL_ERROR = 32;
   public static final int AEM_SCREEN_ERROR = 64;
   public static final int AEM_REQUEST_ERROR = 128;
   public static final int AEM_POWER_ERROR = 256;
   public static final int AEM_TEMPERATURE_ERROR = 512;
   public static final String REQUEST_VERSION = "2.0";
   public static final String REQUEST_CHECK_PROMO_CODE_NAME = "CHECK_PROMO_CODE";
   public static final String REQUEST_PROCESS_PAYMENT_NAME = "PROCESS_PAYMENT";
   public static final String REQUEST_CONFIRM_DISPENSE_NAME = "CONFIRM_DISPENSE";
   public static final String REQUEST_DISC_RETURNED_NAME = "DISC_RETURNED";
   public static final String REQUEST_DISC_REMOVED_NAME = "DISC_REMOVED";
   public static final String REQUEST_DISC_MISSING_NAME = "DISC_MISSING";
   public static final String REQUEST_DISC_FOUND_NAME = "DISC_FOUND";
   public static final String REQUEST_GET_RECEIPT = "GET_RECEIPT";
   public static final String REQUEST_MONITOR_SERVER = "MONITOR_SERVER";
   public static final String REQUEST_GARBAGE_COLLECT = "GARBAGE_COLLECT";
   public static final String REQUEST_CHECK_PROMO_CODE = "CHECK_PROMO_CODE";
   public static final String GET_RECEIPT_TRANSACTIONID = "TransactionID";
   public static final String GET_RECEIPT_RECEIPT_TYPE = "ReceiptType";
   public static final String GET_RECEIPT_PROMOVALUE = "PromoValue";
   public static final String GET_RECEIPT_PROMOCODE = "PromoCode";
   public static final String GET_RECEIPT_PROMOVALIDTO = "PromoValidTo";
   public static final String GET_RECEIPT_TYPE_RENTAL = "Rental";
   public static final String GET_RECEIPT_TYPE_OVERDUE = "OverDue";
   public static final String GET_RECEIPT_TYPE_REFUND = "Refund";
   public static final String GET_RECEIPT_TYPE_PROMONOTIFY = "PromoNotify";
   public static final String RECEIPT_START_DATE = "ReceiptStartDate";
   public static final String RECEIPT_SUBJECT = "Subject";
   public static final String[] GET_RECEIPT_LINE_STATUS = new String[]{"", "Rented", "Purchased", "Not Dispensed", "OverDue", "Refund"};
   public static final String[] GET_RECEIPT_LINE_STATUS1 = new String[]{"", "Rental fee: ", "DVD purchase", "Not Dispensed", "Extra day fee: ", "Refund"};
   public static final int GET_RECEIPT_LINE_ERROR = 0;
   public static final int GET_RECEIPT_LINE_RENTED = 1;
   public static final int GET_RECEIPT_LINE_PURCHASED = 2;
   public static final int GET_RECEIPT_LINE_NOTDISPENSED = 3;
   public static final int GET_RECEIPT_LINE_OVERDUE = 4;
   public static final int GET_RECEIPT_LINE_REFUNDED = 5;
   public static final String GET_RECEIPT_LINE_NIGHT = " night";
   public static final String GET_RECEIPT_LINE_NIGHTS = " nights";
   public static final String GET_RECEIPT_LINE_DAY = " day";
   public static final String GET_RECEIPT_LINE_DAYS = " days";
   public static final String GET_RECEIPT_LINE_PER = " per ";
   public static final String RECEIPT_CUSTOMIZED_FILENAME_EXTENTION = ".txt";
   public static final String RECEIPT_TEMPLATE_FILENAME_EXTENTION = ".html";
   public static final String GET_RECEIPT_SUBJECT_RENTAL = "DVDPlay Receipt";
   public static final String GET_RECEIPT_SUBJECT_OVERDUE = "Your Recent DVDPlay Rental";
   public static final String GET_RECEIPT_SUBJECT_REFUND = "DVDPlay Refund Receipt";
   public static final String GET_RECEIPT_SUBJECT_PROMONOTIFY = "DVDPlay PromoCode";
   public static final String GET_RECEIPT_RESULT_RESULT = "Result";
   public static final String GET_RECEIPT_RESULT_MESSAGE = "Message";
   public static final String MONITOR_SERVER_RESULT_RESULT = "Result";
   public static final String MONITOR_SERVER_RESULT_MESSAGE = "Message";
   public static final String GARBAGE_COLLECT_RESULT_RESULT = "Result";
   public static final String GARBAGE_COOLECT_RESULT_MESSAGE = "Message";
   public static final String PROMO_CODE = "PROMO_CODE";
   public static final String REQUEST_ID = "REQUEST_ID";
   public static final String PAYMENT_CARD_TYPE_ID = "PaymentCardTypeId";
   public static final String TIMEZONE_UTC = "UTC";
   public static final int TIMEZONE_UTC_OFFSET = 0;
   public static final int PROCESSPAYMENT_RETURNCODE_MAX_DISC = -1000;
   public static final int PROCESSPAYMENT_RETURNCODE_DECLINED = -1001;
   public static final int PROCESSPAYMENT_RETURNCODE_EXPIREDCARD = -1002;
   public static final int PROCESSPAYMENT_RETURNCODE_INVALIDACCOUNT = -1003;
   public static final int PROCESSPAYMENT_RETURNCODE_COMM = -1004;
   public static final int PROCESSPAYMENT_RETURNCODE_UNSPECIFIED = -1005;
   public static final int PROCESSPAYMENT_RETURNCODE_MAX_SALE = -1006;
   public static final int PROCESSPAYMENT_RETURNCODE_INVALIDCARD = -1007;
   public static final int PROCESSPAYMENT_RETURNCODE_INVALID_VPARAM = -1008;
   public static final int PROCESSPAYMENT_RETURNCODE_DISABLED_CARD = -1009;
   public static final String PROCESSPAYMENT_RETURNMSG_MAX_DISC = "ERROR: Max Disc reached";
   public static final String PROCESSPAYMENT_RETURNMSG_INVALIDCARD = "ERROR: Invalid Payment Card";
   public static final String PROCESSPAYMENT_RETURNMSG_MAX_SALE = "ERROR: Max Sale Disc reached";
   public static final String PROCESSPAYMENT_RETURNMSG_DISABLED_CARD = "ERROR: PaymentCard is disabled";
   public static final String CONFIRMDISPENSE_RETURNMSG_INVALID_PARAM = "ERROR: Transaction or PaymentCard not found";
   public static final String PROPERTY_NAME_HOST = "FFDataModule_Location";
   public static final String PROPERTY_NAME_USER = "FFDataModule_User";
   public static final String PROPERTY_NAME_PASSWORD = "FFDataModule_Password";
   public static final String DISC_RETURN_RESULT_RESULT = "Result";
   public static final String DISC_RETURN_RESULT_MESSAGE = "Message";
   public static final String DISC_RETURN_GET_RESPONSE_NAME = "Response";
   public static final String[] DISC_RETURN_GET_RESPONSE_FIELD_NAMES = new String[]{"Result", "Message"};
   public static final String[] DISC_RETURN_GET_RESPONSE_FIELD_TYPES = new String[]{"int", "String"};
   public static final String DISC_RETURN_GET_DISCRECORD_NAME = "DiscRecord";
   public static final String DISC_RETURN_GET_TITLERECORD_NAME = "TitleRecord";
   public static final String DISC_REMOVED_RESULT_RESULT = "Result";
   public static final String DISC_REMOVED_RESULT_MESSAGE = "Message";
   public static final String DISC_REMOVED_RESULT_RESPONSE = "Response";
   public static final String[] DISC_REMOVED_RESULT_FIELD_NAMES = new String[]{"Result", "Message"};
   public static final String[] DISC_REMOVED_RESULT_FIELD_TYPES = new String[]{"int", "String"};
   public static final String PROCESS_PAYMENT_RESULT_RESULT = "Result";
   public static final String PROCESS_PAYMENT_RESULT_MESSAGE = "Message";
   public static final String PROCESS_PAYMENT_RESULT_EMAILADDR = "EmailAddr";
   public static final String PROCESS_PAYMENT_RESULT_TRANSACTIONID = "TransactionId";
   public static final String REQUEST_SYNCH = "SYNCH";
   public static final String REQUEST_PING = "PING";
   public static final String REQUEST_ASYNCH = "ASYNCH";
   public static final String SYNC_REQUEST_NAME_PROCESSPAYMENT = "ProcessPayment";
   public static final String PROCESSPAYMENT_TRACK = "Track";
   public static final String PROCESSPAYMENT_VERIFICATION_PARAM = "VerificationParam";
   public static final String PROCESSPAYMENT_LOCALEID = "LocaleId";
   public static final String PROCESSPAYMENT_PAYMENTCARDCATEGORYID = "PaymentCardTypeID";
   public static final String PROCESSPAYMENT_REQUESTID = "RequestId";
   public static final String PROCESSPAYMENT_TRANSACTIONTIME = "TransactionTime";
   public static final String PROCESSPAYMENT_SUBTOTAL = "SubTotal";
   public static final String PROCESSPAYMENT_TAXAMOUNT = "TaxAmount";
   public static final String PROCESSPAYMENT_GRANDTOTAL = "GrandTotal";
   public static final String PROCESSPAYMENT_DTUPDATED = "DTUpdated";
   public static final String PROCESSPAYMENT_LINEITEM = "LineItem";
   public static final String PROCESSPAYMENT_PROMOLINEITEM = "Promo";
   public static final String LINEITEM_COL_LINEITEMID = "LineItemId";
   public static final String LINEITEM_COL_DISCDETAILID = "DiscDetailId";
   public static final String LINEITEM_COL_PRICE = "Price";
   public static final String LINEITEM_COL_OPERATIONID = "OperationId";
   public static final String LINEITEM_COL_TAXABLE = "Taxable";
   public static final String PROMOITEM_COL_PROMOCODE = "PromoCode";
   public static final String PROMOITEM_COL_PROMOVALUE = "PromoValue";
   public static final String CONFIRMDISPENSE_TRANSACTIONID = "TransactionId";
   public static final String CONFIRMDISPENSE_LOCALEID = "LocaleId";
   public static final String CONFIRMDISPENSE_REQUESTID = "RequestId";
   public static final String CONFIRMDISPENSE_EMAILADDR = "EmailAddr";
   public static final String CONFIRMDISPENSE_SUBTOTAL = "SubTotal";
   public static final String CONFIRMDISPENSE_TAXAMOUNT = "TaxAmount";
   public static final String CONFIRMDISPENSE_GRANDTOTAL = "GrandTotal";
   public static final String CONFIRMDISPENSE_DTUPDATED = "DTUpdated";
   public static final String CONFIRMDISPENSE_LINEITEM = "LineItem";
   public static final String CONFIRMDISPENSE_PROMOLINEITEM = "Promo";
   public static final String CONFIRMDISPENSE_POLL = "Poll";
   public static final String DISCRETURNED_RCSETNAME_DISCRETURNED = "DiscItem";
   public static final String DISCRETURNED_REQUESTID = "RequestId";
   public static final String DISCRETURNED_VERSION = "VERSION";
   public static final String DISCRETURNED_RCSET_FIELD_GROUPCODE = "GroupCode";
   public static final String DISCRETURNED_RCSET_FIELD_DISCCODE = "DiscCode";
   public static final String DISCRETURNED_RCSET_FIELD_SLOT = "Slot";
   public static final String DISCRETURNED_RCSET_FIELD_DTUPDATED = "DTUpdated";
   public static final int DISC_FOUND = 9;
   public static final String DISCREMOVED_REQUESTID = "RequestId";
   public static final String DISCREMOVED_RCSETNAME_DISCITEM = "DiscItem";
   public static final String DISCREMOVED_RCSET_FIELD_DISCDETAILID = "DiscDetailId";
   public static final String DISCREMOVED_RCSET_FIELD_GROUPCODE = "GroupCode";
   public static final String DISCREMOVED_RCSET_FIELD_DISCCODE = "DiscCode";
   public static final String DISCREMOVED_RCSET_FIELD_DTUPDATED = "DTUpdated";
   public static final int DISC_MISSING = 8;
   public static final String PING_KIOSKSTATUS = "KioskStatus";
   public static final String PING_ACK = "Ack";
   public static final int PING_KIOSKSETUP_ACTION_SENDLOG1 = 1;
   public static final int PING_KIOSKSETUP_ACTION_SENDLOG2 = 2;
   public static final int PING_KIOSKSETUP_ACTION_NOCOMMAND = 0;
   public static final String PING_RCSET_FIELD_SEQUENCEID = "SequenceId";
   public static final String PING_RCSET_FIELD_CMDACK = "CmdAck";
   public static final String PING_SERVERCMD_RCSET_FIELD_SEQID = "SequenceId";
   public static final String PING_SERVERCMD_RCSET_FIELD_CMDID = "CmdId";
   public static final String PING_SERVERCMD_RCSET_FIELD_ARG = "Arg";
   public static final String PING_SERVERCMD_RCSET_FIELD_SEQID_TYPE = "int";
   public static final String PING_SERVERCMD_RCSET_FIELD_CMDID_TYPE = "int";
   public static final String PING_SERVERCMD_RCSET_FIELD_ARG_TYPE = "String";
   public static final int SUCCESS = 0;
   public static final int FAILURE = -1;
   public static final int NO_RETRY_START_RANGE = -100;
   public static final int NO_RETRY_END_RANGE = -200;
   public static final String SUCCESS_STRING = "success";
   public static final String FAILURE_STRING = "failure";
   public static final String STRING_EMPTY = "";
   public static final String STRING_F = "F";
   public static final String STRING_T = "T";
   public static final String STRING_RESULT = "Result";
   public static final String STRING_MESSAGE = "Message";
   public static final String STRING_SERVER_CMD = "ServerCmd";
   public static final String PAYMENTCARD_CATEGORY_CREDIT = "Credit";
   public static final String PAYMENTCARD_CATEGORY_DEBIT = "Debit";
   public static final String PAYMENTCARD_CATEGORY_CASH = "Cash";
   public static final int PAYMENTCARD_CATEGORYID_INVALID = 0;
   public static final int PAYMENTCARD_CATEGORYID_CREDIT = 1;
   public static final int PAYMENTCARD_CATEGORYID_DEBIT = 2;
   public static final int PAYMENTCARD_CATEGORYID_CASH = 3;
   public static final String PAYMENTCARD_RCSET_FIELD_TRACK = "Track";
   public static final String PAYMENTCARD_RCSET_FIELD_PARAMVALUE = "ParamValue";
   public static final String LINEITEM_RCSET_FIELD_LINEITEMID = "LineItemId";
   public static final String LINEITEM_RCSET_FIELD_DISCDETAILID = "DiscDetailId";
   public static final String LINEITEM_RCSET_FIELD_DISPENSEDDISCDETAILID = "DispensedDiscDetailId";
   public static final String LINEITEM_RCSET_FIELD_PRICE = "Price";
   public static final String LINEITEM_RCSET_FIELD_OPERATIONID = "OperationId";
   public static final String LINEITEM_RCSET_FIELD_DUETIME = "DueTime";
   public static final String PROMOLINEITEM_RCSET_FIELD_PROMOCODE = "PromoCode";
   public static final String PROMOLINEITEM_RCSET_FIELD_PROMOVALUE = "PromoValue";
   public static final String POLL_RCSET_FIELD_POLLID = "PollId";
   public static final String POLL_RCSET_FIELD_RESPONSE = "Response";
   public static final int INVALID = -1;
   public static final int PAYMENTCARDTYPE_VISA = 43;
   public static final int PAYMENTCARDTYPE_MASTERCARD = 42;
   public static final int PAYMENTCARDTYPE_AMERICAN_EXPRESS = 41;
   public static final int PAYMENTCARDTYPE_DISCOVER = 44;
   public static final int TRANSACTION_TYPE_REGULAR = 1;
   public static final int TRANSACTION_TYPE_RENEWAL = 2;
   public static final int TRANSACTION_TYPE_REFUND = 3;
   public static final int TRANSACTION_MODE_AUTHORIZE = 1;
   public static final int TRANSACTION_MODE_CAPTURE = 2;
   public static final int TRANSACTION_MODE_CREDIT = 3;
   public static final String TRANSACTION_TYPE_A = "A";
   public static final String TRANSACTION_TYPE_D = "D";
   public static final String TRANSACTION_TYPE_C = "C";
   public static final int TRANSACTION_ENTRY_TYPE_SWIPE = 1;
   public static final int TRANSACTION_ENTRY_TYPE_MANUAL = 2;
   public static final int TRANSACTION_STATUS_AUTH_REQ_ARRIVED = 2;
   public static final int TRANSACTION_STATUS_AUTH_PROCESS_EXECUTED = 4;
   public static final int TRANSACTION_STATUS_AUTH_PROCESS_APPROVED = 8;
   public static final int TRANSACTION_STATUS_FINAL_REQ_ARRIVED = 16;
   public static final int TRANSACTION_STATUS_FINAL_REQ_DISPENSED = 32;
   public static final int TRANSACTION_STATUS_FINAL_PROCESS_EXECUTED = 64;
   public static final int TRANSACTION_STATUS_FINAL_PROCESS_APPROVED = 128;
   public static final int TRANSACTION_STATUS_EMAIL_EXECUTED = 256;
   public static final int TRANSACTION_STATUS_EMAIL_SUCCESS = 512;
   public static final int LINEITEM_TYPE_REGULAR = 1;
   public static final int LINEITEM_TYPE_PROMO = 3;
   public static final int LINEITEM_TYPE_RENEWAL = 2;
   public static final int PAYMENT_PROCESSOR_VENDOR_VERSIGN = 1;
   public static final int PAYMENT_PROCESSOR_VENDOR_CONCORD = 2;
   public static final int DISCHISTORY_ACTION_IN = 1;
   public static final int DISCHISTORY_ACTION_OUT = 2;
   public static final int OVERDUE_STATUS_NOT_APPLICABLE = 0;
   public static final int OVERDUE_STATUS_NO_OVERDUE = 1;
   public static final int OVERDUE_STATUS_CHARGE_SUCCESS = 2;
   public static final int OVERDUE_STATUS_CHARGE_FAIL = 3;
   public static final int OVERDUE_STATUS_START_CHARGE = 4;
   public static final int OVERDUE_STATUS_STOP_CHARGE = 5;
   public static final String TASK_HEARTBEAT = "HeartbeatMonitor";
   public static final int TASK_SKIP_TRANS_CHECK = 96;
   public static final int TASK_SKIP_OPT_CHECK = 4;
   public static final String VALUE_EMAILALERT_CONTENT_ALERT_REASON = "<alert_reason>";
   public static final String VALUE_EMAILALERT_CONTENT_LAST_HEARTBEAT = "<last_heartbeat>";
   public static final String VALUE_EMAILALERT_CONTENT_KIOSK_ID = "<kiosk_ID>";
   public static final String VALUE_EMAILALERT_CONTENT_KIOSK_NAME = "<kiosk_name>";
   public static final String VALUE_EMAILALERT_DEFAULT_ALERT_REASON = "The AEM not communicating with the server";
   public static final String VALUE_TRX_EMAILALERT_DEFAULT_ALERT_REASON = "Transaction status error";
   public static final int VALUE_EMAILALERT_STATUS_0 = 0;
   public static final int VALUE_STATUS_OLD_KIOSK_SOFTWARE = -1;
   public static final String VALUE_EMAILALERT_EXTERNAL = "External";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_TRX_CHECKTIME = "task.servertrx.checktime";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_TRX_CHECKTIME_DEVIATION = "task.servertrx.checktime.deviation";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_TRX_CONTENT = "task.servertrx.content";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_OPT_CHECKTIME = "task.serveropt.checktime";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_OPT_CONTENT = "task.serveropt.content";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_CHECKTIME = "task.heartbeat.checktime";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_THRESHOLD = "task.heartbeat.threshold";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_CONTENT = "task.heartbeat.content";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_CONTENT_PATH = "task.heartbeat.content.path";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_CONTENT_FILENAME = "task.heartbeat.content.filename";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_DEBUG = "task.heartbeat.debug";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_ALERT_FROM = "task.heartbeat.alertfrom";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_HEARTBEAT_ALERTMAIL = "task.heartbeat.alertemail";
   public static final String SERVERCONFIG_NAME_TASK_SERVER_MAIL_SMTP_HOST = "mail.smtp.host";
   public static final String SERVERCONFIG_NAME_TASK_EOD_START_HOUR = "task.eod.start.hour";
   public static final String SERVERCONFIG_NAME_TASK_EOD_START_MINUTE = "task.eod.start.minute";
   public static final String SERVERCONFIG_NAME_TASK_EOD_FREQUENCY = "task.eod.frequency";
   public static final String SERVERCONFIG_NAME_RECEIPT_CUSTOMIZED_TEXT_PATH = "receipt.customized.text.path";
   public static final String SERVERCONFIG_NAME_TASK_MONITOR_TASK_CHECKTIME = "task.monitortask.checktime";
   public static final String SERVERCONFIG_NAME_TASK_MONITOR_TASK_THRESHOLD = "task.monitortask.threshold";
   public static final String SERVERCONFIG_NAME_TASK_MONITOR_TASK_EMAIL_TEMPLATE = "task.monitortask.email.template";
   public static final String SERVERCONFIG_NAME_TASK_MONITOR_TASK_ALERT_FROM = "task.monitortask.alertfrom";
   public static final String SERVERCONFIG_NAME_TASK_MONITOR_TASK_ALERT_CC = "task.monitortask.alertcc";
   public static final String SERVERCONFIG_NAME_SERVERNAME = "ServerName";
   public static final String SERVERCONFIG_NAME_RECEIPT_URLHOST = "receipt.urlhost";
   public static final int REFUND_STATUS_FAILED_INVALID_EXPDATE_CODE = 24;
   public static final String REFUND_STATUS_FAILED_INVALID_EXPDATE_TXT = "The payment card has invalid expiration date.";
   public static final String REFUND_STATUS_FAILED_TXT = "System error. Refund transaction failed.";
   public static final String REFUND_STATUS_ERROR_TXT = "System error. Error found on refund transaction.";
   public static final int SECONDS_PER_MINUTE = 60;
   public static final int MILLISEC_PER_SECONDS = 1000;
   public static final int PROPERTY_TYPE_ID_DISABLE_BUY = 3;
   public static final int MEDIA_TYPE_ID_TITLE = 1;
   public static final int MEDIA_TYPE_ID_AD = 2;
   public static final int MEDIA_TYPE_ID_PAYMENT = -1;
   public static final int MEDIA_TYPE_ID_FRANCHISE_LOGO = -2;
   public static final int VERIFICATION_TYPE_NONE = 0;
   public static final int VERIFICATION_TYPE_ZIP_CODE = 1;
   public static final int TITLE_TYPE_MOVIE = 1;
   public static final int TITLE_TYPE_GAME = 3;
   public static final int PROMO_VALID_DURATION = 90;
   public static final int LAST_HOUR_OF_DAY = 23;
   public static final int LAST_MINUTE_OF_DAY = 59;
   public static final int LAST_SECOND_OF_DAY = 59;
   public static final int LAST_MILLISECOND_OF_DAY = 999;
   public static final String EMAIL_CONTENT_TYPE_TEXTHTML = "text/html";
   public static final String EMAIL_CONTENT_TYPE_TEXTPLAIN = "text/plain";
   public static final String EMAIL_PROPERTY_MAIL_SMTP_HOST = "mail.smtp.host";
   public static final String PROCESSOR_AVSZIP_IGNORE_AVSZIP_X = "ZI";
   public static final String PROCESSOR_AVSZIP_NOT_IGNORE_AVSZIP_X = "ZX";
   public static final String VERISIGN_AVSZIP_N = "N";
   public static final String VERISIGN_AVSZIP_Y = "Y";
   public static final String VERISIGN_AVSZIP_X = "X";
   public static final String CONCORD_AVSZIP_N = "N";
   public static final String CONCORD_AVSZIP_Y = "Z";
   public static final String CONCORD_AVSZIP_X = "S";
   public static final String CONCORD_AVSZIP_U = "U";
   public static final String CONCORD_AVSZIP_R = "R";
   public static final String CONCORD_AVSZIP_W = "W";
   public static final int PROCESSOR_CONFIG_ZIP_ON = 1;
   public static final int PROCESSOR_CONFIG_MERCH_ON = 2;
   public static final int PROCESSOR_CONFIG_SWIPE_ON = 4;
   public static final String AMPERSAND = "&";
   public static final String SERVER_NAME = "ServerName";
   public static final String TRACK_SENTINEL_PERCENT = "%";
   public static final String TRACK_SENTINEL_SEMICOLON = ";";
   public static final String TRACK_SENTINEL_QUESTION_MARK = "?";
   public static final int HEARTBEAT_COMM_ALERT_NONE = 0;
   public static final int HEARTBEAT_COMM_ALERT_APP = 1;
   public static final String PAYMENTCARD_SKIP_LIMITCHECK = "T";
   public static final int PAYMENTCARD_ENABLE_BIT_INT_VALUE = 0;
   public static final int PAYMENTCARD_DISABLE_BIT_INT_VALUE = 1;
   public static final int ROLE_ID_ADMIN = 5;
   public static final int ROLE_ID_FACTORY = 6;
   public static final int ROLE_ID_OPERATOR = 7;
   public static final int ROLE_ID_LOCATION = 8;
   public static final int AEM_MAIN_SCREEN_BUTTON_NONE = 0;
   public static final int AEM_MAIN_SCREEN_BUTTON_RENT_ME = 1;
   public static final int AEM_MAIN_SCREEN_BUTTON_COMMING_SOON = 2;
   public static final String FILEUPLOADSERVER_SERVLET_PATH = "/fileupload/FileUploadServlet";
   public static final String SERVER_SERVLET_PATH = "/AEMServer/servlet/AEMServer";
   public static final String SERVER_POSTER_PATH = "/webdav/posters";
   public static final String SERVER_TRAILER_PATH = "/webdav/trailers";
   public static final String SERVER_ADS_PATH = "/webdav/posters";
   public static final String SERVER_PAYMENT_PICTURE_PATH = "/webdav/posters";
   public static final String SERVER_ABOUT_FRANCHISE_PATH = "/webdav/html";
   public static final String SERVER_FTP_AEM_LOGS_PATH = "kiosklogs";
   public static final String SERVER_FTP_LOGIN = "admin";
   public static final String SERVER_FTP_PASSWORD = "kiosk";
   public static final int QUEUE_JOB_CONFIRM_DISPENSE_ID = 1;
   public static final int QUEUE_JOB_DISC_RETURN_ID = 2;
   public static final int QUEUE_JOB_DISC_REMOVE_ID = 3;
   public static final int QUEUE_JOB_DISC_MISSING_ID = 4;
   public static final int QUEUE_JOB_DISC_FOUND_ID = 5;
   public static final int QUEUE_JOB_MAX_RETRIES = 5;
   public static final int QUEUE_JOB_MAX_DAYS = 5;
   public static final int QUEUE_RUN_WAIT_TIME = 60000;
   public static final int AEM_CONFIG_EX = 1;
   public static final int AEM_CONFIG_PLUS = 2;
   public static final int AEM_CONFIG_PRIME = 3;
   public static final int EXIT_APP_MODE_CLOSE = 1;
   public static final int EXIT_APP_MODE_RESTART = 2;
   public static final int EXIT_APP_MODE_SHUTDOWN = 3;
   public static final String SYSTEM_PATH = "c:\\windows\\system32\\var\\";
   public static final String QUEUE_FILE_PATH = "c:\\windows\\system32\\var\\qtasks\\";
   public static final String QUEUE_FILE_PREFIX = "q-";
   public static final int MISSING_TRAILERS_COMBINE_TIME_OUT = 60000;
   public static final int PERSISTENCE_SAVE_TIME_OUT = 30000;
   public static final int DEFAULT_REQUEST_TIME_OUT = 60000;
   public static final int QUEUE_REQUEST_TIME_OUT = 300000;
   public static final int EMAIL_TIME_OUT = 60000;
   public static final int SEND_LOG_TIME_OUT = 600000;
   public static final String DEBUG_MODE_FILE = "debug.txt";
   public static final String LOCK_FILE_NAME = "lock";
   public static final String COPY_MOVE_NAME = "copymove.bat";
   public static final String POSTER_FILE_SMALL_AFFIX = "-small";
   public static final int POSTER_FILE_SMALL_WIDTH = 100;
   public static final int POSTER_FILE_SMALL_HEIGHT = 140;
   public static final int POSTER_FILE_BIG_WIDTH = 340;
   public static final int POSTER_FILE_BIG_HEIGHT = 490;
   public static final String APPLICATION_PATH = "c:\\aem\\";
   public static final String POSTER_PATH = "c:\\aem\\content\\posters\\";
   public static final String TRAILER_PATH = "c:\\aem\\content\\trailers\\";
   public static final String DATA_PATH = "c:\\windows\\system32\\var\\pdata\\";
   public static final String DATA_ARCHIVE_PATH = "c:\\windows\\system32\\var\\pdata\\archive\\";
   public static final String ADS_PATH = "c:\\aem\\content\\ads\\";
   public static final String PAYMENT_PICTURE_PATH = "c:\\aem\\content\\images\\";
   public static final String HTML_PATH = "c:\\aem\\content\\html\\";
   public static final String LOG_PATH = "c:\\aem\\logs\\";
   public static final String LOG_ARCHIVE_PATH = "c:\\aem\\logs\\archive\\";
   public static final String TMP_PATH = "c:\\aem\\tmp\\";
   public static final String BIN_PATH = "c:\\aem\\bin\\";
   public static final String ABOUT_PATH = "c:\\aem\\content\\html\\aboutpage\\";
   public static final String UI_FLOW_PATH = "/conf/";
   public static final String UI_FLOW = "UIFlow";
   public static final String CONF_FILE_EXTENSION = ".conf";
   public static final String LOG_FILE_SUMMARY = "summary.log";
   public static final String LOG_FILE_DETAIL = "detail.log";
   public static final int LOG_FILE_RETENTION = 2;
   public static final String XML_FILE_EXTENSION = ".xml";
   public static final String ABOUT_FRANCHISE_FILE_NAME = "\\index.html";
   public static final String ABOUT_FRANCHISE_NAME = "About";
   public static final String DISC_DETAIL_ID = "DiscDetailId";
   public static final String TITLE_DETAIL_ID = "TitleDetailId";
   public static final String DISC_STATUS_ID = "DiscStatusId";
   public static final String PRICE_OPTION_ID = "PriceOptionId";
   public static final String DISC_CODE = "DiscCode";
   public static final String DISC_CODE_RADIO = "DiscCodeRadio";
   public static final String DISC_CODE_RADIO_SINGLE = "single";
   public static final String DISC_CODE_RADIO_RANGE = "range";
   public static final String DISC_CODE_RADIO_SELECTED = "selected";
   public static final String DISC_CODE_STARTING = "DiscCodeStarting";
   public static final String DISC_CODE_ENDING = "DiscCodeEnding";
   public static final String DISC_CODE_SELECTED = "DiscCodeSelected";
   public static final String DISC_BATCH = "DiscBatch";
   public static final String ATTR_DISC_BATCH_CONTENTS = "DiscBatchContents";
   public static final int DISC_BATCH_CSV_COLUMNS = 8;
   public static final String GROUP_CODE = "GroupCode";
   public static final String SLOT = "Slot";
   public static final String PRIORITY = "Priority";
   public static final String MARKED_FOR_SALE = "MarkedForSale";
   public static final String MARKED_FOR_RENT = "MarkedForRent";
   public static final String MARKED_FOR_REMOVAL = "MarkedForRemoval";
   public static final String MARKED_FOR_REMOVAL_DATE = "RemovalDate";
   public static final String DTUPDATED = "DTUpdated";
   public static final String PS_AEM_ID_RADIO = "PS_AEM_ID_RADIO";
   public static final String PS_INSTRUCTIONS = "PACKING_SLIP_INSTRUCTIONS";
   public static final String MFR_STATE = "MarkForRemoval_State";
   public static final String MFR_STATE_SHOW_FOR_AEM = "Cmd_MarkForRemoval_Show_For_Aem";
   public static final String MFR_STATE_TITLE_SELECT = "Cmd_MarkForRemoval_Title_Select";
   public static final String MFR_STATE_SHOW_FOR_TITLE = "Cmd_MarkForRemoval_Show_For_Title";
   public static final String ORIGINAL_TITLE = "Title";
   public static final String TRANSLATED_TITLE = "TranslatedTitle";
   public static final String SORT_TITLE = "SortTitle";
   public static final String SHORT_TITLE = "ShortTitle";
   public static final String GENRE1_ID = "Genre1Id";
   public static final String GENRE2_ID = "Genre2Id";
   public static final String GENRE3_ID = "Genre3Id";
   public static final String DESCRIPTION = "Description";
   public static final String POSTER = "Poster";
   public static final String TRAILER = "Trailer";
   public static final String SOUND_CLIP = "SoundClip";
   public static final String RATING_SYSTEM1_ID = "RatingSystem1Id";
   public static final String RATING_SYSTEM2_ID = "RatingSystem2Id";
   public static final String RATING_SYSTEM3_ID = "RatingSystem3Id";
   public static final String RATING1_ID = "Rating1Id";
   public static final String RATING2_ID = "Rating2Id";
   public static final String RATING3_ID = "Rating3Id";
   public static final String STREET_DATE = "StreetDate";
   public static final String TITLE_TYPE_ID = "TitleTypeId";
   public static final String POSTER_DOWNLOADED = "PosterDownloaded";
   public static final String TRAILER_DOWNLOADED = "TrailerDownloaded";
   public static final String ATTR1 = "Attr1";
   public static final String ATTR2 = "Attr2";
   public static final String ATTR3 = "Attr3";
   public static final String ATTR4 = "Attr4";
   public static final String ATTR5 = "Attr5";
   public static final String ATTR6 = "Attr6";
   public static final String RELEASE_YEAR = "ReleaseYear";
   public static final String LOCALE_ID = "LocaleId";
   public static final String TRANSACTION_TIME = "TransactionTime";
   public static final String GENRE_ID = "GenreId";
   public static final String GENRE = "Genre";
   public static final String SPECIAL_PRICING_ID = "SpecialPricingId";
   public static final String PRICE_MODEL_ID = "PriceModelId";
   public static final String DAY_OF_THE_WEEK = "DayOfTheWeek";
   public static final String NEW_PRICE = "NewPrice";
   public static final String USED_PRICE = "UsedPrice";
   public static final String RENTAL_PRICE = "RentalPrice";
   public static final String LATE_RENTAL_PRICE = "LateRentalPrice";
   public static final String RENTAL_DAYS = "RentalDays";
   public static final String LATE_DAYS = "LateDays";
   public static final String INDEX = "Index";
   public static final String OFFSET = "Offset";
   public static final String AEM_ID = "AemId";
   public static final String AEM_SERVER_ADDRESS = "ServerAddress";
   public static final String AEM_FTP_ADDRESS = "FTPAddress";
   public static final String AEM_TAX_RATE = "TaxRate";
   public static final String AEM_NO_CHARGE_TIME = "NoChargeTime";
   public static final String AEM_MEDIA_DOWNLOAD_START_TIME = "MediaDownloadStartTime";
   public static final String AEM_MEDIA_DOWNLOAD_STOP_TIME = "MediaDownloadStopTime";
   public static final String AEM_SEND_LOG_TIME = "SendLogTime";
   public static final String AEM_DEFAULT_LOCALE_ID = "DefaultLocaleId";
   public static final String AEM_LAST_SYNCH_DATE = "LastSynchDate";
   public static final String AEM_TIME_ZONE_ID = "TimeZoneId";
   public static final String AEM_TIME_ZONE_AUTO_ADJ = "TimeZoneAutoAdj";
   public static final String AEM_FRANCHISEE_LOCATION_NAME = "FranchiseeLocationName";
   public static final String AEM_FRANCHISEE_LOCATION_ADDRESS1 = "FranchiseeLocationAddress1";
   public static final String AEM_FRANCHISEE_LOCATION_ADDRESS2 = "FranchiseeLocationAddrses2";
   public static final String AEM_FRANCHISEE_LOCATION_CITY = "FranchiseeLocationCity";
   public static final String AEM_FRANCHISEE_LOCATION_STATE = "FranchiseeLocationState";
   public static final String AEM_FRANCHISEE_LOCATION_ZIPCODE = "FranchiseeLocationZipCode";
   public static final String AEM_FRANCHISOR_LOCATION_HELP_PHONE = "FranchiseeLocationHelpPhone";
   public static final String AEM_FRANCHISOR_LOCATION_NAME = "FranchisorLocationName";
   public static final String AEM_FRANCHISOR_LOCATION_ADDRESS1 = "FranchisorLocationAddress1";
   public static final String AEM_FRANCHISOR_LOCATION_ADDRESS2 = "FranchisorLocationAddrses2";
   public static final String AEM_FRANCHISOR_LOCATION_CITY = "FranchisorLocationCity";
   public static final String AEM_FRANCHISOR_LOCATION_STATE = "FranchisorLocationState";
   public static final String AEM_FRANCHISOR_LOCATION_ZIPCODE = "FranchisorLocationZipCode";
   public static final String AEM_FRANCHISEE_LOCATION_HELP_PHONE = "FranchisorLocationHelpPhone";
   public static final String AEM_DUE_TIME_OF_DAY = "DueTimeOfDay";
   public static final String AEM_SHUTDOWN_TIME = "ShutdownTime";
   public static final String SERVO_OFFSET = "ServoOffset";
   public static final String SERVO_INPUT_STEP = "ServoInputStep";
   public static final String SERVO_OUTPUT_STEP = "ServoOutputStep";
   public static final String SERVO_DISC_THRESHOLD = "ServoDiscThreshold";
   public static final String SERVO_KP = "ServoKp";
   public static final String SERVO_KP2 = "ServoKp2";
   public static final String SERVO_KD = "ServoKd";
   public static final String SERVO_KD2 = "ServoKd2";
   public static final String SERVO_KI = "ServoKi";
   public static final String SERVO_KI2 = "ServoKi2";
   public static final String SERVO_SERVO_RATE = "ServoRate";
   public static final String SERVO_SERVO_RATE2 = "ServoRate2";
   public static final String SERVO_DEADBAND_COMP = "ServoDeadband";
   public static final String SERVO_DEADBAND_COMP2 = "ServoDeadband2";
   public static final String SERVO_INTEGRATION_LIMIT = "ServoIntegrationLimit";
   public static final String SERVO_INTEGRATION_LIMIT2 = "ServoIntegrationLimit2";
   public static final String SERVO_VELOCITY = "ServoVelocity";
   public static final String SERVO_VELOCITY2 = "ServoVelocity2";
   public static final String SERVO_ACCELERATION = "ServoAcceleration";
   public static final String SERVO_ACCELERATION2 = "ServoAcceleration2";
   public static final String SERVO_OUTPUT_LIMIT = "ServoOutputLimit";
   public static final String SERVO_OUTPUT_LIMIT2 = "ServoOutputLimit2";
   public static final String SERVO_CURRENT_LIMIT = "ServoCurrentLimit";
   public static final String SERVO_CURRENT_LIMIT2 = "ServoCurrentLimit2";
   public static final String SERVO_POSITION_ERROR_LIMIT = "ServoPositionErrorLimit";
   public static final String SERVO_POSITION_ERROR_LIMIT2 = "ServoPositionErrorLimit2";
   public static final String SERVO_ARM_EJECT_WAIT_TIME = "ServoArmEjectWaitTime";
   public static final String SERVO_MOVE_TO_OFFSET_WAIT_TIME = "ServoMoveToOffsetWaitTime";
   public static final String SERVO_MOVE_TO_OFFSET_TIMEOUT = "ServoMoveToOffsetTimeout";
   public static final String SERVO_IS_CASE_IN_SENSOR_REFLECTIVE = "ServoIsCaseInSensorReflective";
   public static final String BAD_SLOT = "BadSlot";
   public static final String BAD_SLOT_ID = "BadSlotId";
   public static final String MEDIA_ID = "MediaId";
   public static final String FILENAME = "Filename";
   public static final String FILE_SIZE = "FileSize";
   public static final String SEGMENT_NAME = "SegmentName";
   public static final String NUM_SEGMENTS = "NumSegments";
   public static final String RENTABLE = "Rentable";
   public static final String MEDIA_FILENAME = "MediaFilename";
   public static final String NUM_DOWNLOADED = "NumDownloaded";
   public static final String MEDIA_DOWNLOADED = "MediaDownloaded";
   public static final String IMAGE_DOWNLOADED = "ImageDownloaded";
   public static final String MEDIA_TYPE_ID = "MediaTypeId";
   public static final String TEXT_ID = "TextId";
   public static final String TEXT = "Text";
   public static final String LANGUAGE = "Language";
   public static final String COUNTRY = "Country";
   public static final String RATING_ID = "RatingId";
   public static final String RATING_SYSTEM_ID = "RatingSystemId";
   public static final String RATING_CODE = "RatingCode";
   public static final String RATING_DESC = "RatingDesc";
   public static final String RATING_SYSTEM = "RatingSystem";
   public static final String CHECK_SUM = "CheckSum";
   public static final String START_DATE = "StartDate";
   public static final String END_DATE = "EndDate";
   public static final String GROUP_CODE_ID = "GroupCodeId";
   public static final String TITLE_TYPE = "TitleType";
   public static final String TITLE_TYPE_SINGULAR = "TitleTypeSingular";
   public static final String TITLE_TYPE_MAX_DISC_FIRST_TIME = "FirstTimeMax";
   public static final String TITLE_TYPE_MAX_DISC_REGULAR_USER = "RegularUserMax";
   public static final String TITLE_TYPE_MAX_CHARGE = "MaxCharge";
   public static final String TITLE_TYPE_CAP_TYPE_ID = "CapTypeId";
   public static final String TITLE_TYPE_CAP_VALUE = "Value";
   public static final String SECURITY_USER_ID = "UserId";
   public static final String SECURITY_USER_NAME = "UserName";
   public static final String SECURITY_USER_PASSWORD = "UserPassword";
   public static final String SECURITY_ROLE_ID = "RoleId";
   public static final String PRIVILEGES_TOOLS_PAGE_ID = "PageId";
   public static final String VERIFICATION_TYPE_ID = "VerificationTypeId";
   public static final String PAYMENT_PICTURE = "PaymentPicture";
   public static final String PAYMENT_CARD_TYPE_NAME = "PaymentCardTypeName";
   public static final String PAYMENT_CARD_CATEGORY_ID = "PaymentCardCategoryId";
   public static final String POLL_ID = "PollId";
   public static final String POLL_TYPE_ID = "PollTypeId";
   public static final String POLL_ORDER_NUM = "OrderNum";
   public static final String POLL_TEXT = "PollText";
   public static final String[] DISC_DETAIL_FIELD_NAMES = new String[]{
      "DiscDetailId",
      "TitleDetailId",
      "DiscStatusId",
      "PriceOptionId",
      "DiscCode",
      "GroupCode",
      "Slot",
      "Priority",
      "MarkedForSale",
      "MarkedForRent",
      "MarkedForRemoval",
      "RemovalDate",
      "DTUpdated"
   };
   public static final String[] DISC_DETAIL_FIELD_TYPES = new String[]{
      "int", "int", "int", "int", "String", "String", "int", "int", "int", "int", "int", "Date", "Date"
   };
   public static final String[] DISC_DETAIL_FIELD_NAMES_PREVIOUS = new String[]{
      "DiscDetailId",
      "TitleDetailId",
      "DiscStatusId",
      "PriceOptionId",
      "DiscCode",
      "GroupCode",
      "Slot",
      "Priority",
      "MarkedForSale",
      "MarkedForRent",
      "MarkedForRemoval",
      "DTUpdated"
   };
   public static final String[] DISC_DETAIL_FIELD_TYPES_PREVIOUS = new String[]{
      "int", "int", "int", "int", "String", "String", "int", "int", "int", "int", "int", "Date"
   };
   public static final String[] TITLE_DETAIL_FIELD_NAMES = new String[]{
      "TitleDetailId",
      "LocaleId",
      "TitleTypeId",
      "Title",
      "TranslatedTitle",
      "SortTitle",
      "ShortTitle",
      "Genre1Id",
      "Genre2Id",
      "Genre3Id",
      "Description",
      "Poster",
      "Trailer",
      "RatingSystem1Id",
      "RatingSystem2Id",
      "RatingSystem3Id",
      "Rating1Id",
      "Rating2Id",
      "Rating3Id",
      "StreetDate",
      "ReleaseYear",
      "Attr1",
      "Attr2",
      "Attr3",
      "Attr4",
      "Attr5",
      "Attr6"
   };
   public static final String[] TITLE_DETAIL_FIELD_TYPES = new String[]{
      "int",
      "int",
      "int",
      "String",
      "String",
      "String",
      "String",
      "int",
      "int",
      "int",
      "String",
      "String",
      "String",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "Date",
      "String",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int"
   };
   public static final String[] GENRE_FIELD_NAMES = new String[]{"GenreId", "LocaleId", "TitleTypeId", "Priority", "Genre"};
   public static final String[] GENRE_FIELD_TYPES = new String[]{"int", "int", "int", "int", "String"};
   public static final String[] REGULAR_PRICING_FIELD_NAMES = new String[]{
      "PriceOptionId", "PriceModelId", "DayOfTheWeek", "NewPrice", "UsedPrice", "RentalPrice", "LateRentalPrice", "RentalDays", "LateDays"
   };
   public static final String[] REGULAR_PRICING_FIELD_TYPES = new String[]{
      "int", "int", "int", "BigDecimal", "BigDecimal", "BigDecimal", "BigDecimal", "int", "int"
   };
   public static final String[] INDEX_FIELD_NAMES = new String[]{"Index"};
   public static final String[] INDEX_FIELD_TYPES = new String[]{"int"};
   public static final String[] SLOT_OFFSET_FIELD_NAMES = new String[]{"Slot", "Offset"};
   public static final String[] SLOT_OFFSET_FIELD_TYPES = new String[]{"int", "int"};
   public static final String[] AEM_PROPERTIES_FIELD_NAMES = new String[]{
      "AemId",
      "DueTimeOfDay",
      "TaxRate",
      "NoChargeTime",
      "ShutdownTime",
      "MediaDownloadStartTime",
      "MediaDownloadStopTime",
      "ServerAddress",
      "FTPAddress",
      "DefaultLocaleId",
      "LastSynchDate",
      "TimeZoneId",
      "TimeZoneAutoAdj",
      "SendLogTime",
      "ServoOffset",
      "ServoInputStep",
      "ServoOutputStep",
      "ServoDiscThreshold",
      "ServoKp",
      "ServoKp2",
      "ServoKd",
      "ServoKd2",
      "ServoKi",
      "ServoKi2",
      "ServoRate",
      "ServoRate2",
      "ServoDeadband",
      "ServoDeadband2",
      "ServoIntegrationLimit",
      "ServoIntegrationLimit2",
      "ServoVelocity",
      "ServoVelocity2",
      "ServoAcceleration",
      "ServoAcceleration2",
      "ServoOutputLimit",
      "ServoOutputLimit2",
      "ServoCurrentLimit",
      "ServoCurrentLimit2",
      "ServoPositionErrorLimit",
      "ServoPositionErrorLimit2",
      "ServoArmEjectWaitTime",
      "ServoMoveToOffsetWaitTime",
      "ServoMoveToOffsetTimeout",
      "ServoIsCaseInSensorReflective"
   };
   public static final String[] AEM_PROPERTIES_FIELD_TYPES = new String[]{
      "int",
      "Date",
      "BigDecimal",
      "Date",
      "Date",
      "Date",
      "Date",
      "String",
      "String",
      "int",
      "Date",
      "String",
      "int",
      "Date",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int",
      "int"
   };
   public static final String[] BAD_SLOT_FIELD_NAMES = new String[]{"BadSlot", "BadSlotId"};
   public static final String[] BAD_SLOT_FIELD_TYPES = new String[]{"int", "int"};
   public static final String[] MEDIA_PLAYLIST_FIELD_NAMES = new String[]{
      "MediaId", "MediaTypeId", "Priority", "FileSize", "Filename", "Attr1", "StartDate", "EndDate", "SegmentName", "NumSegments"
   };
   public static final String[] MEDIA_PLAYLIST_FIELD_TYPES = new String[]{"int", "int", "int", "int", "String", "String", "Date", "Date", "String", "int"};
   public static final String[] MEDIA_DETAIL_FIELD_NAMES = new String[]{"MediaId", "MediaTypeId", "NumDownloaded", "MediaDownloaded", "ImageDownloaded"};
   public static final String[] MEDIA_DETAIL_FIELD_TYPES = new String[]{"int", "int", "int", "int", "int"};
   public static final String[] STATIC_PLAYLIST_FIELD_NAMES = new String[]{
      "MediaId", "MediaTypeId", "Priority", "FileSize", "Filename", "StartDate", "EndDate"
   };
   public static final String[] STATIC_PLAYLIST_FIELD_TYPES = new String[]{"int", "int", "int", "int", "String", "Date", "Date"};
   public static final String[] TRANSLATION_FIELD_NAMES = new String[]{"TextId", "LocaleId", "Text"};
   public static final String[] TRANSLATION_FIELD_TYPES = new String[]{"int", "int", "String"};
   public static final String[] LOCALE_FIELD_NAMES = new String[]{"LocaleId", "Language", "Country"};
   public static final String[] LOCALE_FIELD_TYPES = new String[]{"int", "String", "String"};
   public static final String[] RATING_SYSTEM_FIELD_NAMES = new String[]{"RatingSystemId", "RatingSystem"};
   public static final String[] RATING_SYSTEM_FIELD_TYPES = new String[]{"int", "String"};
   public static final String[] RATING_FIELD_NAMES = new String[]{"RatingId", "RatingSystemId", "RatingCode", "RatingDesc"};
   public static final String[] RATING_FIELD_TYPES = new String[]{"int", "int", "String", "String"};
   public static final String[] CHECK_SUM_FIELD_NAMES = new String[]{"Filename", "CheckSum"};
   public static final String[] CHECK_SUM_FIELD_TYPES = new String[]{"String", "int"};
   public static final String[] SPECIAL_PRICING_FIELD_NAMES = new String[]{
      "SpecialPricingId",
      "PriceOptionId",
      "PriceModelId",
      "StartDate",
      "EndDate",
      "NewPrice",
      "UsedPrice",
      "RentalPrice",
      "LateRentalPrice",
      "RentalDays",
      "LateDays"
   };
   public static final String[] SPECIAL_PRICING_FIELD_TYPES = new String[]{
      "int", "int", "int", "Date", "Date", "BigDecimal", "BigDecimal", "BigDecimal", "BigDecimal", "int", "int"
   };
   public static final String[] GROUP_CODE_FIELD_NAMES = new String[]{"GroupCodeId", "GroupCode"};
   public static final String[] GROUP_CODE_FIELD_TYPES = new String[]{"int", "String"};
   public static final String[] TITLE_TYPE_FIELD_NAMES = new String[]{"TitleTypeId", "LocaleId", "Priority", "TitleType", "TitleTypeSingular"};
   public static final String[] TITLE_TYPE_FIELD_TYPES = new String[]{"int", "int", "int", "String", "String"};
   public static final String[] TITLE_TYPE_CAP_FIELD_NAMES = new String[]{"TitleTypeId", "CapTypeId", "Value"};
   public static final String[] TITLE_TYPE_CAP_FIELD_TYPES = new String[]{"int", "int", "String"};
   public static final String[] PAYMENT_CARD_TYPE_FIELD_NAMES = new String[]{
      "PaymentCardCategoryId", "PaymentCardTypeId", "LocaleId", "Priority", "VerificationTypeId", "PaymentPicture", "PaymentCardTypeName"
   };
   public static final String[] PAYMENT_CARD_TYPE_FIELD_TYPES = new String[]{"int", "int", "int", "int", "int", "String", "String"};
   public static final String[] SECURITY_FIELD_NAMES = new String[]{"UserId", "UserName", "UserPassword", "RoleId"};
   public static final String[] SECURITY_FIELD_TYPES = new String[]{"int", "String", "String", "int"};
   public static final String[] PRIVILEGES_FIELD_NAMES = new String[]{"RoleId", "PageId"};
   public static final String[] PRIVILEGES_FIELD_TYPES = new String[]{"int", "int"};
   public static final String[] POLL_FIELD_NAMES = new String[]{"PollId", "PollTypeId", "LocaleId", "Priority", "OrderNum", "PollText", "StartDate", "EndDate"};
   public static final String[] POLL_FIELD_TYPES = new String[]{"int", "int", "int", "int", "int", "String", "Date", "Date"};
   public static final String PERSISTENCE_DISC_DETAIL_FILE = "DscDtl.dll";
   public static final String PERSISTENCE_DISC_DETAIL_DISPLAY_NAME = "DiscDetail";
   public static final String PERSISTENCE_TITLE_DETAIL_FILE = "TtlDtl.dll";
   public static final String PERSISTENCE_TITLE_DETAIL_DISPLAY_NAME = "TitleDetail";
   public static final String PERSISTENCE_REGULAR_PRICING_FILE = "RglrPrcng.dll";
   public static final String PERSISTENCE_REGULAR_PRICING_DISPLAY_NAME = "RegularPricing";
   public static final String PERSISTENCE_GENRE_FILE = "Gnr.dll";
   public static final String PERSISTENCE_GENRE_DISPLAY_NAME = "Genre";
   public static final String PERSISTENCE_INDEX_FILE = "Indx.dll";
   public static final String PERSISTENCE_INDEX_DISPLAY_NAME = "Index";
   public static final String PERSISTENCE_SLOT_OFFSET_FILE = "SlOffst.dll";
   public static final String PERSISTENCE_SLOT_OFFSET_DISPLAY_NAME = "SlotOffset";
   public static final String PERSISTENCE_AEM_PROPERTIES_FILE = "AmPrprts.dll";
   public static final String PERSISTENCE_AEM_PROPERTIES2_FILE = "AmPrprts2.dll";
   public static final String PERSISTENCE_AEM_PROPERTIES_DISPLAY_NAME = "AemProperties";
   public static final String PERSISTENCE_AEM_PROPERTIES2_DISPLAY_NAME = "AemProperties2";
   public static final String PERSISTENCE_BAD_SLOT_FILE = "BdSlt.dll";
   public static final String PERSISTENCE_BAD_SLOT_DISPLAY_NAME = "BadSlot";
   public static final String PERSISTENCE_MEDIA_PLAYLIST_FILE = "MdPlylst.dll";
   public static final String PERSISTENCE_MEDIA_PLAYLIST_DISPLAY_NAME = "MediaPlaylist";
   public static final String PERSISTENCE_MEDIA_DETAIL_FILE = "MdDtl.dll";
   public static final String PERSISTENCE_MEDIA_DETAIL_DISPLAY_NAME = "MediaDetail";
   public static final String PERSISTENCE_STATIC_PLAYLIST_FILE = "SttcPlylst.dll";
   public static final String PERSISTENCE_STATIC_PLAYLIST_DISPLAY_NAME = "StaticPlaylist";
   public static final String PERSISTENCE_TRANSLATION_FILE = "Trnsltn.dll";
   public static final String PERSISTENCE_TRANSLATION_DISPLAY_NAME = "Translation";
   public static final String PERSISTENCE_LOCALE_FILE = "Lcl.dll";
   public static final String PERSISTENCE_LOCALE_DISPLAY_NAME = "Locale";
   public static final String PERSISTENCE_RATING_SYSTEM_FILE = "RtngSystm.dll";
   public static final String PERSISTENCE_RATING_SYSTEM_DISPLAY_NAME = "RatingSystem";
   public static final String PERSISTENCE_RATING_FILE = "Rtng.dll";
   public static final String PERSISTENCE_RATING_DISPLAY_NAME = "Rating";
   public static final String PERSISTENCE_CHECK_SUM_FILE = "ChckSm.dll";
   public static final String PERSISTENCE_CHECK_SUM2_FILE = "ChckSm2.dll";
   public static final String PERSISTENCE_CHECK_SUM_DISPLAY_NAME = "CheckSum";
   public static final String PERSISTENCE_CHECK_SUM2_DISPLAY_NAME = "CheckSum2";
   public static final String PERSISTENCE_SPECIAL_PRICING_FILE = "SpclPrcng.dll";
   public static final String PERSISTENCE_SPECIAL_PRICING_DISPLAY_NAME = "SpecialPricing";
   public static final String PERSISTENCE_GROUP_CODE_FILE = "GrpCd.dll";
   public static final String PERSISTENCE_GROUP_CODE_DISPLAY_NAME = "GroupCode";
   public static final String PERSISTENCE_TITLE_TYPE_FILE = "TtlTyp.dll";
   public static final String PERSISTENCE_TITLE_TYPE_DISPLAY_NAME = "TitleType";
   public static final String PERSISTENCE_TITLE_TYPE_CAP_FILE = "TtlTypCp.dll";
   public static final String PERSISTENCE_TITLE_TYPE_CAP_DISPLAY_NAME = "TitleTypeCap";
   public static final String PERSISTENCE_PAYMENT_CARD_TYPE_FILE = "PymntCrdTyp.dll";
   public static final String PERSISTENCE_PAYMENT_CARD_TYPE_DISPLAY_NAME = "PaymentCardType";
   public static final String PERSISTENCE_SECURITY_FILE = "Scrty.dll";
   public static final String PERSISTENCE_SECURITY_DISPLAY_NAME = "Security";
   public static final String PERSISTENCE_PRIVILEGES_FILE = "Prvlgs.dll";
   public static final String PERSISTENCE_PRIVILEGES_DISPLAY_NAME = "Privileges";
   public static final String PERSISTENCE_POLL_FILE = "Pll.dll";
   public static final String PERSISTENCE_POLL_DISPLAY_NAME = "Poll";
   public static final String[] PERSISTENCE_FILES_LIST = new String[]{
      "DscDtl.dll",
      "TtlDtl.dll",
      "RglrPrcng.dll",
      "Gnr.dll",
      "Indx.dll",
      "SlOffst.dll",
      "AmPrprts.dll",
      "BdSlt.dll",
      "MdPlylst.dll",
      "MdDtl.dll",
      "SttcPlylst.dll",
      "Trnsltn.dll",
      "Lcl.dll",
      "RtngSystm.dll",
      "Rtng.dll",
      "ChckSm.dll",
      "SpclPrcng.dll",
      "GrpCd.dll",
      "TtlTyp.dll",
      "TtlTypCp.dll",
      "PymntCrdTyp.dll",
      "Scrty.dll",
      "Prvlgs.dll",
      "Pll.dll"
   };
   public static final String QUERY_REQUEST_GROUP_CODE_VIEW = "ffKioskGroupCode";
   public static final String QUERY_REQUEST_GROUP_CODE_QUERY = "select GroupCodeID 'GroupCodeId', GroupCode 'GroupCode' from ffKioskGroupCode";
   public static final String QUERY_REQUEST_GROUP_CODE_NAME = "GroupCode query";
   public static final String QUERY_REQUEST_TRANSLATION_VIEW = "ffKioskTextTranslation";
   public static final String QUERY_REQUEST_TRANSLATION_QUERY = "select TextID 'TextId', LocaleID 'LocaleId', Text 'Text' from ffKioskTextTranslation";
   public static final String QUERY_REQUEST_TRANSLATION_NAME = "Translation query";
   public static final String QUERY_REQUEST_GENRE_VIEW = "ffKioskGenre";
   public static final String QUERY_REQUEST_GENRE_QUERY = "select GenreID 'GenreId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', Priority 'Priority', Genre 'Genre' from ffKioskGenre";
   public static final String QUERY_REQUEST_GENRE_NAME = "Genre query";
   public static final String QUERY_REQUEST_RATING_VIEW = "ffKioskRating";
   public static final String QUERY_REQUEST_RATING_QUERY = "select RatingID 'RatingId', RatingSystemID 'RatingSystemId', RatingCode 'RatingCode', RatingDesc 'RatingDesc' from ffKioskRating";
   public static final String QUERY_REQUEST_RATING_NAME = "Rating query";
   public static final String QUERY_REQUEST_RATING_SYSTEM_VIEW = "ffKioskRatingSystem";
   public static final String QUERY_REQUEST_RATING_SYSTEM_QUERY = "select RatingSystemId 'RatingSystemId', RatingSystem 'RatingSystem' from ffKioskRatingSystem";
   public static final String QUERY_REQUEST_RATING_SYSTEM_NAME = "RatingSystem query";
   public static final String QUERY_REQUEST_REGULAR_PRICING_VIEW = "ffKioskRegularPricing";
   public static final String QUERY_REQUEST_REGULAR_PRICING_QUERY = "select PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', DayOfTheWeekID 'DayOfTheWeek', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskRegularPricing";
   public static final String QUERY_REQUEST_REGULAR_PRICING_NAME = "RegularPricing query";
   public static final String QUERY_REQUEST_SPECIAL_PRICING_VIEW = "ffKioskSpecialPricing";
   public static final String QUERY_REQUEST_SPECIAL_PRICING_QUERY = "select SpecialPricingID 'SpecialPricingId', PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', StartDate 'StartDate', EndDate 'EndDate', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskSpecialPricing";
   public static final String QUERY_REQUEST_SPECIAL_PRICING_NAME = "SpecialPricing query";
   public static final String QUERY_REQUEST_STATIC_PLAYLIST_VIEW = "ffGraphicPlayList";
   public static final String QUERY_REQUEST_STATIC_PLAYLIST_QUERY = "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', StartDate 'StartDate', EndDate 'EndDate' from ffGraphicPlayList";
   public static final String QUERY_REQUEST_STATIC_PLAYLIST_NAME = "GraphicPlaylist query";
   public static final String QUERY_REQUEST_LOCALE_VIEW = "ffKioskLocale";
   public static final String QUERY_REQUEST_LOCALE_QUERY = "select LocaleID 'LocaleId', Language 'Language', Country 'Country' from ffKioskLocale";
   public static final String QUERY_REQUEST_LOCALE_NAME = "Locale query";
   public static final String QUERY_REQUEST_MEDIA_PLAYLIST_VIEW = "ffVideoPlayList";
   public static final String QUERY_REQUEST_MEDIA_PLAYLIST_QUERY = "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', Attr1 'Attr1', StartDate 'StartDate', EndDate 'EndDate', SegmentName 'SegmentName', NumSegments 'NumSegments' from ffVideoPlayList";
   public static final String QUERY_REQUEST_MEDIA_PLAYLIST_NAME = "VideoPlaylist query";
   public static final String QUERY_REQUEST_AEM_PROPERTIES_VIEW = "ffKioskBasicsAndSetup";
   public static final String QUERY_REQUEST_AEM_PROPERTIES_QUERY = "select KioskID 'AemId', convert(varchar(15), DueTimeOfDay, 108) 'DueTimeOfDay', TaxRate 'TaxRate', NoChargeTime 'NoChargeTime', convert(varchar(15), ShutDownTime, 108) 'ShutdownTime', convert(varchar(15), MediaDownloadStartTime, 108) 'MediaDownloadStartTime', convert(varchar(15), MediaDownloadStopTime, 108) 'MediaDownloadStopTime', ServerAddress 'ServerAddress', FTPAddress 'FTPAddress', DefaultLocaleID 'DefaultLocaleId', convert(varchar(30), LastSynchDate, 121) + ' UTC'LastSynchDate, TimeZoneId 'TimeZoneId', TimeZoneAutoAdj 'TimeZoneAutoAdj', convert(varchar(15), SendLogTime, 108) 'SendLogTime', ServoOffset 'ServoOffset', ServoInputStep 'ServoInputStep', ServoOutputStep 'ServoOutputStep', ServoDiscThreshold 'ServoDiscThreshold', ServoKp 'ServoKp', ServoKp2 'ServoKp2', ServoKd 'ServoKd', ServoKd2 'ServoKd2', ServoKi 'ServoKi', ServoKi2 'ServoKi2', ServoRate 'ServoRate', ServoRate2 'ServoRate2', ServoDeadband 'ServoDeadband', ServoDeadband2 'ServoDeadband2', ServoIntegrationLimit 'ServoIntegrationLimit', ServoIntegrationLimit2 'ServoIntegrationLimit2', ServoVelocity 'ServoVelocity', ServoVelocity2 'ServoVelocity2', ServoAcceleration 'ServoAcceleration', ServoAcceleration2 'ServoAcceleration2', ServoOutputLimit 'ServoOutputLimit', ServoOutputLimit2 'ServoOutputLimit2', ServoCurrentLimit 'ServoCurrentLimit', ServoCurrentLimit2 'ServoCurrentLimit2', ServoPositionErrorLimit 'ServoPositionErrorLimit', ServoPositionErrorLimit2 'ServoPositionErrorLimit2', ServoArmEjectWaitTime 'ServoArmEjectWaitTime', ServoMoveToOffsetWaitTime 'ServoMoveToOffsetWaitTime', ServoMoveToOffsetTimeout 'ServoMoveToOffsetTimeout', ServoIsCaseInSensorReflective 'ServoIsCaseInSensorReflective' from ffKioskBasicsAndSetup";
   public static final String QUERY_REQUEST_AEM_PROPERTIES_NAME = "Aem query";
   public static final String QUERY_REQUEST_DISC_DETAIL_VIEW = "ffDiscInventory";
   public static final String QUERY_REQUEST_DISC_DETAIL_QUERY = "select DiscDetailID 'DiscDetailId', TitleID 'TitleDetailId', DiscStatusID 'DiscStatusId', PriceOptionID 'PriceOptionId', DiscCode 'DiscCode', GroupCode 'GroupCode', Slot 'Slot', Priority 'Priority', MarkedForSale 'MarkedForSale', MarkedForRent 'MarkedForRent', MarkedForRemoval 'MarkedForRemoval', RemovalDate 'RemovalDate', convert(varchar(30), LastUpdatedDate, 121) + ' UTC'DTUpdated from ffDiscInventory";
   public static final String QUERY_REQUEST_DISC_DETAIL_NAME = "DiscDetail query";
   public static final String QUERY_REQUEST_MARKED_FOR_REMOVAL_NAME = "MarkedForRemoval query";
   public static final String QUERY_REQUEST_TITLE_DETAIL_VIEW = "ffKioskTitle";
   public static final String QUERY_REQUEST_TITLE_DETAIL_QUERY_1 = "select TitleID 'TitleDetailId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', OriginalTitle 'Title', TranslatedTitle 'TranslatedTitle', SortTitle 'SortTitle', ShortTitle 'ShortTitle', Genre1ID 'Genre1Id', Genre2ID 'Genre2Id', Genre3ID 'Genre3Id', Description 'Description', Poster 'Poster', Trailer 'Trailer', RatingSystem1ID 'RatingSystem1Id', RatingSystem2ID 'RatingSystem2Id', RatingSystem3ID 'RatingSystem3Id', Rating1ID 'Rating1Id', Rating2ID 'Rating2Id', Rating3ID 'Rating3Id', convert(varchar(30), StreetDate, 121) + ' ";
   public static final String QUERY_REQUEST_TITLE_DETAIL_QUERY_2 = "'StreetDate, ReleaseYear 'ReleaseYear', Attr1 'Attr1', Attr2 'Attr2', Attr3 'Attr3', Attr4 'Attr4', Attr5 'Attr5', Attr6 'Attr6' from ffKioskTitle";
   public static final String QUERY_REQUEST_TITLE_DETAIL_NAME = "TitleDetail query";
   public static final String QUERY_REQUEST_PAYMENT_CARD_TYPE_VIEW = "ffKioskPaymentCard";
   public static final String QUERY_REQUEST_PAYMENT_CARD_TYPE_QUERY = "select PaymentCardCategoryID 'PaymentCardCategoryId', PaymentCardTypeID 'PaymentCardTypeId', LocaleID 'LocaleId', Priority 'Priority', SecureMode 'VerificationTypeId', PaymentPicture 'PaymentPicture', PaymentCardName 'PaymentCardTypeName' from ffKioskPaymentCard";
   public static final String QUERY_REQUEST_PAYMENT_CARD_TYPE_NAME = "PaymentCardType query";
   public static final String QUERY_REQUEST_TITLE_TYPE_VIEW = "ffKioskTitleType";
   public static final String QUERY_REQUEST_TITLE_TYPE_QUERY = "select TitleTypeId 'TitleTypeId', LocaleID 'LocaleId', Priority 'Priority', TitleTypeName 'TitleType', TitleTypeName2 'TitleTypeSingular' from ffKioskTitleType";
   public static final String QUERY_REQUEST_TITLE_TYPE_NAME = "TitleType query";
   public static final String QUERY_REQUEST_TITLE_TYPE_CAP_VIEW = "ffKioskTitleTypeCap";
   public static final String QUERY_REQUEST_TITLE_TYPE_CAP_QUERY = "select TitleTypeID 'TitleTypeId', CapTypeID 'CapTypeId', CapValue 'Value' from ffKioskTitleTypeCap";
   public static final String QUERY_REQUEST_TITLE_TYPE_CAP_NAME = "TitleTypeCap query";
   public static final String QUERY_REQUEST_SECURITY_VIEW = "ffKioskRoleLogin";
   public static final String QUERY_REQUEST_SECURITY_QUERY = "select LoginID 'UserId', UserName 'UserName', Password 'UserPassword', RoleID 'RoleId' from ffKioskRoleLogin";
   public static final String QUERY_REQUEST_SECURITY_NAME = "Security query";
   public static final String QUERY_REQUEST_PRIVILEGES_VIEW = "ffKioskRolePage";
   public static final String QUERY_REQUEST_PRIVILEGES_QUERY = "select RoleID 'RoleId', PageID 'PageId' from ffKioskRolePage";
   public static final String QUERY_REQUEST_PRIVILEGES_NAME = "Privileges query";
   public static final String QUERY_REQUEST_SLOT_OFFSET_VIEW = "ffKioskSlotOffset";
   public static final String QUERY_REQUEST_SLOT_OFFSET_QUERY = "select Slot 'Slot', Offset 'Offset' from ffKioskSlotOffset";
   public static final String QUERY_REQUEST_SLOT_OFFSET_NAME = "SlotOffset query";
   public static final String QUERY_REQUEST_BAD_SLOT_VIEW = "ffKioskBadSlot";
   public static final String QUERY_REQUEST_BAD_SLOT_QUERY = "Select Slot 'BadSlot', '1' 'BadSlotId' from ffKioskBadSlot";
   public static final String QUERY_REQUEST_BAD_SLOT_NAME = "BadSlot query";
   public static final String QUERY_REQUEST_POLL_VIEW = "ffKioskPoll";
   public static final String QUERY_REQUEST_POLL_QUERY = "select PollID 'PollId', PollTypeID 'PollTypeId', LocaleID 'LocaleId', SeqNum 'Priority', OrderNum 'OrderNum', PollText 'PollText', StartDate 'StartDate', EndDate 'EndDate' from ffKioskPoll";
   public static final String QUERY_REQUEST_POLL_NAME = "KioskPoll query";
   public static final String QUERY_REQUEST_GET_FRANCHISE_ID_NAME = "FranchiseID query";
   public static final String QUERY_REQUEST_GET_FRANCHISE_ID_QUERY = "Select FranchiseID 'FranchiseID' From ffKiosk";
   public static final String QUERY_REQUEST_DISABLE_BUY_QUERY = "Select * From ffKioskProperty";
   public static final String QUERY_REQUEST_DISABLE_BUY_NAME = "DisableBuy query";
   public static final int INTAKE_DISC_TIMEOUT_1 = 4000;
   public static final int INTAKE_DISC_TIMEOUT_2 = 5000;
   public static final int PING_SLEEP_TIME = 300000;
   public static final int PING_ERROR_SLEEP_TIME = 30000;
   public static final int STROKE_WATCHDOG_SLEEP_TIME = 60000;
   public static final int HEART_BEAT_THREAD_SLEEP_TIME = 5000;
   public static final int MARKED_FOR_REMVOAL_THREAD_SLEEP_TIME = 3600000;
   public static final int MARKED_FOR_REMOVAL_THREAD_WAIT_TIME = 3600000;
   public static final int MISSING_POSTER_SLEEP_TIME = 1800000;
   public static final int SERVER_COMMAND_SLEEP_TIME = 60000;
   public static final String STAND_ALONE_COMMAND_LINE_ARG = "-standalone";
   public static final String STAND_ALONE_LOGIN = "login";
   public static final String STAND_ALONE_PASSWORD = "pass";
   public static final int FTP_TIMEOUT_CAP = 300000;
   public static final int EXIT_APP_TIMEOUT = 30000;
   public static final int INT_TRUE = 1;
   public static final int INT_FALSE = 0;
   public static final int ADULT_GENRE_ID = 10;
   public static final int NEW_RELEASES_CATEGORY_ID = -1;
   public static final int ALL_CATEGORY_ID = -2;
   public static final int TOP_PICKS_CATEGORY_ID = -3;
   public static final int TITLE_TYPE_ID_MOVIES = 1;
   public static final int TITLE_TYPE_ID_GAMES = 3;
   public static final int NEW_RELEASES_MAX = 30;
   public static final int TOP_PICKS_MAX = 15;
   public static final int ALL_CATEGORY_MAX = 999;
   public static final int MAX_VERIFICATION_RETRIES = 3;
   public static final int TOOLS_PAGE_ID_ACCOUNT_MANAGER = 42;
   public static final int TOOLS_PAGE_ID_SERVO_PARAMS = 43;
   public static final int TOOLS_PAGE_ID_OPERATOR = 44;
   public static final int TOOLS_PAGE_ID_CONTROLLER = 45;
   public static final int TOOLS_PAGE_ID_REMOVE_DISCS = 46;
   public static final int TOOLS_PAGE_ID_ERROR_LOG = 47;
   public static final int TOOLS_PAGE_ID_INVENTORY_CHECK = 48;
   public static final int TOOLS_PAGE_ID_CYCLE_TEST = 49;
   public static final int TOOLS_PAGE_ID_CARD_READER_HID = 50;
   public static final int TOOLS_PAGE_ID_BAD_SLOTS = 51;
   public static final int TOOLS_PAGE_ID_BAR_CAM_PEG_TEST = 52;
   public static final int TOOLS_PAGE_ID_SLOT_CAL = 53;
   public static final int TOOLS_PAGE_ID_SIMPLE_REMOVE_DISCS = 54;
   public static final String DEFAULT_POSTER_SMALL = "PosterNotFoundSmall.jpg";
   public static final String INTERNAL_ERROR_MESSAGE_TEXT = "internalErrorMessageText";
   public static final String GENERIC_ERROR_MESSAGE = "An internal error occurred, please try again.";
   public static final String ATTR_LOGIN_ID = "LoginID";
   public static final String ATTR_ROLE_ID = "RoleID";
   public static final String ATTR_USER_NAME = "UserName";
   public static final String ATTR_PASSWORD = "Password";
   public static final String ATTR_DB_USER_NAME = "DBUserName";
   public static final String ATTR_DB_PASSWORD = "DBPassword";
   public static final String ATTR_FRANCHISE_ID = "FranchiseID";
   public static final String ATTR_FRANCHISE_NAME = "FranchiseName";
   public static final String ATTR_ADDRESS1 = "Address1";
   public static final String ATTR_ADDRESS2 = "Address2";
   public static final String ATTR_UNIT_NUMBER = "UnitNumber";
   public static final String ATTR_CITY = "City";
   public static final String ATTR_STATE = "State";
   public static final String ATTR_ZIPCODE = "ZipCode";
   public static final String ATTR_COUNTRY = "Country";
   public static final String ATTR_PHONE1 = "Phone1";
   public static final String ATTR_PHONE2 = "Phone2";
   public static final String ATTR_DAYTIME_PHONE = "DaytimePhone";
   public static final String ATTR_EVENING_PHONE = "EveningPhone";
   public static final String ATTR_FAX1 = "Fax1";
   public static final String ATTR_FAX2 = "Fax2";
   public static final String ATTR_EMAIL = "Email";
   public static final String ATTR_EMAIL_ADDRESS = "EmailAddress";
   public static final String ATTR_RECEIPT_EMAIL = "ReceiptEmail";
   public static final String ATTR_ALERT_EMAIL = "AlertEmail";
   public static final String ATTR_PROMO_POINTS = "PromoPoints";
   public static final String ATTR_PROMO_VALUE = "PromoValue";
   public static final String ATTR_PROMO_POINTS_PER_RENTAL = "PromoPointsPerRental";
   public static final String ATTR_PRICE_MODEL_ID = "PriceModelID";
   public static final String ATTR_PRICE_MODEL = "PriceModel";
   public static final String ATTR_PRICE_OPTION_ID = "PriceOptionID";
   public static final String ATTR_PRICE_OPTION = "PriceOption";
   public static final String ATTR_PRICE_REC_EXISTS = "priceRecordExistsInDB";
   public static final String ATTR_AEM_ID = "KioskID";
   public static final String ATTR_LOCALE_ID = "LocaleID";
   public static final String ATTR_LOCALE1_ID = "Locale1ID";
   public static final String ATTR_LOCALE2_ID = "Locale2ID";
   public static final String ATTR_LOCALE3_ID = "Locale3ID";
   public static final String ATTR_DESCRIPTION = "Description";
   public static final String ATTR_TITLE_ID = "TitleID";
   public static final String ATTR_TITLE = "Title";
   public static final String ATTR_FORMAT_NAME = "FormatName";
   public static final String ATTR_TITLE_TYPE_NAME = "TitleTypeName";
   public static final String ATTR_TITLE_TYPE_ID = "TitleTypeID";
   public static final String ATTR_GENRE_NAME = "GenreName";
   public static final String ATTR_GENRE_ID = "GenreID";
   public static final String ATTR_GENRE1_ID = "Genre1ID";
   public static final String ATTR_GENRE2_ID = "Genre2ID";
   public static final String ATTR_GENRE3_ID = "Genre3ID";
   public static final String ATTR_DISC_ID = "DiscDetailID";
   public static final String ATTR_DISC_CODE = "DiscCode";
   public static final String ATTR_GROUP_CODE = "GroupCode";
   public static final String ATTR_GROUP_CODE_ID = "GroupCodeID";
   public static final String ATTR_FIRST_NAME = "FirstName";
   public static final String ATTR_LAST_NAME = "LastName";
   public static final String ATTR_MIDDLE_INITIAL = "MiddleInitial";
   public static final String ATTR_PROMO_CREDIT_AVAILABLE = "PromoCreditAvailable";
   public static final String ATTR_PROMO_CREDIT_USED = "PromoCreditUsed";
   public static final String ATTR_AGE = "Age";
   public static final String ATTR_GENDER = "Gender";
   public static final String ATTR_INCOME = "Income";
   public static final String ATTR_FIRST_VISIT_DATE = "FirstVisitDate";
   public static final String ATTR_TRANSACTION_ID = "TransactionID";
   public static final String ATTR_REF_TRANSACTION_ID = "RefTransactionID";
   public static final String ATTR_AMOUNT = "Amount";
   public static final String ATTR_TAX = "Tax";
   public static final String ATTR_TOTAL = "Total";
   public static final String ATTR_TRANSACTION_DATE = "TransactionDate";
   public static final String ATTR_TRANSACTION_TIME_LOCAL = "TransactionTimeLocal";
   public static final String ATTR_PAYMENT_CARD_ID = "PaymentCardID";
   public static final String ATTR_CARD_HOLDER = "CardHolder";
   public static final String ATTR_CARD_STATUS = "Config1";
   public static final String ATTR_LINE_ITEM_ID = "LineItemID";
   public static final String ATTR_LATE_FEE = "LateFee";
   public static final String ATTR_DUE_TIME = "DueTime";
   public static final String ATTR_TIME_RETURNED_LOCAL = "TimeReturnedLocal";
   public static final String ATTR_RATING_ID = "RatingID";
   public static final String ATTR_RATING1_ID = "Rating1ID";
   public static final String ATTR_RATING2_ID = "Rating2ID";
   public static final String ATTR_RATING3_ID = "Rating3ID";
   public static final String ATTR_RATING_SYSTEM_ID = "RatingSystemID";
   public static final String ATTR_STREETDATE = "StreetDate";
   public static final String ATTR_SORTTITLE = "SortTitle";
   public static final String ATTR_PROMOCREDIT_AVAILABLE = "PromoCreditAvailable";
   public static final String ATTR_PROMOCREDIT_USED = "PromoCreditUsed";
   public static final String ATTR_CARDNUMBER = "CardNumber";
   public static final String ATTR_CURRENTLY_RENTED = "CurrentlyRented";
   public static final String ATTR_PREVIOUSLY_RENTED = "PreviouslyRented";
   public static final String DISPLAY_ATTR_CUSTOMER_STATUS = "CustomerStatus";
   public static final String ATTR_CUSTOMER_STATUS = "Config1";
   public static final String ATTR_CUSTOMER_NAME = "CustomerName";
   public static final String ATTR_TRANSACTION_TYPE = "ShowTransactions";
   public static final String ATTR_TRANS_TYPE_REGULAR = "Regular";
   public static final String ATTR_TRANS_TYPE_DECLINED = "Declined";
   public static final String ATTR_TRANS_TYPE_LATE_FEE = "LateFees";
   public static final String ATTR_TRANSACTION_SUBTYPE = "TransactionsSubType";
   public static final String ATTR_TRANS_SUBTYPE_CUSTOMER = "Customer";
   public static final String ATTR_TRANS_SUBTYPE_DISC = "Disc";
   public static final String ATTR_TRANS_SUBTYPE_AEM = "AEM";
   public static final String ATTR_TRANS_SUBTYPE_ALL = "All";
   public static final String ATTR_TRANS_SUBTYPE_SEARCH = "Search";
   public static final String ATTR_SHOW_SUCCESS_TRANSACTIONS = "ShowSuccessTransactions";
   public static final String ATTR_SHOW_SUCCESS_YES = "Y";
   public static final String ATTR_SHOW_SUCCESS_NO = "N";
   public static final String ATTR_TRANSACTION_LIST_RESULTS = "transactionListResults";
   public static final String ATTR_TRANSACTION_LIST_DATE = "transactionListDate";
   public static final String ATTR_PREVIOUSLY_SOLD = "PreviouslySold";
   public static final String ATTR_CUSTOMER_ID = "CustomerID";
   public static final String ATTR_FORMAT_ID = "FormatID";
   public static final String ATTR_LOCATION_NAME = "LocationName";
   public static final String ATTR_TIMES_RENTED = "TimesRented";
   public static final String ATTR_REMOVAL_DATE = "RemovalDate";
   public static final String ATTR_EXPIRATION_DATE = "ExpirationDate";
   public static final String ATTR_REFUND_STATUS = "RefundStatus";
   public static final String ATTR_S_PROMO_CODE = "PromoCode";
   public static final String ATTR_S_PROMO_TYPE = "Type";
   public static final String ATTR_S_PROMO_VALUE = "Value";
   public static final String ATTR_S_PROMO_DISCOUNT_METHOD = "DiscountMethod";
   public static final String ATTR_UI_PROMO_DISCOUNT_METHOD = "Discount Method";
   public static final String ATTR_S_PROMO_VALID_FROM = "ValidFrom";
   public static final String ATTR_UI_PROMO_VALID_FROM = "Valid From";
   public static final String ATTR_S_PROMO_VALID_TO = "ValidTo";
   public static final String ATTR_UI_PROMO_VALID_TO = "Valid To";
   public static final String ADMIN_DOWNLOAD_LINK = "http://www.dvdplay.net/admin/flinks";
   public static final String ATTR_LOGIN_ERROR_MESSAGE = "loginErrorMessage";
   public static final String ATTR_DVDPLAY_EXCEPTION = "dvdplayException";
   public static final String ATTR_AEM_NAME = "AEMName";
   public static final String ATTR_DISC_TITLE = "DiscTitle";
   public static final String ATTR_CUSTOMER_LIST_RESULTS = "customerListResults";
   public static final String ATTR_CUSTOMER_LIST_DATE = "customerListDate";
   public static final int DISC_CODE_MINIMUM_LENGTH = 4;
   public static final int DISC_CODE_MAXMUM_LENGTH = 4;
   public static final int DISC_CODE_MAXMUM_VALUE = 9999;
   public static final int GROUP_CODE_MAXMUM_VALUE = 9999;
   public static final int DEFAULT_MAX_REPEAT_RENTAL = 4;
   public static final int DEFAULT_MAX_FIRST_RENTAL = 1;
   public static final int MAX_RENTAL_ALLOWED = 9;
   public static final int MAX_TRAILER_DAYS = 60;
   public static final int SCREEN_MONITOR_FREQUENCY = 300000;
   public static final int EQ_HEARTBEAT_FREQUENCY = 60000;
   public static final String SERVLET_PROP_ADMIN = "FFServer.properties";
   public static final String SYSTEM_PROP_USER_DIR = "user.dir";
   public static final String ABOUT_COMPANY_SCREEN = "AboutCompanyScreen";
   public static final String AEM_STARTUP_ERROR_SCREEN = "AEMStartUpErrorScreen";
   public static final String AUTHORIZING_PAYMENT_SCREEN = "AuthorizingPaymentScreen";
   public static final String CART_TABLE_SCREEN = "CartTableScreen";
   public static final String DELIVERING_DVD_SCREEN = "DeliveringDVDScreen";
   public static final String DISC_NOT_BELONG_SCREEN = "DiscNotBelongScreen";
   public static final String DVD_DESCRIPTION_SCREEN = "DvdDescriptionScreen";
   public static final String EMAIL_SCREEN = "EmailScreen";
   public static final String ERROR_SCREEN = "ErrorScreen";
   public static final String GAME_DESCRIPTION_SCREEN = "GameDescriptionScreen";
   public static final String GAME_SELECTION_SCREEN = "GameSelectionScreen";
   public static final String HELP_ANSWER_SCREEN = "HelpAnswerScreen";
   public static final String HELP_MAIN_SCREEN = "HelpMainScreen";
   public static final String IDENTIFYING_MOVIE_SCREEN = "IdentifyingMovieScreen";
   public static final String INITIALIZING_AEM_SCREEN = "InitializingAEMScreen";
   public static final String MAIN_SCREEN = "MainScreen";
   public static final String MOVIE_SELECTION_SCREEN = "MovieSelectionScreen";
   public static final String MUST_BE_18_SCREEN = "MustBe18Screen";
   public static final String PAYMENT_CARD_APPROVED_SCREEN = "PaymentCardApprovedScreen";
   public static final String POLL_SCREEN = "PollScreen";
   public static final String PROMO_CODE_DESCRIPTION_SCREEN = "PromoCodeDescriptionScreen";
   public static final String PROMO_CODE_SCREEN = "PromoCodeScreen";
   public static final String PUSHING_DISC_ALL_THE_WAY_SCREEN = "PushingDiscAllTheWayScreen";
   public static final String REMOVE_DVD_SCREEN = "RemoveDVDScreen";
   public static final String RENTAL_AGREEMENT_SCREEN = "RentalAgreementScreen";
   public static final String RETURN_ERROR_SCREEN = "ReturnErrorScreen";
   public static final String RETURNING_MOVIE_SCREEN = "ReturningMovieScreen";
   public static final String RETURN_MOVIE_SCREEN = "ReturnMovieScreen";
   public static final String RETURN_THANK_YOU_SCREEN = "ReturnThankYouScreen";
   public static final String SWIPE_PAYMENT_CARD_SCREEN = "SwipePaymentCardScreen";
   public static final String TIME_OUT_SCREEN = "TimeOutScreen";
   public static final String UNABLE_TO_RECOGNIZE_MOVIE_SCREEN = "UnableToRecognizeMovieScreen";
   public static final String UPDATING_SCREEN = "UpdatingScreen";
   public static final String ZIP_CODE_SCREEN = "ZipCodeScreen";
   public static final String OUT_OF_ORDER_SCREEN = "OutOfOrderScreen";
   public static final String MAXIMUM_DISC_EXCEEDED_SCREEN = "MaximumDiscExceededScreen";
   public static final String BACK = "Back";
   public static final String ADD = "Add";
   public static final String SKIP = "Skip";
   public static final String SELECT = "Select";
   public static final String START_OVER = "StartOver";
   public static final String TIME_OUT = "TimeOut";
   public static final String HELP_ANSWER = "HelpAnswer";
   public static final String ABOUT_COMPANY = "AboutCompany";
   public static final String INITIALIZING_AEM = "InitializingAEM";
   public static final String ROBOT_CLICKING = "RobotClicking";
   public static final String DVDPLAY_LOGO = "DVDPlayLogo";
   public static final String ADVERTISEMENT = "Advertisement";
   public static final String HELP = "Help";
   public static final String CHECKOUT = "CheckOut";
   public static final String EMPTY_STRING = "";
   public static final String FILE_STRING = "file:\\";
   public static final String SPACE_STRING = " ";
   public static final String SLASH = " /";
   public static final String AT = "@";
   public static final String STAR = "* ";
   public static final String FROM = " from ";
   public static final String COLON = ":";
   public static final String COMMA = ",";
   public static final String OPEN_QUOTE = "[";
   public static final String CLOSE_QUOTE = "]";
   public static final String CMD = " Cmd ";
   public static final String BLACK = "Black";
   public static final String WHITE = "White";
   public static final String ORANGE = "Orange";
   public static final String RED = "Red";
   public static final String GRAY = "Gray";
   public static final int TSLN_START_OVER = 1004;
   public static final int TSLN_BACK = 1012;
   public static final int TSLN_ABOUT = 1007;
   public static final int TSLN_CART_ADD = 1015;
   public static final int TSLN_CART = 1011;
   public static final int TSLN_HELP = 1001;
   public static final int TSLN_CLEAR = 6501;
   public static final int TSLN_RENT = 1008;
   public static final int TSLN_BUY = 1009;
   public static final int TSLN_DAYS = 5216;
   public static final int TSLN_DAY = 5215;
   public static final int TSLN_CART_CHECKOUT = 5301;
   public static final int TSLN_CART_SUBTOTAL = 5311;
   public static final int TSLN_CART_TAX = 5312;
   public static final int TSLN_CART_TOTAL = 5313;
   public static final int TSLN_CART_PROMOCODE = 5314;
   public static final int TSLN_CART_RENTBUY = 5306;
   public static final int TSLN_CART_TITLE = 5305;
   public static final int TSLN_CART_DUEDATE = 5307;
   public static final int TSLN_CART_DUETIME = 5309;
   public static final int TSLN_CART_PRICE = 5308;
   public static final int TSLN_CART_REMOVE = 5310;
   public static final int TSLN_DELIVERINGDVD_DELIVERING = 5902;
   public static final int TSLN_DELIVERINGDVD_ONEMOMENTPLEASE = 5903;
   public static final int TSLN_DELIVERINGDVD_TITLE = 5901;
   public static final int TSLN_DELIVERINGDVD_DUEDATE = 6003;
   public static final int TSLN_DELIVERINGDVD_DUETIME = 6004;
   public static final int TSLN_DELIVERINGDVD_YOUNOWOWN = 6006;
   public static final int TSLN_DELIVERINGDVD_BUSINESSCLOSINGTIME = 6008;
   public static final int TSLN_DISCNOTBELONG_TITLE = 4041;
   public static final int TSLN_DISCNOTBELONG_WEARESORRY = 4037;
   public static final int TSLN_DVDDESCRIPTION_TITLE = 5005;
   public static final int TSLN_DVDDESCRIPTION_SUBTITLEDIN = 5206;
   public static final int TSLN_DVDDESCRIPTION_STARRING = 5208;
   public static final int TSLN_DVDDESCRIPTION_DIRECTEDBY = 5209;
   public static final int TSLN_DVDDESCRIPTION_RATING = 5210;
   public static final int TSLN_DVDDESCRIPTION_RENTALPRICE = 5212;
   public static final int TSLN_DVDDESCRIPTION_DUEBACKBY = 5213;
   public static final int TSLN_DVDDESCRIPTION_LATEFEEPER = 5204;
   public static final int TSLN_DVDDESCRIPTION_PURCHASEPRICE = 5214;
   public static final int TSLN_DVDDESCRIPTION_OUT = 6403;
   public static final int TSLN_EMAIL_IFYOUWOULD = 5702;
   public static final int TSLN_EMAIL_PLEASEENTEREMAIL = 5703;
   public static final int TSLN_EMAIL_YOURPRIVACY = 5704;
   public static final int TSLN_EMAIL_CONTINUE = 5701;
   public static final int TSLN_KEYBOARD_CLEAR = 6501;
   public static final int TSLN_KEYBOARD_BACKSPACE = 6502;
   public static final int TSLN_KEYBOARD_EMAIL = 6503;
   public static final int TSLN_FRANCHISE_FULLNAME = 2012;
   public static final int TSLN_FRANCHISE_ADDRESS1 = 2013;
   public static final int TSLN_FRANCHISE_ADDRESS2 = 2014;
   public static final int TSLN_FRANCHISE_CITYSTATEZIP = 2015;
   public static final int TSLN_FRANCHISE_PHONE = 2016;
   public static final int TSLN_FRANCHISE_EMAIL = 2017;
   public static final int TSLN_HELP_CLOSEHELP = 1502;
   public static final int TSLN_HELP_ANSWER = 5001;
   public static final String DEBUG_EXTRA_UI_FLOW = "EXTRA_UIFLOW";
   public static final int CLICK_ROBOT_MONITOR_X = 2;
   public static final int CLICK_ROBOT_MONITOR_Y = 2;
   public static final int CLICK_ROBOT_MONITOR_FREQUENCY = 120000;
   public static final int CLICK_ROBOT_THRESHOLD = 5;
   public static final int CLICK_ROBOT_SLEEP_TIME = 120000;
   public static final int ERROR_SCREEN_TIME_OUT = 10000;
   public static final int TIMEOUT_SCREEN_TIME_OUT = 30000;
   public static final int GENERAL_TIME_OUT = 30000;
   public static final int ZIP_CODE_SCREEN_ENTRY_MAX_LENGTH = 20;
   public static final int EMAIL_SCREEN_ENTRY_MAX_LENGTH = 80;
   public static final int PROMOCODE_SCREEN_ENTRY_MAX_LENGTH = 20;
   public static final int POLL_SCREEN_TIME_OUT = 15000;
   public static final int APP_STATUS_LIGHT_FREQUENCY = 3000;
   public static final int CUSTOMER_DISABLED_BIT = 1;
   public static final int SLOT_EMPTY = 1;
   public static final int SLOT_FULL = 2;
   public static final int ROWCOUNT_LIMIT = 1000;
}
