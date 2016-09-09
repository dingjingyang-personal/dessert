package com.dessert.sys.sequence.service;

import com.dessert.sys.common.bean.Page;

import java.util.Map;

public interface SequenceNumService {



	
	/**
	 * 
	 * addOrUpdateSetting:添加、修改  <br/> 
	 * 
	 * @author Administrator 
	 * @param params
	 * @return
	 */
	boolean addOrUpdateSetting(Map<String, Object> params);
	

	
	/**
	 * 
	 * getSeqSetting:一句话描述这个方法的作用 <br/> 
	 * 
	 * @author Administrator 
	 * @param params
	 * @return
	 */
	Map<String,Object> getSeqSetting(Map<String, Object> params);
	
	/**
	 * 
	 * getSeqValuePage:一句话描述这个方法的作用 <br/> 
	 * 
	 * @author Administrator 
	 * @param params
	 * @return
	 */
	Page<?> getSeqValuePage(Map<String, Object> params);
	
	/**
	 * 
	 * getSeqValue:一句话描述这个方法的作用 <br/> 
	 * 
	 * @author Administrator 
	 * @param params
	 * @return
	 */
	Map<String,Object> getSeqValue(Map<String, Object> params);
	
	/**
	 * 
	 * updateSeqValue:修改序列值 <br/> 
	 * 
	 * @author Administrator 
	 * @param params
	 * @return
	 */
	boolean updateSeqValue(Map<String, Object> params);
	/**
	 * 
	 * getSeqBySeqKeyAndOwner:获取流水号 
	 * 
	 * @author Administrator 
	 * @param seqKey
	 * @param owner
	 * @return
	 */
   String readSeqBySeqKeyAndOwner(String seqKey, String owner);
   
   /**
    * 
    * getSeqBySeqKeyAndOwner:获取流水号  <br/> 
    * 
    * @author Administrator 
    * @param seqKey
    * @param owner
    * @return
    */
   String readSeqBySeqKeyAndOwner(String seqKey, String owner, boolean addOwnerAtFirst);
   
   String readSeqBySeqKeyAndOwner(String seqKey, String owner, int defautValue, boolean addOwnerAtFirst);
   /**
    * 读取多个流水号
    * @param seqKey
    * @param owner
    * @param addOwnerAtFirst
    * @param count
    * @return
    */
   String[] readSeqBySeqKeyAndOwner(String seqKey, String owner, boolean addOwnerAtFirst, int count);
   
   String getSeqBySeqKey(String seqKey);
   
   Map<String,Object> getNextSeqNum(Map<String, Object> params);


	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	Page<?> getSeqPage(Map<String, Object> params);

	/**
	 * 查询单个seq
	 * @param params
	 * @return
	 */
	Map<String,Object> getSequence(Map<String, Object> params);

	/**
	 * 添加seq
	 * @param params
	 * @return
     */
	boolean addSequence(Map<String, Object> params);

	/**
	 * 修改seq
	 * @param params
	 * @return
     */
	boolean updatSequence(Map<String, Object> params);


	/**
	 * 查询序列配置
	 * @param params
	 * @return
     */
	Page<?> getSeqSettingPage(Map<String, Object> params);

	/**
	 * 添加序列配置
	 * @param params
	 * @return
     */
	boolean addSettingSequence(Map<String, Object> params);

	/**
	 * 修改序列配置
	 * @param params
	 * @return
     */
	boolean updateSettingSequence(Map<String, Object> params);
}
