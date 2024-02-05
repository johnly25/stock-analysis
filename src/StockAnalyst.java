import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockAnalyst implements IStockAnalyst {
    public String getUrlText() throws Exception {
        return getUrlText(WEB_URL);
    }

    @Override
    public String getUrlText(String url) throws Exception {
        URLConnection stockListURL = new URL(url).openConnection();
        stockListURL.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(stockListURL.getInputStream()));
        String inputLine;
        StringBuilder urlHtml = new StringBuilder();

        while ((inputLine = inputStream.readLine()) != null) {
            urlHtml.append(inputLine).append("\n");
        }

        inputStream.close();
        return urlHtml.toString();
    }

    @Override
    public List<String> getStocksListCategories(final String urlText) {
        Pattern pattern = Pattern.compile("<h2.*?>(.*?)<\\/h2>");
        Matcher matcher = pattern.matcher(urlText);
        List<String> categories = new ArrayList<String>();
        while (matcher.find()) {
            categories.add(matcher.group(1));
        }
        return categories;
    }

    @Override
    public Map<String, String> getStocksListsInListCategory(String urlText, String stockCategoryName) {
        Pattern pattern = Pattern.compile(stockCategoryName + ".*?</div>");
        Matcher matcher = pattern.matcher(urlText);
        String subcategory = "";
        Map<String, String> subcategoryMap = new LinkedHashMap<String, String>();
        while (matcher.find()) {
            subcategory = matcher.group();
        }
        pattern = Pattern.compile("<li.*?href=\\\"(.*?)\\\">(.*?)<");
        matcher = pattern.matcher(subcategory);
        while (matcher.find()) {
            subcategoryMap.put(matcher.group(2), "https://stockanalysis.com" + matcher.group(1));
        }
        return subcategoryMap;
    }

    @Override
    public TreeMap<Double, List<String>> getTopCompaniesByChangeRate(String urlText) throws Exception {
        TreeMap<Double, List<String>> companyByPercentChange = new TreeMap<Double, List<String>>(Collections.reverseOrder());
        Pattern pattern = Pattern.compile("<tr.*?slw.*?>(.*?)<.*?<td\\sclass=\\\"\\w\\w\\s.*?>(-?\\d+\\.\\d+)%");
        Matcher matcher = pattern.matcher(urlText);
        while (matcher.find()) {
            String company = String.valueOf(matcher.group(1));
            Double percent = Double.parseDouble(matcher.group(2));
            if (companyByPercentChange.containsKey(percent)) {
                List<String> list = companyByPercentChange.get(percent);
                list.add(company);
                companyByPercentChange.put(percent, list);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(company);
                companyByPercentChange.put(percent, list);
            }
        }
        return companyByPercentChange;
    }

    public List<TreeMap<Double, List<String>>> getTopCompanies(String urlText) throws Exception {
        List<TreeMap<Double, List<String>>> tableMaps = new ArrayList<>();
        Pattern pattern = Pattern.compile("<table.*?<\\/table>");
        Matcher matcher = pattern.matcher(urlText);
        List<String> tables = new ArrayList<String>();
        while (matcher.find()) {
            tables.add(matcher.group());
            tableMaps.add(getTopCompaniesByChangeRate(matcher.group()));
        }
        return tableMaps;
    }
}
