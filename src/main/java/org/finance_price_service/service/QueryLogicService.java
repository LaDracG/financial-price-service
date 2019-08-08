package org.finance_price_service.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Deals with the processing logic for upcoming queries.
 */
@Service
public class QueryLogicService {
  @Autowired private MySQLService sql;
  @Autowired private AlphaVantageService alpha;
  @Autowired private JSONProcessingService jss;

  /**
   *
   * @param symbol Stock symbol
   * @param days   Days number that the user requests (starting from today)
   * @return
   */
  public PricesSet query (String symbol, int days) {
    PricesSet res = new PricesSet(symbol);
    for (int i = 0; i < days; ++i) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -i);
      String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
      OneDayPrice selected = sql.select(symbol, date);
      if (selected == null) {
        /** TODO*/
      }
      res.addPrice(sql.select(symbol, date));
      }
    return res;
  }


  public String update(String symbol) throws Exception {
    JSONObject alphaJSON = alpha.fetch(symbol);
    PricesSet prices = jss.parse(alphaJSON);
    sql.insert(prices);
    return String.format("Updated %s data successfully!", symbol);
  }
}