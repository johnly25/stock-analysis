import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws Exception {
        StockAnalyst stockAnalyst = new StockAnalyst();
        String urlText = stockAnalyst.getUrlText();
        List<String> categories = stockAnalyst.getStocksListCategories(urlText);
        Map<String, String> subcategories = stockAnalyst.getStocksListsInListCategory(urlText, categories.get(1));
        Map.Entry<String, String> entry = subcategories.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();
        System.out.println(key + ": " + value);
        urlText = stockAnalyst.getUrlText("https://stockanalysis.com/list/ai-stocks/");
        Map<Double, List<String>> percentChange = stockAnalyst.getTopCompaniesByChangeRate(urlText);
        System.out.println(percentChange);

    }


}