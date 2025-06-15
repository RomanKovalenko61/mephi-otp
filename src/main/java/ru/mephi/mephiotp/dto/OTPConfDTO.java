package ru.mephi.mephiotp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPConfDTO {
    private int length;
    private int aliveTime;
}
