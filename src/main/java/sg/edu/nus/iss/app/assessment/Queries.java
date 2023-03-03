package sg.edu.nus.iss.app.assessment;

public class Queries {

        public static final String GET_INDIVIDUAL_ACCOUNT_SQL = """
                        SELECT * FROM accounts WHERE account_id = ?
                        """;
        public static final String GET_LIST_ACCOUNTS_SQL = """
                        SELECT account_id, name FROM accounts;
                                """;

        public static final String GET_ACCOUNT_BAL_SQL = """
                        SELECT balance FROM accounts WHERE account_id = ?
                                """;

        public static final String UPDATE_BAL_SQL = """
                        UPDATE accounts SET balance = ? WHERE account_id = ?;
                        """;
}
