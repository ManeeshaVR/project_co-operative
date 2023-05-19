package lk.ijse.cooperative.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Distribute {
    private String disId;
    private String itemId;
    private String itemName;
    private Date date;
    private String dep;
    private Integer disQty;
    private String desc;
}
