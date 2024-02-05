import java.lang.reflect.Array;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {

    public static void main(String[] args) throws Exception {
        StockAnalyst stockAnalyst = new StockAnalyst();
        List<String> categories = stockAnalyst.getStocksListCategories(stockAnalyst.getUrlText());
        int input = 0;

        while (input == 0) {
            printBar();
            System.out.println("These are the available stock list categories, Please choose one:");
            printCategories(categories);
            input = getUserInput();

            printBar();
            System.out.println("These are the available stock lists within this categories, Please choose one:");
            Map<String, String> subcategories = stockAnalyst.getStocksListsInListCategory(stockAnalyst.getUrlText(), categories.get(input));
            printSubCategories(input, subcategories);
            input = getUserInput();

            List<String> keys = convertSetToList(subcategories.keySet());
            System.out.println("Please type number of top companies you'd like to view: ");
            String urlText = stockAnalyst.getUrlText(subcategories.get(keys.get(input)));
            input = getUserInput();

            printTables(stockAnalyst.getTopCompanies(urlText,input));


            System.out.println("0 to continue or 1 exit");
            input = getUserInput();
        }

    }

    public static void printBar() {
        String bar = "##";
        for (int i = 0; i < 64; i++) {
            bar += "-";
        }
        System.out.println(bar);
    }

    public static void printCategories(List<String> categories) throws Exception {
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(i + ". " + categories.get(i));
        }
    }

    public static void printSubCategories(int input, Map<String, String> subcategories) throws Exception {
        Set<String> keys = subcategories.keySet();
        int i = 0;
        for (String key : keys) {
            System.out.println(i + ". " + key);
            i++;
        }
    }

    public static int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        return input;
    }

    public static void printCompanies(TreeMap<Double, List<String>> companiesByPercent) {
        for(Map.Entry<Double, List<String>> entry : companiesByPercent.entrySet()) {
            Double key = entry.getKey();
            String companies = String.join(",", entry.getValue());
            System.out.printf("%-50s %.2f%% %n", companies, key);
            System.out.println("--------------------------------");
        }

    }

    public static void printTables( List<TreeMap<Double, List<String>>> tables) {
        printCompanies(tables.get(0));
        if(tables.size() > 1) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Related table was found, printing table...");
            printCompanies(tables.get(1));
        }
    }
    public static List<String> convertSetToList(Set<String> set) {
        List<String> list = new ArrayList<>();
        for (String key : set) {
            list.add(key);
        }
        return list;
    }


}
