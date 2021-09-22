#### java多线程

##### 1.并发包
###### 1.1 CountDownLatch(计数器)
```
CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务。
```
###### 1.2 CyclicBarrier(循环屏障)
```
CyclicBarrier初始化时规定一个数目，然后计算调用了CyclicBarrier.await()进入等待的线程数。当线程数达到了这个数目时，所有进入等待状态的线程被唤醒并继续。 CyclicBarrier就象它名字的意思一样，可看成是个障碍， 所有的线程必须到齐后才能一起通过这个障碍。
```
###### 1.3 Semaphore(信号量)
```
Semaphore是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信号，做自己的申请后归还，超过阈值后，线程申请许可信号将会被阻塞。Semaphore可以用来构建一些对象池，资源池之类的，比如数据库连接池，我们也可以创建计数为1的Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态。它的用法如下：
availablePermits(); // 获取当前可用的资源数量
wc.acquire(); //申请资源
wc.release();// 释放资源
```

##### 2 队列
###### 2.1 ArrayDeque(双端队列)
```
ArrayDeque是JDK容器中的一个双端队列实现，内部使用数组进行元素存储，不允许存储null值，可以高效的进行元素查找和尾部插入取出，
是用作队列、双端队列、栈的绝佳选择，性能比LinkedList还要好
```
###### 2.2 PriorityQueue(优先队列)
```
PriorityQueue是基于优先级堆的无界优先级队列。他们的元素可按自然排序，也可在创建ProorityQueue实例时指定比较器。
不能添加null对象，也不能添加不可比对象，这样会抛出ClassCastException异常。
```
###### 2.3 DelayQueue(延迟队列)
```
DelayQueue 是一个无界阻塞队列，要添加进去的元素必须实现Delayed接口，只有在延迟期满时才能从中提取元素。 该队列的头部
是延迟期满后保存时间最长的 Delayed 元素。 如果延迟都还没有期满，则队列没有头部，并且 poll 将返回 null。 当一个元素的
getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于等于 0 的值时，表示该元素到期了。 无法使用 take 或 poll
移除未到期的元素，也不会将这些元素作为正常元素对待。例如，size 方法同时返回到期和未到期元素的计数。 此队列不允许使用 null 元素。
```
###### 2.4 ConcurrentLinkedQueue
```
一个基于链接节点的无界线程安全队列。此队列按照 FIFO（先进先出）原则对元素进行排序。队列的头部 是队列中时间最长的元素。
队列的尾部 是队列中时间最短的元素。新的元素插入到队列的尾部，队列获取操作从队列头部获得元素。当多个线程共享访问一个公共 collection 时
，ConcurrentLinkedQueue 是一个恰当的选择。此队列不允许使用 null 元素。
```
###### 2.5 ArrayBlockingQueue
```
ArrayBlockingQueue是一个有边界的阻塞队列，它的内部实现是一个数组。有边界的意思是它的容量是有限的，我们必须在其初始化的时候指定它的容量大小，容量大小一旦指定就不可改变。
ArrayBlockingQueue是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。
```
###### 2.6 LinkedBlockingQueue
```
LinkedBlockingQueue阻塞队列大小的配置是可选的，如果我们初始化时指定一个大小，它就是有边界的，如果不指定，它就是无边界的。说是无边界，其实是采用了默认大小为Integer.MAX_VALUE的容量 。它的内部实现是一个链表。和ArrayBlockingQueue一样，LinkedBlockingQueue 也是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。
```
###### 2.7 PriorityBlockingQueue
```
PriorityBlockingQueue是一个没有边界的队列，它的排序规则和 java.util.PriorityQueue一样。需要注意，PriorityBlockingQueue中允许插入null对象。所有插入PriorityBlockingQueue的对象必须实现 java.lang.Comparable接口，队列优先级的排序规则就是按照我们对这个接口的实现来定义的。另外，我们可以从PriorityBlockingQueue获得一个迭代器Iterator，但这个迭代器并不保证按照优先级顺序进行迭代。
```
###### 2.8 SynchronousQueue
```
SynchronousQueue队列内部仅允许容纳一个元素。当一个线程插入一个元素后会被阻塞，除非这个元素被另一个线程消费。
```

##### 3. 线程池
###### 3.1 ThreadPoolExecutor自定义线程池
```
corePoolSize:指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去；
maximumPoolSize:指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
keepAliveTime:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
unit:keepAliveTime的单位
workQueue:任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
threadFactory:线程工厂，用于创建线程，一般用默认即可；
handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务；
```
###### 3.2 Future
```
Future
    V get() ：获取异步执行的结果，如果没有结果可用，此方法会阻塞直到异步计算完成。
    V get(Long timeout , TimeUnit unit) ：获取异步执行结果，如果没有结果可用，此方法会阻塞，但是会有时间限制，如果阻塞时间超过设定的timeout时间，该方法将抛出异常。
    boolean isDone() ：如果任务执行结束，无论是正常结束或是中途取消还是发生异常，都返回true。
    boolean isCanceller() ：如果任务完成前被取消，则返回true。
    boolean cancel(boolean mayInterruptRunning) ：如果任务还没开始，执行cancel(...)方法将返回false；
    如果任务已经启动，执行cancel(true)方法将以中断执行此任务线程的方式来试图停止任务，如果停止成功，返回true；
    当任务已经启动，执行cancel(false)方法将不会对正在执行的任务线程产生影响(让线程正常执行到完成)，此时返回false；
    当任务已经完成，执行cancel(...)方法将返回false。mayInterruptRunning参数表示是否中断执行中的线程。
    通过方法分析我们也知道实际上Future提供了3种功能：（1）能够中断执行中的任务（2）判断任务是否执行完成（3）获取任务执行完成后额果。
```


#### java8特性

##### 1. Functional

###### 1.1 Function & BiFunction
```
Function方法是一个输入一个输出，BiFunction是两个输入一个输出
```
###### 1.2 Predicate
```
Predicate给定一个入参 返回一个boolean值
```
###### 1.3 Supplier
```
Supplier不接受参数，返回一个固定的结果
```

##### 2.Lambda 
lambda表达式的作用：传递行为而不仅仅是值（提升抽象层次，API重用性更好，更加灵活）

