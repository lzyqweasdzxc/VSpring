package org.vitoliu.beans;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bean在 IoC 容器中的定义， IoC 容器可以根据这个定义来生成实例 的问题
 *
 * 以 BeanDefinition 类为核心发散出的几个类
 * 都是用于解决 Bean 的具体定义问题:
 * 1： Bean 的名字是什么、
 * 2：它的类型是什么，它的属性赋予了哪些值或者引用
 *
 * @author yukun.liu
 * @since 23 十一月 2018
 */
@Data
@NoArgsConstructor
public class BeanDefinition<T> {


	/**
	 * Bean
	 */
	private T bean;

	/**
	 * bean的类型
	 * 根据类型可以生成一个实例，然后把各种属性注入进去
	 */
	private Class<T> beanClass;

	/**
	 * bean的名称
	 */
	private String beanName;

	/**
	 * bean的属性集合
	 * 每个属性都是键值对 String - Object
	 */
	private PropertyValues propertyValues = new PropertyValues();
}
