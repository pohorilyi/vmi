package com.pohorilyi.vmi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestUtils {

    static Drink buildTestDrink() {
        return Drink.builder()
                .name(randomName())
                .amount(10)
                .volume(1.0)
                .build();
    }

    static SaveDrinkRequest buildTestSaveDrinkRequest() {
        return SaveDrinkRequest.builder()
                .name(randomName())
                .amount(10)
                .volume(1.0)
                .build();
    }

    static Drink createTestDrink(MockMvc mockMvc, SaveDrinkRequest saveDrinkRequest) throws Exception {
        String postResult = mockMvc
                .perform(post("/drinks")
                        .content(asJsonString(saveDrinkRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(saveDrinkRequest.getName()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return getResponseFromJsonString(postResult, Drink.class);
    }

    static String asJsonString(Object obj) {
        try {
            return new ObjectMapper()
                    .writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T getResponseFromJsonString(String string, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(string, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomName() {
        return "drink_name_" + UUID.randomUUID();
    }
}
