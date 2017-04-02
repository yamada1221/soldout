package moi.soldout;

import java.util.regex.Pattern;
import java.util.Date;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import twitter4j.*;

/**
 * 取得した情報を整形し、出力します。
 * @version 0.0.3 
 */
public class GetData implements GetDataIF {
	public static void main(String[] args) throws InterruptedException {
		long num = 1;
		long population;
		while (true) {
			Date date = new Date();
			population = getPopulation(3);
			if (0 < population) {
				tweet("人口:" + population);
			}
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
			long intervalTime = SLEEP_TIME > processTime ? SLEEP_TIME - processTime : 0;
			Thread.sleep(intervalTime);
		}
	}
	
	/**
	 * 人口を取得します。
	 * @return 人口
	 * @since 0.0.1
	 * @version 0.0.3 
	 */
	private static int getPopulation() {
		String urlRank = "http://aqualiss.xyz/soldout/ranking.cgi";
		Document document = null;
		int population = FAILT_POPULATION;
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
	
	/**
	 * 人口を取得します。
	 * @return 人口
	 * @since 0.0.3
	 * @version 0.0.3 
	 */
	private static int getPopulation(int retryCount) {
		int population = getPopulation();
		for (int retry = 0; retry < FAILT_POPULATION; retry++) {
			if (0 < population) {
				getPopulation();
			} else {
				break;
			}
		}
		
		return population;
	}
	
	/** 
	 * ツイートをします。(twitter4j.properties 設定を読み込み)
	 * @param tweetStr ツイート文字列
	 * @since 0.0.2
	 * @version 0.0.2 
	 */
	private static void tweet(String tweetStr) {
		Twitter twitter = TwitterFactory.getSingleton();
		try {
			// ツイート内容
			twitter.updateStatus(tweetStr);
		} catch (TwitterException e) {
			System.err.println(e.getErrorMessage());
		}
	}
}