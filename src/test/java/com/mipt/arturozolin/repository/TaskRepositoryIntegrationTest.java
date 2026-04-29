package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15-alpine"));

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindOnlyTasksDueWithinGivenRange() {
        Task soon = new Task(null, "Due soon", "in 2 days", false);
        soon.setPriority(Priority.HIGH);
        soon.setDueDate(LocalDate.now().plusDays(2));

        Task later = new Task(null, "Due later", "in 30 days", false);
        later.setPriority(Priority.LOW);
        later.setDueDate(LocalDate.now().plusDays(30));

        Task noDate = new Task(null, "No due date", "ever", false);
        noDate.setPriority(Priority.MEDIUM);

        entityManager.persist(soon);
        entityManager.persist(later);
        entityManager.persist(noDate);
        entityManager.flush();
        entityManager.clear();

        List<Task> dueWithinWeek = taskRepository.findTasksDueInDays(LocalDate.now().plusDays(7));

        assertThat(dueWithinWeek)
                .hasSize(1)
                .extracting(Task::getTitle)
                .containsExactly("Due soon");
    }
}
