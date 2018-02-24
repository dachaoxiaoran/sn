package sn.sn.constant;

/**
 * 常量
 * @author 王超
 */
public interface IConstant {
	
	/**
	 * 汇率链接
	 */
	String CURRENCY_URL = "https://zh.tradingeconomics.com/currencies";
	
	/**
	 * 美指系数
	 */
	double USDX = 50.14348112;
	
	/**
	 * 欧元->美元的美指系数
	 */
	double EURUSD = -0.576;
	
	/**
	 * 美元->日元的美指系数
	 */
	double USDJPY = 0.136;
	
	/**
	 * 英镑->美元的美指系数
	 */
	double GBPUSD = -0.119;
	
	/**
	 * 美元->加元的美指系数
	 */
	double USDCAD = 0.091;
	
	/**
	 * 美元->瑞典克朗的美指系数
	 */
	double USDSEK = 0.042;
	
	/**
	 * 美元->瑞士法郎的美指系数
	 */
	double USDCHF = 0.036;
	
	/**
	 * 欧元->美元的搜索文本
	 */
	String EURUSD_TEXT = "<b>EURUSD</b>";
	
	/**
	 * 美元->日元的搜索文本
	 */
	String USDJPY_TEXT = "<b>USDJPY</b>";
	
	/**
	 * 英镑-美元的搜索文本
	 */
	String GBPUSD_TEXT = "<b>GBPUSD</b>";
	
	/**
	 * 美元->加元的搜索文本
	 */
	String USDCAD_TEXT = "<b>USDCAD</b>";
	
	/**
	 * 美元-瑞典克朗的搜索文本
	 */
	String USDSEK_TEXT = "<b>USDSEK</b>";
	
	/**
	 * 美元->瑞士法郎的搜索文本
	 */
	String USDCHF_TEXT = "<b>USDCHF</b>";
	
	/**
	 * 美元->人民币的搜索文本
	 */
	String USDCNY_TEXT = "<b>USDCNY</b>";
	
	/**
	 * 正则，判断是否数字
	 */
	String IS_DOUBLE = "-?[0-9]+.*[0-9]*";
	
	/**
	 * mysql连接url
	 */
	String MYSQL_URL = "jdbc:mysql://localhost:3306/sn";
	
	/**
	 * mysql驱动名
	 */
	String MYSQL_NAME = "com.mysql.jdbc.Driver";
	
	/**
	 * mysql用户名
	 */
	String MYSQL_USER = "root";
	
	/**
	 * mysql密码
	 */
	String MYSQL_PASSWORD = "root";
	
	/**
	 * 商品链接
	 */
	String GOODS_URL = "https://zh.tradingeconomics.com/commodities";
	
	/**
	 * 黄金搜索文本
	 */
	String GOLD_TEXT = "<b>黄金</b>";
}
