package lk.ijse.cooperative.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Service {
    private String serId;
    private String type;
    private Double amount;
    private Double interest;
    private Date date;
    private Integer memberNo;
    private Boolean completed;
}
