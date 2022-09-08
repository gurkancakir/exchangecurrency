package com.luxoft.exchangecurrency.service;

import com.luxoft.exchangecurrency.domain.ExchangeRate;
import com.luxoft.exchangecurrency.dto.ExchangeRateDto;

import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRate> listExchangeRate();

    List<ExchangeRateDto> listPossible();

    ExchangeRateDto findFirstExchangeRate();
}
