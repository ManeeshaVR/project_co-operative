package lk.ijse.cooperative.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Deposit {
    private String depositId;
    private Double shares;
    private Double compDep;
    private Double specDep;
    private Double pensDep;
    private String desc;
    private Integer memberNo;
}
