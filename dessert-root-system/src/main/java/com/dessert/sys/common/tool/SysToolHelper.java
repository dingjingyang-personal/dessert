package com.dessert.sys.common.tool;

import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.remote.service.RemoteService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


public class SysToolHelper {
    public static final int INT_TYPE = 1;
    public static final int STRING_TYPE = 2;
    public static final int LONG_TYPE = 3;
    public static final int DOUBLE_TYPE = 4;

    public static int getIntValue(Map<String, Object> paramsMap, String key, int defaultVal) {
        if (paramsMap == null) {
            return defaultVal;
        }
        Object object = paramsMap.get(key);
        if (object == null) {
            return defaultVal;
        }
        int value = defaultVal;
        try {
            value = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {

        }
        return value;
    }

    public static String readSeqBySeqKeyAndOwner(String seqKey, String owner, boolean addOwnerAtFirst) {
        Object object = getBean("remoteService");
        if (object == null || !(object instanceof RemoteService)) {
            return null;
        }
        RemoteService remoteService = (RemoteService) object;
        Map<String, Object> temMap = new HashMap<String, Object>();
        temMap.put("seqKey", seqKey);
        temMap.put("owner", owner);
        temMap.put("addOwnerAtFirst", String.valueOf(addOwnerAtFirst));
        return getMapValue(remoteService.invokeRemoteMethod("seqService", "getNextSeqNum", temMap), "seqnum");
    }

    public static void setValue(Map<String, Object> returnMap, String key, String value) {
        if (returnMap == null || key == null || value == null) {
            return;
        }
        returnMap.put(key, value);
    }


    public static String getNowDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    public static boolean isValidDate(Map<String, Object> map, String key) {

        String date = String.valueOf(map.get(key));

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            map.put(key + "error", "日期格式错误");
            return false;
        }
    }

