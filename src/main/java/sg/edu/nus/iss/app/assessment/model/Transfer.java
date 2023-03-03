package sg.edu.nus.iss.app.assessment.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import sg.edu.nus.iss.app.assessment.Util;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Size(min = 10, max = 10)
    @NotBlank
    private String fromAccount;

    private Boolean fromAccountExist = false;

    private String fromName;

    @Size(min = 10, max = 10)
    @NotBlank
    private String toAccount;

    private Boolean toAccountExist = false;

    private String toName;

    private Boolean sameAccount = false;

    @Positive
    @Min(value = 10)
    private float amount;

    private String comments;

    private Boolean sufficientBalance = false;

}
