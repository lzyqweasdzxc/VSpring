package org.vitoliu.context;

import org.vitoliu.beans.factory.AbstractBeanFactory;

/**
 *
 * {@link ApplicationContext}的抽象实现
 * @author yukun.liu
 * @since 23 十一月 2018
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

	protected AbstractBeanFactory beanFactory;

	/**
	 * {@link org.vitoliu.beans.BeanDefinition}
	 * 用于实现 BeanFactory 的刷新
	 * 也就是告诉 BeanFactory 该使用哪个资源（Resource）
	 * 加载bean的定义,并实例化，初始化bean
	 * @throws Exception
	 */
	public void refresh() throws Exception {

	}
}
