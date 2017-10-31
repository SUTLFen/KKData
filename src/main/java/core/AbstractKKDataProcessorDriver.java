package core;

import common.Component;
import util.ReflectionUtils;

import java.io.IOException;

public abstract class AbstractKKDataProcessorDriver {

	public abstract void process() throws IOException;
	
	protected void run(Component[] chain) {
		for (int i = 0; i < chain.length - 1; i++) {
			Component current = chain[i];
			Component next = chain[i + 1];
			current.setNext(next);
		}
		
		for (Component componennt : chain) {
			componennt.fire();
		}
	}
	
	public static void start(Class<? extends AbstractKKDataProcessorDriver> driverClass) throws IOException {
		AbstractKKDataProcessorDriver driver = ReflectionUtils.newInstance(driverClass);
		driver.process();
	}
}
