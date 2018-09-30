package sky.index.service.contract.dto;


import lombok.Getter;
import lombok.Setter;
import lucky.sky.util.data.PageInfo;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:27 2018/4/28
 */
public class SearchDto {
  private static final String OBJECT_TYPE = "objectType: ";
  private static final String KEYWORD = "keyword: ";
  private static final String ITEMS = "items: ";
  private static final String FACETS = "facets: ";
  private static final String GROUPS = "groups: ";
  private static final String TOTAL_COUNT = "totalCount: ";
  private static final String SCROLL_ID = "scrollId: ";

  private SearchDto() {

  }

  /**
   * Search 请求参数
   */
  @Setter
  @Getter
  public static class SearchRequest {
    /**
     * 搜索对象类型
     */
    private String objectType;
    /**
     * 搜索关键词
     */
    private String keyword;
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +=OBJECT_TYPE + this.objectType + "\n";
      s +=KEYWORD + this.keyword + "\n";
      s +="pageInfo: " + this.pageInfo + "\n";
      return s;

    }
  }


  /**
   * Search 响应结果
   */
  @Setter
  @Getter
  public static class SearchResponse {
    /**
     * 搜索结果，以 JSON 数组表示
     */
    private String items;
    /**
     * 聚合结果，以 JSON 数组表示
     */
    private String facets;
    /**
     * 分组结果，以 JSON 数组表示
     */
    private String groups;
    /**
     * 总记录数
     */
    private int totalCount;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +=ITEMS + this.items + "\n";
      s +=FACETS + this.facets + "\n";
      s +=GROUPS + this.groups + "\n";
      s +=TOTAL_COUNT + this.totalCount + "\n";
      return s;

    }
  }


  /**
   * ScrollSearch 请求参数
   */
  @Setter
  @Getter
  public static class ScrollSearchRequest {
    /**
     * 搜索对象类型
     */
    private String objectType;
    /**
     * 搜索关键词
     */
    private String keyword;
    /**
     * pageSize
     */
    private int pageSize;
    /**
     * pageSize
     */
    private String scrollId;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +=OBJECT_TYPE + this.objectType + "\n";
      s +=KEYWORD + this.keyword + "\n";
      s +="pageSize: " + this.pageSize + "\n";
      s +=SCROLL_ID + this.scrollId + "\n";
      return s;

    }
  }


  /**
   * ScrollSearch 响应结果
   */
  @Setter
  @Getter
  public static class ScrollSearchResponse {
    /**
     * 搜索结果，以 JSON 数组表示
     */
    private String items;
    /**
     * 聚合结果，以 JSON 数组表示
     */
    private String facets;
    /**
     * 分组结果，以 JSON 数组表示
     */
    private String groups;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * pageSize
     */
    private String scrollId;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +=ITEMS + this.items + "\n";
      s +=FACETS + this.facets + "\n";
      s +=GROUPS + this.groups + "\n";
      s +=TOTAL_COUNT + this.totalCount + "\n";
      s +=SCROLL_ID + this.scrollId + "\n";
      return s;

    }
  }


  /**
   * Index 请求参数
   */
  @Setter
  @Getter
  public static class IndexRequest {
    /**
     * 建立索引对象串
     */
    private String indexMsgJson;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +="indexMsgJson: " + this.indexMsgJson + "\n";
      return s;

    }
  }


  /**
   * CloseScroll 请求参数
   */
  @Setter
  @Getter
  public static class CloseScrollRequest {
    /**
     * 释放指定scroll
     */
    private String scrollId;
    /**
     * 指定集群
     */
    private String clusterRouter;

    @Override
    public String toString() {
      String s = getClass().getSimpleName() + ":\n";
      s +=SCROLL_ID + this.scrollId + "\n";
      s +="clusterRouter: " + this.clusterRouter + "\n";
      return s;

    }
  }

}
