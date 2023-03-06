package assessment.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import assessment.model.Account;

import static assessment.repo.Queries.*;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class AccountsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Boolean doesAccountExist(String accountId) {
        Object[] args = new Object[] { accountId };
        Account acc = jdbcTemplate.queryForObject(GET_INDIVIDUAL_ACCOUNT_SQL,
                BeanPropertyRowMapper.newInstance(Account.class), args);
        if (acc != null) {
            return true;
        }
        return false;
    }

    public List<Account> getListAccounts() {
        return jdbcTemplate.query(GET_LIST_ACCOUNTS_SQL,
                BeanPropertyRowMapper.newInstance(Account.class));
    }

    public Account getBalance(String accountId) {
        return jdbcTemplate.queryForObject(GET_ACCOUNT_BAL_SQL, BeanPropertyRowMapper.newInstance(Account.class),
                accountId);
    }

    public int updateBalance(String accountId, BigDecimal balance) {
        Object[] args = new Object[] { balance, accountId };
        return jdbcTemplate.update(UPDATE_BAL_SQL, args);
    }

    // method not used but could be when transferring funds between accounts
    public int[] batchUpdate(String fromAccount, String toAccount, BigDecimal fromBalance, BigDecimal toBalance) {

        Object[] args1 = new Object[] { fromAccount, fromBalance };
        Object[] args2 = new Object[] { toAccount, toBalance };
        List<Object[]> argsList = List.of(args1, args2);
        return jdbcTemplate.batchUpdate(UPDATE_BAL_SQL, argsList);

    }
}
