package lk.ijse.cooperative.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class PayLoanTM {
    private String loanId;
    private Double amount;
    private String payLoanId;
    private Double payAmount;
    private Double paidAmount;
    private Integer ins;
}
