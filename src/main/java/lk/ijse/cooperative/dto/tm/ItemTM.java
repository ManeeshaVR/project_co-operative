package lk.ijse.cooperative.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ItemTM {
    private String itemId;
    private String itemName;
    private String type;
    private Double uniPrice;
    private String desc;
    private Integer qty;
}
