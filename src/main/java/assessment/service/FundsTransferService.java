package assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assessment.exception.TransactionFailedException;
import assessment.repo.AccountsRepository;
import assessment.util.Util;

import java.math.BigDecimal;

@Service
public class FundsTransferService {

    @Autowired
    private AccountsRepository acctRepo;

    @Transactional(rollbackFor = TransactionFailedException.class)
    public String transferAmount(String fromAccount, String toAccount, BigDecimal amount)
            throws TransactionFailedException {

        // check if account exist
        if (!(acctRepo.doesAccountExist(fromAccount) || acctRepo.doesAccountExist(toAccount))) {
            throw new TransactionFailedException("Accounts does not exist");
        }

        String transactionId = Util.generateId();
        BigDecimal fromAccBal = acctRepo.getBalance(fromAccount).getBalance();
        BigDecimal toAccBal = acctRepo.getBalance(toAccount).getBalance();

        // checks if there's sufficient balance for deduction in from-account
        if (fromAccBal.compareTo(amount) == -1) {
            throw new TransactionFailedException("Account " + fromAccount + " has insufficient balance");
        }

        fromAccBal.add(amount.negate());
        toAccBal.add(amount);

        // updates balances
        int resultOne = acctRepo.updateBalance(fromAccount, fromAccBal);
        int resultTwo = acctRepo.updateBalance(toAccount, toAccBal);

        if (resultOne != 1 || resultTwo != 1) {
            throw new TransactionFailedException("Transfer of funds fail");
        }

        return transactionId;
    }

}
