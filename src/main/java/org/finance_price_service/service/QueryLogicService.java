package org.finance_price_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import org.finance_price_service.domain.OneDayPrice;
import org.finance_price_service.domain.PricesSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.finance_price_service.domain.AlphaVantageJSONKeys.*;

/**
 * Deals with the processing logic for upcoming queries.
 */
@Service
public class QueryLogicService {
  @Autowired private MySQLService sql;
  @Autowired private AlphaVantageService alpha;

  /**
   * Querying requests   http://{server_address}/query?symbol=ABCD&days=10
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

  /**
   * Updating requests  http://{server_address}/update?symbol=ABCD
   * @param symbol Stock Symbol
   * @return       Single message
   * @throws Exception
   */
  public String update(String symbol) throws Exception {
    String alphaJsonString = alpha.fetch(symbol);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode alphaJsonNode = mapper.readTree(alphaJsonString);

    /**
     * Converts JsonNode to PricesSet
     */
    PricesSet prices = new PricesSet(symbol);
    JsonNode priceArray = alphaJsonNode.get(Time_Series.key);
    Iterator<Entry<String, JsonNode>> fields = priceArray.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> jsonField = fields.next();
      OneDayPrice price = new OneDayPrice();
      String date = jsonField.getKey();
      double open = jsonField.getValue().get(OPEN.key).asDouble();
      double high = jsonField.getValue().get(HIGH.key).asDouble();
      double low = jsonField.getValue().get(LOW.key).asDouble();
      double close = jsonField.getValue().get(CLOSE.key).asDouble();
      int volume = jsonField.getValue().get(VOLUME.key).asInt();
      price.setValue(symbol, date, open, high, low, close, volume);
      prices.addPrice(price);
    }

    sql.insert(prices);
    return String.format("Updated %s data successfully!", symbol);
  }
}