package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.HIKER)
public class Hiker extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "ktp", nullable = true, unique = true)
    private Image ktp;

    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true, nullable = true)
    private UserAccount userAccount;
}
