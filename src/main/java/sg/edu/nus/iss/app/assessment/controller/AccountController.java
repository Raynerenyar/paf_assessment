package sg.edu.nus.iss.app.assessment.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.app.assessment.exception.SomethingException;
import sg.edu.nus.iss.app.assessment.model.Account;
import sg.edu.nus.iss.app.assessment.model.Transfer;
import sg.edu.nus.iss.app.assessment.repo.AccountsRepository;
import sg.edu.nus.iss.app.assessment.service.FundsTransferService;
import sg.edu.nus.iss.app.assessment.service.LogsAuditService;

@Controller
public class AccountController {

    @Autowired
    AccountsRepository acctRepo;

    @Autowired
    FundsTransferService fundsService;

    @Autowired
    LogsAuditService logsService;

    @GetMapping(path = "/")
    public String getLandingPage(Transfer transfer, Model model) {

        List<Account> listOfAccounts = acctRepo.getListAccounts();
        model.addAttribute("listOfAccounts", listOfAccounts);
        model.addAttribute("transfer", transfer);
        return "landing";
    }

    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String posting(@Valid Transfer transfer, BindingResult binding, Model model) {

        System.out.println("transfer object >>>> " + transfer.toString());

        List<Account> listOfAccounts = acctRepo.getListAccounts();

        String fromAcc = transfer.getFromAccount();
        String toAcc = transfer.getToAccount();
        float amount = transfer.getAmount();

        Boolean fromResult = acctRepo.doesAccountExist(fromAcc);
        Boolean toResult = acctRepo.doesAccountExist(toAcc);
        Boolean sameAccountResult = fromAcc.equalsIgnoreCase(toAcc);
        Boolean accountBalResult = acctRepo.getBalance(fromAcc).getBalance() > amount;

        List<Boolean> results = List.of(fromResult, toResult, sameAccountResult);

        for (int index = 0; index < results.size(); index++) {
            if (index == 0 && results.get(index) == false) {
                FieldError fieldErr = new FieldError("Transfer", "fromAccount", "Account does not exists");
                binding.addError(fieldErr);
            }
            if (index == 1 && results.get(index) == false) {
                FieldError fieldErr = new FieldError("Transfer", "toAccount", "Account does not exists");
                binding.addError(fieldErr);
            }
            System.out.println(results.get(index));
            if (index == 2 && results.get(index) == true) {
                FieldError fieldErr = new FieldError("Transfer", "sameAccount",
                        "From and to accounts must not be the same");
                binding.addError(fieldErr);
            }
        }
        if (!accountBalResult) {
            FieldError fieldErr = new FieldError("Transfer", "balance",
                    "In sufficient balance in account");
            binding.addError(fieldErr);

        }

        transfer.setFromAccountExist(true);
        transfer.setToAccountExist(true);
        transfer.setSufficientBalance(true);

        if (binding.hasErrors()) {
            model.addAttribute("listOfAccounts", listOfAccounts);
            model.addAttribute("transfer", transfer);
            return "landing";
        }
        String transactionId;
        try {
            transactionId = fundsService.transferAmount(fromAcc, toAcc, amount);
        } catch (SomethingException e) {
            FieldError fieldErr = new FieldError("Transfer", "transferFail",
                    "Funds fail to transfer");
            binding.addError(fieldErr);
            model.addAttribute("listOfAccounts", listOfAccounts);
            model.addAttribute("transfer", transfer);
            return "landing";
        }
        System.out.println("successful transfer");
        System.out.println(transfer.toString());

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
        System.out.println(transfer.toString());
        logsService.logTransactionToRedis(transactionId, transfer);
        model.addAttribute("transfer", transfer);
        model.addAttribute("transactionId", transactionId);
        return "success";
    }
}
