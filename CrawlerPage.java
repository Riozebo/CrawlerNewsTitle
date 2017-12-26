package crawlerNews;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawl.CrawlDatatoBase;
import crawl.RetrivePage;
import test.GetNewsTitle;

//import java.util.Timer;
//import java.util.TimerTask;

public class CrawlerPage {

	// 创建一个客户端
	//private static CloseableHttpClient httlpClient = HttpClients.createDefault();
	private static boolean bdb = true;
	// 定义arraylist类集，用来保存每一条数据
	//private static ArrayList<String> datalist = new ArrayList<String>();
	// 计数变量
	private static int countrs = 0;
/*	public static String downloadPage(String url) throws Exception {
		String htmlString = "";
		HttpGet request = new HttpGet(url);
		CloseableHttpResponse response = httlpClient.execute(request);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			htmlString = EntityUtils.toString(entity);
			EntityUtils.consume(entity);

		} finally {
			response.close();
		}
		return htmlString;
	}*/

	public static void getNewsTitle(String surl) throws Exception {
		String html = "http://www.cdepb.gov.cn/cdepbws/Web/Template/GovDefaultList.aspx?cid=1267";
		// 解析document对象
		Document doc = Jsoup.parse(html);
		doc = Jsoup.connect(html).get();
		Elements listDiv = doc.getElementsByAttributeValue("class", "ArticleList");
		for (Element element : listDiv) {
			int i = 0;
			String args ="";
			Elements texts = element.getElementsByTag("span");
			List<String> tempList = new ArrayList<>();
			for (Element text : texts) {
				String title = text.text();
				if (!title.equals("") && title != null) {
					if(i%2==0)
						args = "标题：";
					else
						args = "时间:";
					tempList.add(args + title.trim());
					i++;					 
				}
			}
			//System.out.println(datalist);
			outputRs(tempList);
		}
	}

	public static void outputRs(List<String> tempList) throws Exception {
		String strout = "";
		for (int i = 0; i < tempList.size(); i++) {

			strout = strout + tempList.get(i) + "";
		}
		//System.out.println(strout);
		countrs = countrs+1;
		System.out.println("("+countrs+")"+strout);
		// 插入数据库
		if (bdb) {
			Database.InsertProduct(tempList);
		}
	}


	public static void main(String[] args) {

		String strURL = "http://www.cdepb.gov.cn/cdepbws/Web/Template/GovDefaultList.aspx?cid=1267";
		try {
			Database.setConn();
			getNewsTitle(strURL);
			Database.closeConn(); // 关闭数据库
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
}
