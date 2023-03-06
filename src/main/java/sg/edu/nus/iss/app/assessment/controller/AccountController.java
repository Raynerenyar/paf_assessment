package sg.edu.nus.iss.app.assessment.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import sg.edu.nus.iss.app.assessment.exception.TransactionFailedException;
import sg.edu.nus.iss.app.assessment.model.Account;
import sg.edu.nus.iss.app.assessment.model.Transfer;
import sg.edu.nus.iss.app.assessment.repo.AccountsRepository;
import sg.edu.nus.iss.app.assessment.service.FundsTransferService;
import sg.edu.nus.iss.app.assessment.service.LogsAuditService;
import sg.edu.nus.iss.app.assessment.util.Validations;

import static sg.edu.nus.iss.app.assessment.Constants.*;

@Controller
public class AccountController {

    @Autowired
    private AccountsRepository acctRepo;
    @Autowired
    private FundsTransferService fundsService;
    @Autowired
    private LogsAuditService logsService;
    @Autowired
    private Validations validations;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping(path = "/")
    public String getLandingPage(Transfer transfer, Model model) {

        List<Account> listOfAccounts = acctRepo.getListAccounts();
        model.addAttribute("listOfAccounts", listOfAccounts);
        model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
        return LANDING_HTML_FILENAME;
    }

    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String posting(@Valid Transfer transfer, BindingResult binding, Model model) {

        // sends object to validate
        binding = validations.validateThis(transfer, binding, acctRepo);

        // if any input validation error
        List<Account> listOfAccounts = acctRepo.getListAccounts();
        if (binding.hasErrors()) {
            model.addAttribute("listOfAccounts", listOfAccounts);
            model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
            model.addAttribute("listOfErr", binding.getFieldErrors());
            return LANDING_HTML_FILENAME;
        }

        String fromAcc = transfer.getFromAccount();
        String toAcc = transfer.getToAccount();
        BigDecimal amount = transfer.getAmount();
        String transactionId;
        try {
            transactionId = fundsService.transferAmount(fromAcc, toAcc, amount);
        } catch (TransactionFailedException e) {
            logger.error(e.getMessage());
            FieldError fieldErr = new FieldError(TRANSFER_OBJECT_NAME, "Transfer Fail",
                    "Funds fail to transfer");
            binding.addError(fieldErr);
            model.addAttribute("listOfAccounts", listOfAccounts);
            model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
            model.addAttribute("listOfErr", binding.getFieldErrors());
            return LANDING_HTML_FILENAME;
        }

        // assigning names to accountId in transfer object
        listOfAccounts.stream()
                .forEach(x -> {
                    String accountId = x.getAccountId();
                    if (accountId.equals(transfer.getFromAccount())) {
                        transfer.setFromName(x.getName());
                    }
                    if (accountId.equals(transfer.getToAccount())) {
                        transfer.setToName(x.getName());
                    }
                });

        // no input errors and transaction successful
        logsService.logTransactionToRedis(transactionId, transfer);
        model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
        model.addAttribute("transactionId", transactionId);
        return SUCCESS_HTML_FILENAME;
    }
}
