package lk.ijse.cooperative.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class TransTM {
    private String depositId;
    private String transId;
    private String type;
    private Double amount;
    private Date date;
    private String desc;
    private JFXButton button;
}
