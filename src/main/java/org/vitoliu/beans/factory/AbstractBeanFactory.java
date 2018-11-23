package org.vitoliu.beans.factory;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.vitoliu.beans.BeanDefinition;
import org.vitoliu.beans.BeanPostProcessor;

/**
 * BeanFactory 的一种抽象类实现，规范了 IoC 容器的基本结构。
 * IoC 容器的结构：AbstractBeanFactory
 * 维护一个 beanDefinitionMap 哈希表用于保存类的定义信息（BeanDefinition）。
 * @author yukun.liu
 * @since 23 十一月 2018
 */
public abstract class AbstractBeanFactory<T> implements BeanFactory<T> {

	/**
	 * key:bean的名称
	 * value:bean的定义信息
	 * threadSafe
	 */
	ConcurrentMap<String, BeanDefinition<T>> beanDefinitionConcurrentMap = Maps.newConcurrentMap();

	/**
	 * 保存完成注册的bean的name
	 */
	private final List<String> beanDefinitionNames = Lists.newArrayList();

	/**
	 * 增加bean处理程序：
	 * 例如通过AspectJAwareAdvisorAutoProxyCreator#postProcessAfterInitialization()实现AOP的织入
	 */
	private List<BeanPostProcessor<T>> beanPostProcessors = Lists.newArrayList();

	/**
	 * 根据名字获取bean实例(实例化并初始化bean)
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public T getBean(String name) throws Exception {
		//获取该bean的定义
		BeanDefinition<T> beanDefinition = beanDefinitionConcurrentMap.get(name);
		//若没有，抛异常
		if (beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + "is defined");
		}
		T bean = beanDefinition.getBean();
		//如果该bean还没被装配
		if (bean == null) {
			//装配bean(初始化+注入属性）
			//生成相关代理类,用于实现AOP织入
			bean = doCreateBean(beanDefinition);
			bean = initializeBean(bean, name);
			beanDefinition.setBean(bean);
		}
		return bean;
	}

	/**
	 * 初始化bean
	 * 可以在此做AOP处理，返回的是一个代理对象
	 * @param bean
	 * @param name
	 * @return Object
	 * @throws Exception
	 */
	private T initializeBean(T bean, String name) throws Exception {
		for (BeanPostProcessor<T> beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}
		for (BeanPostProcessor<T> beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
		return bean;
	}

	private T doCreateBean(BeanDefinition<T> beanDefinition) throws Exception {
		//实例化bean
		T bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		injectPropertyValues(bean, beanDefinition);
		return bean;
	}

	/**
	 * 模板模式
	 * 具体的属性注入方法由子类实现
	 * @param bean
	 * @param beanDefinition
	 * @return Object
	 * @throws Exception
	 */
	protected abstract T injectPropertyValues(Object bean, BeanDefinition<T> beanDefinition) throws Exception;

	private T createBeanInstance(BeanDefinition<T> beanDefinition) throws IllegalAccessException, InstantiationException {
		return beanDefinition.getBeanClass().newInstance();
	}

	/**
	 * 预处理bean的定义，将bean的名字提前存好，实现IOC容器中的单例bean
	 * @throws Exception
	 */
	public void preInstantiateSingletons() throws Exception {
		for (String beanName : beanDefinitionNames) {
			getBean(beanName);
		}
	}

	/**
	 * 根据类型获取所有实例化的bean
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<T> getBeansOfType(Class<T> type) throws Exception {
		List<T> beans = Lists.newArrayList();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionConcurrentMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

	/**
	 * 注册某个beanName的定义
	 * @param name
	 * @param beanDefinition
	 */
	public void registerBeanDefinition(String name, BeanDefinition<T> beanDefinition) {
		beanDefinitionConcurrentMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}

	/**
	 * 添加处理器
	 * @param beanPostProcessor
	 */
	public void addBeanPostProcessors(BeanPostProcessor<T> beanPostProcessor) {
		this.beanPostProcessors.add(beanPostProcessor);
	}
}
