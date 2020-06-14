package com.pohorilyi.vmi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SaveDrinkRequest {
    private String name;
    private Integer amount;
    private Double volume;
}
