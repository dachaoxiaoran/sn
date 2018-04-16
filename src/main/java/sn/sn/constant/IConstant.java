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
	 * 伦敦金链接
	 */
	String XAUUSD_URL = "https://js.mc-forex.net/xs_data/xs_reflash_price.php?callback=jQuery17203355605912427688_1520477294752&_=";
	
	/**
	 * 伦敦金搜索文本
	 */
	String XAUUSD_TEXT = "\"product\":\"XAUUSD\",\"bid\":\"";
	
	/**
	 * 债券
	 */
	String BONDS_URL = "https://zh.tradingeconomics.com/bonds";
	
	/**
	 * 美国10年期政府债券搜索文本
	 */
	String BONDS_TEXT = "<b>10Y</b>";
	
	/**
	 * 日历链接
	 */
	String CALENDAR_URL = "https://zh.tradingeconomics.com/calendar";
	
	/**
	 * 定时任务每次执行时的间隔时间
	 */
	long TIMER_WAIT = 3600000;
	
	/**
	 * 定时任务起始年份
	 */
	int TIMER_YEAR = 2018;
	
	/**
	 * 定时任务起始月份
	 */
	int TIMER_MONTH = 1;
	
	/**
	 * 定时任务起始日期
	 */
	int TIMER_DATE = 26;
	
	/**
	 * 定时任务起始小时
	 */
	int TIMER_HOUR = 5;
	
	/**
	 * 定时任务起始分钟
	 */
	int TIMER_MINUTE = 0;
	
	/**
	 * 定时任务起始秒
	 */
	int TIMER_SECOND = 0;
	
	/**
	 * 定时任务结束天
	 */
	int TIMER_DAY_END = 7;
	
	/**
	 * 定时任务结束小时
	 */
	int TIMER_HOUR_END = 6;
	
	/**
	 * 鑫圣客户交易情况
	 */
	String XS_CRM_URL = "https://crmapi.xs9999.com/Crawl/DataCenter_RealTimeTrade?size=7&days=7";
	
	/**
	 * 伦敦金
	 */
	String LDJ_TEXT = "伦敦金";
	
	/**
	 * 连接数据源超时时间
	 */
	int READ_TIME_OUT = 10000;
	
	/**
	 * 时间格式
	 */
	String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 金道客户交易情况
	 */
	String JD_CRM_URL = "https://www.24k.hk/public/datas/24k_index_realTime_data.json";
	
	/**
	 * 建仓
	 */
	String CREATE = "建仓";
	
	/**
	 * 平仓
	 */
	String CLEAR = "平仓";
	
	/**
	 * 卖
	 */
	String SELL = "卖";
	
	/**
	 * 买
	 */
	String BUY = "买";
	
	/**
	 * 界面配置文件路径
	 */
	String MAIN_VIEW_PATH = "C:/MainView.fxml";
	
	/**
	 * 编码集
	 */
	String ENCODE = "UTF-8";
	
	/**
	 * textarea最大字符数
	 */
	Integer TEXTAREA_LIMIT = 10000;
	
	/**
	 * 插入gold表
	 */
	String INSERT_GOLD = "insert into gold(price, modifyTime) values(%s, '%s')";
	
	/**
	 * 插入bond表
	 */
	String INSERT_BOND = "insert into bond(price, modifyTime) values(%s, '%s')";
}
