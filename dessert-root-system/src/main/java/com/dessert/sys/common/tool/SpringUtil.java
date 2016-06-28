package com.dessert.sys.common.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * spring工具类
 * 
 */
public class SpringUtil implements ApplicationContextAware {
	private static final Log LOGGER = LogFactory.getLog(SpringUtil.class);

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取想要的bean
	 * 
	 * @param beanClass
	 *            bean的class
	 * @return
	 */
	public static <T> T getBean(Class<T> beanClass) {
		return applicationContext.getBean(beanClass);
	}
	
	/**
	 * 获取想要的beans
	 * @param <T>
	 * 
	 * @param beanClass
	 *            bean的class
	 * @return
	 */
	public static <T> Collection<T> getBeans(Class<T> beanClass) {
		Collection<T> list = applicationContext.getBeansOfType(beanClass).values();
		List<T> beans = new ArrayList<T>();
		if(list != null){
			beans.addAll(list);
			sortBeans(beans);
		}
		return beans;
	}
	
	/**
	 * bean 按照sort排序
	 * @param beans
	 */
	public static <T> void sortBeans(List<T> beans){
		if(beans != null){
			Collections.sort(beans, AnnotationAwareOrderComparator.INSTANCE);
		}
	}
	
	/**
	 * 获取想要的bean
	 * 
	 * @param beanName
	 *            spring配置文件中bean 的 id
	 * @return
	 */
	public static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}

	/**
	 * 获取想要的bean
	 * 
	 * @param beanName
	 *            spring配置文件中bean 的 id
	 * @param clazz
	 *            类型
	 * @return
	 */
	public static <T> T getBean(String beanId, Class<T> clazz) {
		return applicationContext.getBean(beanId, clazz);
	}

	/**
	 * 获得代理对象的bean
	 * 
	 * @param <T>
	 * @param proxy
	 * @param targetClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object proxy, Class<T> targetClass) {
		T t = null;
		try {
			if (AopUtils.isJdkDynamicProxy(proxy)) {
				t = (T) ((Advised) proxy).getTargetSource().getTarget();
			} else {
				t = (T) proxy;
			}
		} catch (Exception e) {
			LOGGER.error("getTargetObject error", e);
		}
		return t;
	}

	/**
	 * 获得本地化信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return getLocalMessage(key);
	}

	/**
	 * 获得本地化信息
	 * 
	 * @param key
	 * @param param
	 * @return
	 */
	public static String getMessage(String key, Object... param) {
		return getLocalMessage(key, param);
	}

	private static String getLocalMessage(String key, Object... param) {
		HttpServletRequest request = getRequest();
		Locale locale = null;
		if (request != null) {
			locale = getBean(SessionLocaleResolver.class).resolveLocale(getRequest());
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		String message = applicationContext.getMessage(key, param, locale);
		return message;
	}

	/**
	 * 获得ApplicationContext
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获得request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest httpServletRequest = null;
		if (servletRequestAttributes != null) {
			httpServletRequest = servletRequestAttributes.getRequest();
		}
		return httpServletRequest;
	}
	//
	// /**
	// * 刷新spring容器
	// */
	// public static void reloadApplicationContext(boolean reloadDataSource) {
	// ((ConfigurableApplicationContext) applicationContext).refresh();
	// }
}
