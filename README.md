# ElasticSearch 搜索服务

### maven 坐标
```
<dependency>
      <artifactId>index-client</artifactId>
      <groupId>sky.index</groupId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

### 服务名称
```
cmc.index.admin.search.service
```

### 端口号
```
9601
```

### 使用方式

> 不需要单独定义smodel类，也不需要再添加什么配置 ，省去了比较繁琐的类型转换和每建一个新类都要添加配置文件。但是传进来的类必须要有主键。
默认会把字段名为id的作为主键，如果自己的主键字段名不是id，则要在主键上添加注解@SearchId

```

public class User {

    private long id;
    
    pirvate String username;
    
    
    ```
    省略setter  getter
    ```
}

或者

public class User {

    @SearchId
    private long userid;
    
    pirvate String username;
    
    
    ```
    省略setter  getter
    ```
}

```

### 支持的数据类型

```

public class User{
    
    private int intField;
    
    private String StringField;
    
    private long longField;
    
    private float floatField;
    
    private double doubleField;
    
    private boolean booleanField;
    
    private LocalDateTime LocalDateTimeField;
    
    private LocalDate LocalDateField;
    
   

}

```


###  写接口IndexerClient使用说明(全部为静态方法)

> 方法列表中 方法名以Asy结尾的都是异步方法，有一定延迟，如果操作实时性要求不是很高，建议使用异步方法，可以提高你接口的性能。不以Asy结尾的方法都是同步方法。

```

// 添加索引
createIndex(T obj)

//添加索引列表
createIndexList(List<T> objs)

//更新索引
updateIndex(T obj,Set<String> needNullFields)

//更新索引
updateIndex(T obj)

//更新索引列表
updateIndexList(List<T> objs)

//删除索引
deleteIndex(Class<T> tclass, Object uniqueKeyValue)

//删除索引列表
deleteIndexList(Class<T> tclass, List<Object> uniqueKeyValues)

// 添加索引
createIndexAsy(T obj)

//添加索引列表
createIndexListAsy(List<T> objs)

//更新索引
updateIndexAsy(T obj,Set<String> needNullFields)

//更新索引
updateIndexAsy(T obj)

//更新索引列表
updateIndexListAsy(List<T> objs)

//删除索引
deleteIndexAsy(Class<T> tclass, Object uniqueKeyValue)

//删除索引列表
deleteIndexListAsy(Class<T> tclass, List<Object> uniqueKeyValues)

```

### 查询接口SearcherClient和SearcherLogClient使用说明

> SearcherClient 是针对cmc 后台业务的客户端 SearcherLogClient 是针对后台日志的客户端，大家要对号入座，不要注入错误了

```
//使用注入的方式 用spring来管理
@Autowired
SearcherClient searcherClient

@Autowired
SearcherLogClient searcherLogClient


/**
* 分页检索
* @param typeRef
* @param tClass
* @param indexQuery
* @param pageIndex
* @param pageSize
* @return
*/
search(TypeRef<List<T>> typeRef, Class<T> tClass,IndexQuery indexQuery, int pageIndex, int pageSize)


/**
* 全量检索
* @param typeRef
* @param tClass
* @param indexQuery
* @param action
* @param pageSize 控制在1000以内
* @return
*/
public <T> void scrollSearch(TypeRef<List<T>> typeRef, Class<T> tClass,IndexQuery indexQuery, int pageSize , Consumer<SearchResult<T>> action)



/**
 * 分页或者全量
 * 该方法为 需要查询全量，但数据量又不是特别大的情况下使用。
 * 如果数据量比较大， 建议使用scrollSearch 接口。
 *
 * 当pageInfo 为null 时，查全量，其他情况分页查询
 *
 * @param typeRef 反序列化的类型
 * @param tClass  查询es的 model类
 * @param indexQuery  查询的条件
 * @param pageInfo  分页信息
 * @return 返回结果
 */
public <T> SearchResult<T> search(TypeRef<List<T>> typeRef, Class<T> tClass,IndexQuery indexQuery , PageInfoSupport pageInfo)

