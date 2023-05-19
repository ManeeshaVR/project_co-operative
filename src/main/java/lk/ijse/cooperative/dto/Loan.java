package lk.ijse.cooperative.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Loan {
    String loanId;
    Double interest;
    Double loanAmount;
    Integer installments;
    Double firInsAmount;
    Double insAmount;
    Date date;
    Integer memberNo;
    Boolean completed;
}
