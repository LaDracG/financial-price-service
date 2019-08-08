package org.finance_price_service.service;

import java.util.Vector;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.finance_price_service.domain.rspy.PricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MySQLService {
  @Autowired private PricesRepository pricesRepository;

  public OneDayPrice select(String symbol, String date) {
    Vector<OneDayPrice> price = pricesRepository.findBySymbolAndDate(symbol, date);
    return price.isEmpty() ? null : price.firstElement();
  }

  public void insert(PricesSet prices) {
    for (OneDayPrice price : prices.getPrices()) {
      pricesRepository.save(price);
    }
  }
}
