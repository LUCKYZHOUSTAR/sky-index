package sky.index.comm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lucky.sky.util.lang.EnumValueSupport;
import lucky.sky.util.lang.FaultException;
import lucky.sky.util.lang.StrKit;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import sky.index.comm.util.StrUtil;
import sky.index.comm.util.Utils;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:15 2018/4/28
 */


public class IndexQuery {

  Logger logger = LoggerManager.getLogger(IndexQuery.class);

  /**
   * 排序枚举
   */
  public enum ORDER {
    desc, asc;

    /**
     * 反序
     */
    public ORDER reverse() {
      return (this == asc) ? desc : asc;
    }
  }

  /**
   * solr 关系运算符
   */
  public enum COMMAND {
    /**  */
    IN("IN"),
    /**  */
    LK("LIKE"),
    /**
     * 等于
     */
    EQ("="),
    /**
     * 不等于
     */
    NE("-"),
    /**
     * <=
     **/
    LTE("]"),
    /**
     * >=
     **/
    GTE("["),
    /**
     * <
     **/
    LT("}"),
    /**
     * >
     **/
    GT("{");

    private String command;

    private COMMAND(String command) {
      this.command = command;
    }

    /**
     * 获取枚举值
     */
    public String value() {
      return this.command;
    }
  }

  /**
   * query
   */
  private Map<String, String[]> query;

  /**
   * 过滤条件
   */
  private Map<String, String[]> filterQuery;

  /**
   * 自定义过滤条件(复杂查询使用)
   */
  private String filterQueryExec;

  /**
   * 排序条件
   */
  private Map<String, ORDER> sort;

  private GroupQuery groupQuery;

  /**
   * 脚本查询
   **/
  private String scriptQuery;

  /**
   * ES 集群路由关键字
   */
  private String clusterRouter;

  /**
   * 初始化条件容器
   */
  public IndexQuery() {
    this.query = new LinkedHashMap<>();
    this.filterQuery = new LinkedHashMap<>();
    this.sort = new LinkedHashMap<>();
  }

  public Map<String, String[]> getQuery() {
    return query;
  }

  public Map<String, String[]> getFilterQuery() {
    return filterQuery;
  }

  public String getFilterQueryExec() {
    return filterQueryExec;
  }

  public Map<String, ORDER> getSort() {
    return sort;
  }

  public GroupQuery getGroupQuery() {
    return groupQuery;
  }

  public void setGroupQuery(GroupQuery groupQuery) {
    this.groupQuery = groupQuery;
  }

  public String getScriptQuery() {
    return scriptQuery;
  }

  public void setScriptQuery(String scriptQuery) {
    this.scriptQuery = scriptQuery;
  }

  public String getClusterRouter() {
    return clusterRouter;
  }

  public void setClusterRouter(String clusterRouter) {
    this.clusterRouter = clusterRouter;
  }

  /**
   * 添加查询条件
   */
  public void setQuery(String fieldName, String... fieldValues) {
    if (StrUtil.isEmpty(fieldName, fieldValues)) {
      return;
    }
    this.query.put(fieldName, fieldValues);
  }

  /**
   * 添加查询条件
   *
   * @param fieldName 查询字段名称
   * @param command 匹配类型
   * @param fieldValues 查询字段值
   */
  public IndexQuery addQuery(String fieldName, COMMAND command, Object... fieldValues) {
    if (fieldValues == null || fieldValues.length == 0) {
      throw new FaultException("参数不可为空");
    }
    String[] values = getArrayValueString(true, fieldValues);
    if (command == COMMAND.EQ || command == COMMAND.IN) { //= 或 in
      setFilterQuery(fieldName, values);
    } else if (command == COMMAND.NE) { //不等于
      setFilterQuery(COMMAND.NE.value() + fieldName, values);
    } else if (command == COMMAND.LK) {  //like
      setFilterQuery(fieldName, "*" + values[0] + "*");
    } else if (command == COMMAND.LTE || command == COMMAND.LT) { //<= 或 <
      handleRangeQuery(fieldName, null, null, command, values[0], false);
    } else if (command == COMMAND.GTE || command == COMMAND.GT) { //>= 或 >
      handleRangeQuery(fieldName, command, values[0], null, null, false);
    } else {
      throw new FaultException("addQuery() 该方法不支持范围查询");
    }
    return this;
  }

  /**
   * 过滤0  如果值为0 则不参与搜索
   */
  public IndexQuery addQueryIfNotZero(String fieldName, COMMAND command, Object... fieldValues) {
    Object[] values = filterZero(fieldValues);
    if (values.length > 0) {
      addQuery(fieldName, command, values);
    }
    return this;
  }

  /**
   * 过滤null 和""  如果值为null或"" 则不参与搜索
   */
  public IndexQuery addQueryIfNotNull(String fieldName, COMMAND command, Object... fieldValues) {
    Object[] values = filterNull(fieldValues);
    if (values.length > 0) {
      addQuery(fieldName, command, values);
    }
    return this;
  }

  private Object[] filterZero(Object... fieldValues) {
    List<Object> values = new ArrayList<>();
    for (Object val : fieldValues) {
      if (val == null || val instanceof Number &&
          BigDecimal.valueOf(((Number) val).doubleValue()).compareTo(BigDecimal.valueOf(0)) == 0) {
        continue;
      }
      values.add(val);
    }
    return values.toArray();
  }

  private Object[] filterNull(Object... fieldValues) {
    List<Object> values = new ArrayList<>();
    for (Object val : fieldValues) {
      if (val == null || (val instanceof String && StrUtil.isEmpty(val))) {
        continue;
      }
      values.add(val);
    }
    return values.toArray();
  }

