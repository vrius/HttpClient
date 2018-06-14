package com.discovery.httpclient.http;


/**
 * 响应缓存接口
 * @author ruanwenjiang
 * CacheDao 可作为后期拓展数据库缓存用
 */
public interface ISqlite {
      /**
       * 保存至数据库
       * @return
       */
      boolean saveToDB(String body, CommonRequest request);

      /**
       * 从数据库获取数据
       * @return
       */
      String getByDB(CommonRequest request);
}

