package com.dessert.sys.sequence.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
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
        String sqlId = "SequenceNumService.getSeqSetting";
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
        String sqlId = "SequenceNumService.addSeqVal";
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
        String sqlId = "SequenceNumService.getSeqNum";
        Map<String, Object> temp = daoClient.selectMap(sqlId, params);
        long value = SysToolHelper.getLong(temp, "seqvalue");
        if (value <= 0) {//没有记录
            addSeqVal(params);
            value = 0;
        }
        params.put("seqnum", value);
        sqlId = "SequenceNumService.updateSeqNum";
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
    public Page<?> getSeqPage(Map<String, Object> params) {
        String sqlId = "SequenceNumService.selectSeq";
        return daoClient.selectPage(sqlId, params);
    }

    @Override
    public Map<String, Object> getSequence(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey")) {
            return null;
        }
        String sqlId = "SequenceNumService.selectSeq";
        return daoClient.selectMap(sqlId, params);
    }

    @Override
    public boolean addOrUpdateSequence(Map<String, Object> params) {
        String operType = String.valueOf(params.get("operType"));
        if (!SysToolHelper.equal(operType, "1", "2")
                || !SysToolHelper.isExists(params, "seqkey", "seqdesc")) {
            return false;
        }
        String sqlId;
        if ("1".equals(operType)) {
            sqlId = "SequenceNumService.addSeq";
        } else {
            sqlId = "SequenceNumService.updateSeq";
        }
        return daoClient.update(sqlId, params) > 0;
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
            sqlId = "SequenceNumService.addSetting";
            params.put("seqid", readSeqBySeqKeyAndOwner("NOPREFIX", "SEQID", false));
        } else {
            if (!SysToolHelper.isExists(params, "seqid")) {
                return false;
            }
            sqlId = "SequenceNumService.updateSetting";
        }
        return daoClient.update(sqlId, params) > 0;
    }

    @Override
    public Page<?> getSeqSettingPage(Map<String, Object> params) {
        String sqlId = "SequenceNumService.getSetting";
        return daoClient.selectPage(sqlId, params);
    }

    @Override
    public Page<?> getSeqValuePage(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey")) {
            return null;
        }
        String sqlId = "SequenceNumService.getValuePage";
        return daoClient.selectPage(sqlId, params);
    }

    @Override
    public boolean updateSeqValue(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey", "seqvalue", "seqowner")) {
            return false;
        }
        String sqlId = "SequenceNumService.updateValue";
        return daoClient.update(sqlId, params) > 0;
    }

    @Override
    public Map<String, Object> getSeqSetting(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqid")) {
            return null;
        }
        String sqlId = "SequenceNumService.getSetting";
        return daoClient.selectMap(sqlId, params);
    }

    @Override
    public Map<String, Object> getSeqValue(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "seqkey", "seqowner")) {
            return null;
        }
        String sqlId = "SequenceNumService.getValuePage";
        return daoClient.selectMap(sqlId, params);
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
}