    /**
     * 安全获取map中时间值
     */
    public static Date getMapDate(Map<String, Object> map, String key, String dateFieldFormat) {

        String date = String.valueOf(map.get(key));

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFieldFormat);
            Date mapdate = dateFormat.parse(date);
            return mapdate;
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 判断字段是否存在
     *
     * @param params param
     * @param keys   key值
     * @return true:params不为空且所需字段都存在,false:params为空或所需字段不存在
     */
    public static boolean isExists(Map<String, Object> params, String... keys) {
        if (params == null || keys == null) {
            return false;
        }
        for (String key : keys) {
            if (StringUtil.isNullOrEmpty(params.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 连接字符串
     *
     * @param values
     * @return
     */
    public static String combineString(Object... values) {
        StringBuilder builder = new StringBuilder();
        for (Object str : values) {
            builder.append(str);
        }
        return builder.toString();
    }


    /**
     * 获取IP
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return parseIp(ip);
    }

    private static String parseIp(String ip) {
        if (ip == null) {
            return "";
        }
        if (ip.indexOf(",") < 0) {
            return ip;
        }
        String[] temp = ip.split(",");
        for (String str : temp) {
            if (!"unknown".equalsIgnoreCase(str)) {
                return str;
            }
        }
        return null;
    }


    /**
     * 功能描述: 获取请求参数
     * author:liwm
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> parameter = request.getParameterMap();
        String[] obj;
        Set<Entry<String, String[]>> entries = parameter.entrySet();
        Map<String, Object> params = new HashMap<String, Object>(entries.size());
        for (Entry<String, String[]> m : entries) {
            obj = m.getValue();
            params.put(m.getKey(), (obj.length > 1) ? obj : obj[0]);
        }
        params.put(SysConstants.LANGUAGECODE, getMapValue(params, SysConstants.LANGUAGECODE, "zh"));
        return params;
    }

    public static Map<String, Object> getLowerKeyParams(Map<String, Object> params) {
        Set<Entry<String, Object>> entries = params.entrySet();
        Map<String, Object> temp = new HashMap<String, Object>(entries.size());
        String key;
        for (Entry<String, Object> m : entries) {
            key = m.getKey();
            if (key == null) {
                continue;
            }
            temp.put(key.toLowerCase(), m.getValue());
        }
        return temp;
    }

    /**
     * 安全获取map中页码值
     */
    public static int getPage(Map<String, Object> params) {
        return getIntegerValue(params, SysConstants.CURRENT_PAGE_KEY, SysConstants.CURRENT_PAGE_INEX);
    }

    /**
     * 安全获取map中页大小值
     */
    public static int getPageSize(Map<String, Object> params) {
        return getIntegerValue(params, SysConstants.PAGE_SIZE_KEY, SysConstants.PAGER_SIZE);
    }

    /**
     * 安全获取map中整数值
     */
    public static int getIntegerValue(Map<String, Object> map, String key, int defaultValue) {
        return getIntValue(map, key, defaultValue);
    }

    public static boolean convertType(Map<String, Object> params, String[] keys, int[] types) {
        if (params == null || keys == null || types == null || keys.length != types.length) {
            return false;
        }
        int len = keys.length;
        String key;
        Object object;
        for (int i = 0; i < len; i++) {
            key = keys[i];
            object = params.get(key);
            if (object == null) {
                continue;
            }
            object = getValidTypeObject(object, types[i]);
            if (object == null) {
                return false;
            }
            params.put(key, object);
        }
        return true;
    }

    private static Object getValidTypeObject(Object object, int type) {
        Object temp = null;
        try {
            switch (type) {
                case SysToolHelper.STRING_TYPE:
                    temp = String.valueOf(object);
                    break;
                case SysToolHelper.INT_TYPE:
                    temp = Integer.parseInt(String.valueOf(object));
                    break;
                case SysToolHelper.DOUBLE_TYPE:
                    temp = Double.parseDouble(String.valueOf(object));
                    break;
                case SysToolHelper.LONG_TYPE:
                    temp = Long.parseLong(String.valueOf(object));
                    break;
            }
            return temp;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 安全获取map中整数值
     */
    public static int getIntegerValue(Map<String, Object> map, String key) {
        return getIntValue(map, key, 0);
    }

    /**
     * 安全获取map值
     */
    public static String getMapValue(Map<String, Object> map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        Object tempObject = map.get(key);
        if (tempObject == null) {
            return defaultValue;
        }
        return String.valueOf(tempObject);
    }

    /**
     * 安全获取map值
     */
    public static String getMapValue(Map<String, Object> map, String key) {
        return getMapValue(map, key, "").trim();
    }

    /**
     * 功能描述:
     * author:liwm
     *
     * @param map
     * @param key
     * @param defaultKey
     * @return
     */
    public static String getMapValueEx(Map<String, Object> map, String key, String defaultKey) {
        String value = getMapValue(map, key, "");
        if (StringUtils.isEmpty(value)) {
            return getMapValue(map, defaultKey);
        }
        return value;
    }

    /**
     * 安全获取map值
     */
    public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object object = map.get(key);
        if (object == null || StringUtils.equals("", String.valueOf(object))) {
            return null;
        }
        return new BigDecimal(String.valueOf(object).trim());
    }

    /**
     * 〈resopnse输出〉 〈功能详细描述〉
     *
     * @param srcList
     * @param key
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static Map<String, String> listToMap(List<Map<String, Object>> srcList, String key) {
        Map<String, String> map = new HashMap<String, String>(srcList.size());
        for (Map<String, Object> temp : srcList) {
            map.put(String.valueOf(temp.get(key)), "");
        }
        return map;
    }

    public static Map<String, Map<String, Object>> listsToMap(List<Map<String, Object>> srcList, String key) {
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>(srcList.size());
        for (Map<String, Object> temp : srcList) {
            map.put(String.valueOf(temp.get(key)), temp);
        }
        return map;
    }

    public static Map<String, Object> listToMap(List<Map<String, Object>> srcList, String key, String valueKey) {
        Map<String, Object> map = new HashMap<String, Object>(srcList.size());
        for (Map<String, Object> temp : srcList) {
            map.put(String.valueOf(temp.get(key)), temp.get(valueKey));
        }
        return map;
    }

    /**
     * 〈response输出〉 〈功能详细描述〉
     *
     * @param obj
     * @param response
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static boolean outputByResponse(Object obj, HttpServletResponse response) {
        try {
            String temp;
            if (obj == null) {
                temp = "";
            } else if ((obj instanceof Map) || (obj instanceof List)) {
                temp = getJsonOfCollection(obj);
            } else {
                temp = String.valueOf(obj);
            }
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(temp);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 〈json〉 〈功能详细描述〉
     *
     * @param list
     * @param response
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String getJsonOfCollection(Object obj) {
        if (obj == null) {
            return null;
        }
        String rt = null;
        if (obj instanceof Map) {
            rt = JSONObject.fromObject(obj).toString();
        } else if (obj instanceof List) {
            rt = JSONArray.fromObject(obj).toString();
        }
        return rt;
    }

    /**
     * 〈设置session中变量〉 〈功能详细描述〉
     *
     * @param resquest
     * @param key
     * @param value
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static void setValueInSession(HttpServletRequest request, String key, Object value) {
        WebUtils.setSessionAttribute(request, key, value);
    }


    /**
     * 〈获取session中变量〉 〈功能详细描述〉
     *
     * @param request
     * @param key
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static Object getValueInSession(HttpServletRequest request, String key) {
        return WebUtils.getSessionAttribute(request, key);
    }

    /**
     * 〈移除session中变量〉 〈功能详细描述〉
     *
     * @param request
     * @param key
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static void removeValueInSession(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }

    /**
     * 复制一个map的值，构造list,常用于批量更新的场景
     *
     * @param addDatas 新增值
     * @param addKey   新增值的key
     * @param srcMap   需复制的map
     * @return list
     */
    public static List<Map<String, Object>> copyMapToList(String[] addDatas, String addKey, Map<String, Object> srcMap,
                                                          String addDataKey, String[] addData) {
        if (addDatas == null || addDatas.length == 0) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(addDatas.length);
        Map<String, Object> map;
        int i = 0;
        for (String temp : addDatas) {
            if (temp == null) {
                continue;
            }
            map = new HashMap<String, Object>();
            if (srcMap != null) {
                map.putAll(srcMap);
            }
            map.put(addKey, temp);
            if (!StringUtils.isEmpty(addDataKey) && addData != null) {
                map.put(addDataKey, addData[i++]);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 复制一个map的值，构造list,常用于批量更新的场景
     *
     * @param addDatas 新增值
     * @param addKey   新增值的key
     * @param srcMap   需复制的map
     * @return list
     */
    public static List<Map<String, Object>> copyMapToList(String[] addDatas, String addKey,
                                                          Map<String, Object> srcMap) {
        if (addDatas == null || addDatas.length == 0) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(addDatas.length);
        Map<String, Object> map;
        for (String temp : addDatas) {
            if (temp == null) {
                continue;
            }
            map = new HashMap<String, Object>();
            if (srcMap != null) {
                map.putAll(srcMap);
            }
            map.put(addKey, temp);
            list.add(map);
        }
        return list;
    }

    /**
     * 〈生成in条件格式表达式〉
     *
     * @param array
     * @param isString
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String arrayToSqlInStr(String[] array, boolean isString) {
        if (array == null || array.length == 0) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder("(");
        String temp;
        for (int i = 0; i < array.length; i++) {
            temp = array[i];
            if (StringUtils.isEmpty(temp)) {
                continue;
            }
            temp = temp.replace("'", "");
            if (isString) {
                stringBuffer.append("'").append(temp).append("'");
            } else {
                stringBuffer.append(temp);
            }
            stringBuffer.append(",");
        }
        int len = stringBuffer.length();
        if (len > 1) {
            stringBuffer.append(")");
            return stringBuffer.substring(0, len - 1);
        }
        return null;
    }

    /**
     * 〈生成in条件格式表达式〉
     *
     * @param array
     * @param isString
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String arrayToSqlInStr(String[] array) {
        return arrayToSqlInStr(array, true);
    }

    /**
     * 功能描述: 生产in的sql条件值
     * author:liwm
     *
     * @param scrList
     * @param key
     * @return
     */
    public static String getListInSql(List<Map<String, Object>> scrList, String key) {
        if (scrList == null || scrList.isEmpty()) {
            return null;
        }
        String[] temp = new String[scrList.size()];
        int i = 0;
        for (Map<String, Object> obj : scrList) {
            temp[i++] = String.valueOf(obj.get(key));
        }
        return arrayToSqlInStr(temp);
    }

    /**
     * 〈判断list是否为空〉
     *
     * @param list
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 〈判断Map是否为空〉
     *
     * @param list
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static boolean isEmptyMap(Map<String, Object> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 〈判断是否相等〉
     *
     * @param map
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static boolean equal(Map<String, Object> map, String key, Object value, boolean castString) {
        if (map == null) {
            return false;
        }
        Object object = map.get(key);
        if (object == null) {// 不考虑null相等的情况
            return false;
        }
        if (castString) {
            object = String.valueOf(object);
        }
        return object.equals(value);
    }

    /**
     * 〈判断是否相等〉
     *
     * @param map
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static boolean equal(Map<String, Object> map, String key, Object value) {
        return equal(map, key, value, false);
    }

    /**
     * 〈安全使用字符串方法〉
     *
     * @param map
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String getStr(String value) {
        return value == null ? "" : value;
    }

    /**
     * 〈处理oracle中文乱码〉
     *
     * @param data
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static Map<String, Object> decodeOracleMap(Map<String, Object> data) {
        String codeEncoding = "GBK";
        String dbEncoding = "ISO8859_1";
        for (Entry<String, Object> m : data.entrySet()) {
            String key = m.getKey();
            Object obj = m.getValue();
            if (obj == null || String.class != obj.getClass()) {
                continue;
            }
            try {
                data.put(key, new String(String.valueOf(obj).getBytes(dbEncoding), codeEncoding));
            } catch (UnsupportedEncodingException e) {
                // LogUtil.printErrorLog("String encode UnsupportedEncoding");
            }

        }
        return data;
    }

    /**
     * 〈处理oracle中文乱码〉
     *
     * @param data
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static List<Map<String, Object>> decodeOracleMap(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        for (Map<String, Object> temp : data) {
            decodeOracleMap(temp);
        }
        return data;
    }

    /**
     * 〈均不存在键值〉
     *
     * @param params
     * @param keys
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static boolean isNotExistsKey(Map<String, Object> params, String... keys) {
        if (params == null) {
            return true;
        }
        for (String temp : keys) {
            if (params.containsKey(temp)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 〈上传文件至图片服务器〉 〈功能详细描述〉
     *
     * @param request
     * @param filename 文件域name值
     * @return 文件名
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String uploadFileToImgServer(HttpServletRequest request, String filename) {
        InputStream in = null;
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            MultipartFile multFile = fileMap.get(filename);
            if (multFile == null) {
                return null;
            }
            String fileName = multFile.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            in = multFile.getInputStream();
            String filePath = uploadFile(in, ext);
            return getFileName(filePath, ext);
        } catch (IOException e) {
            // LogUtil.printErrorLog(e.getMessage());
        } catch (Exception e) {
            // LogUtil.printErrorLog(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // LogUtil.printErrorLog(e.getMessage());
                }
                in = null;
            }
        }
        return null;

    }

    public static void error(String item, Exception ex) {
        LoggerFactory.getLogger(SysToolHelper.class).error(item, ex);
    }

    public static void error(String item, String ex) {
        LoggerFactory.getLogger(SysToolHelper.class).error(item, ex);
    }

    /**
     * 〈上传文件〉
     *
     * @param inputStream
     * @return
     * @throws ImageServiceException
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String uploadFile(InputStream inputStream, String fileExt) {
        // ImageServerClient imageService = new ImageServerClientImpl();
        // return imageService.uploadFile(inputStream,fileExt);
        return null;
    }

    /**
     * 〈拼装文件路径〉
     *
     * @param name
     * @param ext
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String getFileName(String name, String ext) {
        StringBuilder builder = new StringBuilder(SysSettings.IMGSERVERE_PATH);
        if (!SysSettings.IMGSERVERE_PATH.endsWith("/") && !name.startsWith("/")) {
            builder.append("/");
        }
        builder.append(name);
        if (!StringUtils.isEmpty(ext)) {
            builder.append(".").append(ext);
        }
        return builder.toString();

    }

    /**
     * 〈上传html至图片服务器〉
     *
     * @param html
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String uploadFile(HttpServletRequest request, String html, String ext) {
        FileInputStream inputStream = null;
        File file = null;
        try {
            String fileName = request.getSession().getServletContext().getRealPath("/")
                    + UUID.randomUUID().toString().replace("-", "") + "." + ext;
            file = new File(fileName);
            if (createTxtFile(file, html)) {
                inputStream = new FileInputStream(file);
                return uploadFile(inputStream, ext);
            }
        } catch (FileNotFoundException e) {
            error("uploadFile", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    error("uploadFile", e);
                }
                inputStream = null;
            }
            if (file != null) {
                try {
                    file.delete();
                } catch (RuntimeException e2) {
                    error("uploadFile", e2);
                }

            }
        }
        return null;
    }

    /**
     * 〈创建本地文件〉
     *
     * @param file
     * @param data
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    private static boolean createTxtFile(File file, String data) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.flush();
            return true;
        } catch (FileNotFoundException e) {
            error("createTxtFile", e);
        } catch (IOException e) {
            error("createTxtFile", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    error("createTxtFile", e);
                }
                writer = null;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    error("createTxtFile", e);
                }
                outputStream = null;
            }
        }
        return false;
    }

    /**
     * 〈获取通知内容〉
     *
     * @param filePath
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static String getImageServerHtml(String filePath, String ext) {
        if (StringUtil.isNullOrEmpty(filePath)) {
            return null;
        }
        HttpGet httpGet = new HttpGet(getFileName(filePath, ext));
        HttpResponse response;
        try {
            final int status = 200;
            response = new DefaultHttpClient().execute(httpGet);
            if (response.getStatusLine().getStatusCode() == status) {
                HttpEntity tempEntity = response.getEntity();
                String data = EntityUtils.toString(tempEntity, "UTF-8");
                return data;
            }

        } catch (ClientProtocolException e) {
            error("getImageServerHtml", e);
        } catch (IOException e) {
            error("getImageServerHtml", e);
        }
        return null;
    }

    /**
     * 〈删除文件〉
     *
     * @param filepath
     * @param ext
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static void deleteFileFromImg(String filepath) {
        // ImageServerClient client = new ImageServerClientImpl();
        // client.deleteFile(filepath);
    }

    public static Object getBean(String beanId) {
        if (StringUtil.isNullOrEmpty(beanId)) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
                request.getSession().getServletContext(),
                "org.springframework.web.servlet.FrameworkServlet.CONTEXT.appServlet");
        return webApplicationContext.getBean(beanId);
    }

    /**
     * @param map
     * @param key
     * @return
     */
    public static long getLong(Map<String, Object> map, String key) {
        return getLong(map, key, 0);
    }

    /**
     * @param map
     * @param key
     * @return
     */
    public static long getLong(Map<String, Object> map, String key, long value) {
        try {
            return Long.valueOf(String.valueOf(map.get(key))).longValue();
        } catch (RuntimeException e) {
            return value;
        }

    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * isValidValideCode:是否有效的验证码 <br/>
     *
     * @param request
     * @author Administrator
     */
    public static boolean isValidValideCode(HttpServletRequest request) {
        String code = request.getParameter("validatecode");
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        return code.equals(request.getSession().getAttribute(SysConstants.VALIDATE_CODE_KEY));
    }

    /**
     * isExistAndEqual:存在并且等于values里面其中一个 <br/>
     *
     * @param map
     * @param key
     * @param values
     * @return
     * @author Administrator
     */
    public static boolean isExistAndEqual(Map<String, Object> map, String key, String... values) {
        if (!map.containsKey(key)) {
            return false;
        }
        return equal(String.valueOf(map.get(key)), values);
    }

    /**
     * equal:等于values里面其中一个 <br/>
     *
     * @param data
     * @param values
     * @return
     * @author Administrator
     */
    public static boolean equal(String data, String... values) {
        if (data == null) {
            return false;
        }
        for (String val : values) {
            if (data.equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * toBean:实例化bean<br/>
     *
     * @param paramsMap
     * @param cls
     * @return
     * @author Administrator
     */
    public static Object toBean(Map<String, Object> paramsMap, Class<?> cls) {
        Object t = null;
        try {
            t = cls.newInstance();
            Method[] methods = t.getClass().getDeclaredMethods();
            if (paramsMap != null && methods != null && methods.length > 0) {
                String methodName;
                for (Method m : methods) {
                    methodName = m.getName();
                    if (!methodName.startsWith("set")) {
                        continue;
                    }
                    methodName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                    if (paramsMap.containsKey(methodName)) {
                        m.invoke(t, paramsMap.get(methodName));
                    } else {
                        methodName = methodName.toLowerCase();
                        if (paramsMap.containsKey(methodName)) {
                            m.invoke(t, paramsMap.get(methodName));
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String encodeString(String src, String srcEncoder, String destEncoder) {
        String temp = StringUtils.isEmpty(src) ? "" : src;
        try {
            temp = new String(src.getBytes(srcEncoder), destEncoder);
        } catch (UnsupportedEncodingException e) {
            LoggerFactory.getLogger("encodeString").error(String.valueOf(e));
        }
        return temp;
    }


    /**
     * 根据传入的虚拟路径获取物理路径
     *
     * @param path
     * @return
     */
    public static String getPhysicalPath(HttpServletRequest request, String path) {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        return new StringBuilder(realPath).append(path).toString();
    }

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    public static String getFileExt(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index);
        }
        return "";
    }

    /**
     * 加密密码
     *
     * @param data
     * @return
     */
    public static String encryptPwd(String data) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        return DigestUtils.sha1Hex(data);
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getTimeString() {
        return dateFormat.format(new Date());
    }

    public static String getJsonOfObject(Object object) {
        return JSONObject.fromObject(object).toString();
    }


}
