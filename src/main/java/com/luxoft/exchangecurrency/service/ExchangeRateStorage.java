package com.luxoft.exchangecurrency.service;

import com.luxoft.exchangecurrency.domain.ExchangeRate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public final class ExchangeRateStorage {

    private List<ExchangeRate> exchangeRateList;

    private static ExchangeRateStorage exchangeRateStorage;

    private ExchangeRateStorage() {}

    public static ExchangeRateStorage getInstance() {
        if (exchangeRateStorage == null)
            exchangeRateStorage = new ExchangeRateStorage();
        return exchangeRateStorage;
    }
}
