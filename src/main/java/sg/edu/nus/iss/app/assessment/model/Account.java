package sg.edu.nus.iss.app.assessment.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String accountId;
    private String name;
    private BigDecimal balance;
}
