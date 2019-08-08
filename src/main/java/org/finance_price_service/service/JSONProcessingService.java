package org.finance_price_service.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import static org.finance_price_service.domain.AlphaVantageJSONKeys.*;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JSONProcessingService {
  public PricesSet parse(JSONObject json) throws JSONException, ParseException {
    String symbol = json.getJSONObject(Meta_Data.key).get(Symbol.key).toString();
    JSONObject priceArray = json.getJSONObject(Time_Series.key);
    Iterator<String> dates = priceArray.keys();

    PricesSet prices = new PricesSet(symbol);
    while (dates.hasNext()) {
      String date = dates.next();
      JSONObject price = priceArray.getJSONObject(date);
      final double open = Double.parseDouble(price.get(OPEN.key).toString());
      final double high = Double.parseDouble(price.get(HIGH.key).toString());
      final double low = Double.parseDouble(price.get(LOW.key).toString());
      final double close = Double.parseDouble(price.get(CLOSE.key).toString());
      final int volume = Integer.parseInt(price.get(VOLUME.key).toString());
      OneDayPrice odp = new OneDayPrice();
      odp.setValue(symbol, date, open, high, low, close, volume);
      prices.addPrice(odp);
    }

    return prices;
  }
}
