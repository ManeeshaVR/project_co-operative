package lk.ijse.cooperative.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class PaySerTM {
    private String serId;
    private String payId;
    private Double amount;
    private Double payAmount;
    private Date date;
}
