package lk.ijse.cooperative.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountTM {
    private String memberNo;
    private String name;
    private String nic;
    private Double shares;
    private Double comDeposits;
    private Double specDeposits;
    private Double penDeposits;
}
