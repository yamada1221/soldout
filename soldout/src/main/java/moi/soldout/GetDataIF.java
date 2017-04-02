package moi.soldout;

public interface GetDataIF {
	/** 1回あたりの停止時間(ミリ秒) */ 
	final long SLEEP_TIME = 1000 * 60 * 60; // 1時間
	
	/** 人口取得に失敗したときを表す人数 */
	final int FAILT_POPULATION = -1;
}
