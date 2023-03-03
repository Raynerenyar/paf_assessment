package sg.edu.nus.iss.app.assessment;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.app.assessment.model.Transfer;
import sg.edu.nus.iss.app.assessment.repo.AccountsRepository;

public class Util {

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static JsonObject logToRedis(String transactionId, Transfer transfer) {

        JsonObject jsobj = Json.createObjectBuilder()
                .add("transactionId", transactionId)
                .add("date", new Date().toString())
                .add("from_account", transfer.getFromAccount())
                .add("to_account", transfer.getToAccount())
                .add("amount", transfer.getAmount())
                .build();

        return jsobj;
    }

}
