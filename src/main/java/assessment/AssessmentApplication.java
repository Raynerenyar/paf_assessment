package assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import assessment.repo.AccountsRepository;

@SpringBootApplication
public class AssessmentApplication {

	@Autowired
	AccountsRepository accRepo;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}
}
