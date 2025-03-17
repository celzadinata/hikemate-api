package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.RANGERS)
public class Ranger extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "mountain_id", unique = true, nullable = true)
    private Mountain mountain;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false, length = 12)
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Column(name = "assigned_at", nullable = false)
    private Timestamp assignedAt;

    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true, nullable = true)
    private UserAccount userAccount;
}
