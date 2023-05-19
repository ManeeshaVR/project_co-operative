package lk.ijse.cooperative.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class DepositsTM {
    private Integer memberNo;
    private String depositId;
    private Double shares;
    private Double comDeposits;
    private Double specDeposits;
    private Double penDeposits;
    private String desc;
}
