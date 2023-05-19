package lk.ijse.cooperative.dto;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PayLoan {
    private String dpLId;
    private Double loanAmount;
    private Double payAmount;
    private Double paidAmount;
    private int compInstallments;
    private String lId;
}
