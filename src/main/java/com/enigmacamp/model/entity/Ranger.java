package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.RANGERS)
public class Ranger extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false, length = 12)
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @Column(name = "assigned_at", nullable = false)
    private Timestamp assignedAt;
}
