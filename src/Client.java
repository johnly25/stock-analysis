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
            List<TreeMap<Double, List<String>>> companiesByPercent = stockAnalyst.getTopCompanies(stockAnalyst.getUrlText(subcategories.get(keys.get(input))));
            System.out.println("There are " + companiesByPercent.get(0).size() + " stocks in this category, Please type number of stocks you'd like to view: ");
            input = getUserInput();

            printTables(companiesByPercent, input);
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

    public static void printCompanies(TreeMap<Double, List<String>> companiesByPercent, int count) {
        List<Double> keys = new ArrayList<>();
        Set<Double> keySet = companiesByPercent.keySet();
        Iterator<Double> itr = keySet.iterator();
        int i = 0;
        while (itr.hasNext() && i < count) {
            keys.add(itr.next());
            i++;
        }

        for (Double key : keys) {
            String companies = String.join(", ", companiesByPercent.get(key));
            System.out.printf("%-50s %.2f%% %n", companies, key);
            System.out.println("--------------------------------");
        }
    }

    public static void printTables(List<TreeMap<Double, List<String>>> tables, int count) {
        TreeMap<Double,List<String>> companiesByPercent = tables.get(0);
        printCompanies(companiesByPercent, count);

        if(tables.size() > 1) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Related table was found, printing table...");
            printCompanies(tables.get(1), count);

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
