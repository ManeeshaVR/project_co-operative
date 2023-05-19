package lk.ijse.cooperative.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class PayService {
    private String payId;
    private Double amount;
    private Double payAmount;
    private String serviceId;
    private Date date;
}
