package datahandlers;

import java.time.LocalDateTime;
import java.util.List;

import marketdata.Bar;

/**
 * 该DataHandler是一个抽象基类（ABC）呈现的界面处理既有历史或实时市场数据。
 * 这提供了显著灵活性的策略和投资组合的模块因此可以这两种方法之间重用。 DataHandler在生成一个新的MarketEvent在每一个心跳系统。
 * 
 * @author darkness
 *
 */
public interface IDataHandler {
	
	List<String> getSymbols();

	boolean getContinueBacktest();

	LocalDateTime getCurrentTime();

	Bar GetLast(String symbol);

	double GetLastClosePrice(String symbol);

	void Update();
}
