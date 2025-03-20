package com.enigmacamp.seed;

import com.enigmacamp.model.entity.*;
import com.enigmacamp.constant.enums.*;
import com.enigmacamp.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
@ConditionalOnProperty(name = "spring.jpa.hibernate.ddl-auto", havingValue = "create")
public class DatabaseSeeder {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HikerRepository hikerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private RangerRepository rangerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {

        Role rangerRole = roleRepository.save(Role.builder()
                .role(UserRole.RANGER)
                .build());

        Role hikerRole = roleRepository.save(Role.builder()
                .role(UserRole.HIKER)
                .build());

        UserAccount rangerUser1 = userAccountRepository.save(UserAccount.builder()
                .email("ranger1@enigmacamp.com")
                .password(passwordEncoder.encode("ranger123"))
                .role(List.of(rangerRole))
                .build());

        UserAccount rangerUser2 = userAccountRepository.save(UserAccount.builder()
                .email("ranger2@enigmacamp.com")
                .password(passwordEncoder.encode("ranger123"))
                .role(List.of(rangerRole))
                .build());

        UserAccount hikerUser1 = userAccountRepository.save(UserAccount.builder()
                .email("johndoe@example.com")
                .password(passwordEncoder.encode("hiker123"))
                .role(List.of(hikerRole))
                .build());

        UserAccount hikerUser2 = userAccountRepository.save(UserAccount.builder()
                .email("janesmith@example.com")
                .password(passwordEncoder.encode("hiker123"))
                .role(List.of(hikerRole))
                .build());

        Hiker hiker1 = hikerRepository.save(Hiker.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .phoneNumber("08123456789")
                .userAccount(hikerUser1)
                .build());

        Hiker hiker2 = hikerRepository.save(Hiker.builder()
                .name("Jane Smith")
                .email("janesmith@example.com")
                .phoneNumber("08123456788")
                .userAccount(hikerUser2)
                .build());

        Image mountainImage1 = imageRepository.save(Image.builder()
                .name("mount-everest.jpg")
                .path("https://upload.wikimedia.org/wikipedia/commons/b/b6/Mount_Everest_as_seen_from_Drukair2_PLW_edit_Cropped.jpg")
                .size(1024L)
                .contentType("image/jpeg")
                .build());

        Image mountainImage2 = imageRepository.save(Image.builder()
                .name("mount-fuji.jpg")
                .path("https://lh5.googleusercontent.com/p/AF1QipOt2N13WEXL86UsaX-WRzcjH_GT8AV5riImO_lG=w540-h312-n-k-no")
                .size(1024L)
                .contentType("image/jpeg")
                .build());

        Image baseCampImage1 = imageRepository.save(Image.builder()
                .name("mount-fuji.jpg")
                .path("http://image.url/basecamp1.jpg")
                .size(2048L)
                .contentType("image/jpeg")
                .build());

        Image baseCampImage2 = imageRepository.save(Image.builder()
                .name("basecamp2.jpg")
                .path("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnEd_G_HGAFkRmJ1fw41aCj3gZhil6t6s-1g&s")
                .size(2048L)
                .contentType("image/jpeg")
                .build());

        Image baseCampImage3 = imageRepository.save(Image.builder()
                .name("basecamp3.jpg")
                .path("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS66aA5X_ahR5uv5htswuVv7xQrsZepbq30BA&s")
                .size(2048L)
                .contentType("image/jpeg")
                .build());

        Image baseCampImage4 = imageRepository.save(Image.builder()
                .name("basecamp4.jpg")
                .path("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5MY_C_l9NKsuH1T-c7qVlyaEu1RBpnjRHkg&s")
                .size(2048L)
                .contentType("image/jpeg")
                .build());

        Mountain mountain1 = mountainRepository.save(Mountain.builder()
                .name("Mount Everest")
                .location("Nepal")
                .status(MountainStatus.SAFE)
                .price(new BigDecimal(1000000))
                .image(mountainImage1)
                .baseCampImages(List.of(baseCampImage1, baseCampImage2))
                .build());

        Mountain mountain2 = mountainRepository.save(Mountain.builder()
                .name("Mount Fuji")
                .location("Japan")
                .status(MountainStatus.OPEN)
                .price(new BigDecimal(800000))
                .image(mountainImage2)
                .baseCampImages(List.of(baseCampImage3, baseCampImage4))
                .build());

        Ranger ranger1 = rangerRepository.save(Ranger.builder()
                .name("Ranger Joe")
                .userAccount(rangerUser1)
                .mountain(mountain1)
                .phoneNumber("08953234412")
                .assignedAt(new Timestamp(new Date().getTime()))
                .build());

        Ranger ranger2 = rangerRepository.save(Ranger.builder()
                .name("Ranger Tom")
                .userAccount(rangerUser2)
                .mountain(mountain2)
                .phoneNumber("08126473823")
                .assignedAt(new Timestamp(new Date().getTime()))
                .build());

    }
}
