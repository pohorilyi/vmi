package com.pohorilyi.vmi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drinks")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Drink>> getDrinks() {
        return new ResponseEntity<>(inventoryService.getDrinks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drink> getDrinkById(@PathVariable Long id) {
        return new ResponseEntity<>(inventoryService.getDrinkById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Drink> createDrink(@RequestBody SaveDrinkRequest drinkRequest) {
        return new ResponseEntity<>(inventoryService.createDrink(drinkRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drink> updateDrink(
            @PathVariable Long id,
            @RequestBody SaveDrinkRequest saveDrinkRequest) {
        Drink updatedDrink = inventoryService.updateDrink(id, saveDrinkRequest);
        return new ResponseEntity<>(updatedDrink, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Drink> deleteDrink(@PathVariable Long id) {
        inventoryService.deleteDrink(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
