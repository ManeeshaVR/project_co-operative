package lk.ijse.cooperative.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class All {
    private Integer memberNo;
    private String name;
    private String nic;
    private String depId;
    private Double shares;
    private Double comDep;
    private Double specDep;
    private Double penDep;
    private String loanId;
    private Double loanAmount;
    private Integer installment;
    private String serId;
    private Double serAmount;
}
