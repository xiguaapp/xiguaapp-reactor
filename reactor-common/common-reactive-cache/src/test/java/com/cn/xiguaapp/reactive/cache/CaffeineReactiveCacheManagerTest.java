package com.cn.xiguaapp.reactive.cache;

import com.cn.xiguaapp.reactive.cache.support.CaffeineReactiveCacheManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest(classes = TestApplication.class,args = {
        "--xiguaapp.cache.type=caffeine"
})
@RunWith(SpringRunner.class)
@DirtiesContext
public class CaffeineReactiveCacheManagerTest {

    @Autowired
    ReactiveCacheManager cacheManager;

    @Test
    public void test(){
        Assert.assertNotNull(cacheManager);
        Assert.assertTrue(cacheManager instanceof CaffeineReactiveCacheManager);

        ReactiveCache<String> cache= cacheManager.getCache("test");
        cache.clear()
                .as(StepVerifier::create)
                .verifyComplete();

        cache.flux("test-flux")
                .onCacheMissResume(Flux.just("1","2","3"))
                .as(StepVerifier::create)
                .expectNext("1","2","3")
                .verifyComplete();

        cache.put("test-flux",Flux.just("3","2","1"))
                .as(StepVerifier::create)
                .verifyComplete();

        cache.getFlux("test-flux")
                .as(StepVerifier::create)
                .expectNext("3","2","1")
                .verifyComplete();


        cache.mono("test-mono")
                .onCacheMissResume(Mono.just("1"))
                .as(StepVerifier::create)
                .expectNext("1")
                .verifyComplete();

        cache.put("test-mono",Mono.just("2"))
                .as(StepVerifier::create)
                .verifyComplete();

        cache.getMono("test-mono")
                .as(StepVerifier::create)
                .expectNext("2")
                .verifyComplete();


    }
}