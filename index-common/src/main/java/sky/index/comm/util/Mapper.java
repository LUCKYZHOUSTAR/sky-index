package sky.index.comm.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lucky.sky.util.lang.FaultException;
import lucky.sky.util.lang.StrKit;
import sky.index.comm.enums.Analyzed;
import sky.index.comm.enums.ESDataType;
import sky.index.comm.enums.Store;
import sky.index.comm.esannotation.ESColumn;
import sky.index.comm.esannotation.ESIndex;
import sky.index.comm.esannotation.ESType;
import sky.index.comm.esannotation.SearchId;
import sky.index.comm.esmeta.ElasticIndex;
import sky.index.comm.esmeta.FieldProperties;
import sky.index.comm.esmeta.MappingProperties;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:00 2018/5/2
 */
public class Mapper {

  private static Map<String,EntityInfo> infos = new ConcurrentHashMap<>();
  private static Map<String,String> infoJson = new ConcurrentHashMap<>();


  /**
   * 获取java类对应的 ES Mapping
   *
   * @param clazz java 类的class
   * @return ES 索引映射信息
   */
  public static EntityInfo getEntityInfo(Class<?> clazz) {
    String key = clazz.getTypeName();
    EntityInfo info = infos.get(key);
    if (info != null) return info;
    info = initEntityInfo(clazz);
    infos.put(key,info);
    return info;
  }

  /**
   * 获取java类对应的 ES Mapping json串
   *
   * @param clazz java 类的class
   * @return ES 索引映射信息 json
   */
  public static String getEntityMappingJson(Class<?> clazz){
    String key = clazz.getTypeName();
    String mapping = infoJson.get(key);
    if (!StrUtil.isEmpty(mapping)) return mapping;
    EntityInfo info = getEntityInfo(clazz);
    mapping = Utils.toJson(info);
    infoJson.put(key,mapping);
    return mapping;
  }




  private static EntityInfo initEntityInfo(Class<?> clazz){
    Map<String, Object> properties = getProperties(clazz,null,null);
    ElasticIndex index = getIndex(clazz);
    String idColumn = getIdColumn(clazz,null);
    return new EntityInfo(properties,index,idColumn);
  }


  private static Map<String,Object> getProperties(Class<?> clazz,Class<?> subClazz,Map<String, Object> prop){
    Field[] fields = clazz.getDeclaredFields();
    if (prop == null){
      prop = new HashMap<>();
    }
    for (Field f : fields) {


      String javaType = f.getType().getTypeName();
      //判断是不是泛型类
      if(f.getType().equals(Object.class)){
        Type type = subClazz.getGenericSuperclass();
        if (type instanceof ParameterizedType){
          Type trueType = ((ParameterizedType)type).getActualTypeArguments()[0];
          javaType = trueType.getTypeName();
        }
      }
      //判断是不是数组
      Class<?> componentType = f.getType().getComponentType();
      if (componentType != null){
        javaType =componentType.getTypeName();
      }
      //判断是不是集合
      Class<?> tt = f.getType();
      if (tt.isAssignableFrom(List.class) && !tt.equals(Object.class)){
        Type t = f.getGenericType();
        Type actrualType = ((ParameterizedType) t).getActualTypeArguments()[0];
        if (isPrimetive( (Class<?>) actrualType)){
          javaType = actrualType.getTypeName();
        }else {
          throw new FaultException("不支持的数据类型: "+((Class<?>) actrualType).getName());
//                    Map<String,Object> obj = getSub((Class<?>)actrualType);
//                    prop.put(f.getName(),obj);
//                    continue;
        }
      }

      //判断是不是Set集合
      if (tt.isAssignableFrom(Set.class) && !tt.equals(Object.class)){
        Type t = f.getGenericType();
        Type actrualType = ((ParameterizedType) t).getActualTypeArguments()[0];
        if (isPrimetive( (Class<?>) actrualType)){
          javaType = actrualType.getTypeName();
        }else {
          throw new FaultException("不支持的数据类型: "+ ((Class<?>) actrualType).getName());
//                    Map<String,Object> obj = getSub((Class<?>)actrualType);
//                    prop.put(f.getName(),obj);
//                    continue;
        }
      }

      //判断是不是自定义类
      if (!isPrimetive(f.getType())){
        throw new FaultException("不支持的数据类型: "+f.getType().getName());
//                Map<String,Object> obj = getSub(f.getType());
//                prop.put(f.getName(),obj);
//                continue;
      }
      FieldProperties fprop = new FieldProperties();
      ESColumn column = f.getAnnotation(ESColumn.class);
      if (column != null) {
        fprop.setStore(column.store().equals(Store.STORE));
        if ("java.lang.String".equals(f.getType().getTypeName())) {
          fprop.setIndex(column.analyzed().getName());
        }else {
          fprop.setIndex(Analyzed.NOT_ANALYZED.name());
        }
      } else {
        fprop.setStore(true);
        fprop.setIndex(Analyzed.NOT_ANALYZED.getName());
      }
      fprop.setType(ESDataType.getEsType(javaType));
      prop.put(f.getName(),fprop);
    }
    //判断有没有父类
    if (clazz.getSuperclass() != Object.class) {
      return getProperties(clazz.getSuperclass(),clazz,prop);
    }
    return prop;
  }


  private static Map<String,Object> getSub(Class<?> clazz){
    Map<String,Object> obj = new HashMap<>();
    Map<String, Object> subProp = getProperties(clazz,null,null);
    obj.put("type", ESDataType.NESTED.getEsType());
    obj.put("properties",subProp);
    return obj;
  }

  private static String getIdColumn(Class<?> clazz ,Boolean isId) {
    if (isId == null){
      isId = false;
    }
    //获取索引主键字段
    Field[] fields = clazz.getDeclaredFields();
    for(Field f : fields){
      if (f.getAnnotation(SearchId.class)!=null){
        return f.getName();
      }
      if (f.getName().equalsIgnoreCase("id")){
        isId = true;
      }
    }
    if (clazz.getSuperclass() != Object.class) {
      return getIdColumn(clazz.getSuperclass(),isId);
    }
    if (isId){
      return "id";
    }
    throw new FaultException("没有找到主键");
  }


  /**
   * 判断当前类是不是java基础类
   */
  private static boolean isPrimetive(Class<?> clazz){
    return clazz.getClassLoader() == null;
  }

  /**
   * 获取索引的名称 和 类型
   */
  private static ElasticIndex getIndex(Class<?> clazz){
    ElasticIndex index = new ElasticIndex();
    //获取索引名
    ESIndex indexName = clazz.getAnnotation(ESIndex.class);
    if (indexName == null || StrKit.isBlank(indexName.value())){
      index.setIndexName(clazz.getSimpleName().toLowerCase());
    }else{
      index.setIndexName(indexName.value().toLowerCase());
    }
    //获取索引type
    ESType type = clazz.getAnnotation(ESType.class);
    if (type == null || StrKit.isBlank(type.value())){
      index.setIndexType(index.getIndexName());
    }else {
      index.setIndexType(type.value().toLowerCase());
    }
    return index;
  }

  @Getter
  @Setter
  public static class EntityInfo {

    public EntityInfo(){}

    /** es mapping **/
    MappingProperties mappings;

    /** id列 **/
    String idColumn;

    /** 索引的基本信息 **/
    ElasticIndex index;

    private EntityInfo(Map<String, Object> properties,ElasticIndex index, String idColumn){
      this.mappings = new MappingProperties(properties);
      this.index = index;
      this.idColumn = idColumn;
    }
  }
}
