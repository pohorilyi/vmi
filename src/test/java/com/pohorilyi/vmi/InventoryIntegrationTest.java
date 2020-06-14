package com.pohorilyi.vmi;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.pohorilyi.vmi.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    @Test
    public void whenCreateDrink_drinkCreated() throws Exception {

        Drink createdDrink = createTestDrink(mockMvc, buildTestSaveDrinkRequest());

        mockMvc.perform(get("/inventory/" + createdDrink.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDrink.getId()))
                .andExpect(jsonPath("$.name").value(createdDrink.getName()));
    }

    @Test
    public void whenDeleteDrink_drinkDeleted() throws Exception {

        Drink createdDrink = createTestDrink(mockMvc, buildTestSaveDrinkRequest());

        mockMvc.perform(delete("/inventory/" + createdDrink.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdateDrink_DrinkUpdated() throws Exception {
        SaveDrinkRequest saveDrinkRequest = buildTestSaveDrinkRequest();
        Drink drink = createTestDrink(mockMvc, saveDrinkRequest);

        saveDrinkRequest.setName("new_name");

        mockMvc.perform(put("/inventory/" + drink.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(saveDrinkRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/inventory/" + drink.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new_name"));
    }
}