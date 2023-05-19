package lk.ijse.cooperative.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Member {

    private String nic;
    private String name;
    private Integer age;
    private String position;
    private String department;
    private Double salary;
    private Date joinDate;
}
