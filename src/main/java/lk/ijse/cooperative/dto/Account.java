package lk.ijse.cooperative.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {

    private Integer memberNo;
    private Double shares;
    private Double compulsoryDeposits;
    private Double specialDeposits;
    private Double pensionDeposits;
    private String NIC;
    private String name;
    private String mail;
}
