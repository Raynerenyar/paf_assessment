package sg.edu.nus.iss.app.assessment;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.app.assessment.model.Account;
import sg.edu.nus.iss.app.assessment.model.Transfer;
import sg.edu.nus.iss.app.assessment.repo.AccountsRepository;
import sg.edu.nus.iss.app.assessment.service.LogsAuditService;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner {

	// @Autowired
	// AccountsRepository acctRepo;

	@Autowired
	LogsAuditService logsService;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Boolean result = acctRepo.doesAccountExist("ckTV56axff");
		// System.out.println("cmdline runner >>>> " + result);

		// List<Account> list = acctRepo.getListAccounts();
		// list.stream().forEach(x -> System.out.println(x.toString()));

		// Account acc = acctRepo.getBalance("ckTV56axff");
		// System.out.println(acc.getBalance());

		// int res = acctRepo.updateBalance("ckTV56axff", 1000);
		// String id = Util.generateId();

		// Transfer transfer = new Transfer();
		// transfer.setAmount(10);
		// transfer.setFromAccount("ckTV56axff");
		// transfer.setToAccount("fhRq46Y6vB");

		// logsService.logTransactionToRedis(id, transfer);
	}

}
