package sg.edu.nus.iss.app.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.edu.nus.iss.app.assessment.Util;
import sg.edu.nus.iss.app.assessment.exception.SomethingException;
import sg.edu.nus.iss.app.assessment.repo.AccountsRepository;

@Service
public class FundsTransferService {

    @Autowired
    AccountsRepository acctRepo;

    private static final Logger logger = LoggerFactory.getLogger(FundsTransferService.class);

    @Transactional(rollbackFor = SomethingException.class)
    public void transferAmount(String fromAccount, String toAccount, float amount) throws SomethingException {
        String transactionId = Util.generateId();
        float fromAccBal = acctRepo.getBalance(fromAccount).getBalance();
        float toAccBal = acctRepo.getBalance(toAccount).getBalance();

        fromAccBal -= amount;
        toAccBal += amount;

        int resultOne = acctRepo.updateBalance(fromAccount, fromAccBal);
        int resultTwo = acctRepo.updateBalance(toAccount, toAccBal);

        if (resultOne != 1 || resultTwo != 1) {
            try {
                throw new SomethingException("Transfer of funds fail");
            } catch (Exception e) {
                if (e instanceof SomethingException) {
                    logger.info(e.getLocalizedMessage());
                    throw new SomethingException();
                }
                logger.error(e.getMessage());

            }
        }

        // to test if @transaction works use statement below
        // throw new SomethingException("Transfer of funds fail");

    }

}
