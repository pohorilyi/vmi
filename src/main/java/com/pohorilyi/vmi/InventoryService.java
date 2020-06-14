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

    Drink getDrinkByName(String name) {
        return drinkRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    Drink createDrink(SaveDrinkRequest drinkRequest) {
        drinkValidator.validateBeforeCreate(drinkRequest);
        return drinkRepository.save(createDrinkEntity(drinkRequest));
    }

    Drink updateDrink(Long id, SaveDrinkRequest saveDrinkRequest) {
        drinkValidator.validateBeforeUpdate(saveDrinkRequest);
        Drink found = getDrinkById(id);
        found.setName(saveDrinkRequest.getName());
        found.setAmount(saveDrinkRequest.getAmount());
        found.setVolume(saveDrinkRequest.getVolume());

        return drinkRepository.save(found);
    }

    void deleteDrink(Long id) {
        drinkRepository.deleteById(id);
    }

    private Drink createDrinkEntity(SaveDrinkRequest drinkRequest) {
        return Drink.builder()
                .name(drinkRequest.getName())
                .amount(drinkRequest.getAmount())
                .volume(drinkRequest.getVolume())
                .build();
    }

}
