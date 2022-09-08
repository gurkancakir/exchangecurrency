package com.luxoft.exchangecurrency.dto;

import com.luxoft.exchangecurrency.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto implements Comparable<ExchangeRateDto>{

    private ExchangeRate buyRate;
    private ExchangeRate sellRate;
    private BigDecimal gain;

    @Override
    public int compareTo(ExchangeRateDto o) {
        return this.getGain().compareTo(o.getGain());
    }
}
