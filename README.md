# financial-price-service
 Service for stock price querying requests.

### Project Overview
This project is to setup a server to be able to respond to requests for the last N days of prices for a specific stock ticker.The response should be JSON. The data should be persisted to MySQL, and if not available, should be fetched and loaded from Alpha Vantage.

### Dependencies
Spring boot
Maven
MySQL
Java Persistence API

### Goals
Maintain data on MySQL, when a user request comes, the server requests data from MySQL first, if not available, it gets data from Alpha Vantage, updates MySQL and responses the new results.

### System Design
#### Entry Points
- PricesController

    The PricesController catches price queries, passes it to QueryLogic service, and return the query results.
#### Services
- QueryLogicService

    Deals with the processing logic for upcoming queries.
- MySQLService
    
    Interacts with MySQL server, handle operations as INSERT, SELECT, UPDATE and so on.
- AlphaVantageAccessingService

    Handles requests for accessing Alpha Vantage through API.
#### Domain Objects
- OneDayPrice

    The price data of one day for some stock. Fields include symbol, date, price values (open, high, low, close), volume.
- PricesSet

    Union of price data for a specific stock in recent days. Fields include symbol, days, prices(list of OneDayPrice). 
- AlphaVantageJSONKeys

    An Enum including all possible keys for JSON object fetched from Alpha Vantage.
#### Repositories
- PricesRepository

    A JPA repository for prices data.
#### Resources
- AlphaVantageConfig

    Configuration file for Alpha Vantage API accessing.
### Query Logic Process
1) heck symbol & date (count back for N days, excluding holidays) in the DB, if all found, return and construct a JSON response.
2) If any dates data is missing, note the missing dates, access to Alpha Vantage and update the DB. 
### Interface Design
- Query URL Example

    _`{server_address}/query?symbol=ABCD&days=10`_
     
    Gets prices of recent 10 days for stock ABCD.
- Update URL Example

  _`{server address}/update?symbol=ABCD`_
### DB Design
TABLE stock_price

| Field |  symbol |   date  |  open  |  high  |   low  |  close | volume |
|:-----:|:-------:|:-------:|:------:|:------:|:------:|:------:|:------:|
|  Type | VARCHAR | VARCHAR | DOUBLE | DOUBLE | DOUBLE | DOUBLE |   INT  |
| PKey |   Yes   |   Yes   |   No   |   No   |   No   |   No   |   No   |

### _Notes & Drafts_
1) Possible exceptions:
- Stock ticker does not exist (and other errors from API)
- Missing data (not holiday) both locally and on Alpha Vantage
2) Holiday checking
3) Missing data, days < 100. Update 100 data points at once.
4) Missing data, days > 100. Update until oldest date or update all 20 years data points?
5)Two options on Alpha Vantage: compact(100 data points) and full(past 20 years). Retrieve full data when first time querying for a symbol?
