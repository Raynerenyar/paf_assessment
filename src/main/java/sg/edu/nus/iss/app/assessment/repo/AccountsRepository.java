package sg.edu.nus.iss.app.assessment.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.app.assessment.model.Account;

import static sg.edu.nus.iss.app.assessment.Queries.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
public class AccountsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Boolean doesAccountExist(String accountId) {

        return jdbcTemplate.query(GET_INDIVIDUAL_ACCOUNT_SQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, accountId);
            }

        }, new ResultSetExtractor<Boolean>() {

            @Override
            public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return true;
                }
                return false;
            }

        });
    }

    public List<Account> getListAccounts() {
        return jdbcTemplate.query(GET_LIST_ACCOUNTS_SQL, new ResultSetExtractor<List<Account>>() {

            List<Account> list = new LinkedList<>();

            @Override
            public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    BeanPropertyRowMapper<Account> bprm = new BeanPropertyRowMapper<>(Account.class);
                    list.add(bprm.mapRow(rs, 0));
                }
                return list;
            }

        });
    }

    public Account getBalance(String accountId) {
        return jdbcTemplate.queryForObject(GET_ACCOUNT_BAL_SQL, BeanPropertyRowMapper.newInstance(Account.class),
                accountId);
    }

    public int updateBalance(String accountId, float balance) {
        return jdbcTemplate.update(UPDATE_BAL_SQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setFloat(1, balance);
                ps.setString(2, accountId);
            }

        });
    }
}
