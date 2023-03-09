package assessment.controller;

import static assessment.Constants.*;

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

import assessment.exception.TransactionFailedException;
import assessment.model.Account;
import assessment.model.Transfer;
import assessment.repo.AccountsRepository;
import assessment.service.FundsTransferService;
import assessment.service.LogsAuditService;
import assessment.util.Validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

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

        // pass entity for validation
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
            FieldError fieldErr = new FieldError(
                    TRANSFER_OBJECT_NAME,
                    "Transfer Fail",
                    e.getMessage());
            binding.addError(fieldErr);
            model.addAttribute("listOfAccounts", listOfAccounts);
            model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
            model.addAttribute("listOfErr", binding.getFieldErrors());
            return LANDING_HTML_FILENAME;
        }

        // assigning names to accountId in transfer object
        listOfAccounts.stream()
                .forEach(account -> {
                    String accountId = account.getAccountId();
                    if (accountId.equals(transfer.getFromAccount())) {
                        transfer.setFromName(account.getName());
                    }
                    if (accountId.equals(transfer.getToAccount())) {
                        transfer.setToName(account.getName());
                    }
                });

        // no input errors and transaction successful
        logsService.logTransactionToRedis(transactionId, transfer);
        model.addAttribute(TRANSFER_OBJECT_NAME, transfer);
        model.addAttribute("transactionId", transactionId);
        return SUCCESS_HTML_FILENAME;
    }
}
