package com.pohorilyi.vmi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Drink {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, scale = 100)
    private Integer amount;

    @Column(nullable = false, precision = 2)
    private Double volume;
}
