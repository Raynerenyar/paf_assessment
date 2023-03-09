package assessment;

import java.math.BigDecimal;

public class Constants {

    public static final String LOCAL_DEV = "LOCALDEVELOPMENT";

    // mysql config constants
    public static final String MYSQL_URL = "MYSQL_URL";
    public static final String MYSQL_DATABASE = "MYSQLDATABASE";
    public static final String MYSQL_USER = "MYSQLUSER";
    public static final String MYSQL_PASSOWORD = "MYSQLPASSWORD";
    public static final String MYSQL_PORT = "MYSQLPORT";
    public static final String MYSQL_HOST = "MYSQLHOST";

    // redis config constants
    public static final String REDIS_HOST = "REDISHOST";
    public static final String REDIS_PORT = "REDISPORT";
    public static final String REDIS_USERNAME = "REDISUSERNAME";
    public static final String REDIS_PASSWORD = "REDISPASSWORD";

    // minimum value for transfer
    private static final int MIN_VAL_TRANSFER_INT = 10;
    public static final BigDecimal MIN_VAL_TRANSFER_BIG_DECI = BigDecimal.valueOf(MIN_VAL_TRANSFER_INT);

    // Transfer class
    public static final String TRANSFER_OBJECT_NAME = "Transfer";
    public static final String TRANSFER_FIELD_FROM_ACCOUNT = "From Account";
    public static final String TRANSFER_FIELD_TO_ACCOUNT = "To Account";
    public static final String TRANSFER_FIELD_AMOUNT = "Amount";

    // Account class
    public static final String ACCOUNT_OBJECT_NAME = "Account";
    public static final String ACCOUNT_FIELD_BALANCE = "Balance";

    // html file names
    public static final String LANDING_HTML_FILENAME = "landing";
    public static final String SUCCESS_HTML_FILENAME = "success";
}
