package com.luxoft.exchangecurrency.controller;

import com.luxoft.exchangecurrency.domain.ExchangeRate;
import com.luxoft.exchangecurrency.dto.ExchangeRateDto;
import com.luxoft.exchangecurrency.service.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/exhange-rate")
@AllArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("list")
    public List<ExchangeRate> list() {
        return exchangeRateService.listExchangeRate();
    }

    @GetMapping("possible-list")
    public List<ExchangeRateDto> listPossible() {
        return exchangeRateService.listPossible();
    }

    @GetMapping("find-first")
    public ExchangeRateDto findFirstExchangeRate() {
        return exchangeRateService.findFirstExchangeRate();
    }
}
