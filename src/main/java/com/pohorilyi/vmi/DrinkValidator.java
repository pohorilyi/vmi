package com.pohorilyi.vmi;

import com.pohorilyi.vmi.exception.DrinkValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DrinkValidator {

    private static final Double MIN_VOLUME = 0.33;
    private static final Double MAX_VOLUME = 1.0;
    private static final Integer MIN_AMOUNT = 0;
    private static final Integer MAX_AMOUNT = 20;

    private final DrinkRepository drinkRepository;

    void validateBeforeCreate(SaveDrinkRequest request) {
        checkNulls(request);
        checkVolumeLimit(request.getVolume());
        checkAmountLimit(request.getAmount());
        checkIfExists(request.getName(), request.getVolume());
    }

    void validateBeforeUpdate(SaveDrinkRequest request) {
        checkNulls(request);
        checkVolumeLimit(request.getVolume());
        checkAmountLimit(request.getAmount());
    }

    private void checkNulls(SaveDrinkRequest request) {
        if (request.getName() == null) {
            throw new DrinkValidationException("Name should not be null");
        } else if (request.getVolume() == null) {
            throw new DrinkValidationException("Volume should not be null");
        } else if (request.getAmount() == null) {
            throw new DrinkValidationException("Amount should not be null");
        }
    }

    private void checkIfExists(String name, Double volume) {
        if (drinkRepository.findByNameAndVolume(name, volume).isPresent()) {
            throw new DrinkValidationException(
                    String.format("Drink record with name = %s and volume = %s already exists", name, volume)
            );
        }
    }

    private void checkAmountLimit(Integer amount) {
        if (amount > MAX_AMOUNT || amount < MIN_AMOUNT) {
            throw new DrinkValidationException(
                    String.format("Amount should be in range %s-%s", MIN_AMOUNT, MAX_AMOUNT)
            );
        }
    }

    private void checkVolumeLimit(Double volume) {
        if (volume > MAX_VOLUME || volume < MIN_VOLUME) {
            throw new DrinkValidationException(
                    String.format("Volume should be in range %s-%s", MIN_VOLUME, MAX_VOLUME)
            );
        }
    }
}
