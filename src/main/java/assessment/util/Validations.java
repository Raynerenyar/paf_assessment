package assessment.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import assessment.model.Transfer;
import assessment.repo.AccountsRepository;

import static assessment.Constants.*;

/* 
 * This class contains all the validation required by the controller.
 * 1.   Each non-private field is a criteria for validation.
 * 2.   Indexed field that is targetted at. 
 * 3.   Indexed Error messages for targetted field.
 * 4.   Each criteria is mapped to its respective error messages that would be shown
 * 5.   Populate the criteria. true when criteria has been met, vice versa.
 * 6.   Instantiate FieldError for each criteria that has failed
 *      and add to BindingResult object
 * 7.   Return BindingResult object to controller which will then be
 *      attached to the model in the controller.
 * 
 *      TO USE:
 *      add or remove validation fields in 1.
 *      Change the targetted field in 2. for each validation error based on indexed of the fields
 *      add or remove error checking in 5.
 */
@Component
public class Validations {
    private static final Logger logger = LoggerFactory.getLogger(Validations.class);

    // 1. this variable names should be same as the constant string
    boolean fromResult;
    boolean toResult;
    boolean diffAccountResult;
    boolean accountBalResult;
    boolean amtDecimalResult;
    boolean amtPostiveResult;
    boolean amtMinResult;
    boolean amtNotNullResult;

    private Map<String, String> criteriaFieldmap = new HashMap<>();
    private Map<String, String> errorMessagesMap = new HashMap<>();

    // 2. 
    private List<String> criteriaTargetValueForMap = List.of(
            "From Account",
            "To Account",
            "From Account" + System.lineSeparator() + "To Account",
            "Balance",
            "Amount",
            "Amount",
            "Amount",
            "Amount");
    // 3.
    private List<String> errorMessagesValueForMap = List.of(
            "Account does not exists.",
            "Account does not exists.",
            "From and to accounts must not be the same.",
            "Insufficient balance in account.",
            "Maximum 2 decimal places allowed.",
            "Must be positive value.",
            "Minimum value to transfer is " + MIN_VAL_TRANSFER_BIG_DECI + ".",
            "Please fill in amount to transfer.");

    // 4.
    // constructor
    public Validations() {
        List<Field> fields = List.of(this.getClass().getDeclaredFields());
        List<Field> filteredFields = fields.stream()
                .filter(field -> field.getModifiers() == 0)
                .toList();

        // end server if Validation class is not set up correctly
        if (filteredFields.size() != errorMessagesValueForMap.size()
                && filteredFields.size() != criteriaTargetValueForMap.size()) {
            logger.error("""
                    number of validation fields is incorrect.
                    Please check number of Validation properties,
                    size of errorMessagesValueForMap , and size of criteriaTargetValueForMap.
                         """);
            System.exit(1);
        }

        IntStream.range(0, filteredFields.size())
                .forEach(index -> {
                    criteriaFieldmap.put(filteredFields.get(index).getName(), criteriaTargetValueForMap.get(index));
                    errorMessagesMap.put(filteredFields.get(index).getName(), errorMessagesValueForMap.get(index));
                });
    }

    public BindingResult validateThis(Transfer entity, BindingResult binding, AccountsRepository acctRepo) {

        String fromAcc = entity.getFromAccount();
        String toAcc = entity.getToAccount();
        BigDecimal amount = (entity.getAmount()) == null ? BigDecimal.valueOf(0) : entity.getAmount();

        boolean isWholeNumber = amount.toString().indexOf(".") == -1; // true if cannot find "."
        int numOfDecimalPoints = (amount.toString().length() - (amount.toString().indexOf(".") + 1));

        // 5.
        fromResult = fromAcc.isEmpty() ? false : acctRepo.doesAccountExist(fromAcc);
        toResult = toAcc.isEmpty() ? false : acctRepo.doesAccountExist(toAcc);
        diffAccountResult = !fromAcc.equalsIgnoreCase(toAcc); // true when both are empty
        if (fromResult && amount.compareTo(BigDecimal.valueOf(0)) >= 0) {
            accountBalResult = (acctRepo.getBalance(fromAcc).getBalance().compareTo(amount)) >= 0 ? true : false;
        }
        amtDecimalResult = (isWholeNumber || numOfDecimalPoints <= 2) ? true : false;
        amtPostiveResult = (amount.compareTo(BigDecimal.valueOf(0))) != -1 ? true : false;
        amtMinResult = amount.compareTo(MIN_VAL_TRANSFER_BIG_DECI) >= 0 ? true : false;
        amtNotNullResult = entity.getAmount() != null ? true : false;

        /* 
        * 6.
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

        // 7.
        return binding;
    }
}
