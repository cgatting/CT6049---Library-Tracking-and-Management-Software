/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.ClientInfoStatus;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLRecoverableException;
import java.sql.SQLWarning;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.xa.XAException;
import oracle.jdbc.driver.Message;
import oracle.jdbc.driver.OracleSQLException;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.SQLStateMapping;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Log;

@DisableTrace
public class DatabaseError {
    private static final int ORAERROR_END_OF_FILE_ON_COM_CHANNEL = 3113;
    private static final int ORAERROR_NOT_CONNECTED_TO_ORACLE = 3114;
    private static final int ORAERROR_INIT_SHUTDOWN_IN_PROGRESS = 1033;
    private static final int ORAERROR_ORACLE_NOT_AVAILABLE = 1034;
    private static final int ORAERROR_IMMEDIATE_SHUTDOWN_IN_PROGRESS = 1089;
    private static final int ORAERROR_SHUTDOWN_IN_PROGRESS_NO_CONN = 1090;
    private static final int ORAERROR_NET_IO_EXCEPTION = 17002;
    private static final int ORAERROR_NO_MORE_DATA_TO_READ = 17410;
    private static final int ORAERROR_OALL8_INCONSISTENT_STATE = 17447;
    private static final int ORAERROR_PROTOCOL_VIOLATION = 17401;
    private static final int ORAERROR_SCHEMA_CHANGED = 17294;
    static final int ORAERROR_DRCP_ATTACH_TIMEOUT = 56611;
    static final int ORAERROR_INVALID_TOKEN = 25707;
    static final int ORAERROR_EXPIRED_TOKEN = 25708;
    private static boolean loadedMessages = false;
    private static Message message = null;
    private static String msgClassName = "oracle.jdbc.driver.Message11";
    static final SQLStateMapping[] mappings = SQLStateMapping.getMappings();
    public static final int JDBC_ERROR_BASE = 17000;
    public static final int JDBC_MAX_ERRORS = 2000;
    public static final int JDBC_MAX_17000_ERROR = 17499;
    public static final int JDBC_MAX_18700_ERROR = 18999;
    public static final int EOJ_SUCCESS = 0;
    public static final int EOJ_ERROR = 1;
    public static final int EOJ_IOEXCEPTION = 2;
    public static final int EOJ_INVALID_COLUMN_INDEX = 3;
    public static final int EOJ_INVALID_COLUMN_TYPE = 4;
    public static final int EOJ_UNSUPPORTED_COLUMN_TYPE = 5;
    public static final int EOJ_INVALID_COLUMN_NAME = 6;
    public static final int EOJ_INVALID_DYNAMIC_COLUMN = 7;
    public static final int EOJ_CLOSED_CONNECTION = 8;
    public static final int EOJ_CLOSED_STATEMENT = 9;
    public static final int EOJ_CLOSED_RESULTSET = 10;
    public static final int EOJ_EXHAUSTED_RESULTSET = 11;
    public static final int EOJ_TYPE_CONFLICT = 12;
    public static final int EOJ_WAS_NULL = 13;
    public static final int EOJ_RESULTSET_BEFORE_FIRST_ROW = 14;
    public static final int EOJ_STATEMENT_WAS_CANCELLED = 15;
    public static final int EOJ_STATEMENT_TIMED_OUT = 16;
    public static final int EOJ_CURSOR_ALREADY_INITIALIZED = 17;
    public static final int EOJ_INVALID_CURSOR = 18;
    public static final int EOJ_CAN_ONLY_DESCRIBE_A_QUERY = 19;
    public static final int EOJ_INVALID_ROW_PREFETCH = 20;
    public static final int EOJ_MISSING_DEFINES = 21;
    public static final int EOJ_MISSING_DEFINES_AT_INDEX = 22;
    public static final int EOJ_UNSUPPORTED_FEATURE = 23;
    public static final int EOJ_NO_DATA_READ = 24;
    public static final int EOJ_IS_DEFINES_NULL_ERROR = 25;
    public static final int EOJ_NUMERIC_OVERFLOW = 26;
    public static final int EOJ_STREAM_CLOSED = 27;
    public static final int EOJ_NO_NEW_DEFINE_IF_RESULT_SET_NOT_CLOSED = 28;
    public static final int EOJ_READ_ONLY = 29;
    public static final int EOJ_INVALID_TRANSLEVEL = 30;
    public static final int EOJ_AUTO_CLOSE_ONLY = 31;
    public static final int EOJ_ROW_PREFETCH_NOT_ZERO = 32;
    public static final int EOJ_MALFORMED_SQL92 = 33;
    public static final int EOJ_NON_SUPPORTED_SQL92_TOKEN = 34;
    public static final int EOJ_NON_SUPPORTED_CHAR_SET = 35;
    public static final int EOJ_ORACLE_NUMBER_EXCEPTION = 36;
    public static final int EOJ_FAIL_CONVERSION_UTF8_TO_UCS2 = 37;
    public static final int EOJ_CONVERSION_BYTE_ARRAY_ERROR = 38;
    public static final int EOJ_CONVERSION_CHAR_ARRAY_ERROR = 39;
    public static final int EOJ_SUB_SUB_PROTOCOL_ERROR = 40;
    public static final int EOJ_INVALID_IN_OUT_BINDS = 41;
    public static final int EOJ_INVALID_BATCH_VALUE = 42;
    public static final int EOJ_INVALID_STREAM_SIZE = 43;
    public static final int EOJ_DATASET_ITEMS_NOT_ALLOCATED = 44;
    public static final int EOJ_BEYOND_BINDS_BATCH = 45;
    public static final int EOJ_INVALID_RANK = 46;
    public static final int EOJ_TDS_FORMAT_ERROR = 47;
    public static final int EOJ_UNDEFINED_TYPE = 48;
    public static final int EOJ_INCONSISTENT_ADT = 49;
    public static final int EOJ_NOSUCHELEMENT = 50;
    public static final int EOJ_NOT_AN_OBJECT_TYPE = 51;
    public static final int EOJ_INVALID_REF = 52;
    public static final int EOJ_INVALID_SIZE = 53;
    public static final int EOJ_INVALID_LOB_LOCATOR = 54;
    public static final int EOJ_FAIL_CONVERSION_CHARACTER = 55;
    public static final int EOJ_UNSUPPORTED_CHARSET = 56;
    public static final int EOJ_CLOSED_LOB = 57;
    public static final int EOJ_INVALID_NLS_RATIO = 58;
    public static final int EOJ_CONVERSION_JAVA_ERROR = 59;
    public static final int EOJ_FAIL_CREATE_DESC = 60;
    public static final int EOJ_NO_DESCRIPTOR = 61;
    public static final int EOJ_INVALID_REF_CURSOR = 62;
    public static final int EOJ_NOT_IN_A_TRANSACTION = 63;
    public static final int EOJ_DATABASE_IS_NULL = 64;
    public static final int EOJ_CONV_WAS_NULL = 65;
    public static final int EOJ_ACCESS_SPECIFIC_IMPL = 66;
    public static final int EOJ_INVALID_URL = 67;
    public static final int EOJ_INVALID_ARGUMENTS = 68;
    public static final int EOJ_USE_XA_EXPLICIT = 69;
    public static final int EOJ_INVALID_DATASIZE_LENGTH = 70;
    public static final int EOJ_EXCEEDED_VARRAY_LENGTH = 71;
    public static final int EOJ_VALUE_TOO_BIG = 72;
    public static final int EOJ_INVALID_NAME_PATTERN = 74;
    public static final int EOJ_INVALID_FORWARD_RSET_OP = 75;
    public static final int EOJ_INVALID_READONLY_RSET_OP = 76;
    public static final int EOJ_FAIL_REF_SETVALUE = 77;
    public static final int EOJ_CONNECTIONS_ALREADY_EXIST = 78;
    public static final int EOJ_USER_CREDENTIALS_FAIL = 79;
    public static final int EOJ_INVALID_BATCH_COMMAND = 80;
    public static final int EOJ_BATCH_ERROR = 81;
    public static final int EOJ_NO_CURRENT_ROW = 82;
    public static final int EOJ_NOT_ON_INSERT_ROW = 83;
    public static final int EOJ_ON_INSERT_ROW = 84;
    public static final int EOJ_UPDATE_CONFLICTS = 85;
    public static final int EOJ_NULL_INSERT_ROW_VALUE = 86;
    public static final int WARN_IGNORE_FETCH_DIRECTION = 87;
    public static final int EOJ_UNSUPPORTED_SYNTAX = 88;
    public static final int EOJ_INTERNAL_ERROR = 89;
    public static final int EOJ_OPER_NOT_ALLOWED = 90;
    public static final int WARN_ALTERNATE_RSET_TYPE = 91;
    public static final int EOJ_NO_JDBC_AT_END_OF_CALL = 92;
    public static final int EOJ_WARN_SUCCESS_WITH_INFO = 93;
    public static final int EOJ_VERSION_MISMATCH = 94;
    public static final int EOJ_NO_STMT_CACHE_SIZE = 95;
    public static final int EOJ_INVALID_ELEMENT_TYPE = 97;
    public static final int EOJ_INVALID_EMPTYLOB_OP = 98;
    public static final int EOJ_INVALID_INDEXTABLE_ARRAY_LENGTH = 99;
    public static final int EOJ_INVALID_JAVA_OBJECT = 100;
    public static final int EOJ_CONNECTIONPOOL_INVALID_PROPERTIES = 101;
    public static final int EOJ_BFILE_IS_READONLY = 102;
    public static final int EOJ_WRONG_CONNECTION_TYPE_FOR_METHOD = 103;
    public static final int EOJ_NULL_SQL_STRING = 104;
    public static final int EOJ_SESSION_TZ_NOT_SET = 105;
    public static final int EOJ_CONNECTIONPOOL_INVALID_CONFIG = 106;
    public static final int EOJ_CONNECTIONPOOL_INVALID_PROXY_TYPE = 107;
    public static final int WARN_DEFINE_COLUMN_TYPE = 108;
    public static final int EOJ_STANDARD_ENCODING_NOT_FOUND = 109;
    public static final int EOJ_THIN_WARNING = 110;
    public static final int EOJ_WARN_CONN_CACHE_TIMEOUT = 111;
    public static final int EOJ_WARN_THREAD_TIMEOUT_INTERVAL = 112;
    public static final int EOJ_WARN_THREAD_INTERVAL_TOO_BIG = 113;
    public static final int EOJ_LOCAL_COMMIT_IN_GLOBAL_TXN = 114;
    public static final int EOJ_LOCAL_ROLLBACK_IN_GLOBAL_TXN = 115;
    public static final int EOJ_AUTOCOMMIT_IN_GLOBAL_TXN = 116;
    public static final int EOJ_SETSVPT_IN_GLOBAL_TXN = 117;
    public static final int EOJ_GETID_FOR_NAMED_SVPT = 118;
    public static final int EOJ_GETNAME_FOR_UNNAMED_SVPT = 119;
    public static final int EOJ_SETSVPT_WITH_AUTOCOMMIT = 120;
    public static final int EOJ_ROLLBACK_WITH_AUTOCOMMIT = 121;
    public static final int EOJ_ROLLBACK_TO_SVPT_IN_GLOBAL_TXN = 122;
    public static final int EOJ_INVALID_STMT_CACHE_SIZE = 123;
    public static final int EOJ_WARN_CACHE_INACTIVITY_TIMEOUT = 124;
    public static final int EOJ_IMPROPER_STATEMENT_TYPE = 125;
    public static final int EOJ_FIXED_WAIT_TIMEOUT = 126;
    public static final int EOJ_WARN_CACHE_FIXEDWAIT_TIMEOUT = 127;
    public static final int EOJ_INVALID_QUERY_STRING = 128;
    public static final int EOJ_INVALID_DML_STRING = 129;
    public static final int EOJ_QUERY_TIMEOUT_CLASS_NOT_FOUND = 130;
    public static final int EOJ_QUERY_TIMEOUT_INVALID_STATE = 131;
    public static final int EOJ_INVALID_OBJECT_TO_CONVERT = 132;
    public static final int EOJ_INVALID_IDENTIFIER_OR_LITERAL = 133;
    public static final int EOJ_PARAMETER_NAME_TOO_LONG = 134;
    public static final int EOJ_PARAMETER_NAME_APPEARS_MORE_THAN_ONCE = 135;
    public static final int EOJ_MALFORMED_DLNK_URL = 136;
    public static final int EOJ_INVALID_CACHE_ENABLED_DATASOURCE = 137;
    public static final int EOJ_INVALID_CONNECTION_CACHE_NAME = 138;
    public static final int EOJ_INVALID_CONNECTION_CACHE_PROPERTIES = 139;
    public static final int EOJ_CONNECTION_CACHE_ALREADY_EXISTS = 140;
    public static final int EOJ_CONNECTION_CACHE_DOESNOT_EXIST = 141;
    public static final int EOJ_CONNECTION_CACHE_DISABLED = 142;
    public static final int EOJ_INVALID_CACHED_CONNECTION = 143;
    public static final int EOJ_STMT_NOT_EXECUTED = 144;
    public static final int EOJ_INVALID_ONS_EVENT = 145;
    public static final int EOJ_INVALID_ONS_EVENT_VERSION = 146;
    public static final int EOJ_UNKNOWN_PARAMETER_NAME = 147;
    public static final int EOJ_T4C_ONLY = 148;
    public static final int EOJ_ALREADY_PROXY = 149;
    public static final int EOJ_PROXY_WRONG_ARG = 150;
    public static final int EOJ_CLOB_TOO_LARGE = 151;
    public static final int EOJ_METHOD_FOR_LOGICAL_CONNECTION_ONLY = 152;
    public static final int EOJ_METHOD_FOR_PHYSICAL_CONNECTION_ONLY = 153;
    public static final int EOJ_EX_MAP_ORACLE_TO_UCS = 154;
    public static final int EOJ_EX_MAP_UCS_TO_ORACLE = 155;
    public static final int EOJ_E2E_METRIC_ARRAY_SIZE = 156;
    public static final int EOJ_SETSTRING_LIMIT = 157;
    public static final int EOJ_INVALID_DURATION = 158;
    public static final int EOJ_E2E_METRIC_TOO_LONG = 159;
    public static final int EOJ_E2E_SEQUENCE_NUMBER_OUT_OF_RANGE = 160;
    public static final int EOJ_INVALID_TXN_MODE = 161;
    public static final int EOJ_UNSUPPORTED_HOLDABILITY = 162;
    public static final int EOJ_GETXACONN_WHEN_CACHE_ENABLED = 163;
    public static final int EOJ_GETXARESOURCE_FROM_PHYSICAL_CONN = 164;
    public static final int EOJ_PRIVATE_JDBC_NOT_PRESENT = 165;
    public static final int EOJ_NO_FETCH_ON_PLSQL = 166;
    public static final int EOJ_ORACLEPKI_JAR_NOT_FOUND = 167;
    public static final int EOJ_PKI_WALLET_ERROR = 168;
    public static final int EOJ_NO_STREAM_BIND_ALLOWED = 169;
    public static final int EOJ_APP_CTXT_NULL_NAMESPACE = 170;
    public static final int EOJ_APP_CTXT_ATTR_TOO_LONG = 171;
    public static final int EOJ_APP_CTXT_VAL_TOO_LONG = 172;
    public static final int EOJ_DML_RETURNING_PARAM_NOT_REGISTERED = 173;
    public static final int EOJ_APP_CTXT_INVALID_NAMESPACE = 174;
    public static final int EOJ_REMOTE_ONS_CONFIG_ERROR = 175;
    public static final int EOJ_UNKNOWN_LOCALE = 176;
    public static final int EOJ_DOES_NOT_WRAP_INTERFACE = 177;
    public static final int EOJ_ANYTYPE_PICKLER = 178;
    public static final int EOJ_KOTAD_MAGIC_NUMBER_ERROR = 179;
    public static final int EOJ_KOTAD_FORMAT_ERROR = 180;
    public static final int EOJ_CHARACTER_CONVERTER_GENERAL_ERROR = 181;
    public static final int EOJ_CHARACTER_CONVERTER_OVERRUN_ERROR = 182;
    public static final int EOJ_CHARACTER_CONVERTER_IMPOSSIBLE_ERROR = 183;
    public static final int EOJ_INCORRECT_FORM_OF_USE = 184;
    public static final int EOJ_CONNECTION_PROPERTIES_DEFAULT_MISSING = 185;
    public static final int EOJ_CONNECTION_PROPERTIES_ACCESSMODE_MISSING = 186;
    public static final int EOJ_CONNECTION_PROPERTIES_INVALID_TYPE = 187;
    public static final int EOJ_CONNECTION_PROPERTIES_REFLECTION_ILLEGAL_ACCESS = 188;
    public static final int EOJ_CONNECTION_PROPERTIES_MISSING_INSTANCE_VARIABLE = 189;
    public static final int EOJ_CONNECTION_PROPERTIES_FORMAT_ERROR = 190;
    public static final int EOJ_INVALID_COMMIT_OPTIONS = 191;
    public static final int EOJ_FREED_LOB = 192;
    public static final int EOJ_INVALID_AQ_MESSAGE_FORMAT = 193;
    public static final int EOJ_MARK_RESET_NOT_SUPPORTED = 194;
    public static final int EOJ_INVALID_MARK = 195;
    public static final int EOJ_READAHEAD_LIMIT_EXCEEDED = 196;
    public static final int EOJ_INCORRECT_PARAMETER_USAGE = 197;
    public static final int EOJ_SESSION_TIME_ZONE_NOT_SET = 198;
    public static final int EOJ_SESSION_TIME_ZONE_NOT_SUPPORTED = 199;
    public static final int EOJ_HETEROXA_GET_UTF_OPENSTR = 200;
    public static final int EOJ_HETEROXA_GET_UTF_CLOSESTR = 201;
    public static final int EOJ_HETEROXA_GET_UTF_RMNAME = 202;
    public static final int EOJ_HETEROXA_JHANDLE_SIZE = 203;
    public static final int EOJ_HETEROXA_ARRAY_TOO_SHORT = 204;
    public static final int EOJ_HETEROXA_SVCCTX_HANDLE = 205;
    public static final int EOJ_HETEROXA_ENV_HANDLE = 206;
    public static final int EOJ_HETEROXA_NULL_TNSENTRY = 207;
    public static final int EOJ_HETEROXA_OPEN_RMERR = 213;
    public static final int EOJ_HETEROXA_OPEN_INVAL = 215;
    public static final int EOJ_HETEROXA_OPEN_PROTO = 216;
    public static final int EOJ_HETEROXA_CLOSE_RMERR = 233;
    public static final int EOJ_HETEROXA_CLOSE_INVAL = 235;
    public static final int EOJ_HETEROXA_CLOSE_PROTO = 236;
    static final int NEXT_ERROR = 240;
    public static final int EOJ_NTF_UNKNOWN_LOCALHOST = 240;
    public static final int EOJ_NTF_SECURITY_MANAGER = 241;
    public static final int EOJ_NTF_TCP_OPTION = 242;
    public static final int EOJ_NTF_TIMEOUT_OPTION = 243;
    public static final int EOJ_DCN_CHANGELAG_OPTION = 244;
    public static final int EOJ_NTF_DELETE_REG_WRONG_INSTANCE = 245;
    public static final int EOJ_NTF_NULL_LISTENER = 246;
    public static final int EOJ_NTF_PLSQL_LISTENER = 247;
    public static final int EOJ_NTF_DUP_LISTENER = 248;
    public static final int EOJ_NTF_RM_MISSING_LISTENER = 249;
    public static final int EOJ_NTF_TCPPORT_ALREADY_USED = 250;
    public static final int EOJ_NTF_CLOSED_REGISTRATION = 251;
    public static final int EOJ_AQ_UNDEFINED_PAYLOAD_TYPE = 252;
    public static final int EOJ_INVALID_NAME_FOR_CLIENTINFO = 253;
    public static final int EOJ_OUT_OF_MEMORY_ERROR = 254;
    public static final int EOJ_CANNOT_DISABLE_FCF = 255;
    public static final int EOJ_INSTANCE_PROP_NOT_AVAILABLE = 256;
    public static final int EOJ_INVALID_DRIVER_NAME_ATTR = 257;
    public static final int EOJ_SYNONYM_LOOP_DETECTED = 258;
    public static final int EOJ_MISSING_JAR_FOR_XML = 259;
    public static final int EOJ_READ_EMPTY_SQLXML = 260;
    public static final int EOJ_NOT_READABLE_SQLXML = 261;
    public static final int EOJ_NOT_WRITEABLE_SQLXML = 262;
    public static final int EOJ_UNSUPPORTED_SQLXML_RESULT_TYPE = 263;
    public static final int EOJ_UNSUPPORTED_SQLXML_SOURCE_TYPE = 264;
    public static final int EOJ_INVALID_TIMEZONE_NAME = 265;
    public static final int EOJ_USER_STREAM_BIND_EXCEPTION = 266;
    public static final int EOJ_INVALID_LOB_PREFETCH_SIZE = 267;
    public static final int EOJ_INVALID_DATE_YEAR = 268;
    public static final int EOJ_OCI_FATAL_ERROR = 269;
    public static final int EOJ_DUPLICATE_STREAM_PARAMETER = 270;
    public static final int EOJ_SETPLSQLINDEXTABLE_NULL_ARRAY = 271;
    public static final int EOJ_SETPLSQLINDEXTABLE_ZERO_ARRAY = 272;
    public static final int EOJ_CALLCOMMIT_WITH_AUTOCOMMIT = 273;
    public static final int EOJ_CALLROLLBACK_WITH_AUTOCOMMIT = 274;
    public static final int EOJ_RESULTSET_MAXROWS_LIMIT_REACHED = 275;
    public static final int EOJ_ATTEMPT_TO_USE_RESERVED_NAMESPACE = 276;
    public static final int EOJ_CANNOT_OPEN_TRANSLATION_FILE = 277;
    public static final int EOJ_TRANSLATION_FILE_PARSING_ERROR = 278;
    public static final int EOJ_TRANSLATE_QUERY_IN_LOCAL_MODE = 279;
    public static final int EOJ_SERVER_TRANSLATION_ERROR = 280;
    public static final int EOJ_NULL_FACTORY = 281;
    public static final int EOJ_NULL_CLASS = 282;
    public static final int EOJ_NO_RESULTSET = 283;
    public static final int EOJ_NULL_EXECUTOR = 284;
    public static final int EOJ_NEGATIVE_TIMEOUT = 285;
    public static final int EOJ_RECURSIVE_EXECUTION = 286;
    public static final int EOJ_NTF_REGISTRATION_FAILED = 287;
    public static final int EOJ_SERVER_QUERY_TRANSLATION_NULL = 288;
    public static final int EOJ_RESULTSET_AFTER_LAST_ROW = 289;
    public static final int EOJ_BIND_CHECKSUM_MISMATCH = 290;
    public static final int EOJ_FREED_ARRAY = 291;
    public static final int EOJ_NO_VALID_LOGON_METHOD = 292;
    public static final int EOJ_INVALID_VALUE_ALLOWEDLOGONVERSION = 293;
    public static final int EOJ_SCHEMA_CHANGED = 294;
    public static final int EOJ_REQUIRE_SELECT = 295;
    public static final int EOJ_NULL_URL = 296;
    public static final int EOJ_INVALID_SESSION_PURITY = 297;
    public static final int EOJ_DRCP_ATTACH_TIMEOUT = 298;
    public static final int EOJ_PLSQL_BOOLEAN_SUPPORT_UNAVAILABLE_IN_SVR = 299;
    public static final int EOJ_INVAILD_IDENTIFIER = 300;
    private static final int EOJ_ROWSET_ERROR_BASE = 300;
    public static final int EOJ_JRS_UNABLE_CONNECT_VIA_DS = 300;
    public static final int EOJ_JRS_AUTH_PROPS_NOT_SET = 301;
    public static final int EOJ_JRS_CONN_NOT_OPEN = 302;
    public static final int EOJ_JRS_DEL_ROWS_INVISIBLE = 303;
    public static final int EOJ_JRS_SYNC_PROVIDER = 304;
    public static final int EOJ_JRS_RESULTSET_NOT_OPEN = 305;
    public static final int EOJ_JRS_FETCH_DIR_FOR_SCROLL = 306;
    public static final int EOJ_JRS_FETCH_REV_FOR_FWD_TYPE = 307;
    public static final int EOJ_JRS_INVALID_FETCH_DIR = 308;
    public static final int EOJ_JRS_ROWSET_NOT_WRITABLE = 309;
    public static final int EOJ_JRS_INVALID_PARAM_INDEX = 310;
    public static final int EOJ_JRS_CNVT_COL_TO_STREAM = 311;
    public static final int EOJ_JRS_CANT_COVT_COL_TO_STRM = 312;
    public static final int EOJ_JRS_CALL_NEXT_PREV_FIRST = 313;
    public static final int EOJ_JRS_INVALID_OP_FWD_TYPE = 314;
    public static final int EOJ_JRS_NO_ROWS_CHANGED = 315;
    public static final int EOJ_JRS_MAP_IN_TOCOLLECTION = 316;
    public static final int EOJ_JRS_ROW_NOT_INSERTED = 317;
    public static final int EOJ_JRS_ROW_NOT_DELETED = 318;
    public static final int EOJ_JRS_ROW_NOT_UPDATED = 319;
    public static final int EOJ_JRS_NOT_ALL_COLS_SET = 320;
    public static final int EOJ_JRS_CONV_READER_TO_STR = 321;
    public static final int EOJ_JRS_STREAM_READ = 322;
    public static final int EOJ_JRS_INVALID_PARAM_TYPE = 323;
    public static final int EOJ_JRS_INVALID_NUM_KEY_COLS = 324;
    public static final int EOJ_JRS_INVALID_PAGE_SIZE = 325;
    public static final int EOJ_JRS_MARK_INS_ROW_ORIG = 326;
    public static final int EOJ_JRS_INVALID_OP_BEFORE_INS = 327;
    public static final int EOJ_JRS_INVALID_OP_ON_RESULTSET = 328;
    public static final int EOJ_JRS_INVALID_OP_BEFORE_PAGING = 329;
    public static final int EOJ_JRS_INVALID_ROW_NUM_PARAM = 330;
    public static final int EOJ_JRS_NEGATIVE_START_POS = 331;
    public static final int EOJ_JRS_NULL_RESULTSET_TO_POPULATE = 332;
    public static final int EOJ_JRS_FEW_ROWS_START_POPULATE = 333;
    public static final int EOJ_JRS_NO_MATCH_COL_INDEXES_SET = 334;
    public static final int EOJ_JRS_NO_MATCH_COL_NAMES_SET = 335;
    public static final int EOJ_JRS_INVALID_MATCH_COL_INDEX = 336;
    public static final int EOJ_JRS_INVALID_MATCH_COL_NAME = 337;
    public static final int EOJ_JRS_CANT_SET_MATCH_COL_INDEX = 338;
    public static final int EOJ_JRS_CANT_SET_MATCH_COL_NAME = 339;
    public static final int EOJ_JRS_COL_INDEX_NOT_BEEN_SET = 340;
    public static final int EOJ_JRS_COL_NAME_NOT_BEEN_SET = 341;
    public static final int EOJ_JRS_CANT_OBTAIN_CONNECTION = 342;
    public static final int EOJ_JRS_TABLE_NAME_IN_SQL = 343;
    public static final int EOJ_JRS_WRONG_ROWSET_SCROLL_TYPE = 344;
    public static final int EOJ_JRS_OBJ_NOT_SATISFY_FILTER = 345;
    public static final int EOJ_JRS_SERIAL_BLOB_CONSTRUCTOR = 346;
    public static final int EOJ_JRS_SERIAL_CLOB_CONSTRUCTOR = 347;
    public static final int EOJ_JRS_CANT_CREATE_OBJ_COPY = 348;
    public static final int EOJ_JRS_ERROR_CREATE_OBJ_COPY = 349;
    public static final int EOJ_JRS_EMPTY_ROWSET_PARAM = 350;
    public static final int EOJ_JRS_PARAM_NOT_A_ROWSET = 351;
    public static final int EOJ_JRS_UNSUPPORTED_JOIN_TYPE = 352;
    public static final int EOJ_JRS_ROWSET_MATCH_COL_NUM_DIFF = 353;
    public static final int EOJ_JRS_UNSUPPORTED_3RD_PARTY = 354;
    public static final int EOJ_JRS_INVALID_XMLREADER = 355;
    public static final int EOJ_JRS_INVALID_XMLWRITER = 356;
    public static final int EOJ_JRS_NON_NULLABLE_PROPERTY = 357;
    public static final int EOJ_JRS_NON_NULLABLE_METADATA = 358;
    public static final int EOJ_JRS_INVALID_WEBROWSET_ARG = 359;
    public static final int NO_REPLAY_NESTED_TOPLEVEL_CALL = 365;
    public static final int REPLAY_ERROR_SET_CONTAINER = 366;
    public static final int NO_REPLAY_INSUFFICIENT_MEMORY = 367;
    public static final int GETLTXID_CALLED_IN_REPLAY_INIT_CALLBACK = 368;
    public static final int NO_REPLAY_RECONNECT_AC_CHANGED = 369;
    public static final int EOJ_NO_REPLAY = 370;
    public static final int NO_REPLAY_ACTIVE_TXN = 371;
    public static final int NO_REPLAY_NONREPLAYABLE_CALL = 372;
    public static final int NO_REPLAY_LAST_CALL_PLSQL = 373;
    public static final int NO_REPLAY_TXN_MONITORING_FAILED = 374;
    public static final int NO_REPLAY_BEGIN_REPLAY_FAILED = 375;
    public static final int NO_REPLAY_END_REPLAY_FAILED = 376;
    public static final int NO_REPLAY_INITIATION_TIMEOUT_EXCEEDED = 377;
    public static final int NO_REPLAY_RETRIES_EXCEEDED = 378;
    public static final int NO_REPLAY_INIT_CALLBACK_FAILURE = 379;
    public static final int NO_REPLAY_ACTIVE_TXN_IN_INIT_CALLBACK = 380;
    public static final int NO_REPLAY_AFTER_ENDREQUEST = 381;
    public static final int NO_REPLAY_RECONNECT_RETRIES_EXCEEDED = 382;
    public static final int NO_REPLAY_GET_REPLAY_CONTEXT_FAILED = 383;
    public static final int NO_REPLAY_SERVER_CM_DISABLED = 384;
    public static final int NO_REPLAY_PREPARE_REPLAY_FAILED = 385;
    public static final int NO_REPLAY_EMBEDDED_COMMIT = 386;
    public static final int EOJ_REPLAY_ERROR = 387;
    public static final int REPLAY_ERROR_CHECKSUM_MISMATCH = 388;
    public static final int REPLAY_ERROR_ERRCODES_MESG_MISMATCH = 389;
    public static final int REPLAY_ERROR_ACTIVE_TXN = 390;
    public static final int BEGIN_REQUEST_CALLED_DURING_REQUEST = 391;
    public static final int BEGIN_REQUEST_CALLED_WITH_OPEN_TXN = 392;
    public static final int END_REQUEST_CALLED_WITH_OPEN_TXN = 393;
    public static final int SVR_TOO_OLD_TO_SUPPORT_REPLAY = 394;
    public static final int REPLAY_SUPPORT_UNAVAILABLE_IN_SVR = 395;
    public static final int SVR_FAILOVER_TYPE_NOT_TRANSACTION = 396;
    public static final int NO_REPLAY_CONCRETE_CLASSES = 397;
    public static final int NO_REPLAY_SVR_NOREPLAY_FORCE = 398;
    public static final int NO_REPLAY_SVR_DIRECTIVE_MISMATCH = 399;
    public static final int TTC_ERR_BASE = 400;
    public static final int TTC0000 = 401;
    public static final int TTC0001 = 402;
    public static final int TTC0002 = 403;
    public static final int TTC0003 = 404;
    public static final int TTC0004 = 405;
    public static final int TTC0005 = 406;
    public static final int TTC0100 = 407;
    public static final int TTC0101 = 408;
    public static final int TTC0102 = 409;
    public static final int TTC0103 = 410;
    public static final int TTC0104 = 411;
    public static final int TTC0105 = 412;
    public static final int TTC0106 = 413;
    public static final int TTC0107 = 414;
    public static final int TTC0108 = 415;
    public static final int TTC0109 = 416;
    public static final int TTC0110 = 417;
    public static final int TTC0111 = 418;
    public static final int TTC0112 = 419;
    public static final int TTC0113 = 420;
    public static final int TTC0114 = 421;
    public static final int TTC0115 = 422;
    public static final int TTC0116 = 423;
    public static final int TTC0117 = 424;
    public static final int TTC0118 = 425;
    public static final int TTC0119 = 426;
    public static final int TTC0120 = 427;
    public static final int TTC0200 = 428;
    public static final int TTC0201 = 429;
    public static final int TTC0202 = 430;
    public static final int TTC0203 = 431;
    public static final int TTC0204 = 432;
    public static final int TTC0205 = 433;
    public static final int TTC0206 = 434;
    public static final int TTC0207 = 435;
    public static final int TTC0208 = 436;
    public static final int TTC0209 = 437;
    public static final int TTC0210 = 438;
    public static final int TTC0211 = 439;
    public static final int TTC0212 = 440;
    public static final int TTC0213 = 441;
    public static final int TTC0214 = 442;
    public static final int TTC0217 = 444;
    public static final int TTC0218 = 445;
    public static final int TTC0219 = 446;
    public static final int TTC0220 = 447;
    public static final int TTC0221 = 448;
    public static final int TTC0222 = 449;
    public static final int TTC0223 = 450;
    public static final int TTC0224 = 451;
    public static final int TTC0225 = 452;
    public static final int TTC0226 = 453;
    public static final int TTC0227 = 454;
    public static final int TTC0228 = 455;
    private static final int EOJ_EXTENDED_BASE = 1700;
    private static final int EOJ_EXTENDED_MAX = 1999;
    public static final int EOJ_CONFIG_FILE_READ = 1700;
    public static final int EOJ_CONFIG_FILE_PARSE = 1701;
    public static final int EOJ_MULTIPLE_CREDENTIALS_FOUND = 1702;
    public static final int EOJ_RESULT_DESCRIPTION_CHANGED = 1703;
    public static final int EOJ_INVALID_SHARDING_KEY_INFO = 1704;
    public static final int EOJ_UNSUPPORTED_SHARDING_MODE = 1705;
    public static final int EOJ_OSON_TOO_LARGE = 1706;
    public static final int EOJ_INVALID_STATEMENT_DELEGATE = 1707;
    public static final int EOJ_UNSUPPORTED_SHARDING_KEY_CONNECTION = 1708;
    public static final int EOJ_UNSUPPORTED_CATALOG_SERVICE_CONNECTION = 1709;
    public static final int EOJ_UNSUPPORTED_SHARDING_DATABASE_VERSION = 1710;
    public static final int EOJ_EXPIRED_ORACLE_ROW = 1711;
    public static final int EOJ_PUBLISHING_AFTER_ROW_MOVEMENT = 1712;
    public static final int EOJ_SUBSCRIBER_RECEIVED_ON_ERROR = 1713;
    public static final int EOJ_LOGON_TIMEOUT_EXPIRED = 1714;
    public static final int EOJ_TOKEN_CONFIGURATION_FAILURE = 1718;
    public static final int EOJ_INVALID_CONNECTION_PROPERTY = 1721;
    public static final int EOJ_MISSING_CONNECTION_PROPERTY = 1722;

