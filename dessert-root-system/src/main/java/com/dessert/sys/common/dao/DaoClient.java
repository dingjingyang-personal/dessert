package com.dessert.sys.common.dao;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.tool.SysToolHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


public class DaoClient extends SqlSessionDaoSupport {
    private WeakHashMap<String, SqlNode> sqlsMap = new WeakHashMap<String, SqlNode>();

    /**
     * 查询list
     *
     * @param sqlId
     * @param params
     * @return
     */
    public List<Map<String, Object>> selectList(String sqlId, Map<String, Object> params) {
        return this.getSqlSession().selectList(sqlId, params);
    }

    /**
     * 查询一条记录
     *
     * @param sqlId
     * @param params
     * @return
     */
    public Map<String, Object> selectMap(String sqlId, Map<String, Object> params) {
        List<Map<String, Object>> list = selectList(sqlId, params);
        if (list != null && !list.isEmpty()) {
            if(list.size()==1){
                return list.get(0);
            }else{
                return null;
            }
        }
        return null;
    }


    /**
     * 分页查询
     *
     * @param sqlId
     * @param params
     * @return
     */
    public Page<?> selectPage(String sqlId, String countSqlId, Map<String, Object> params) {
        params.put("countSql", countSqlId);
        return selectPage(sqlId, params);
    }

    /**
     * 分页查询
     *
     * @param sqlId
     * @param params
     * @return
     */
    public Page<?> selectPage(String sqlId, Map<String, Object> params) {
        String sqlString = getSql(sqlId, params);
        if (StringUtils.isEmpty(sqlString)) {
            return null;
        }
        params.put("sql_clause", sqlString);
        String countSqlId = SysToolHelper.getMapValue(params, "countSql", "dao.pageCount");
        int currentPage = SysToolHelper.getIntValue(params, SysConstants.CURRENT_PAGE_KEY, SysConstants.CURRENT_PAGE_INEX);
        int pageSize = SysToolHelper.getIntValue(params, SysConstants.PAGE_SIZE_KEY, SysConstants.PAGER_SIZE);
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? SysConstants.PAGER_SIZE : pageSize;
        params.put("isCount", "1");
        Map<String, Object> countMap = selectMap(countSqlId, params);
        params.remove("isCount");
        int count = SysToolHelper.getIntValue(countMap, "count", 0);
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        if (count > 0) {
            sqlString = "dao.page";
            List<Map<String, Object>> list;
            if (count <= pageSize) {
                list = selectList(sqlId, params);
            } else {
                params.put("maxRownum", (currentPage) * pageSize);
                params.put("minRownum", (currentPage - 1) * pageSize + 1);
                list = selectList(sqlString, params);
            }
            page.setPageList(list);
        }
        page.setStatisMap(countMap);
        page.setRecordCount(count);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        return page;
    }


    /**
     * 缓存SQL
     *
     * @param sqlId
     * @param params
     * @return
     */
    private String getSql(String sqlId, Map<String, Object> params) {
        try {
            SqlNode sqlNode = sqlsMap.get(sqlId);
            if (sqlNode == null) {
                MappedStatement statement = this.getSqlSession().getConfiguration()
                        .getMappedStatement(sqlId);
                SqlSource sqlSource = statement.getSqlSource();
                Field field = sqlSource.getClass().getDeclaredField("rootSqlNode");
                field.setAccessible(true);
                sqlNode = (SqlNode) field.get(sqlSource);
            }
            DynamicContext context = new DynamicContext(this.getSqlSession()
                    .getConfiguration(), params);
            sqlNode.apply(context);
            String sql = context.getSql();
            sqlsMap.put(sqlId, sqlNode);
            return sql;
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        }
        return null;
    }

    /**
     * 更新sql
     *
     * @param sqlId
     * @param params
     * @return
     */
    public int update(String sqlId, Map<String, Object> params) {
        return this.getSqlSession().update(sqlId, params);
    }

    /**
     * 批量更新sql
     *
     * @param sqlId
     * @param params
     * @return
     */
    public boolean batchUpdate(String sqlId, List<Map<String, Object>> params) {
        try {
            Configuration c = this.getSqlSession().getConfiguration();
            ManagedTransactionFactory managedTransactionFactory = new ManagedTransactionFactory();
            BatchExecutor batchExecutor = new BatchExecutor(c,
                    managedTransactionFactory.newTransaction(this.getSqlSession().getConnection()));
            final int BATCH_SIZE = 500;
            int i = 0;
            MappedStatement mappedStatement = c.getMappedStatement(sqlId);
            for (Map<String, Object> temp : params) {
                batchExecutor.doUpdate(mappedStatement, temp);
                if (i++ > 0 && i % BATCH_SIZE == 0) {
                    batchExecutor.doFlushStatements(false);
                }
            }
            batchExecutor.doFlushStatements(false);
            return true;
        } catch (SQLException e) {
        }
        return false;
    }

}
