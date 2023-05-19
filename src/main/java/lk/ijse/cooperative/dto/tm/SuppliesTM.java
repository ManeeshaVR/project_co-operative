package lk.ijse.cooperative.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class SuppliesTM {
    private String supId;
    private String supName;
    private String itemId;
    private String itemName;
    private Integer qty;
    private Double uniPrice;
    private Double amount;
    private Date date;
    private String orderId;
}