    public static SQLException newSQLException(OracleConnection oracleConnection, String string, int n2) {
        return DatabaseError.newSQLException(oracleConnection, string, n2, null);
    }

    public static SQLException newSQLException(OracleConnection oracleConnection, String string, int n2, Throwable throwable) {
        int n3;
        SQLStateMapping sQLStateMapping = DatabaseError.findSQLStateMapping(n2);
        if (oracleConnection != null && ((n3 = n2 - 17000) >= 401 && n3 <= 453 || n2 == 17294)) {
            switch (n3) {
                case 433: 
                case 439: 
                case 445: 
                case 446: 
                case 448: 
                case 453: {
                    break;
                }
                default: {
                    try {
                        ((PhysicalConnection)oracleConnection).internalClose();
                        break;
                    }
                    catch (SQLException sQLException) {
                        // empty catch block
                    }
                }
            }
        }
        SQLException sQLException = sQLStateMapping.newSQLException(string, n2);
        if (throwable != null) {
            sQLException.initCause(throwable);
        }
        if (oracleConnection != null && sQLException instanceof SQLRecoverableException) {
            oracleConnection.setUsable(false);
        }
        return sQLException;
    }

    public static SQLException newSQLException(OracleConnection oracleConnection, int n2) {
        int n3 = DatabaseError.getVendorCode(n2);
        String string = DatabaseError.findMessage(n2, null);
        return DatabaseError.newSQLException(oracleConnection, string, n3);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, String string, int n2, Throwable throwable) {
        return DatabaseError.newSQLException(oracleConnection, string, n2, throwable);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, String string, int n2) {
        return DatabaseError.newSQLException(oracleConnection, string, n2, null);
    }

