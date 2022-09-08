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
        List<String> lines = data.lines().toList();
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] rates = lines.get(i).split(" ");
            exchangeRateList.add(new ExchangeRate(i + 1, new BigDecimal(rates[0]), new BigDecimal(rates[1])));
        }
        ExchangeRateStorage.getInstance().setExchangeRateList(exchangeRateList);
    }


    @Override
    public List<ExchangeRate> listExchangeRate() {
        return ExchangeRateStorage.getInstance().getExchangeRateList();
    }

    @Override
    public List<ExchangeRateDto> listPossible() {
        BigDecimal localAmount = new BigDecimal("1000");
        List<ExchangeRateDto> exchangeRateDtoList = new ArrayList<>();
        List<ExchangeRate> exchangeRateList = ExchangeRateStorage.getInstance().getExchangeRateList();
        for (int i = 0; i <exchangeRateList.size(); i++) {
            for (int j = i + 1; j < exchangeRateList.size(); j++) {
                if (exchangeRateList.get(i).getSellExchangeRate().compareTo(exchangeRateList.get(j).getBuyExchangeRate()) < 0) {
                    BigDecimal gain = localAmount
                            .divide(exchangeRateList.get(i).getSellExchangeRate(), 14, RoundingMode.HALF_DOWN)
                            .multiply(exchangeRateList.get(j).getBuyExchangeRate())
                            .subtract(localAmount);
                    exchangeRateDtoList.add(new ExchangeRateDto(exchangeRateList.get(i), exchangeRateList.get(j), gain));
                }
            }
        }
        Collections.sort(exchangeRateDtoList);
        return exchangeRateDtoList;
    }

    @Override
    public ExchangeRateDto findFirstExchangeRate() {
        List<ExchangeRateDto> exchangeRateDtoList = this.listPossible();
        return exchangeRateDtoList.get(exchangeRateDtoList.size() - 1);
    }
}
