package assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import assessment.model.Transfer;
import assessment.util.Util;
import jakarta.json.JsonObject;

@Service
public class LogsAuditService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void logTransactionToRedis(String transactionsId, Transfer transfer) {

        JsonObject json = Util.convertToJson(transactionsId, transfer);
        redisTemplate.opsForValue().set(transactionsId, json.toString());

    }
}