    public static SQLException createSqlException(int n2, Object object) {
        return DatabaseError.createSqlException(null, n2, object);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, int n2, Object object, Throwable throwable) {
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        String string = DatabaseError.findMessage(n2, object);
        int n3 = DatabaseError.getVendorCode(n2);
        return DatabaseError.createSqlException(oracleConnection, string, n3, throwable);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, int n2, Object object) {
        return DatabaseError.createSqlException(oracleConnection, n2, object, null);
    }

    public static SQLException createSqlException(OracleSQLException oracleSQLException) {
        return DatabaseError.createSqlException(null, oracleSQLException);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, OracleSQLException oracleSQLException) {
        int n2 = oracleSQLException.getErrorCode();
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        String string = oracleSQLException.getMessage();
        SQLException sQLException = DatabaseError.newSQLException(oracleConnection, string, n2);
        sQLException.initCause(oracleSQLException);
        return sQLException;
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, SQLException sQLException, int n2, Object object) {
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        String string = DatabaseError.findMessage(n2, object);
        int n3 = DatabaseError.getVendorCode(n2);
        SQLException sQLException2 = DatabaseError.newSQLException(oracleConnection, string, n3);
        sQLException2.setNextException(sQLException);
        return sQLException2;
    }

    public static SQLException createSqlException(int n2) {
        return DatabaseError.createSqlException(null, n2);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, int n2) {
        return DatabaseError.createSqlException(oracleConnection, n2, null);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, int n2, Object object, Throwable throwable, String string) {
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        String string2 = DatabaseError.findMessage(n2, object);
        String string3 = string2 + string;
        int n3 = DatabaseError.getVendorCode(n2);
        return DatabaseError.createSqlException(oracleConnection, string3, n3, throwable);
    }

