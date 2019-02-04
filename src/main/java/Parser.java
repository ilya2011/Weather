import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws Exception {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }
//   \d{2}\.\d{2}
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");


    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }

    private static int printForValues(Elements values, int index) {
        int iterationCount = 4;
        Element valueLn = values.get(3);
        if (valueLn.text().contains("Утро")) {
            iterationCount = 3;
        } else if (valueLn.text().contains("День")) {
            iterationCount = 2;
        } else if (valueLn.text().contains("Вечер")) {
            iterationCount = 1;
        }
            for (int i = 0; i < 4; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + " \t\t");
                }
                System.out.println();
            }
         return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "\t\tявления \t\t\t\tтемпература \t\tдавление \t\tвлажность \t\tветер");
            int iterationCount = printForValues(values, index);
            index = index + iterationCount;
        }
    }
}
