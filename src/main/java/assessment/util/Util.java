package assessment.util;

import java.util.Date;
import java.util.UUID;

import assessment.model.Transfer;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Util {

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static JsonObject convertToJson(String transactionId, Transfer transfer) {

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
