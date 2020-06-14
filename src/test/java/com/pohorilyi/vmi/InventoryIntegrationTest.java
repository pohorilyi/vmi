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
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        mockMvc.perform(get("/drinks/" + createdDrink.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDrink.getId()))
                .andExpect(jsonPath("$.name").value(createdDrink.getName()));
    }

    @Test
    public void whenDeleteDrink_drinkDeleted() throws Exception {

        Drink createdDrink = createTestDrink(mockMvc, buildTestSaveDrinkRequest());

        mockMvc.perform(delete("/drinks/" + createdDrink.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdateDrink_DrinkUpdated() throws Exception {
        SaveDrinkRequest saveDrinkRequest = buildTestSaveDrinkRequest();
        Drink drink = createTestDrink(mockMvc, saveDrinkRequest);

        saveDrinkRequest.setName("new_name");

        mockMvc.
                perform(put("/drinks/" + drink.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveDrinkRequest)))
                .andExpect(status().isOk());

        mockMvc.
                perform(get("/drinks/" + drink.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new_name"));
    }

    @Test
    public void whenGetNotExistedDrink_DrinkNotFound() throws Exception {

        mockMvc.perform(get("/drinks/99999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteNotExistedDrink_DrinkNotFound() throws Exception {

        mockMvc.perform(delete("/drinks/99999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateNotExistedDrink_DrinkNotFound() throws Exception {

        mockMvc
                .perform(put("/drinks/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(buildTestSaveDrinkRequest())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateDrinkWithNull_ExceptionThrown() throws Exception {

        SaveDrinkRequest saveDrinkRequest = buildTestSaveDrinkRequest();
        saveDrinkRequest.setName(null);

        String message = mockMvc
                .perform(post("/drinks")
                        .content(asJsonString(saveDrinkRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(message.contains("Name should not be null"));
    }

    @Test
    public void whenCreateDrinkWithVolumeAboveLimit_ExceptionThrown() throws Exception {

        SaveDrinkRequest saveDrinkRequest = buildTestSaveDrinkRequest();
        saveDrinkRequest.setAmount(99999);

        String message = mockMvc
                .perform(post("/drinks")
                        .content(asJsonString(saveDrinkRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(message.contains("Amount should be in range"));
    }

    @Test
    public void whenCreateDrinkWithAmountAboveLimit_ExceptionThrown() throws Exception {

        SaveDrinkRequest saveDrinkRequest = buildTestSaveDrinkRequest();
        saveDrinkRequest.setVolume(99.0);

        String message = mockMvc
                .perform(post("/drinks")
                        .content(asJsonString(saveDrinkRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(message.contains("Volume should be in range"));
    }
}