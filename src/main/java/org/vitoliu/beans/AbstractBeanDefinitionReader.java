package org.vitoliu.beans;

import java.util.Map;

import lombok.Getter;
import org.vitoliu.beans.io.ResourceLoader;

/**
 *
 * {@link BeanDefinitionReader}的抽象实现
 * 定义了BeanDefinitionReader的基本结构
 * 未具体实现loadBeanDefinition方法
 * @author yukun.liu
 * @since 24 十一月 2018
 */
@Getter
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	/**
	 * 通过String->BeanDefinition存储IOC容器中的类定义
	 */
	private Map<String, BeanDefinition<?>> registry;

	/**
	 * 保存了资源加载器，将加载过后的BeanDefinition保存到registry中
	 */
	private ResourceLoader resourceLoader;

	/**
	 * 缩小类的实例域
	 * @param resourceLoader
	 */
	protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
