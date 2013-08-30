package org.nupter.nupter.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA.
 * User: helloworld
 * Date: 13-8-16
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
public class JsoupTest {
    private StringBuffer testString;
    public String parse(String html) {
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
        } catch (Exception e) {
        }
        Elements trs = doc.getElementById("Datagrid1").getElementsByTag("tr");
        testString=new StringBuffer();
        for(int i=0;i<trs.size();i++){
            Elements tds=trs.get(i).getElementsByTag("td");
            Log.d(tds.toString());
            for(int j=0;j<tds.size();j++){
                testString.append(tds.get(j).text()).append("&");
            }
            testString.append("$");
        }

        Elements tds=doc.getElementById("TabTj").getElementsByTag("tr").get(14).getElementsByTag("td");
        testString.append(tds.get(0).text()).append("&").append(tds.get(1).text()).append("&").append(tds.get(2).text());
        return testString.toString();
    }
}
