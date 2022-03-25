package com.example.simbirsoft.integration;

import com.example.simbirsoft.entity.App;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.repository.AppRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppRepositoryInTest {
    private final AppRepository appRepository;

    @Autowired
    public AppRepositoryInTest(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testpostres")
                    .withPassword("2424285")
                    .withUsername("postgres")
                    .withInitScript("database-integration/init.sql");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQL95Dialect");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQL95Dialect");
    }

    @Test
    @Order(1)
    public void container_Is_Up_And_Running(){
        assertTrue(container.isRunning());
    }

    @Test
    @Order(2)
    public void findAllByUserEmail_By_Pages_Test(){
        var actualFullPage = appRepository
                .findAllByUserEmail("lyah.artem10@mail.ru", PageRequest.of(0, 4))
                .toList();
        assertEquals(4, actualFullPage.size());
        assertEquals(4, actualFullPage.get(0).getId());
        var actualFirstPagePart = appRepository
                .findAllByUserEmail("lyah.artem10@mail.ru", PageRequest.of(0, 3))
                .toList();
        assertEquals(3, actualFirstPagePart.size());
        assertEquals(4, actualFirstPagePart.get(0).getId());
        var actualSecondPagePart = appRepository
                .findAllByUserEmail("lyah.artem10@mail.ru", PageRequest.of(1, 3))
                .toList();
        assertEquals(1, actualSecondPagePart.size());
        assertEquals(1, actualSecondPagePart.get(0).getId());
    }

    @Test
    @Order(3)
    public void findAllByUserEmail_By_Pages_When_UserApps_Is_Empty_Test(){
        var actualFullPage = appRepository
                .findAllByUserEmail("lyah.artem10@gmail.com", PageRequest.of(0, 4))
                .toList();
        assertTrue(actualFullPage.isEmpty());
    }

    @Test
    @Order(4)
    public void getUserAppsAmount_When_UserApps_Is_Not_Empty_Test(){
        var actual = appRepository.getUserAppsAmount("lyah.artem10@mail.ru");
        assertEquals(4, actual);
    }

    @Test
    @Order(5)
    public void getUserAppsAmount_When_UserApps_Is_Empty_Test(){
        var actual = appRepository.getUserAppsAmount("lyah.artem10@gmail.com");
        assertEquals(0, actual);
    }

    @Test
    @Order(6)
    public void findUserAppById_When_App_Exists_Test(){
        var actual = appRepository.findUserAppById(1, "lyah.artem10@mail.ru");
        assertTrue(actual.isPresent());
        assertEquals(1, actual.get().getId());
    }

    @Test
    @Order(7)
    public void return_Empty_Optional_If_FindUserAppById_WhenApp_Exists_Test(){
        var actual = appRepository.findUserAppById(10, "lyah.artem10@mail.ru");
        assertTrue(actual.isEmpty());
    }

    @Test
    @Order(8)
    public void return_Empty_Optional_If_FindUserAppById_When_User_Does_Not_Have_App_Test(){
        var actual = appRepository.findUserAppById(1, "lyah.artem10@gmail.com");
        assertTrue(actual.isEmpty());
    }

    @Test
    @Order(9)
    public void do_Not_DeleteByIdAndUserEmail_If_User_Does_Not_Have_App_Test(){
        appRepository.deleteByIdAndUserEmail(1, "lyah.artem10@gmail.com");
        var actual = appRepository.findUserAppById(1, "lyah.artem10@mail.ru");
        assertTrue(actual.isPresent());
    }

    @Test
    @Order(10)
    public void deleteByIdAndUserEmail_If_User_Has_App_Test(){
        appRepository.deleteByIdAndUserEmail(1, "lyah.artem10@mail.ru");
        var actual = appRepository.findUserAppById(1, "lyah.artem10@mail.ru");
        assertTrue(actual.isEmpty());
    }

    @Test
    @Order(11)
    public void save_New_App_Test(){
        var actualBefore = appRepository.findUserAppById(5, "lyah.artem10@mail.ru");
        assertTrue(actualBefore.isEmpty());
        var app = App.builder()
                .name("ToDoLIst app")
                .creationTime(new Timestamp(new Date().getTime()))
                .user(User.builder().id(1).build())
                .build();
        appRepository.save(app);
        var actualAfter = appRepository.findUserAppById(5, "lyah.artem10@mail.ru");
        assertTrue(actualAfter.isPresent());
    }

    @Test
    @Order(12)
    public void update_App_Test(){
        var actualBefore = appRepository
                .findUserAppById(2, "lyah.artem10@mail.ru");
        assertTrue(actualBefore.isPresent());
        assertEquals("Todo list", actualBefore.get().getName());
        var app = App.builder()
                .id(2)
                .name("Devanmejia Todo list")
                .creationTime(new Timestamp(new Date().getTime()))
                .user(User.builder().id(1).build())
                .build();
        appRepository.save(app);
        var actualAfter = appRepository.findUserAppById(2, "lyah.artem10@mail.ru");
        assertTrue(actualAfter.isPresent());
        assertEquals("Devanmejia Todo list", actualAfter.get().getName());
    }
}
