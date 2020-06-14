package com.pohorilyi.vmi;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.reflect.Executable;
import java.util.Optional;

import static com.pohorilyi.vmi.TestUtils.createTestDrink;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VmiApplication.class)
public class DrinkRepositoryTest {

    @Autowired
    private DrinkRepository drinkRepository;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    @Test
    void givenDrinkCreated_whenGetDrink_thenDrinkFound() {
        Drink saved = drinkRepository.save(createTestDrink());
        Drink found = drinkRepository.findById(saved.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(saved, found);
    }

    @Test
    void givenDrinkCreated_whenDeleteDrink_thenDrinkDeleted() {
        Drink saved = drinkRepository.save(createTestDrink());
        drinkRepository.delete(saved);
        assertTrue(drinkRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void givenDrinkCreated_whenUpdateDrink_thenDrinkUpdated() {
        Drink saved = drinkRepository.save(createTestDrink());
        saved.setAmount(42);
        Drink updated = drinkRepository.save(saved);

        assertEquals(42, updated.getAmount());
    }
}

