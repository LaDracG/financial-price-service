package org.finance_price_service.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageService {
  @Value("${alphavantage.apikey}")
  private String apiKey;

  @Value("${alphavantage.url}")
  private String url;

  public JSONObject fetch(String symbol) throws Exception {
    String AlphaURL = String.format(url, symbol, this.apiKey);
    return new JSONObject(IOUtils.toString(new URL(AlphaURL), Charset.forName("UTF-8")));
  }
}
