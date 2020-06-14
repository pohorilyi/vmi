package com.pohorilyi.vmi;

import com.pohorilyi.vmi.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryService {

    private final DrinkRepository drinkRepository;
    private final DrinkValidator drinkValidator;

    List<Drink> getDrinks() {
        return drinkRepository.findAll();
    }

    Drink getDrinkById(Long id) {
        return drinkRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    Drink createDrink(SaveDrinkRequest drinkRequest) {
        drinkValidator.validateBeforeCreate(drinkRequest);
        return drinkRepository.save(createDrinkEntity(drinkRequest));
    }

    Drink updateDrink(Long id, SaveDrinkRequest saveDrinkRequest) {
        Drink found = getDrinkById(id);
        drinkValidator.validateBeforeUpdate(saveDrinkRequest);
        found.setName(saveDrinkRequest.getName());
        found.setAmount(saveDrinkRequest.getAmount());
        found.setVolume(saveDrinkRequest.getVolume());

        return drinkRepository.save(found);
    }

    void deleteDrink(Long id) {
        Drink toBeDeleted = getDrinkById(id);
        drinkRepository.delete(toBeDeleted);
    }

    private Drink createDrinkEntity(SaveDrinkRequest drinkRequest) {
        return Drink.builder()
                .name(drinkRequest.getName())
                .amount(drinkRequest.getAmount())
                .volume(drinkRequest.getVolume())
                .build();
    }

}
