package com.enigmacamp.model.entity;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.model.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Tables.MOUNTAINS)
public class Mountain extends DateUtils {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MountainStatus status;

    @Column(name = "price", columnDefinition = "BIGINT CHECK(price > 0)", nullable = false)
    private BigDecimal price;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "toilet", nullable = false)
    private Boolean toilet;

    @Column(name = "water", nullable = false)
    private String water;

    @Column(name = "quota_limit", nullable = false)
    private Integer quotaLimit;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = true, unique = true)
    private Image image;

    @OneToMany
    private List<Image> baseCampImages;
}
