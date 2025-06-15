package ru.mephi.mephiotp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPConfiguration {
    @Id
    private Long id;

    @Column
    private int length;

    @Column
    private int aliveTime;
}
