package assessment.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    private String fromAccount;
    private String fromName;
    private String toAccount;
    private String toName;
    private BigDecimal amount;
    private String comments;

}
