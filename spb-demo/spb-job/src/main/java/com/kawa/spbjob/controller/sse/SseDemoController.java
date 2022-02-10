package com.kawa.spbjob.controller.sse;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * SSE 服务端向客户端单工通信，一般与前端EventSource结合使用
 * WebSocket 全双工通信
 * 应用场景选型可以参考这篇博客：https://zhuanlan.zhihu.com/p/31297574
 */
@RestController
public class SseDemoController {

    @GetMapping(value = "sse", produces = "text/event-stream")
    public Flux<Long> sse() {
        return Flux.interval(Duration.ofSeconds(1));
    }
}
