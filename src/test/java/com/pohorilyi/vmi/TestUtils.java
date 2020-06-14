package com.pohorilyi.vmi;

import java.util.UUID;

class TestUtils {
    static Drink createTestDrink() {
        return Drink.builder()
                .name(randomName())
                .amount(10)
                .volume(1.0)
                .build();
    }

    private static String randomName() {
        return "drink_name" + UUID.randomUUID();
    }
}