    public static SQLException formatSqlException(OracleConnection oracleConnection, int n2, Object object, Throwable throwable, Object ... objectArray) {
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        String string = DatabaseError.findMessage(n2, object);
        String string2 = MessageFormat.format(string, objectArray);
        int n3 = DatabaseError.getVendorCode(n2);
        return DatabaseError.createSqlException(oracleConnection, string2, n3, throwable);
    }

    public static SQLException createSqlException(IOException iOException) {
        return DatabaseError.createSqlException(null, iOException);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, IOException iOException) {
        int n2;
        String string = iOException.getMessage();
        int n3 = 0;
        if (string != null && (n2 = string.indexOf("ORA-")) != -1) {
            int n4 = string.length() - 1;
            int n5 = string.indexOf(":", n2 += 4);
            int n6 = string.indexOf(",", n2);
            if (n5 != -1) {
                n4 = n5;
            }
            if (n6 != -1) {
                n4 = Math.min(n6, n4);
            }
            try {
                n3 = Integer.parseInt(string.substring(n2, n4));
            }
            catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                return DatabaseError.createSqlException(oracleConnection, 2, string, (Throwable)iOException);
            }
            catch (NumberFormatException numberFormatException) {
                return DatabaseError.createSqlException(oracleConnection, 2, string, (Throwable)iOException);
            }
        } else {
            return DatabaseError.createSqlException(oracleConnection, 2, string, (Throwable)iOException);
        }
        return DatabaseError.createSqlException(oracleConnection, string, n3, (Throwable)iOException);
    }

    public static SQLException createSqlException(Exception exception) {
        return DatabaseError.createSqlException(null, exception);
    }

    public static SQLException createSqlException(OracleConnection oracleConnection, Exception exception) {
        if (exception instanceof SQLException) {
            return (SQLException)exception;
        }
        return DatabaseError.createSqlException(oracleConnection, exception.getMessage(), DatabaseError.getVendorCode(1), (Throwable)exception);
    }

    public static void addSqlException(SQLException sQLException, String string, int n2) {
        SQLException sQLException2 = DatabaseError.newSQLException(null, string, n2);
        sQLException.setNextException(sQLException2);
    }

    public static BatchUpdateException createBatchUpdateException(SQLException sQLException, int n2, int[] nArray) {
        int n3 = 0;
        int[] nArray2 = null;
        if (n2 < 0) {
            n2 = 0;
        }
        if (nArray == null) {
            nArray2 = new int[]{};
        } else if (n2 >= nArray.length) {
            nArray2 = nArray;
        } else {
            nArray2 = new int[n2];
            for (n3 = 0; n3 < n2; ++n3) {
                nArray2[n3] = nArray[n3];
            }
        }
        BatchUpdateException batchUpdateException = new BatchUpdateException(sQLException.getMessage(), sQLException.getSQLState(), sQLException.getErrorCode(), nArray2);
        batchUpdateException.setNextException(sQLException);
        return batchUpdateException;
    }

    public static BatchUpdateException createBatchUpdateException(String string, String string2, int n2, int n3, int[] nArray) {
        int n4 = 0;
        int[] nArray2 = null;
        if (n3 < 0) {
            n3 = 0;
        }
        if (nArray == null) {
            nArray2 = new int[]{};
        } else if (n3 >= nArray.length) {
            nArray2 = nArray;
        } else {
            nArray2 = new int[n3];
            for (n4 = 0; n4 < n3; ++n4) {
                nArray2[n4] = nArray[n4];
            }
        }
        return new BatchUpdateException(string, string2, n2, nArray2);
    }

    public static BatchUpdateException createBatchUpdateException(int n2, Object object, int n3, int[] nArray) {
        if (n2 == 0) {
            return null;
        }
        String string = DatabaseError.findMessage(n2, object);
        int n4 = DatabaseError.getVendorCode(n2);
        return DatabaseError.createBatchUpdateException(string, null, n4, n3, nArray);
    }

    public static BatchUpdateException createBatchUpdateException(int n2, int n3, int[] nArray) {
        return DatabaseError.createBatchUpdateException(n2, null, n3, nArray);
    }

    public static BatchUpdateException createBatchUpdateException(SQLException sQLException, int n2, long[] lArray) {
        return DatabaseError.createBatchUpdateException(sQLException, null, n2, lArray);
    }

    public static BatchUpdateException createBatchUpdateException(SQLException sQLException, String string, int n2, long[] lArray) {
        int n3 = 0;
        long[] lArray2 = null;
        if (n2 < 0) {
            n2 = 0;
        }
        if (lArray == null) {
            lArray2 = new long[]{};
        } else if (n2 >= lArray.length) {
            lArray2 = lArray;
        } else {
            lArray2 = new long[n2];
            for (n3 = 0; n3 < n2; ++n3) {
                lArray2[n3] = lArray[n3];
            }
        }
        String string2 = sQLException.getMessage();
        if (string != null) {
            if (string2 != null) {
                string2 = string2.replace("\n", ".");
            }
            string2 = string2 != null ? string2 + string : string;
        }
        BatchUpdateException batchUpdateException = new BatchUpdateException(string2, sQLException.getSQLState(), sQLException.getErrorCode(), lArray2, (Throwable)null);
        batchUpdateException.setNextException(sQLException);
        return batchUpdateException;
    }

    public static BatchUpdateException createBatchUpdateException(String string, String string2, int n2, int n3, long[] lArray) {
        int n4 = 0;
        long[] lArray2 = null;
        if (n3 < 0) {
            n3 = 0;
        }
        if (lArray == null) {
            lArray2 = new long[]{};
        } else if (n3 >= lArray.length) {
            lArray2 = lArray;
        } else {
            lArray2 = new long[n3];
            for (n4 = 0; n4 < n3; ++n4) {
                lArray2[n4] = lArray[n4];
            }
        }
        return new BatchUpdateException(string, string2, n2, lArray2, (Throwable)null);
    }

    public static BatchUpdateException createBatchUpdateException(int n2, Object object, int n3, long[] lArray) {
        if (n2 == 0) {
            return null;
        }
        String string = DatabaseError.findMessage(n2, object);
        int n4 = DatabaseError.getVendorCode(n2);
        return DatabaseError.createBatchUpdateException(string, null, n4, n3, lArray);
    }

    public static BatchUpdateException createBatchUpdateException(int n2, int n3, long[] lArray) {
        return DatabaseError.createBatchUpdateException(n2, null, n3, lArray);
    }

    public static SQLFeatureNotSupportedException createUnsupportedFeatureSqlException() {
        return DatabaseError.createSQLFeatureNotSupportedException();
    }

    public static SQLFeatureNotSupportedException createUnsupportedFeatureSqlException(String string) {
        return DatabaseError.createSQLFeatureNotSupportedException(string);
    }

    public static SQLFeatureNotSupportedException createSQLFeatureNotSupportedException() {
        return DatabaseError.createSQLFeatureNotSupportedException("");
    }

    public static SQLFeatureNotSupportedException createSQLFeatureNotSupportedException(String string) {
        return DatabaseError.createSQLFeatureNotSupportedException(23, string);
    }

    private static SQLFeatureNotSupportedException createSQLFeatureNotSupportedException(int n2, String string) {
        int n3 = DatabaseError.getVendorCode(n2);
        String string2 = DatabaseError.findMessage(n2, (Object)string);
        SQLStateMapping sQLStateMapping = DatabaseError.findSQLStateMapping(n3);
        String string3 = sQLStateMapping.sqlState;
        return new SQLFeatureNotSupportedException(string2, string3, n3);
    }

    public static SQLClientInfoException createSQLClientInfoException(int n2, Map<String, ClientInfoStatus> map, Throwable throwable) {
        if (n2 == 0 || n2 == 13) {
            return null;
        }
        int n3 = DatabaseError.getVendorCode(n2);
        String string = DatabaseError.findMessage(n2, null);
        SQLStateMapping sQLStateMapping = DatabaseError.findSQLStateMapping(n3);
        String string2 = sQLStateMapping.sqlState;
        return new SQLClientInfoException(string, string2, n3, map, throwable);
    }

    public static IOException createIOException(SQLException sQLException) {
        return new IOException(sQLException.getMessage(), sQLException);
    }

    public static XAException createXAException(int n2) {
        return new XAException(n2);
    }

    public static XAException createXAException(String string, int n2) {
        XAException xAException = new XAException(string);
        xAException.errorCode = n2;
        return xAException;
    }

    static String findMessage(int n2, Object object) {
        String string = DatabaseError.getMsgKey(n2);
        return DatabaseError.findMessage(string, object);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String findMessage(String string, Object object) {
        String string2 = null;
        if (!loadedMessages) {
            try {
                message = (Message)Class.forName(msgClassName).newInstance();
                loadedMessages = true;
            }
            catch (ClassNotFoundException classNotFoundException) {
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (InstantiationException instantiationException) {
            }
            finally {
                if (!loadedMessages) {
                }
            }
        }
        string2 = message == null ? (object == null ? string + ": (no message for error)" : string + ": (no message for error) " + object) : message.msg(string, object);
        return string2;
    }

    public static SQLWarning newSqlWarning(String string, int n2) {
        return DatabaseError.addSqlWarning(null, string, n2);
    }

    public static SQLWarning newSqlWarning(int n2, Object object) {
        return DatabaseError.addSqlWarning(null, n2, object);
    }

    public static SQLWarning newSqlWarning(int n2) {
        return DatabaseError.addSqlWarning(null, n2);
    }

    public static SQLWarning addSqlWarning(SQLWarning sQLWarning, String string, int n2) {
        String string2 = DatabaseError.ErrorToSQLState(n2);
        SQLWarning sQLWarning2 = new SQLWarning(string, string2, n2);
        return DatabaseError.addSqlWarning(sQLWarning, sQLWarning2);
    }

    public static SQLWarning addSqlWarning(SQLWarning sQLWarning, SQLWarning sQLWarning2) {
        if (sQLWarning == null) {
            return sQLWarning2;
        }
        sQLWarning.setNextWarning(sQLWarning2);
        return sQLWarning;
    }

    public static SQLWarning addSqlWarning(SQLWarning sQLWarning, int n2, Object object) {
        if (n2 == 0 || n2 == 13) {
            return sQLWarning;
        }
        String string = DatabaseError.findMessage(n2, object);
        int n3 = DatabaseError.getVendorCode(n2);
        return DatabaseError.addSqlWarning(sQLWarning, "Warning: " + string, n3);
    }

    public static SQLWarning addSqlWarning(SQLWarning sQLWarning, int n2) {
        return DatabaseError.addSqlWarning(sQLWarning, n2, null);
    }

    public static String ErrorToSQLState(int n2) {
        return DatabaseError.findSQLStateMapping((int)n2).sqlState;
    }

    static SQLStateMapping findSQLStateMapping(int n2) {
        for (int i2 = 0; i2 < mappings.length; ++i2) {
            if (!mappings[i2].isIncluded(n2)) continue;
            return mappings[i2];
        }
        return SQLStateMapping.DEFAULT_SQLSTATE;
    }

    public static int getVendorCode(int n2) {
        if (DatabaseError.isCommonError(n2)) {
            return n2;
        }
        if (n2 < 2000 && n2 > 17499 && n2 < 1700) {
        }
        return 17000 + n2;
    }

    private static boolean isCommonError(int n2) {
        switch (n2) {
            case 25707: 
            case 25708: {
                return true;
            }
        }
        return false;
    }

    static String getMsgKey(int n2) {
        int n3 = DatabaseError.getVendorCode(n2);
        String string = "ORA-" + Integer.toString(n3);
        return string;
    }

    public static void test() {
        try {
            throw DatabaseError.createSqlException(null, "exception_message_1", 25);
        }
        catch (SQLException sQLException) {
            try {
                throw DatabaseError.createSqlException(null, 412, "object_string");
            }
            catch (SQLException sQLException2) {
                try {
                    throw DatabaseError.createSqlException(null, 6);
                }
                catch (SQLException sQLException3) {
                    try {
                        throw DatabaseError.createSqlException(null, 999);
                    }
                    catch (SQLException sQLException4) {
                        try {
                            throw DatabaseError.createSqlException(null, 13);
                        }
                        catch (SQLException sQLException5) {
                            try {
                                IOException iOException = new IOException("ORA-00601: cleanup lock conflict");
                                throw DatabaseError.createSqlException(null, iOException);
                            }
                            catch (SQLException sQLException6) {
                                try {
                                    IOException iOException = new IOException("some unknown io exception");
                                    throw DatabaseError.createSqlException(null, iOException);
                                }
                                catch (SQLException sQLException7) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Log
    public static void printSqlException(SQLException sQLException) {
        Logger.getLogger("oracle.jdbc").log(Level.FINEST, "SQLException:\n  message  = \"" + sQLException.getMessage() + "\"\n  sqlState = \"" + sQLException.getSQLState() + "\"\n  errCode  = " + sQLException.getErrorCode());
    }
}

