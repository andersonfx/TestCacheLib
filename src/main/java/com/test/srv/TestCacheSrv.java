/**
 * 
 */
package com.test.srv;

import com.fox.platform.cache.dom.ent.Cacheable;
import com.fox.platform.cache.dom.ent.Key;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author andersonv
 *
 */
public class TestCacheSrv 
{
  private static final Logger logger = LoggerFactory.getLogger(TestCacheSrv.class);
  Vertx vertx;
  
  /**
   * 
   */
  public TestCacheSrv(Vertx vertx)
  {
    this.vertx = vertx;
  }
  
  
  @Cacheable             
  public Future<JsonObject> getShows(Future<JsonObject> future, @Key String name) 
  {
    try
    {
      JsonObject jsonResponse = new JsonObject().put("key1", name); 
      future.complete(jsonResponse);                    
   
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      future.fail(ex.getMessage());                                         
    }
    return future;                                                          
  }
   
  public void getShows(Handler<AsyncResult<JsonObject>> handler) {
    vertx.executeBlocking(future -> getShows(future, "Anderson"), handler);
  }  
  
}
