package lk.ijse.cooperative.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MemberTM {
    private String nic;
    private String name;
    private Integer age;
    private String position;
    private String department;
    private Double salary;
}