  /**
   * 范围查询
   *
   * @param fieldName 查询字段名称
   * @param startcommand 匹配范围开始类型
   * @param startFieldValue 查询范围开始字段值
   * @param endcommand 匹配范围结束类型
   * @param endFieldValue 查询范围结束字段值
   */
  public IndexQuery addRangeQuery(String fieldName,
      COMMAND startcommand,
      Object startFieldValue,
      COMMAND endcommand,
      Object endFieldValue) {
    handleRangeQuery(fieldName, startcommand, startFieldValue, endcommand, endFieldValue, true);
    return this;
  }


  private void handleRangeQuery(String fieldName,
      COMMAND startcommand,
      Object startFieldValue,
      COMMAND endcommand,
      Object endFieldValue, boolean isEscape) {
    if (startcommand == COMMAND.EQ ||
        startcommand == COMMAND.IN ||
        startcommand == COMMAND.LK ||
        startcommand == COMMAND.NE ||
        endcommand == COMMAND.EQ ||
        endcommand == COMMAND.IN) {
      throw new FaultException("addRangeQuery() 该方法仅支持范围查询");
    }
    String start = startFieldValue == null ? "*" : getValueString(startFieldValue, isEscape);
    String end = endFieldValue == null ? "*" : getValueString(endFieldValue, isEscape);
    if ("*".equals(start) && "*".equals(end)) {
      return;
    }
    COMMAND startRange = startcommand == COMMAND.GTE ? startcommand : COMMAND.GT;
    COMMAND endRange = endcommand == COMMAND.LTE ? endcommand : COMMAND.LT;
    setFilterQuery(fieldName, startRange.value() + start + " TO " + end + endRange.value());
  }

  private String getValueString(Object fieldValue, boolean isEsacpe) {
    if (fieldValue != null &&
        !(fieldValue instanceof String) &&
        !(fieldValue instanceof LocalDateTime) &&
        !(fieldValue instanceof LocalDate) &&
        !(fieldValue instanceof Date) &&
        !(fieldValue instanceof Long) &&
        !(fieldValue instanceof Integer) &&
        !(fieldValue instanceof Short) &&
        !(fieldValue instanceof Float) &&
        !(fieldValue instanceof Double) &&
        !(fieldValue instanceof BigDecimal) &&
        !(fieldValue instanceof Boolean) &&
        !(fieldValue instanceof EnumValueSupport)) {
      throw new FaultException("不支持的数据类型！");
    }

    String valueString;
    if (fieldValue instanceof LocalDateTime) {
      LocalDateTime time = (LocalDateTime) fieldValue;
      valueString = time.toInstant(ZoneOffset.UTC).toString();
    } else if (fieldValue instanceof LocalDate) {
      LocalDate time = (LocalDate) fieldValue;
      valueString = time.atStartOfDay().toInstant(ZoneOffset.UTC).toString();
    } else if (fieldValue instanceof Date) {
      Date time = (Date) fieldValue;
      valueString = time.toInstant().toString();
    } else if (fieldValue == null || "".equals(fieldValue)) {
      valueString = " * ";
    } else if (fieldValue instanceof String) {
      if (isEsacpe) {
        valueString = Utils.escapeQueryChars((String) fieldValue);
      } else {
        valueString = (String) fieldValue;
      }
    } else if (fieldValue instanceof EnumValueSupport) {
      EnumValueSupport val = (EnumValueSupport) fieldValue;
      valueString = String.valueOf(val.value());
    } else {
      valueString = fieldValue.toString();
    }

    return valueString;
  }

  private String[] getArrayValueString(boolean isEscape, Object... fieldValues) {
    String[] values = new String[fieldValues.length];
    for (int i = 0; i < fieldValues.length; i++) {
      values[i] = getValueString(fieldValues[i], isEscape);
    }
    return values;
  }

  /**
   * @param fieldName
   * @param fieldValues
   */
  public void setFilterQuery(String fieldName, String... fieldValues) {
    if (StrUtil.isEmpty(fieldName, fieldValues)) {
      return;
    }
    this.filterQuery.put(fieldName, fieldValues);
  }

  public void setFilterQueryExec(String query) {
    if (StrUtil.isEmpty(query)) {
      return;
    }
    if (this.filterQueryExec == null || "".equals(this.filterQueryExec)) {
      this.filterQueryExec = " ( " + query + " ) ";
    } else {
      this.filterQueryExec += " AND ( " + query + " ) ";
    }
  }

  /**
   * 复杂搜索执行语句
   */
  public IndexQuery addQueryExec(String query) {
    this.setFilterQueryExec(query);
    return this;
  }

  /**
   * 添加排序条件
   */
  public void setSort(String fieldName, ORDER order) {
    if (StrUtil.isEmpty(fieldName, order)) {
      return;
    }
    this.sort.put(fieldName, order);
  }

  /**
   * 排序
   */
  public IndexQuery addSort(String fieldName, ORDER order) {
    this.setSort(fieldName, order);
    return this;
  }

  /**
   * 添加排序字符串
   *
   * @param sortStr 如 name,-age,description 首先以name 正序排序，如果name 相同， 再以age倒序排序 ，
   * 如果age相同，再以description 正序排序
   * @return this
   */
  public IndexQuery addSorts(String sortStr) {
    if (StrKit.isBlank(sortStr)) {
      return this;
    }
    logger.info("搜索中排序的字段为: {}", sortStr);
    String[] fields = sortStr.split(",");
    for (String field : fields) {
      if (StrKit.isBlank(field)) {
        continue;
      }
      if (field.startsWith("-")) {
        this.addSort(field.substring(1), ORDER.desc);
      } else {
        this.addSort(field, ORDER.asc);
      }
    }
    return this;
  }

}

