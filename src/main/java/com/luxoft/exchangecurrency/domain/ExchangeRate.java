package com.luxoft.exchangecurrency.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    private Integer day;
    private BigDecimal buyExchangeRate;
    private BigDecimal sellExchangeRate;
}
