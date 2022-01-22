package com.kawa.reactor3;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


/**
 * 1.flatMap并不像map操作那样简单地将一个对象转换到另一个对象，而是将对象转换为新的Mono或Flux。
 * 结果形成的Mono或Flux会扁平化为新的Flux。当与subscribeOn()方法结合使用时，flatMap操作可以释放Reactor反应式的异步能力。
 */
public class FluxTest {

    @Test
    public void Flux_Test_Buffer() {

        Flux.just("A", "b", "c", "d", "e", "f", "g", "h").buffer(3).flatMap(val ->
                Flux.fromIterable(val).map(x -> x.toLowerCase())
                        .subscribeOn(Schedulers.parallel())
                        .log()
        ).subscribe();
    }
}
