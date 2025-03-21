package com.enigmacamp.seed;

import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.entity.*;
import com.enigmacamp.constant.enums.*;
import com.enigmacamp.repository.*;
import com.enigmacamp.service.TransactionService;
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

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    public void seed() {

        Role rangerRole = roleRepository.save(Role.builder()
                .role(UserRole.RANGER)
                .build());

        Role hikerRole = roleRepository.save(Role.builder()
                .role(UserRole.HIKER)
                .build());

        Route jalurUtama = routeRepository.save(Route.builder()
                .route("Jalur Utama")
                .build());

        Route jalurAlternatif = routeRepository.save(Route.builder()
                .route("Jalur Alternatif")
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

        UserAccount hikerUser3 = userAccountRepository.save(UserAccount.builder()
                .email("budi@example.com")
                .password(passwordEncoder.encode("hiker123"))
                .role(List.of(hikerRole))
                .build());

        UserAccount hikerUser4 = userAccountRepository.save(UserAccount.builder()
                .email("frank@example.com")
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

        Hiker hiker3 = hikerRepository.save(Hiker.builder()
                .name("Budi Suwandono")
                .email("budi@example.com")
                .phoneNumber("08789203123")
                .userAccount(hikerUser3)
                .build());

        Hiker hiker4 = hikerRepository.save(Hiker.builder()
                .name("Frankie Fallie")
                .email("frank@example.com")
                .phoneNumber("08589038232")
                .userAccount(hikerUser4)
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
                .description("Mount Everest attracts many climbers, including highly experienced mountaineers. There are two main climbing routes, one approaching the summit from the southeast in Nepal (known as the standard route) and the other from the north in Tibet. While not posing substantial technical climbing challenges on the standard route, Everest presents dangers such as altitude sickness, weather, and wind, as well as hazards from avalanches and the Khumbu Icefall. As of May 2024, 340 people have died on Everest. Over 200 bodies remain on the mountain, having been abandoned because of the dangerous conditions.[7][8]")
                .water("Hanya ada di beberapa tempat")
                .quotaLimit(3)
                .toilet(true)
                .isOpen(true)
                .baseCampImages(List.of(baseCampImage1, baseCampImage2))
                .build());

        Mountain mountain2 = mountainRepository.save(Mountain.builder()
                .name("Mount Fuji")
                .location("Japan")
                .status(MountainStatus.OPEN)
                .price(new BigDecimal(800000))
                .image(mountainImage2)
                .description("Gunung Fuji adalah salah satu dari \"Tiga Gunung Suci\" (三霊山, Sanreizan) bersama dengan Gunung Tate dan Gunung Haku. Gunung ini merupakan salah satu Situs Bersejarah Jepang.[7] Gunung itu ditambahkan ke Daftar Warisan Dunia sebagai Situs Budaya pada 22 Juni 2013.[7] Menurut UNESCO, Gunung Fuji telah \"menginspirasi seniman dan penyair dan menjadi objek ziarah selama berabad-abad\". UNESCO mengakui 25 situs budaya yang menarik di dalam wilayah Gunung Fuji. 25 lokasi ini termasuk gunung dan kuil Shinto, Fujisan Hongū Sengen Taisha, serta Kuil Kepala Buddha Taisekiji yang didirikan pada 1290, yang kemudian digambarkan oleh seniman ukiyo-e Jepang Katsushika Hokusai.")
                .water("Tersedia di semua pos")
                .quotaLimit(341)
                .toilet(true)
                .isOpen(true)
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

        TransactionRequest transactionRequest1 = TransactionRequest.builder()
                .hikerId(hiker1.getId())
                .rangerId(ranger1.getId())
                .mountainId(mountain1.getId())
                .startDate("2025-04-19 14:26:02.028")
                .endDate("2025-04-21 14:26:02.028")
                .routeId(jalurUtama.getId())
                .build();

        TransactionRequest transactionRequest2 = TransactionRequest.builder()
                .hikerId(hiker2.getId())
                .rangerId(ranger1.getId())
                .mountainId(mountain1.getId())
                .startDate("2025-08-07 10:26:02.028")
                .endDate("2025-08-10 08:26:02.028")
                .routeId(jalurUtama.getId())
                .build();

        TransactionRequest transactionRequest3 = TransactionRequest.builder()
                .hikerId(hiker4.getId())
                .rangerId(ranger2.getId())
                .mountainId(mountain2.getId())
                .startDate("2025-11-27 20:26:02.028")
                .endDate("2025-11-29 20:26:02.028")
                .routeId(jalurUtama.getId())
                .build();

        TransactionRequest transactionRequest4 = TransactionRequest.builder()
                .hikerId(hiker3.getId())
                .rangerId(ranger1.getId())
                .mountainId(mountain1.getId())
                .startDate("2025-04-17 14:26:02.028")
                .endDate("2025-04-19 14:26:02.028")
                .routeId(jalurUtama.getId())
                .build();

        TransactionRequest transactionRequest5 = TransactionRequest.builder()
                .hikerId(hiker1.getId())
                .rangerId(ranger2.getId())
                .mountainId(mountain2.getId())
                .startDate("2025-09-13 19:26:02.028")
                .endDate("2025-09-16 17:26:02.028")
                .routeId(jalurUtama.getId())
                .build();


        transactionService.create(transactionRequest1).setIsUp(true);
        transactionService.create(transactionRequest2);
        transactionService.create(transactionRequest3);
        transactionService.create(transactionRequest4);
        transactionService.create(transactionRequest5);


    }
}
