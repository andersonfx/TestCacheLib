/**
 * 
 */
package com.test.api;

import java.time.LocalDateTime;
import com.fox.platform.cache.infra.conf.CacheConfig;
import com.fox.platform.lib.vrt.AbstractConfigurationVerticle;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.test.srv.TestCacheSrv;
import com.test.testcache.dep.TestDependency;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author andersonv
 *
 */
public class CustomerDetailClient extends AbstractConfigurationVerticle
{
  private static final Logger logger = LoggerFactory.getLogger(TestCacheSrv.class);

  @Inject
  TestCacheSrv serviceCache;
  @Override
  public void start(Future<Void> startFuture) throws Exception
  {
    Future<Void> superFuture = Future.future();
    super.start(superFuture);
    JsonObject jsonConfig = config();
    CacheConfig  cacheConfig = new CacheConfig();
    RedisOptions redisOptions = new RedisOptions(jsonConfig.getJsonObject("cacheConfig").getJsonObject("options"));
    
    cacheConfig.setOptions(redisOptions);
    cacheConfig.setSecondsExpire(jsonConfig.getJsonObject("cacheConfig").getInteger("secondsExpire") );
    TestDependency testDependency = new TestDependency(vertx, cacheConfig);
    Guice.createInjector(testDependency).injectMembers(this);
    
    superFuture.setHandler(handler -> {
      if (handler.succeeded()) {
        Router router = Router.router(vertx);
        router.route(HttpMethod.GET, "/service1").handler(this::handleService1);
        
        vertx.createHttpServer().requestHandler(router::accept)
        .listen(8085, result -> {
          if (result.succeeded()) {
            logger.info("Service : http://localhost:8085/service1");
            startFuture.complete();
          } else {
              startFuture.fail(result.cause());
          }
          
        });
        
     }else
     {
       startFuture.fail(handler.cause());
     }
    });
  }
  
  private void handleService1( RoutingContext context)
  {
    //logger.info(cacheMessage.getRegards());
    logger.info("Handling Service 1 +++");
    JsonObject result = new JsonObject();
    result.put("app", "BaselineVertx");
    result.put("version", "0.0.1");
    result.put("now", LocalDateTime.now().toString());
    
    HttpServerResponse response = context.response();
    
    serviceCache.getShows( name -> {
      logger.info(" result = "+name.result().encode()
      );
      response
      .setStatusCode(200) 
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(result.encode());
    });
    

  }
}
