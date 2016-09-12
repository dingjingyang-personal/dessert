package com.dessert.sys.sequence.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.UserTool;
import com.dessert.sys.log.service.SysLogService;
import com.dessert.sys.sequence.service.SequenceNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service("seqService")
public class SequenceNumServiceImpl implements SequenceNumService {


    @Autowired
    private DaoClient daoClient;
    private Lock lock = new ReentrantLock();
    @Autowired
    private SysLogService sysLogService;


    @Override
    public String readSeqBySeqKeyAndOwner(String seqKey, String owner) {
        return readSeqBySeqKeyAndOwner(seqKey, owner, true);
    }

    /**
     * @param seq
     * @param addOwnerAtFirst
     * @param
     * @return
     */
    private String joinSeq(Map<String, Object> seq, boolean addOwnerAtFirst) {
        String sqlId = "com.dessert.sequence.getSeqSetting";
        List<Map<String, Object>> list = daoClient.selectList(sqlId, seq);
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        String temp = SysToolHelper.getMapValue(seq, "seqowner");
        StringBuilder builder = new StringBuilder(temp);
        if (!addOwnerAtFirst || "0".equals(temp)) {
            builder = new StringBuilder();
        } else {
            builder = new StringBuilder(temp);
        }
        for (int i = 0; i < size; i++) {
            builder.append(list.get(i).get("itemValue"));
        }
        return builder.toString();
    }

    private void addSeqVal(Map<String, Object> params) {
        String sqlId = "com.dessert.sequence.addSeqVal";
        if (!SysToolHelper.isExists(params, "seqvalue")) {
            params.put("seqvalue", 0);
        }
        daoClient.update(sqlId, params);
    }

    @Override
    public String getSeqBySeqKey(String seqKey) {
        return readSeqBySeqKeyAndOwner(seqKey, null);
    }

