/**
 * 
 */
package com.test.testcache.dep;

import com.fox.platform.cache.dom.ent.Cacheable;
import com.fox.platform.cache.infra.aop.CacheSearch;
import com.fox.platform.cache.infra.conf.CacheConfig;
import com.fox.platform.cache.infra.dep.AbstractModuleHelper;
import com.google.inject.matcher.Matchers;
import com.test.srv.TestCacheSrv;
import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author andersonv
 *
 */
public class TestDependency extends AbstractModuleHelper
{

  private TestCacheSrv testCacheSrv;
  /**
   * @param vertx
   * @param options
   */
  public TestDependency(Vertx vertx, CacheConfig cacheconfig)
  {
    
    super(vertx, cacheconfig);
    testCacheSrv = new TestCacheSrv(vertx);
    
  }
 
  @Override
  protected void configure()
  {
    super.configure();

    
    bind(TestCacheSrv.class).toInstance(testCacheSrv);
    
  }

}
