package org.finance_price_service.entrypoint;
  import org.finance_price_service.domain.PricesSet;
  import org.finance_price_service.service.QueryLogicService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;

@RestController
public class PricesController {
  @Autowired
  private QueryLogicService logic;
  @RequestMapping("/query")
  public PricesSet response(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "days", defaultValue = "0") int days) {
    return logic.query(symbol, days);
  }

  @RequestMapping("/update")
  public String update(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "date", defaultValue = "") String date) throws Exception {
    return logic.update(symbol);
  }

}