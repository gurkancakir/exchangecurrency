package com.luxoft.exchangecurrency.service.impl;

import com.luxoft.exchangecurrency.domain.ExchangeRate;
import com.luxoft.exchangecurrency.dto.ExchangeRateDto;
import com.luxoft.exchangecurrency.service.ExchangeRateService;
import com.luxoft.exchangecurrency.service.ExchangeRateStorage;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void postConstruct() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:example-input.txt");
        String data = IOUtils.toString(resource.getInputStream());
        readFile(data);
    }

    private void readFile(String data) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        List<ExchangeRate> exchangeRateList = data.lines()
                .map(line -> mapToExchangeRate(line, atomicInteger.getAndIncrement()))
                        .collect(Collectors.toList());
        ExchangeRateStorage.getInstance().setExchangeRateList(exchangeRateList);
    }

    private ExchangeRate mapToExchangeRate(String line, int day) {
        String[] rates = line.split(" ");
        return new ExchangeRate(day, new BigDecimal(rates[0]), new BigDecimal(rates[1]));
    }


    @Override
    public List<ExchangeRate> listExchangeRate() {
        return ExchangeRateStorage.getInstance().getExchangeRateList();
    }

    @Override
    public List<ExchangeRateDto> listPossible() {
        List<ExchangeRateDto> exchangeRateDtoList = new ArrayList<>();
        List<ExchangeRate> exchangeRateList = ExchangeRateStorage.getInstance().getExchangeRateList();
        for (int i = 0; i <exchangeRateList.size(); i++) {
            for (int j = i + 1; j < exchangeRateList.size(); j++) {
                if (this.isSellExchangeRateLower(exchangeRateList, i, j)) {
                    BigDecimal gain = this.calculateGain(exchangeRateList.get(i).getSellExchangeRate(), exchangeRateList.get(j).getBuyExchangeRate());
                    exchangeRateDtoList.add(new ExchangeRateDto(exchangeRateList.get(i), exchangeRateList.get(j), gain));
                }
            }
        }
        Collections.sort(exchangeRateDtoList);
        return exchangeRateDtoList;
    }

    private boolean isSellExchangeRateLower(List<ExchangeRate> exchangeRateList, int i, int j) {
        return exchangeRateList.get(i).getSellExchangeRate().compareTo(exchangeRateList.get(j).getBuyExchangeRate()) < 0;
    }

    private BigDecimal calculateGain(BigDecimal sellExchangeRate, BigDecimal buyExchangeRate) {
        BigDecimal localAmount = new BigDecimal("1000");
        return localAmount
                .divide(sellExchangeRate, 14, RoundingMode.HALF_DOWN)
                .multiply(buyExchangeRate)
                .subtract(localAmount);
    }

    @Override
    public ExchangeRateDto findFirstExchangeRate() {
        List<ExchangeRateDto> exchangeRateDtoList = this.listPossible();
        return exchangeRateDtoList.get(exchangeRateDtoList.size() - 1);
    }
}
