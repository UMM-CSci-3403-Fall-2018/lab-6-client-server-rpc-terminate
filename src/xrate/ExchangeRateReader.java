package xrate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author terminate
 */
public class ExchangeRateReader {

    private String baseURL;
    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */
    public ExchangeRateReader(String baseURL) throws MalformedURLException {

        this.baseURL = baseURL;

    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        // Add leading zeros to month and day
        String monthLeading = String.format("%02d", month);
        String dayLeading = String.format("%02d", day);

        // Construct URL with specific date endpoint
        URL dateURL = new URL(baseURL + year + "-" + monthLeading + "-" + dayLeading);

        // Get raw data and convert to JSON
        InputStream inputStream = dateURL.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject payload = new JsonParser().parse(reader).getAsJsonObject();

        // Parse out value at "rates" key
        JsonObject rates = payload.getAsJsonObject("rates");

        // Use helper method to get the exchange rate at key currencyCode in "rates"
        return getRate(rates, currencyCode);

    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {
        // TODO Your code here
        throw new UnsupportedOperationException();
    }

    /**
     * Find exchange rate for particular currency.
     *
     * @param ratesInfo
     *          the JsonObject containing exchange rates for various currencies
     * @param currency
     *          the currency for which we want rate of exchange
     * @return the float exchange rate of currency in JsonObject ratesInfo
     */
    public float getRate(JsonObject ratesInfo, String currency) {
        return ratesInfo.get(currency).getAsFloat();
    }
}