package sg.edu.nus.iss.app.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.app.assessment.Util;
import sg.edu.nus.iss.app.assessment.model.Transfer;

import static sg.edu.nus.iss.app.assessment.Util.*;

@Service
public class LogsAuditService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void logTransactionToRedis(String transactionsId, Transfer transfer) {

        JsonObject json = Util.logToRedis(transactionsId, transfer);
        redisTemplate.opsForValue().set(transactionsId, json.toString());

    }
}
