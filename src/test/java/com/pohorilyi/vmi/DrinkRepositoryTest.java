package com.pohorilyi.vmi;

import com.pohorilyi.vmi.exception.EntityNotFoundException;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.pohorilyi.vmi.TestUtils.buildTestDrink;
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
        Drink saved = drinkRepository.save(buildTestDrink());
        Drink found = drinkRepository.findById(saved.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(saved, found);
    }

    @Test
    void givenDrinkCreated_whenDeleteDrink_thenDrinkDeleted() {
        Drink saved = drinkRepository.save(buildTestDrink());
        drinkRepository.delete(saved);
        assertTrue(drinkRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void givenDrinkCreated_whenUpdateDrink_thenDrinkUpdated() {
        Drink saved = drinkRepository.save(buildTestDrink());
        saved.setAmount(42);
        Drink updated = drinkRepository.save(saved);

        assertEquals(42, updated.getAmount());
    }

    @Test
    void whenCreateDrinkWithNulls_thenExceptionThrown() {
        Drink saved = drinkRepository.save(buildTestDrink());
        saved.setAmount(null);

        assertThrows(DataIntegrityViolationException.class, () -> drinkRepository.save(saved));
    }
}