```

> 特别需要注意的是，如果需要查询的数据量很大，总条数在10000条以上，就需要使用scrollSearch方法，千万不要使用search分页的方法。

##### scrollSearch方法使用方式

>特别注意 在使用scrollSearch方法时，**尽量不要使用排序功能，除非必须使用** 。 pageSize的大小控制在**1000**以内。
> 处理pageSize条数据消耗的时间 要控制在1分钟以内。如果你的程序处理需要时间较长，则要把pageSize 设置的相对较小一些。

```

@Test
public void scrollSearch() {
    IndexQuery indexQuery = new IndexQuery();
    indexQuery.addQuery("name", IndexQuery.COMMAND.EQ, "lisi");
    indexQuery.addRangeQuery("age", IndexQuery.COMMAND.GT,10, IndexQuery.COMMAND.LTE,12);
    indexQuery.setFilterQuery("userId", "123456");
    indexQuery.addRangeQuery("birthday", IndexQuery.COMMAND.GT,"2016-10-24T17:55:43", IndexQuery.COMMAND.LTE,"2016-10-24T17:57:23");
    indexQuery.addQuery("list", IndexQuery.COMMAND.IN,"1");
    searcherClient.scrollSearch(new TypeRef<List<Member>>() {}, Member.class, indexQuery, 900 ,result ->{
        System.out.println("=---------------------------------------=");
        System.out.println(FastJsonUtil.bean2Json(result));
        
        //处理你的业务逻辑(处理这900条数据，要控制在1分钟以内)
        ......
        ......
        ......
        ......
        return true; //return false 后不再返回数据，return true 后继续返回数据
        
    });
}

@Test
public void scrollSearch() {
    IndexQuery indexQuery = new IndexQuery();
    indexQuery.addQuery("name", IndexQuery.COMMAND.EQ, "lisi");
    indexQuery.addRangeQuery("age", IndexQuery.COMMAND.GT,10, IndexQuery.COMMAND.LTE,12);
    indexQuery.setFilterQuery("userId", "123456");
    indexQuery.addRangeQuery("birthday", IndexQuery.COMMAND.GT,"2016-10-24T17:55:43", IndexQuery.COMMAND.LTE,"2016-10-24T17:57:23");
    indexQuery.addQuery("list", IndexQuery.COMMAND.IN,"1");
    searcherClient.scrollSearch(new TypeRef<List<Member>>() {}, Member.class, indexQuery, 900 ,(result,scrollId) ->{
        System.out.println("ScrollId:"+scrollId);
        System.out.println("=---------------------------------------=");
        System.out.println(FastJsonUtil.bean2Json(result));
        
        //处理你的业务逻辑(处理这900条数据，要控制在1分钟以内)
        ......
        ......
        ......
        ......
        return true; //return false 后不再返回数据，return true 后继续返回数据
        
    });
}

```

### 分组(group) 、 聚合(facet) 的使用方式

> 分组(group) : 根据某个字段进行分组 来求 MAX 、MIN 、COUNT、AVG、SUM 。

> 聚合(facet) : 根据某个字段进行分组 来求 {key:"key" ,count:count}

> search 方法和scrollSearch方法都支持聚合、分组查询，聚合、分组的结果是在查询条件的基础上执行的，
在使用scrollSearch方法既查询全量的数据，又想要聚合结果时，注意判空，因为scroll是分批次查出全量结果，
但只在第一批次中返回聚合结果。例如以下实例:

```
/**
 * 测试聚合  分组
 */
