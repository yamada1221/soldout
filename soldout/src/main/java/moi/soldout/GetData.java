package moi.soldout;

import java.util.regex.Pattern;
import java.util.Date;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * 取得した情報を整形し、出力します。
 * @version 0.0.1 
 */
public class GetData {
	private static long sleepTime = 1000;
	public static void main(String[] args) throws InterruptedException, TwitterException {
		long num = 1;
		long population;
		while (true) {
			Date date = new Date();
			population = getPopulation();
			// ツイート
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.updateStatus("人口:" + population);
			// コンソール出力
			long processTime = new Date().getTime() - date.getTime();
			StringBuffer sb = new StringBuffer();
			sb.append(num++);
			sb.append(",");
			sb.append(date);
			sb.append(",");
			sb.append(population);
			sb.append(",");
			sb.append(processTime);
			sb.append("ms");
			System.out.println(sb.toString());
			if (sleepTime > processTime) {
				Thread.sleep(sleepTime - processTime);
			}
		}
	}
	
	/**
	 * 人口を取得します。
	 * @return 人口
	 * @since 0.0.1
	 * @version 0.0.1 
	 */
	private static int getPopulation() {
		String urlRank = "http://aqualiss.xyz/soldout/ranking.cgi";
		Document document = null;
		int population = -1;
		try {
			document = Jsoup.connect(urlRank).get();
		} catch (Exception e) {
			return population;
		}
		String body = document.select("body").html();
		// 人口
		Pattern ptn = Pattern.compile("(人口:)[0-9]+");
		Matcher match = ptn.matcher(body);
		if (match.find()) {
			population = Integer.valueOf(match.group().substring(3));
		}
		return population;
	}
}