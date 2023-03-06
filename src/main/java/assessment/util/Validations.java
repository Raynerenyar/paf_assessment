package assessment.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import assessment.model.Transfer;
import assessment.repo.AccountsRepository;

import static assessment.Constants.*;
import static java.util.Map.entry;

/* 
 * This class contains all the validation required by the controller.
 * 1.   Each non-private field is a criteria for validation.
 * 2.   Each criteria is mapped to a field of the entity i.e. Transfer.class
 * 3.   Each criteria is also mapped to its respective error messages that would be shown
 * 4.   Populate the criteria. true when criteria has been met, vice versa.
 * 5.   Instantiate FieldError for each criteria that has failed
 *      and add to BindingResult object
 * 6.   Return BindingResult object to controller which will then be
 *      attached to the model in the controller.
 */
@Component
public class Validations {
    private static final Logger logger = LoggerFactory.getLogger(Validations.class);

    // 1.
    boolean fromResult;
    boolean toResult;
    boolean diffAccountResult;
    boolean accountBalResult;
    boolean amtDecimalResult;
    boolean amtPostiveResult;
    boolean amtMinResult;
    boolean amtNotNullResult;

    // 2.
    private Map<String, String> criteriaFieldmap = Map.ofEntries(
            entry(VALID_FIELD_FROM_ACCT, VALID_FIELD_NAME_FROM_ACCT),
            entry(VALID_FIELD_TO_ACCT, VALID_FIELD_NAME_TO_ACCT),
            entry(VALID_FIELD_SAME_ACCTS, VALID_FIELD_NAME_SAME_ACCTS),
            entry(VALID_FIELD_ACCT_BAL, VALID_FIELD_NAME_ACCT_BAL),
            entry(VALID_FIELD_AMOUNT_DECI, VALID_FIELD_NAME_AMOUNT_DECI),
            entry(VALID_FIELD_AMOUNT_POSI, VALID_FIELD_NAME_AMOUNT_POSI),
            entry(VALID_FIELD_AMOUNT_MIN, VALID_FIELD_NAME_AMOUNT_MIN),
            entry(VALID_FIELD_AMOUNT_NOT_NULL, VALID_FIELD_NAME_AMOUNT_NOT_NULL));

    // 3.
    private Map<String, String> errorMessagesMap = Map.ofEntries(
            entry(VALID_FIELD_FROM_ACCT, "Account does not exists."),
            entry(VALID_FIELD_TO_ACCT, "Account does not exists."),
            entry(VALID_FIELD_SAME_ACCTS, "From and to accounts must not be the same."),
            entry(VALID_FIELD_ACCT_BAL, "Insufficient balance in account."),
            entry(VALID_FIELD_AMOUNT_DECI, "Maximum 2 decimal places allowed."),
            entry(VALID_FIELD_AMOUNT_POSI, "Must be positive value."),
            entry(VALID_FIELD_AMOUNT_MIN, "Minimum value to transfer is " + MIN_VAL_TRANSFER + "."),
            entry(VALID_FIELD_AMOUNT_NOT_NULL, "Please fill in amount to transfer."));

    public BindingResult validateThis(Transfer entity, BindingResult binding, AccountsRepository acctRepo) {

        String fromAcc = entity.getFromAccount();
        String toAcc = entity.getToAccount();
        BigDecimal amount = (entity.getAmount()) == null ? BigDecimal.valueOf(0) : entity.getAmount();

        boolean isWholeNumber = amount.toString().indexOf(".") == -1;
        int numOfDecimalPoints = (amount.toString().length() - (amount.toString().indexOf(".") + 1));

        // 4.
        fromResult = acctRepo.doesAccountExist(fromAcc);
        toResult = acctRepo.doesAccountExist(toAcc);
        diffAccountResult = !fromAcc.equalsIgnoreCase(toAcc);
        accountBalResult = (acctRepo.getBalance(fromAcc).getBalance().compareTo(amount)) >= 0 ? true : false;
        amtDecimalResult = (isWholeNumber || numOfDecimalPoints <= 2) ? true : false;
        amtPostiveResult = (amount.compareTo(BigDecimal.valueOf(0))) != -1 ? true : false;
        amtMinResult = amount.compareTo(MIN_VAL_TRANSFER) >= 0 ? true : false;
        amtNotNullResult = entity.getAmount() != null ? true : false;

        /* 
        * 5.
        * filter.  remove private fields
        * map.     get field name and boolean value
        * filter.  filters those that are false
        * forEach. create FieldError and bind it to BindingResult
        */
        List<Field> fields = List.of(this.getClass().getDeclaredFields());
        fields.stream()
                .filter(field -> field.getModifiers() == 0)
                .map(field -> {
                    boolean fieldIsFalse = false;
                    try {
                        fieldIsFalse = field.getBoolean(this);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        logger.error(e.getMessage(), e.getCause());
                    }
                    return List.of(field.getName(), fieldIsFalse);
                })
                .filter(item -> (boolean) item.get(1) == false)
                .forEach(item -> {
                    FieldError fieldError = new FieldError(
                            TRANSFER_OBJECT_NAME,
                            criteriaFieldmap.get(item.get(0)),
                            errorMessagesMap.get(item.get(0)));
                    binding.addError(fieldError);
                });

        return binding;
    }
}