@Test
public void searchAggs(){
    IndexQuery indexQuery = new IndexQuery();
    GroupQuery group = new GroupQuery();
    group.addAggField("age");
    group.addFuncAgg("age",GroupQuery.Func.AVG);
    group.addFuncAgg("age",GroupQuery.Func.COUNT);
    group.addFuncAgg("age",GroupQuery.Func.MAX);
    group.addFuncAgg("age",GroupQuery.Func.MIN);
    group.addFuncAgg("age",GroupQuery.Func.SUM);
    indexQuery.setGroupQuery(group);
    SearchResult<Member> result = searcherClient.search(new TypeRef<List<Member>>() {
    }, Member.class, indexQuery, 1, 30);
    Map<String, Map<Object, Long>> facetResult = result.getFacetResult();
    Map<String, Map<GroupQuery.Func, Object>> groupResult = result.getGroupResult();
    for (Map.Entry<String, Map<Object, Long>> entry : facetResult.entrySet()) {
        System.out.println("field: "+entry.getKey());
        Map<Object, Long> value = entry.getValue();
        for (Map.Entry<Object, Long> map : value.entrySet()) {
            System.out.println("key : "+map.getKey() + "  count: "+map.getValue());
        }
    }
    for (Map.Entry<String, Map<GroupQuery.Func, Object>> entry : groupResult.entrySet()) {
        System.out.println("field: "+entry.getKey());
        Map<GroupQuery.Func, Object> value = entry.getValue();
        for (Map.Entry<GroupQuery.Func, Object> map : value.entrySet()) {
            System.out.println(map.getKey().name() + "  :  "+map.getValue());
        }
    }
}

打印结果为:
field: age
key : 1  count: 1
key : 2  count: 2
key : 3  count: 1
key : 5  count: 1
field: ageCOUNT
COUNT  :  5
field: ageAVG
AVG  :  2.6
field: ageMIN
MIN  :  1
field: ageMAX
MAX  :  5
field: ageSUM
SUM  :  13



/**
 * 全量检索测试聚合  分组
 * 该方法是全量检索，分批次查出全部数据，但是只会在第一批次返回聚合、分组结果，在使用过程中注意 **判空**
 */
@Test
public void scrollSearchAggs(){
    IndexQuery indexQuery = new IndexQuery();
    GroupQuery group = new GroupQuery();
    group.addAggField("age");
    group.addFuncAgg("age",GroupQuery.Func.AVG);
    group.addFuncAgg("age",GroupQuery.Func.COUNT);
    group.addFuncAgg("age",GroupQuery.Func.MAX);
    group.addFuncAgg("age",GroupQuery.Func.MIN);
    group.addFuncAgg("age",GroupQuery.Func.SUM);
    indexQuery.setGroupQuery(group);
    searcherClient.scrollSearch(new TypeRef<List<Member>>() {
    }, Member.class, indexQuery, 1, res->{
        Map<String, Map<Object, Long>> facetResult = res.getFacetResult();
        Map<String, Map<GroupQuery.Func, Object>> groupResult = res.getGroupResult();
        if (facetResult !=null && !facetResult.isEmpty()){
            for (Map.Entry<String, Map<Object, Long>> entry : facetResult.entrySet()) {
                System.out.println("field: "+entry.getKey());
                Map<Object, Long> value = entry.getValue();
                for (Map.Entry<Object, Long> map : value.entrySet()) {
                    System.out.println("key : "+map.getKey() + "  count: "+map.getValue());
                }
            }
        }
        if (groupResult != null && !groupResult.isEmpty()){
            for (Map.Entry<String, Map<GroupQuery.Func, Object>> entry : groupResult.entrySet()) {
                System.out.println("field: "+entry.getKey());
                Map<GroupQuery.Func, Object> value = entry.getValue();
                for (Map.Entry<GroupQuery.Func, Object> map : value.entrySet()) {
                    System.out.println(map.getKey().name() + "  :  "+map.getValue());
                }
            }
        }
        return true; //return false 后不再返回数据，return true 后继续返回数据
    });
}

打印结果为:
field: age
key : 1  count: 1
key : 2  count: 2
key : 3  count: 1
key : 5  count: 1
field: ageCOUNT
COUNT  :  5
field: ageAVG
AVG  :  2.6
field: ageMIN
MIN  :  1
field: ageMAX
MAX  :  5
field: ageSUM
SUM  :  13


```




### 查看数据说明

### 删除数据说明

* 访问地址


* 删除整个索引

```
DELETE indexname
```

* 删除单个索引，根据id删除

```
DELETE indexname/indexname/id

eg：删除索引名为member的，id为1的这条数据

DELETE member/member/1
```

### 添加索引

> 在第一次使用时，如果没有索引进行查询，会抛出 no such index的Exception,需要新建索引。

* 访问地址

```

```
添加索引
```
PUT indexname
```