    /**
     * getNextNumber:读取下一个序列值
     *
     * @param params
     * @return
     * @author Administrator
     */
    private long readNextNumber(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey", "seqowner")) {
            return -1;
        }
        String sqlId = "com.dessert.sequence.getSeqNum";
        Map<String, Object> temp = daoClient.selectMap(sqlId, params);
        long value = SysToolHelper.getLong(temp, "seqvalue");
        if (value <= 0) {//没有记录
            addSeqVal(params);
            value = 0;
        }
        params.put("seqnum", value);
        sqlId = "com.dessert.sequence.updateSeqNum";
        return daoClient.update(sqlId, params) > 0 ? (value + 1) : -1;
    }


    @Override
    public String readSeqBySeqKeyAndOwner(String seqKey, String owner, boolean addOwnerAtFirst) {
        try {
            lock.lock();
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("seqkey", seqKey);
            if (owner == null) {
                temp.put("seqowner", "0");
            } else {
                temp.put("seqowner", owner);
            }
            long value = readNextNumber(temp);
            int count = 0;

            //尝试获取三次
            while (value <= 0 && count <= 3) {
                value = readNextNumber(temp);
                count++;
            }
            temp.put("seqvalue", value);
            return joinSeq(temp, addOwnerAtFirst);
        } catch (RuntimeException e) {
            sysLogService.error("getSeqBySeqKeyAndOwner", e);
        } finally {
            lock.unlock();
        }
        return null;
    }



    @Override
    public boolean addOrUpdateSetting(Map<String, Object> params) {
        String operType = String.valueOf(params.get("operType"));
        if (!SysToolHelper.equal(operType, "1", "2")
                || !SysToolHelper.isExists(params, "seqkey", "seqtype", "seqorder", "seqtypevalue")) {
            return false;
        }
        String sqlId;
        if ("1".equals(operType)) {
            sqlId = "com.dessert.sequence.addSetting";
            params.put("seqid", readSeqBySeqKeyAndOwner("NOPREFIX", "SEQID", false));
        } else {
            if (!SysToolHelper.isExists(params, "seqid")) {
                return false;
            }
            sqlId = "com.dessert.sequence.updateSetting";
        }
        return daoClient.update(sqlId, params) > 0;
    }







    @Override
    public String[] readSeqBySeqKeyAndOwner(String seqKey, String owner,
                                            boolean addOwnerAtFirst, int count) {
        if (count <= 0) {
            return null;
        }
        String[] temp = new String[count];
        for (int i = 0; i < count; i++) {
            temp[i] = readSeqBySeqKeyAndOwner(seqKey, owner, addOwnerAtFirst);
        }
        return temp;
    }

    @Override
    public String readSeqBySeqKeyAndOwner(String seqKey, String owner,
                                          int defautValue, boolean addOwnerAtFirst) {
        try {
            lock.lock();
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("seqkey", seqKey);
            if (owner == null) {
                temp.put("seqowner", "0");
            } else {
                temp.put("seqowner", owner);
            }
            temp.put("seqvalue", defautValue);
            long value = readNextNumber(temp);
            int count = 0;
            while (value <= 0 && count <= 3) {//尝试获取三次
                value = readNextNumber(temp);
                count++;
            }
            temp.put("seqvalue", value);
            return joinSeq(temp, addOwnerAtFirst);
        } catch (RuntimeException e) {
            sysLogService.error("getSeqBySeqKeyAndOwner", e);
        } finally {
            lock.unlock();
        }
        return null;
    }


    @Override
    public Map<String, Object> getNextSeqNum(Map<String, Object> params) {
        Map<String, Object> temp = new HashMap<String, Object>();
        if (!SysToolHelper.isExists(params, "seqKey", "owner", "addOwnerAtFirst")) {
            return temp;
        }
        String seqKey = String.valueOf(params.get("seqKey"));
        String owner = String.valueOf(params.get("owner"));
        boolean addOwnerAtFirst = SysToolHelper.equal(params, "addOwnerAtFirst", "true");
        String seq = readSeqBySeqKeyAndOwner(seqKey, owner, addOwnerAtFirst);
        temp.put("seqnum", seq);
        return temp;
    }






    @Override
    public Page<?> getSeqPage(Map<String, Object> params) {
        String sqlId = "com.dessert.sequence.selectSeq";
        return daoClient.selectPage(sqlId, params);
    }


    @Override
    public Map<String, Object> getSequence(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "seqkey")) {
            return null;
        }
        String sqlId = "com.dessert.sequence.selectSeq";
        return daoClient.selectMap(sqlId, params);
    }

    @Override
    public boolean addSequence(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "seqkey", "seqdesc")) {
            return false;
        }
        params.put("createuser", UserTool.getUserForShiro().getUsername());
        return daoClient.update("com.dessert.sequence.addSeq", params) > 0;
    }

    @Override
    public boolean updatSequence(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "seqkey")) {
            return false;
        }
        return daoClient.update("com.dessert.sequence.updateSeq",params)>0;
    }


    @Override
    public Page<?> getSeqSettingPage(Map<String, Object> params) {
        String sqlId = "com.dessert.sequence.getSetting";
        return daoClient.selectPage(sqlId, params);
    }


    @Override
    public Map<String, Object> getSeqSetting(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "seqsettingid")) {
            return daoClient.selectMap("com.dessert.sequence.getSetting", params);
        }
        return null;
    }

    @Override
    public boolean addSettingSequence(Map<String, Object> params) {
        if(SysToolHelper.isExists(params,"seqkey","seqtype","seqorder","seqtypevalue")){
            params.put("seqsettingid",SysToolHelper.getUuid());
            return daoClient.update("com.dessert.sequence.addSetting",params)>0;
        }
        return false;
    }

    @Override
    public boolean updateSettingSequence(Map<String, Object> params) {
        if(SysToolHelper.isExists(params,"seqsettingid")){
            return daoClient.update("com.dessert.sequence.updateSetting",params)>0;
        }
        return false;
    }

    @Override
    public Page<?> getSeqValuePage(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey")) {
            return null;
        }
        String sqlId = "com.dessert.sequence.getValue";
        return daoClient.selectPage(sqlId, params);
    }


    @Override
    public Map<String, Object> getSeqValue(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey", "seqowner")) {
            return null;
        }
        String sqlId = "com.dessert.sequence.getValue";
        return daoClient.selectMap(sqlId, params);
    }



    @Override
    public boolean updateSeqValue(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "seqkey", "seqvalue", "seqowner")) {
            String sqlId = "com.dessert.sequence.updateValue";
            return daoClient.update(sqlId, params) > 0;
        }
        return false;
    }


}
