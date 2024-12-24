-- 准备数据
INSERT INTO forum.column_info
(id, column_name, user_id, introduction, cover, state, publish_time, create_time, update_time)
VALUES (1, '一灰灰的专栏', 1, '这里是小灰灰的技术专栏，欢迎关注',
        'https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4ba0bc79579c488eb79df93cecd12390~tplv-k3u1fbpfcp-watermark.image',
        1, '2022-09-15 01:00:00', '2022-09-15 01:00:00', '2022-09-15 01:00:00');


-- 添加专栏文章

INSERT INTO forum.article
(id, author_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted,
 create_time, update_time, flag_bit)
VALUES (100, 1, 1, '分布式系统的38个知识点', '38个知识点', '', '分布式系统的38个精选知识点', 1, 2, '', 1, 0,
        '2022-10-08 19:12:32', '2022-10-08 19:12:32', 0);
INSERT INTO forum.article
(id, author_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted,
 create_time, update_time, flag_bit)
VALUES (101, 1, 1, '分布式系统的8个谬误', '8个经典谬误', '', '分布式系统常见的8个理解偏差', 1, 2, '', 1, 0,
        '2022-10-08 19:13:38', '2022-10-08 19:13:38', 0);
INSERT INTO forum.article
(id, author_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted,
 create_time, update_time, flag_bit)
VALUES (102, 1, 1, '分布式系统的特征、瓶颈以及性能指标介绍', '分布式系统概要',
        'https://spring.hhui.top/spring-blog/imgs/220819/logo.jpg', '什么是分布式系统，特点是什么，问题又有哪些？', 1, 2,
        '', 1, 0, '2022-10-08 19:14:17', '2022-10-08 19:14:17', 0);

INSERT INTO forum.article_tag
    (article_id, tag_id, deleted, create_time, update_time)
VALUES (100, 1, 0, '2022-10-08 19:23:14', '2022-10-08 19:23:14');
INSERT INTO forum.article_tag
    (article_id, tag_id, deleted, create_time, update_time)
VALUES (101, 1, 0, '2022-10-08 19:23:35', '2022-10-08 19:23:35');
INSERT INTO forum.article_tag
    (article_id, tag_id, deleted, create_time, update_time)
VALUES (102, 1, 0, '2022-10-08 19:23:43', '2022-10-08 19:23:43');


INSERT INTO forum.article_detail
    (article_id, version, content, deleted, create_time, update_time)
VALUES (100, 2, '
> 大家好我是咸鱼了大半年的一灰灰，终于放暑假了，把小孩送回老家，作为咸鱼的我也可以翻翻身了，接下来将趁着暑假的这段时间，将准备一个全新的分布式专栏，为了给大家提供更好的阅读体验，可以再我的个人站点上查看系列的专栏内容：
>
> [https://hhui.top/分布式](https://hhui.top/分布式)
>

天天说分布式分布式，那么我们是否知道什么是分布式，分布式会遇到什么问题，有哪些理论支撑，有哪些经典的应对方案，业界是如何设计并保证分布式系统的高可用呢？

## 1.架构设计

这一节将从一些经典的开源系统架构设计出发，来看一下，如何设计一个高质量的分布式系统；

而一般的设计出发点，无外乎

- 冗余：简单理解为找个备胎，现任挂掉之后，备胎顶上
- 拆分：不能让一个人承担所有的重任，拆分下，每个人负担一部分，压力均摊


### 1.1 主备架构

给现有的服务搭建一个备用的服务，两者功能完全一致，区别在于平时只有主应用对外提供服务能力；而备应用则只需要保证与主应用能力一致，随时待机即可，并不用对外提供服务；当主应用出现故障之后，将备应用切换为主应用，原主应用下线；迅速的主备切换可以有效的缩短故障时间



基于上面的描述，主备架构特点比较清晰

- 采用冗余的方案，加一台备用服务
- 缺点就是资源浪费



其次就是这个架构模型最需要考虑的则是如何实现主备切换？

- 人工
- VIP(虚拟ip) + keepalived 机制

### 1.2 主从架构

主从一般又叫做读写分离，主提供读写能力，而从则只提供读能力

鉴于当下的互联网应用，绝大多数都是读多写少的场景；读更容易成为性能瓶颈，所以采用读写分离，可以有效的提高整个集群的响应能力

主从架构可以区分为：一主多从 + 一主一从再多从，以mysql的主从架构模型为例进行说明

![MySql主从](https://hhui.top/分布式/设计模式/imgs/220708/mysql03.jpg)




主从模式的主要特点在于

- 添加从，源头依然是数据冗余的思想
- 读写分离：主负责读写，从只负责读，可以视为负载均衡策略
- 从需要向主同步数据，所若有的从都同步与主，对主的压力依然可能很大；所以就有了主从从的模式

关键问题则在于

- 主从延迟
- 主的写瓶颈
- 主挂之后如何选主




### 1.3 多主多从架构

一主多从面临单主节点的瓶颈问题，那就考虑多主多从的策略，同样是主负责提供读写，从提供读；

但是这里有一个核心点在于多主之间的数据同步，如何保证数据的一致性是这个架构模型的重点

如MySql的双主双从可以说是一个典型的应用场景，在实际使用的时候除了上面的一致性之外，还需要考虑主键id冲突的问题


### 1.4 普通集群模式

无主节点，集群中所有的应用职能对等，没有主次之分（当下绝大多数的业务服务都属于这种），一个请求可以被集群中任意一个服务响应；

这种也可以叫做去中心化的设计模式，如redis的集群模式，eureka注册中心，以可用性为首要目标



对于普通集群模式而言，重点需要考虑的点在于

- 资源竞争：如何确保一个资源在同一时刻只能被一个业务操作
  - 如现在同时来了申请退款和货物出库的请求，如果不对这个订单进行加锁，两个请求同时响应，将会导致发货又退款了，导致财货两失
- 数据一致性：如何确保所有的实例数据都是一致的，或者最终是一致的
  - 如应用服务使用jvm缓存，那么如何确保所有实例的jvm缓存一致？
  - 如Eureka的分区导致不同的分区的注册信息表不一致



### 1.5 数据分片架构

> 这个分片模型的描述可能并不准确，大家看的时候重点理解一下这个思想

前面几个的架构中，采用的是数据冗余的方式，即所有的实例都有一个全量的数据，而这里的数据分片，则从数据拆分的思路来处理，将全量的数据，通过一定规则拆分到多个系统中，每个系统包含部分的数据，减小单个节点的压力，主要用于解决数据量大的场景

比如redis的集群方式，通过hash槽的方式进行分区

如es的索引分片存储


### 1.6 一灰灰的小结

这一节主要从架构设计层面对当前的分布式系统所采用的方案进行了一个简单的归类与小结，并不一定全面，欢迎各位大佬留言指正



基于冗余的思想：

- 主备
- 主从
- 多主多从
- 无中心集群

基于拆分的思想：

- 数据分片

> 对于拆分这一块，我们常说的分库分表也体现的是这一思想



## 2.理论基础

这一小节将介绍分布式系统中的经典理论，如广为流程的CAP/BASE理论，一致性理论基础paxios,raft，信息交换的Gossip协议，两阶段、三阶段等

本节主要内容参考自

* [一致性算法-Gossip协议详解 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1662426)
* [P2P 网络核心技术：Gossip 协议 - 知乎](https://zhuanlan.zhihu.com/p/41228196)
* [从Paxos到Raft，分布式一致性算法解析_mb5fdb0a87e2fa1的技术博客_51CTO博客](https://blog.51cto.com/u_15060467/2678779)
* [【理论篇】浅析分布式中的 CAP、BASE、2PC、3PC、Paxos、Raft、ZAB - 知乎](https://zhuanlan.zhihu.com/p/338628717)

### 2.1 CAP定理

CAP 定理指出，分布式系统 **不可能** 同时提供下面三个要求：

- Consistency：一致性
  - 操作更新完成并返回客户端之后，所有节点数据完全一致
- Availability：可用性
  - 服务一直可用
- Partition tolerance：分区容错性
  - 分布式系统在遇到某节点或网络分区故障的时候，仍然能够对外提供满足**一致性**和**可用性**的服务

通常来讲P很难不保证，当服务部署到多台实例上时，节点异常、网络故障属于常态，根据不同业务场景进行选择

对于服务有限的应用而言，首选AP，保证高可用，即使部分机器异常，也不会导致整个服务不可用；如绝大多数的前台应用都是这种

对于数据一致性要求高的场景，如涉及到钱的支付结算，CP可能更重要了



对于CAP的三种组合说明如下

| 选择 | 说明 |
| --- | --- |
| CA | 放弃分区容错性，加强一致性和可用性，其实就是传统的单机场景 |
| AP | 放弃一致性（这里说的一致性是强一致性），追求分区容错性和可用性，这是很多分布式系统设计时的选择，例如很多NoSQL系统就是如此 |
| CP | 放弃可用性，追求一致性和分区容错性，基本不会选择，网络问题会直接让整个系统不可用 |



 ### 2.2 BASE理论

 base理论作为cap的延伸，其核心特点在于放弃强一致性，追求最终一致性

- Basically Available: 基本可用
  - 指分布式系统在出现故障的时候，允许损失部分可用性，即保证核心可用
  - 如大促时降级策略
- Soft State：软状态
  - 允许系统存在中间状态，而该中间状态不会影响系统整体可用性
  - MySql异步方式的主从同步，可能导致的主从数据不一致
- Eventual Consistency：最终一致性
  - 最终一致性是指系统中的所有数据副本经过一定时间后，最终能够达到一致的状态



基于上面的描述，可以看到BASE理论适用于大型高可用可扩展的分布式系统

注意其不同于ACID的强一致性模型，而是通过牺牲强一致性 来获得可用性，并允许数据在一段时间内是不一致的，但最终达到一致状态




### 2.3 PACELEC 定理

> 这个真没听说过，以下内容来自:
> * [Distributed System Design Patterns | by Nishant | Medium](https://medium.com/@nishantparmar/distributed-system-design-patterns-2d20908fecfc)

- 如果有一个分区（''P''），分布式系统可以在可用性和一致性（即''A''和''C''）之间进行权衡;
- 否则（''E''），当系统在没有分区的情况下正常运行时，系统可以在延迟（''L''）和一致性（''C''）之间进行权衡。

![](https://hhui.top/分布式/设计模式/imgs/220708/peace.jpg)

定理（PAC）的第一部分与CAP定理相同，ELC是扩展。整个论点假设我们通过复制来保持高可用性。因此，当失败时，CAP定理占上风。但如果没有，我们仍然必须考虑复制系统的一致性和延迟之间的权衡。


### 2.4 Paxos共识算法

> Paxos算法解决的问题是分布式共识性问题，即一个分布式系统中的各个进程如何就某个值（决议）通过共识达成一致

基于上面这个描述，可以看出它非常适用于选举；其工作流程

- 一个或多个提议进程 (Proposer) 可以发起提案 (Proposal)，
- Paxos算法使所有提案中的某一个提案，在所有进程中达成一致。 系统中的多数派同时认可该提案，即达成了一致

角色划分:

- Proposer: 提出提案Proposal，包含编号 + value
- Acceptor: 参与决策，回应Proposers的提案；当一个提案，被半数以上的Acceptor接受，则该提案被批准
  - 每个acceptor只能批准一个提案
- Learner: 不参与决策，获取最新的提案value


### 2.5 Raft算法

> 推荐有兴趣的小伙伴，查看
> * [Raft 算法动画演示](http://thesecretlivesofdata.com/raft/)
> * [Raft算法详解 - 知乎](https://zhuanlan.zhihu.com/p/32052223)


为了解决paxos的复杂性，raft算法提供了一套更易理解的算法基础，其核心流程在于：

leader接受请求，并转发给follow，当大部分follow响应之后，leader通知所有的follow提交请求、同时自己也提交请求并告诉调用方ok

角色划分：

- Leader：领导者，接受客户端请求，并向Follower同步请求，当数据同步到大多数节点上后告诉Follower提交日志
- Follow: 接受并持久化Leader同步的数据，在Leader告之日志可以提交之后，提交
- Candidate：Leader选举过程中的临时角色，向其他节点拉选票，得到多数的晋升为leader，选举完成之后不存在这个角色


![raft共识流程](https://hhui.top/分布式/设计模式/imgs/220708/raft03.jpg)


### 2.6 ZAB协议

> ZAB(Zookeeper Atomic Broadcast) 协议是为分布式协调服务ZooKeeper专门设计的一种支持崩溃恢复的一致性协议，基于该协议，ZooKeeper 实现了一种 主从模式的系统架构来保持集群中各个副本之间的数据一致性。
>
> * [zookeeper核心之ZAB协议就这么简单！](https://segmentfault.com/a/1190000037550497)


主要用于zk的数据一致性场景，其核心思想是Leader再接受到事务请求之后，通过给Follower，当半数以上的Follower返回ACK之后，Leader提交提案，并向Follower发送commit信息

**角色划分**

- Leader: 负责整个Zookeeper 集群工作机制中的核心
  - 事务请求的唯一调度和处理者，保证集群事务处理的顺序性
  - 集群内部各服务器的调度者
- Follower：Leader的追随者
  - 处理客户端的非实物请求，转发事务请求给 Leader 服务器
  - 参与事务请求 Proposal 的投票
  - 参与 Leader 选举投票
- Observer：是 zookeeper 自 3.3.0 开始引入的一个角色，
  - 它不参与事务请求 Proposal 的投票，
  - 也不参与 Leader 选举投票
  - 只提供非事务的服务（查询），通常在不影响集群事务处理能力的前提下提升集群的非事务处理能力。


![ZAB消息广播](https://hhui.top/分布式/设计模式/imgs/220708/zab00.jpg)



### 2.7 2PC协议

> two-phase commit protocol，两阶段提交协议，主要是为了解决强一致性，中心化的强一致性协议

**角色划分**

- 协调节点(coordinator)：中心化
- 参与者节点(partcipant)：多个

**执行流程**

协调节点接收请求，然后向参与者节点提交 `precommit`，当所有的参与者都回复ok之后，协调节点再给所有的参与者节点提交`commit`，所有的都返回ok之后，才表明这个数据确认提交

当第一个阶段，有一个参与者失败，则所有的参与者节点都回滚


![2pc流程](https://hhui.top/分布式/设计模式/imgs/220708/2pc00.jpg)



**特点**

优点在于实现简单

缺点也很明显

- 协调节点的单点故障
- 第一阶段全部ack正常，第二阶段存在部分参与者节点异常时，可能出现不一致问题



### 2.8 3PC协议

> [分布式事务：两阶段提交与三阶段提交 - SegmentFault 思否](https://segmentfault.com/a/1190000012534071)
>

在两阶段的基础上进行扩展，将第一阶段划分两部，cancommit + precommit，第三阶段则为 docommit


**第一阶段 cancommit**

该阶段协调者会去询问各个参与者是否能够正常执行事务，参与者根据自身情况回复一个预估值，相对于真正的执行事务，这个过程是轻量的

**第二阶段 precommit**

本阶段协调者会根据第一阶段的询盘结果采取相应操作，若所有参与者都返回ok，则协调者向参与者提交事务执行(单不提交)通知；否则通知参与者abort回滚

**第三阶段 docommit**

如果第二阶段事务未中断，那么本阶段协调者将会依据事务执行返回的结果来决定提交或回滚事务，若所有参与者正常执行，则提交；否则协调者+参与者回滚


在本阶段如果因为协调者或网络问题，导致参与者迟迟不能收到来自协调者的 commit 或 rollback 请求，那么参与者将不会如两阶段提交中那样陷入阻塞，而是等待超时后继续 commit，相对于两阶段提交虽然降低了同步阻塞，但仍然无法完全避免数据的不一致



**特点**

- 降低了阻塞与单点故障：
  - 参与者返回 CanCommit 请求的响应后，等待第二阶段指令，若等待超时/协调者宕机，则自动 abort，降低了阻塞；
  - 参与者返回 PreCommit 请求的响应后，等待第三阶段指令，若等待超时/协调者宕机，则自动 commit 事务，也降低了阻塞；
- 数据不一致问题依然存在
  - 比如第三阶段协调者发出了 abort 请求，然后有些参与者没有收到 abort，那么就会自动 commit，造成数据不一致




### 2.9 Gossip协议

> Gossip 协议，顾名思义，就像流言蜚语一样，利用一种随机、带有传染性的方式，将信息传播到整个网络中，并在一定时间内，使得系统内的所有节点数据一致。Gossip 协议通过上面的特性，可以保证系统能在极端情况下（比如集群中只有一个节点在运行）也能运行
>
> * [P2P 网络核心技术：Gossip 协议 - 知乎](https://zhuanlan.zhihu.com/p/41228196)

主要用在分布式数据库系统中各个副本节点同步数据之用，这种场景的一个最大特点就是组成的网络的节点都是对等节点，是非结构化网络


**工作流程**

- 周期性的传播消息，通常周期时间为1s
- 被感染的节点，随机选择n个相邻节点，传播消息
- 每次传播消息都选择还没有发送过的节点进行传播
- 收单消息的节点，不会传播给向它发送消息的节点

![Gossip传播示意图](https://hhui.top/分布式/设计模式/imgs/220708/gossip.gif)



**特点**

- 扩展性：允许节点动态增加、减少，新增的节点状态最终会与其他节点一致
- 容错：网络中任意一个节点宕机重启都不会影响消息传播
- 去中心化：不要求中心节点，所有节点对等，任何一个节点无需知道整个网络状况，只要网络连通，则一个节点的消息最终会散播到整个网络
- 一致性收敛：协议中的消息会以一传十、十传百一样的指数级速度在网络中快速传播，因此系统状态的不一致可以在很快的时间内收敛到一致。消息传播速度达到了 logN
- 简单：Gossip 协议的过程极其简单，实现起来几乎没有太多复杂性

**缺点**

- 消息延迟：节点只会随机向少数几个节点发送消息，消息最终是通过多个轮次的散播而到达全网的，因此使用 Gossip 协议会造成不可避免的消息延迟
- 消息冗余：节点会定期随机选择周围节点发送消息，而收到消息的节点也会重复该步骤，导致消息的冗余




### 2.10 一灰灰的小结

本节主要介绍的是分布式系统设计中的一些常见的理论基石，如分布式中如何保障一致性，如何对一个提案达成共识

- BASE，CAP，PACELEC理论：构建稳定的分布式系统应该考虑的方向
- paxos,raft共识算法
- zab一致性协议
- gossip消息同步协议

## 3.算法

这一节将主要介绍下分布式系统中的经典的算法，比如常用于分区的一致性hash算法，适用于一致性的Quorum NWR算法，PBFT拜占庭容错算法，区块链中大量使用的工作量证明PoW算法等

### 3.1 一致性hash算法

一致性hash算法，主要应用于数据分片场景下，有效降低服务的新增、删除对数据复制的影响

通过对数据项的键进行哈希处理映射其在环上的位置，然后顺时针遍历环以查找位置大于该项位置的第一个节点，将每个由键标识的数据分配给hash环中的一个节点

![一致性hash算法](https://hhui.top/分布式/设计模式/imgs/220708/hash.jpg)

一致散列的主要优点是增量稳定性; 节点添加删除，对整个集群而言，仅影响其直接邻居，其他节点不受影响。

**注意：**

- redis集群实现了一套hash槽机制，其核心思想与一致性hash比较相似


### 3.2 Quorum NWR算法

> 用来保证数据冗余和最终一致性的投票算法，其主要数学思想来源于鸽巢原理
>
> * [分布式系统之Quorum （NRW）算法-阿里云开发者社区](https://developer.aliyun.com/article/53498)

- N 表示副本数，又叫做复制因子（Replication Factor）。也就是说，N 表示集群中同一份数据有多少个副本
- W，又称写一致性级别（Write Consistency Level），表示成功完成 W 个副本更新写入，才会视为本次写操作成功
- R 又称读一致性级别（Read Consistency Level），表示读取一个数据对象时需要读 R 个副本, 才会视为本次读操作成功

Quorum NWR算法要求每个数据拷贝对象 都可以投1票，而每一个操作的执行则需要获取最小的读票数，写票数；通常来讲写票数W一般需要超过N/2，即我们通常说的得到半数以上的票才表示数据写入成功

事实上当W=N、R=1时，即所谓的WARO(Write All Read One)。就是CAP理论中CP模型的场景


### 3.3 PBFT拜占庭算法

拜占庭算法主要针对的是分布式场景下无响应，或者响应不可信的情况下的容错问题，其核心分三段流程，如下

![拜占庭算法](https://hhui.top/分布式/设计模式/imgs/220708/bzt.jpg)

假设集群节点数为 N，f个故障节点(无响应)和f个问题节点(无响应或错误响应),f+1个正常节点，即 3f+1=n

- 客户端向主节点发起请求，主节点接受请求之后，向其他节点广播 pre-prepare 消息
- 节点接受pre-prepare消息之后，若同意请求，则向其他节点广播 prepare 消息；
- 当一个节点接受到2f+1个prepare新消息，则进入commit阶段，并广播commit消息
- 当收到 2f+1 个 commit 消息后（包括自己），代表大多数节点已经进入 commit 阶段，这一阶段已经达成共识，于是节点就会执行请求，写入数据



相比 Raft 算法完全不适应有人作恶的场景，PBFT 算法能容忍 (n 1)/3 个恶意节点 (也可以是故障节点)。另外，相比 PoW 算法，PBFT 的优点是不消耗算 力。PBFT 算法是O(n ^ 2) 的消息复杂度的算法，所以以及随着消息数 的增加，网络时延对系统运行的影响也会越大，这些都限制了运行 PBFT 算法的分布式系统 的规模，也决定了 PBFT 算法适用于中小型分布式系统



### 3.4 PoW算法

工作量证明 (Proof Of Work，简称 PoW)，同样应用于分布式下的一致性场景，区别于前面的raft, pbft, paxos采用投票机制达成共识方案，pow采用工作量证明

客户端需要做一定难度的工作才能得出一个结果，验证方却很容易通过结果来检查出客户端是不是做了相应的工作，通过消耗一定工作浪，增加消息伪造的成本，PoW以区块链中广泛应用而广为人知，下面以区块链来简单说一下PoW的算法应用场景

以BTC的转账为例，A转n个btc给B，如何保证不会同时将这n个币转给C？

- A转账给B，交易信息记录在一个区块1中
- A转账给C，交易信息被记录在另一个区块2中
- 当区块1被矿工成功提交到链上，并被大多数认可（通过校验区块链上的hash值验证是否准确，而这个hash值体现的是矿工的工作量），此时尚未提交的区块2则会被抛弃
- 若区块1被提交，区块2也被提交，各自有部分人认可，就会导致分叉，区块链中采用的是优选最长的链作为主链，丢弃分叉的部分（这就属于区块链的知识点了，有兴趣的小伙伴可以扩展下相关知识点，这里就不展开了）


PoW的算法，主要应用在上面的区块提交验证，通过hash值计算来消耗算力，以此证明矿工确实有付出，得到多数认可的可以达成共识


### 3.5 一灰灰的小结

本节主要介绍了下当前分布式下常见的算法，

- 分区的一致性hash算法: 基于hash环，减少节点动态增加减少对整个集群的影响；适用于数据分片的场景
- 适用于一致性的Quorum NWR算法: 投票算法，定义如何就一个提案达成共识
- PBFT拜占庭容错算法: 适用于集群中节点故障、或者不可信的场景
- 区块链中大量使用的工作量证明PoW算法: 通过工作量证明，认可节点的提交


## 4.技术思想

这一节的内容相对前面几个而言，并不太容易进行清晰的分类；主要包含一些高质量的分布式系统的实践中，值得推荐的设计思想、技术细节

### 4.1 CQRS

> * [DDD 中的那些模式 — CQRS - 知乎](https://zhuanlan.zhihu.com/p/115685384)
> * [详解CQRS架构模式_架构_Kislay Verma_InfoQ精选文章](https://www.infoq.cn/article/wdlpjosudoga34jutys9)

Command Query Responsibility Segregation 即我们通俗理解的读写分离，其核心思想在于将两类不同操作进行分离，在独立的服务中实现

![cqrs](https://hhui.top/分布式/设计模式/imgs/220708/cqrs.jpg)

用途在于将领域模型与查询功能进行分离，让一些复杂的查询摆脱领域模型的限制，以更为简单的 DTO 形式展现查询结果。同时分离了不同的数据存储结构，让开发者按照查询的功能与要求更加自由的选择数据存储引擎


### 4.2 复制负载平衡服务

> * [分布式系统设计:服务模式之复制负载平衡服务 - 知乎](https://zhuanlan.zhihu.com/p/34191846)
> * [负载均衡调度算法大全 | 菜鸟教程](https://www.runoob.com/w3cnote/balanced-algorithm.html)

复制负载平衡服务(Replication Load Balancing Service, RLBS)，可以简单理解为我们常说的负载均衡，多个相同的服务实例构建一个集群，每个服务都可以响应请求，负载均衡器负责请求的分发到不同的实例上，常见的负载算法


| 算法 | 说明 | 特点 |
| --- | --- | --- |
| 轮询 | 请求按照顺序依次分发给对应的服务器 | 优点简单，缺点在于未考虑不同服务器的实际性能情况 |
| 加权轮询 | 权重高的被分发更多的请求 | 优点：充分利用机器的性能 |
| 最少连接数 | 找连接数最少的服务器进行请求分发,若所有服务器相同的连接数，则找第一个选择的 | 目的是让优先让空闲的机器响应请求 |
| 少连接数慢启动时间 | 刚启动的服务器，在一个时间段内，连接数是有限制且缓慢增加 | 避免刚上线导致大量的请求分发过来而超载 |
| 加权最少连接 | 平衡服务性能 + 最少连接数 | |
| 基于代理的自适应负载均衡 | 载主机包含一个自适用逻辑用来定时监测服务器状态和该服务器的权重 | |
| 源地址哈希法 | 获取客户端的IP地址，通过哈希函映射到对应的服务器 | 相同的来源请求都转发到相同的服务器上 |
| 随机 | 随机算法选择一台服务器 |  |
| 固定权重 | 最高权重只有在其他服务器的权重值都很低时才使用。然而，如果最高权重的服务器下降，则下一个最高优先级的服务器将为客户端服务 | 每个真实服务器的权重需要基于服务器优先级来配置|
| 加权响应 | 服务器响应越小其权重越高，通常是基于心跳来判断机器的快慢 | 心跳的响应并不一定非常准确反应服务情况 |


### 4.3 心跳机制

在分布式环境里中，如何判断一个服务是否存活，当下最常见的方案就是心跳

比如raft算法中的leader向所有的follow发送心跳，表示自己还健在，避免发生新的选举；

比如redis的哨兵机制，也是通过ping/pong的心跳来判断节点是否下线，是否需要选新的主节点；

再比如我们日常的业务应用得健康监测，判断服务是否正常


### 4.4 租约机制

租约就像一个锁，但即使客户端离开，它也能工作。客户端请求有限期限的租约，之后租约到期。如果客户端想要延长租约，它可以在租约到期之前续订租约。


租约主要是了避免一个资源长久被某个对象持有，一旦对方挂了且不会主动释放的问题；在实际的场景中，有两个典型的应用

**case1 分布式锁**

业务获取的分布式锁一般都有一个有效期，若有效期内没有主动释放，这个锁依然会被释放掉，其他业务也可以抢占到这把锁；因此对于持有锁的业务方而言，若发现在到期前，业务逻辑还没有处理完，则可以续约，让自己继续持有这把锁

典型的实现方式是redisson的看门狗机制

**case2 raft算法的任期**

在raft算法中，每个leader都有一个任期，任期过后会重新选举，而Leader为了避免重新选举，一般会定时发送心跳到Follower进行续约


### 4.5 Leader & Follow

这个比较好理解，上面很多系统都采用了这种方案，特别是在共识算法中，由领导者负责代表整个集群做出决策，并将决策传播到所有其他服务器

领导者选举在服务器启动时进行。每个服务器在启动时都会启动领导者选举，并尝试选举领导者。除非选出领导者，否则系统不接受任何客户端请求

### 4.6 Fencing

在领导者-追随者模式中，当领导者失败时，不可能确定领导者已停止工作，如慢速网络或网络分区可能会触发新的领导者选举，即使前一个领导者仍在运行并认为它仍然是活动的领导者

Fencint是指在以前处于活动状态的领导者周围设置围栏，使其无法访问集群资源，从而停止为任何读/写请求提供服务

- 资源屏蔽：系统会阻止以前处于活动状态的领导者访问执行基本任务所需的资源。
- 节点屏蔽：系统会阻止以前处于活动状态的领导者访问所有资源。执行此操作的常见方法是关闭节点电源或重置节点。

### 4.7 Quorum法定人数

法定人数，常见于选举、共识算法中，当超过Quorum的节点数确认之后，才表示这个提案通过(数据更新成功)，通常这个法定人数为 = 半数节点 + 1

### 4.8 High-Water mark高水位线


高水位线，跟踪Leader（领导者）上的最后一个日志条目，且该条目已成功复制到>quorum（法定人数）的Follow（跟谁者），即表示这个日志被整个集群接受

日志中此条目的索引称为高水位线索引。领导者仅公开到高水位线索引的数据。

如Kafka：为了处理非可重复读取并确保数据一致性，Kafka broker会跟踪高水位线，这是特定分区的最大偏移量。使用者只能看到高水位线之前的消息。


### 4.9 Phi 累计故障检测

Phi Accrual Failure Detection,使用历史检测信号信息使阈值自适应

通用的应计故障检测器不会判断服务器是否处于活动状态，而是输出有关服务器的可疑级别。

如Cassandra（Facebook开源的分布式NoSql数据库）使用 Phi 应计故障检测器算法来确定群集中节点的状态

### 4.10 Write-ahead Log预写日志


预写日志记录是解决操作系统中文件系统不一致的问题的高级解决方案，当我们提交写到操作系统的文件缓存，此时业务会认为已经提交成功；但是在文件缓存与实际写盘之间会有一个时间差，若此时机器宕机，会导致缓存中的数据丢失，从而导致完整性缺失

为了解决这个问题，如mysql，es等都采用了预写日志的机制来避免这个问题

MySql：

- 事务提交的流程中，先写redolog precommit， 然后写binlog，最后再redolog commit；当redolog记录成功之后，才表示事务执行成功；
- 因此当出现上面的宕机恢复时，则会加载redologo，然后重放对应的命令，来恢复未持久化的数据

ElasticSearch:

- 在内存中数据生成段写到操作系统文件缓存前，会先写事务日志，出现异常时，也是从事务日志进行恢复

### 4.11 分段日志

将日志拆分为多个较小的文件，而不是单个大文件，以便于操作。

单个日志文件在启动时读取时可能会增长并成为性能瓶颈。较旧的日志会定期清理，并且很难对单个大文件执行清理操作。


单个日志拆分为多个段。日志文件在指定的大小限制后滚动。使用日志分段，需要有一种将逻辑日志偏移量（或日志序列号）映射到日志段文件的简单方法。


这个其实也非常常见，比如我们实际业务应用配置的log，一般都是按天、固定大小进行拆分，并不会把所有的日志都放在一个日志文件中


再比如es的分段存储，一个段就是一个小的存储文件



### 4.12 checksum校验

在分布式系统中，在组件之间移动数据时，从节点获取的数据可能会损坏。

计算校验和并将其与数据一起存储。

要计算校验和，请使用 MD5、SHA-1、SHA-256 或 SHA-512 等加密哈希函数。哈希函数获取输入数据并生成固定长度的字符串（包含字母和数字）;此字符串称为校验和。

当系统存储某些数据时，它会计算数据的校验和，并将校验和与数据一起存储。当客户端检索数据时，它会验证从服务器接收的数据是否与存储的校验和匹配。如果没有，则客户端可以选择从另一个副本检索该数据。

HDFS和Chubby将每个文件的校验和与数据一起存储。

### 4.13 一灰灰的小结

这一节很多内容来自下面这篇博文，推荐有兴趣的小伙伴查看原文

* [Distributed System Design Patterns | by Nishant | Medium](https://medium.com/@nishantparmar/distributed-system-design-patterns-2d20908fecfc)

这一节主要简单的介绍了下分布式系统中应用到的一些技术方案，如有对其中某个技术有兴趣的小伙伴可以留言，后续会逐一进行补全


## 5.分布式系统解决方案

最后再介绍一些常见的分布式业务场景及对应的解决方案，比如全局唯一的递增ID-雪花算法，分布式系统的资源抢占-分布式锁，分布式事务-2pc/3pc/tcc ，分布式缓存等

### 5.1 缓存

缓存实际上并不是分布式独有的，这里把它加进来，主要是因为实在是应用得太广了，无论是应用服务、基础软件工具还是操作系统，大量都可以见到缓存的身影

缓存的核心思想在于： 借助更高效的IO方式，来替代代价昂贵的IO方式

如：

- redis的性能高于mysql
- 如内存的读写，远高于磁盘IO，文件IO
- 磁盘顺序读写 > 随机读写



用好缓存可以有效提高应用性能，下面以一个普通的java前台应用为例说明

- JVM缓存 -> 分布式缓存(redis/memcache) -> mysql缓存 -> 操作系统文件缓存 -> 磁盘文件

缓存面临的核心问题，则在于

- 一致性问题：缓存与db的一致性如何保障（相信大家都听说过或者实际处理过这种问题）
- 数据完整性：比如常见的先写缓存，异步刷新到磁盘，那么缓存到磁盘刷新这段时间内，若宕机导致数据丢失怎么办？
  - TIP: 上面这个问题可以参考mysql的redolog




### 5.2 全局唯一ID

在传统的单体架构中，业务id基本上是依赖于数据库的自增id来处理；当我们进入分布式场景时，如我们常说的分库分表时，就需要我们来考虑如何实现全局唯一的业务id了，避免出现在分表中出现冲突




全局唯一ID解决方案：

- uuid
- 数据库自增id表
- redis原子自增命令
- 雪花算法 (原生的，扩展的百度UidGenerator, 美团Leaf等)
- Mist 薄雾算法




### 5.3 分布式锁

常用于分布式系统中资源控制，只有持有锁的才能继续操作，确保同一时刻只会有一个实例访问这个资源




常见的分布式锁有

* 基于数据库实现分布式锁
* [Redis实现分布式锁（应用篇） | 一灰灰Learning](https://hhui.top/spring-db/09.%E5%AE%9E%E4%BE%8B/20.201030-springboot%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8Bredis%E5%AE%9E%E7%8E%B0%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%BA%94%E7%94%A8%E7%AF%87/)
* [从0到1实现一个分布式锁 | 一灰灰Learning](https://hhui.top/spring-middle/03.zookeeper/02.210415-springboot%E6%95%B4%E5%90%88zookeeper%E4%BB%8E0%E5%88%B01%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81/)
* etcd实现分布式锁
* 基于consul实现分布式锁



### 5.4 分布式事务

事务表示一组操作，要么全部成功，要么全部不成功；单机事务通常说的是数据库的事务；而分布式事务，则可以简单理解为多个数据库的操作，要么同时成功，要么全部不成功

更确切一点的说法，分布式事务主要是要求事务的参与方，可能涉及到多个系统、多个数据资源，要求它们的操作要么都成功，要么都回滚；


一个简单的例子描述下分布式事务场景：

**下单扣库存**

- 用户下单，付钱
- 此时订单服务，会生成订单信息
- 支付网关，会记录付款信息，成功or失败
- 库存服务，扣减对应的库存

一个下单支付操作，涉及到三个系统，而分布式事务则是要求，若支付成功，则上面三个系统都应该更新成功；若有一个操作失败，如支付失败，则已经扣了库存的要回滚（还库存），生成的订单信息回滚（删掉--注：现实中并不会去删除订单信息，这里只是用于说明分布式事务，请勿带入实际的实现方案）




分布式事务实现方案：

- 2PC: 前面说的两阶段提交，就是实现分布式事务的一个经典解决方案
- 3PC: 三阶段提交
- TCC：补偿事务，简单理解为应用层面的2PC
- SAGA事务
- 本地消息表
- MQ事务方案




### 5.5 分布式任务

分布式任务相比于我们常说单机的定时任务而言，可以简单的理解为多台实例上的定时任务，从应用场景来说，可以区分两种

- 互斥性的分布式任务
  - 即同一时刻，集群内只能有一个实例执行这个任务
- 并存式的分布式任务
  - 同一时刻，所有的实例都可以执行这个任务
  - 续考虑如何避免多个任务操作相同的资源




分布式任务实现方案：

- Quartz Cluster
- XXL-Job
- Elastic-Job
- 自研：
  - 资源分片策略
  - 分布式锁控制的唯一任务执行策略



### 5.6 分布式Session

> Session一般叫做会话，Session技术是http状态保持在服务端的解决方案，它是通过服务器来保持状态的。我们可以把客户端浏览器与服务器之间一系列交互的动作称为一个 Session。是服务器端为客户端所开辟的存储空间，在其中保存的信息就是用于保持状态。因此，session是解决http协议无状态问题的服务端解决方案，它能让客户端和服务端一系列交互动作变成一个完整的事务。

单机基于session/cookie来实现用户认证，那么在分布式系统的多实例之间，如何验证用户身份呢？这个就是我们说的分布式session




分布式session实现方案：

- session stick：客户端每次请求都转发到同一台服务器(如基于ip的hash路由转发策略）
- session复制: session生成之后，主动同步给其他服务器
- session集中保存：用户信息统一存储，每次需要时统一从这里取(也就是常说的redis实现分布式session方案)
- cookie: 使用客户端cookie存储session数据，每次请求时携带这个



### 5.7 分布式链路追踪


分布式链路追踪也可以叫做全链路追中，而它可以说是每个开发者的福音，通常指的是一次前端的请求，将这个请求过程中，所有涉及到的系统、链路都串联起来，可以清晰的知道这一次请求中，调用了哪些服务，有哪些IO交互，瓶颈点在哪里，什么地方抛出了异常


当前主流的全链路方案大多是基于google的`Dapper` 论文实现的



全链路实现方案

- zipkin
- pinpoint
- SkyWalking
- CAT
- jaeger



### 5.8 布隆过滤器

Bloom过滤器是一种节省空间的概率数据结构，用于测试元素是否为某集合的成员。

布隆过滤器由一个长度为 m 比特的位数组（bit array）与 k 个哈希函数（hash function）组成的数据结构。

原理是当一个元素被加入集合时，通过 K 个散列函数将这个元素映射成一个位数组中的 K 个点，把它们置为 1。

检索时，我们只要看看这些点是不是都是 1 就大约知道集合中有没有它了，也就是说，如果这些点有任何一个 0 ，则被检元素一定不在；如果都是 1 ，则被检元素很可能在。


关于布隆过滤器，请牢记一点

- 判定命中的，不一定真的命中
- 判定没有命中的，则一定不在里面

![布隆过滤器](https://hhui.top/分布式/设计模式/imgs/220708/bloom-filter.png)

常见的应用场景，如

- 防止缓存穿透
- 爬虫时重复检测


### 5.9 一灰灰的小结

分布式系统的解决方案当然不局限于上面几种，比如分布式存储、分布式计算等也属于常见的场景，当然在我们实际的业务支持过程中，不太可能需要让我们自己来支撑这种大活；而上面提到的几个点，基本上或多或少会与我们日常工作相关，这里列出来当然是好为了后续的详情做铺垫


## 6.一灰灰的总结

### 6.1 综述

这是一篇概括性的综述类文章，可能并没有很多的干货，当然也限于“一灰灰”我个人的能力，上面的总结可能并不准确，如有发现，请不吝赐教

全文总结如下

常见的分布式架构设计方案：

- 主备，主从，多主多从，普通无中心集群，数据分片架构

分布式系统中的理论基石：

- CAP， BASE, PACELEC
- 共识算法：paxos, raft, zab
- 一致性协议：2pc, 3pc
- 数据同步：gossip

分布式系统中的算法：

- 分区的一致性hash算法: 基于hash环，减少节点动态增加减少对整个集群的影响；适用于数据分片的场景
- 适用于一致性的Quorum NWR算法: 投票算法，定义如何就一个提案达成共识
- PBFT拜占庭容错算法: 适用于集群中节点故障、或者不可信的场景
- 区块链中大量使用的工作量证明PoW算法: 通过工作量证明，认可节点的提交

分布式系统解决方案：

- 分布式缓存
- 全局唯一ID
- 分布式锁
- 分布式事务
- 分布式任务
- 分布式会话
- 分布式链路追踪
- 布隆过滤器


### 6.2 题外话

最后总结一下这篇耗时两周写完的“心血巨作”（有点自吹了哈），准备这篇文章确实花了很大的精力，首先我个人对于分布式这块的理解并不能算深刻，其次分布式这块的理论+实践知识特别多，而且并不是特别容易上手理解，在输出这篇文章的同时，遇到一些疑问点我也会去查阅相关资料去确认，整个过程并不算特别顺利； 那么为什么还要去做这个事情呢？

1. 咸鱼太久了，想做一些有意思的东西，活跃一下大脑
2. 准备依托于《分布式专栏》来将自己的知识体系进行归纳汇总，让零散分布在大脑中的知识点能有一个脉络串联起来
3. 不想做架构的码农不是好码农，而想成为一个好的架构，当然得做一些基础准备，向业务精品学习取经


', 0, '2022-10-08 19:12:32', '2022-10-08 19:23:14');

INSERT INTO forum.article_detail
    (article_id, version, content, deleted, create_time, update_time)
VALUES (101, 2, '![](https://files.mdnice.com/user/6227/49529ff9-2c47-42bf-ba9b-480ac43eb913.png)

你在分布式系统上工作吗？微服务，Web API，SOA，Web服务器，应用服务器，数据库服务器，缓存服务器，负载均衡器 - 如果这些描述了系统设计中的组件，那么答案是肯定的。分布式系统由许多计算机组成，这些计算机协调以实现共同的目标。

20多年前，Peter Deutsch和James Gosling定义了分布式计算的8个谬误。这些是许多开发人员对分布式系统做出的错误假设。从长远来看，这些通常被证明是错误的，导致难以修复错误。

8个谬误是：

1.  网络可靠。
2.  延迟为零。
3.  带宽是无限的。
4.  网络是安全的。
5.  拓扑不会改变。
6.  有一个管理员。
7.  运输成本为零。
8.  网络是同质的。

让我们来看看每个谬误，讨论问题和潜在的解决方案。

## 1.网络可靠

### 问题

> 通过网络呼叫将失败。

今天的大多数系统都会调用其他系统。您是否正在与第三方系统（支付网关，会计系统，CRM）集成？你在做网络服务电话吗？如果呼叫失败会发生什么？如果您要查询数据，则可以进行简单的重试。但是如果您发送命令会发生什么？我们举一个简单的例子：

```
var creditCardProcessor = new CreditCardPaymentService();
creditCardProcessor.Charge(chargeRequest);
```


如果我们收到HTTP超时异常会怎么样？如果服务器没有处理请求，那么我们可以重试。但是，如果它确实处理了请求，我们需要确保我们不会对客户进行双重收费。您可以通过使服务器具有幂等性来实现此目的。这意味着如果您使用相同的收费请求拨打10次，则客户只需支付一次费用。如果您没有正确处理这些错误，那么您的系统是不确定的。处理所有这些情况可能会非常复杂。

### 解决方案

因此，如果网络上的呼叫失败，我们能做什么？好吧，我们可以自动重试。排队系统非常擅长这一点。它们通常使用称为存储和转发的模式。它们在将消息转发给收件人之前在本地存储消息。如果收件人处于脱机状态，则排队系统将重试发送邮件。MSMQ是这种排队系统的一个例子。

但是这种变化将对您的系统设计产生重大影响。您正在从请求/响应模型转移到触发并忘记。由于您不再等待响应，因此您需要更改系统中的用户行程。您不能只使用队列发送替换每个Web服务调用。

### 结论

你可能会说网络现在更可靠 - 而且它们是。但事情发生了。硬件和软件可能会出现故障 - 电源，路由器，更新或补丁失败，无线信号弱，网络拥塞，啮齿动物或鲨鱼。是的，鲨鱼：在一系列鲨鱼叮咬之后，谷歌正在加强与Kevlar的海底数据线。

还有人为因素。人们可以开始DDOS攻击，也可以破坏物理设备。

这是否意味着您需要删除当前的技术堆栈并使用消息传递系统？并不是的！您需要权衡失败的风险与您需要进行的投资。您可以通过投资基础架构和软件来最小化失败的可能性。在许多情况下，失败是一种选择。但在设计分布式系统时，您确实需要考虑失败的问题。

## 2.延迟是零

### 问题

> 通过网络拨打电话不是即时的。

内存呼叫和互联网呼叫之间存在七个数量级的差异。您的应用程序应该是网络感知。这意味着您应该清楚地将本地呼叫与远程呼叫分开。让我们看看我在代码库中看到的一个例子：


```
var viewModel = new ViewModel();
var documents = new DocumentsCollection();
foreach (var document in documents)
{
	var snapshot = document.GetSnapshot();
	viewModel.Add(snapshot);
}
```


没有进一步检查，这看起来很好。但是，有两个远程呼叫。第2行进行一次调用以获取文档摘要列表。在第5行，还有另一个调用，它检索有关每个文档的更多信息。这是一个经典的Select n + 1问题。为了解决网络延迟问题，您应该在一次调用中返回所有必需的数据。一般的建议是本地调用可以细粒度，但远程调用应该更粗粒度。这就是为什么分布式对象和网络透明度的想法死了。但是，即使每个人都同意分布式对象是一个坏主意，有些人仍然认为延迟加载总是一个好主意：


```
var employee = EmployeeRepository.GetBy(someCriteria)
var department = employee.Department;
var manager = department.Manager;
foreach (var peer in manager.Employees;)
{
// do something
}
```


您不希望财产获取者进行网络呼叫。但是，每个。 在上面的代码中调用实际上可以触发数据库之旅。

### 解决方案

#### 带回您可能需要的所有数据

如果您进行远程呼叫，请确保恢复可能需要的所有数据。网络通信不应该是唠叨的。

#### 将Data Closer移动到客户端

另一种可能的解决方案是将数据移近客户端。如果您正在使用云，请根据客户的位置仔细选择可用区。缓存还可以帮助最小化网络呼叫的数量。对于静态内容，内容交付网络（CDN）是另一个不错的选择。

#### 反转数据流

删除远程调用的另一个选项是反转数据流。我们可以使用Pub / Sub并在本地存储数据，而不是查询其他服务。这样，我们就可以在需要时获取数据。当然，这会带来一些复杂性，但它可能是工具箱中的一个很好的工具。

### 结论

虽然延迟可能不是LAN中的问题，但当您转移到WAN或Internet时，您会注意到延迟。这就是为什么将网络呼叫与内存中的呼叫明确分开是很重要的。在采用微服务架构模式时，您应该牢记这一点。您不应该只使用远程调用替换本地呼叫。这可能会使你的系统变成分布式的大泥球。

## 3.带宽是无限的

### 问题

> 带宽是有限的。

带宽是网络在一段时间内发送数据的容量。到目前为止，我还没有发现它是一个问题，但我可以看到为什么它在某些条件下可能是一个问题。虽然带宽随着时间的推移而有所改善，但我们发送的数据量也有所增加。与通过网络传递简单DTO的应用相比，视频流或VoIP需要更多带宽。带宽对于移动应用程序来说更为重要，因此开发人员在设计后端API时需要考虑它。

错误地使用ORM也会造成伤害。我见过开发人员在查询中过早调用.ToList（）的示例，因此在内存中加载整个表。

### 解决方案

#### 领域驱动的设计模式

那么我们怎样才能确保我们不会带来太多数据呢？域驱动设计模式可以帮助：

*   首先，您不应该争取单一的企业级域模型。您应该将域划分为有界上下文。
*   要避免有界上下文中的大型复杂对象图，可以使用聚合模式。聚合确保一致性并定义事务边界。

#### 命令和查询责任隔离

我们有时会加载复杂的对象图，因为我们需要在屏幕上显示它的一部分。如果我们在很多地方这样做，我们最终会得到一个庞大而复杂的模型，对于写作和阅读来说都是次优的。另一种方法可以是使用命令和查询责任隔离 - CQRS。这意味着将域模型分为两部分：

*   在写模式将确保不变保持真实的数据是一致的。由于写模型不关心视图问题，因此可以保持较小且集中。
*   该读取模型是视图的担忧进行了优化，所以我们可以获取所有所需的特定视图中的数据（例如，我们的应用程序的屏幕）。

### 结论

在第二个谬误（延迟不是0）和第三个谬误（带宽是无限的）之间有延伸，您应该传输更多数据，以最大限度地减少网络往返次数。您应该传输较少的数据以最小化带宽使用。您需要平衡这两种力量，并找到通过线路发送的_正确_数据量。

虽然您可能不会经常遇到带宽限制，但考虑传输的数据非常重要。更少的数据更容易理解。数据越少意味着耦合越少。因此，只传输您可能需要的数据。

## 4.网络是安全的

### 问题

> 网络并不安全。

这是一个比其他人更多的媒体报道的假设。您的系统仅与最薄弱的链接一样安全。坏消息是分布式系统中有很多链接。您正在使用HTTPS，除非与不支持它的第三方遗留系统进行通信。您正在查看自己的代码，寻找安全问题，但正在使用可能存在风险的开源库。一个OpenSSL的漏洞允许人们通过盗取SSL / TLS保护的数据。Apache Struts中的一个错误允许攻击者在服务器上执行代码。即使你正在抵御所有这些，仍然存在人为因素。恶意DBA可能错放数据库备份。今天的攻击者掌握着大量的计算能力和耐心。所以问题不在于他们是否会攻击你的系统，而是什么时候。

### 解决方案

#### 深度防御

您应该使用分层方法来保护您的系统。您需要在网络，基础架构和应用程序级别进行不同的安全检查。

#### 安全心态

在设计系统时要牢记安全性。十大漏洞列表在过去5年中没有发生太大变化。您应遵循安全软件设计的最佳实践，并检查常见安全漏洞的代码。您应该定期搜索第三方库以查找新漏洞。常见漏洞和暴露列表可以提供帮助。

#### 威胁建模

威胁建模是一种识别系统中可能存在的安全威胁的系统方法。首先确定系统中的所有资产（数据库中的用户数据，文件等）以及如何访问它们。之后，您可以识别可能的攻击并开始执行它们。我建议阅读高级API安全性的第2章，以便更好地概述威胁建模。

### 结论

唯一安全的系统是关闭电源的系统，不连接到任何网络（理想情况下是在一个有形模块中）。它是多么有用的系统！事实是，安全是艰难而昂贵的。分布式系统中有许多组件和链接，每个组件和链接都是恶意用户的可能目标。企业需要平衡攻击的风险和概率与实施预防机制的成本。

攻击者手上有很多耐心和计算能力。我们可以通过使用威胁建模来防止某些类型的攻击，但我们无法保证100％的安全性。因此，向业务部门明确表示这一点是个好主意，共同决定投资安全性的程度，并制定安全漏洞何时发生的计划。

## 5.拓扑不会改变

### 问题

> 网络拓扑不断变化。

网络拓扑始终在变化。有时它会因意外原因而发生变化 - 当您的应用服务器出现故障并需要更换时。很多时候它是故意的 - 在新服务器上添加新进程。如今，随着云和容器的增加，这一点更加明显。弹性扩展 - 根据工作负载添加或删除服务器的能力 - 需要一定程度的网络灵活性。

### 解决方案

#### 摘要网络的物理结构

您需要做的第一件事是抽象网络的物理结构。有几种方法可以做到这一点：

*   停止硬编码IP - 您应该更喜欢使用主机名。通过使用URI，我们依靠DNS将主机名解析为IP。
*   当DNS不够时（例如，当您需要映射IP和端口时），则使用发现服务。
*   Service Bus框架还可以提供位置透明性。

#### 无价值的，而非重要的

通过将您的服务器视为没有价值的，而不是很重要的，您确保没有服务器是不可替代的。这一点智慧可以帮助您进入正确的思维模式：任何服务器都可能出现故障（从而改变拓扑结构），因此您应该尽可能地自动化。

#### 测试

最后一条建议是测试你的假设。停止服务或关闭服务器，看看您的系统是否仍在运行。像Netflix的Chaos Monkey这样的工具可以通过随机关闭生产环境中的VM或容器来实现这一目标。通过带来痛苦，您更有动力构建一个可以处理拓扑更改的更具弹性的系统。

### 结论

十年前，大多数拓扑结构并没有经常改变。但是当它发生时，它可能发生在生产中并引入了一些停机时间。如今，随着云和容器的增加，很难忽视这种谬误。你需要为失败做好准备并进行测试。不要等到它在生产中发生！

## 6.有一位管理员

### 问题

> 这个知道一切的并不存在。

嗯，这个看起来很明显。当然，没有一个人知道一切。这是一个问题吗？只要应用程序运行顺利，它就不是。但是，当出现问题时，您需要修复它。因为很多人触摸了应用程序，知道如何解决问题的人可能不在那里。

有很多事情可能会出错。一个例子是配置。今天的应用程序在多个商店中存储配置：配置文件，环境变量，数据库，命令行参数。没有人知道每个可能的配置值的影响是什么。

另一件可能出错的事情是系统升级。分布式应用程序有许多移动部件，您需要确保它们是同步的。例如，您需要确保当前版本的代码适用于当前版本的数据库。如今，人们关注DevOps和持续交付。但支持零停机部署并非易事。

但是，至少这些东西都在你的控制之下。许多应用程序与第三方系统交互。这意味着，如果它们失效，你可以做的事情就不多了。因此，即使您的系统有一名管理员，您仍然无法控制第三方系统。

### 解决方案

#### 每个人都应对释放过程负责

这意味着从一开始就涉及Ops人员或系统管理员。理想情况下，他们将成为团队的一员。尽早让系统管理员了解您的进度可以帮助您发现限制因素。例如，生产环境可能具有与开发环境不同的配置，安全限制，防火墙规则或可用端口。

#### 记录和监控

系统管理员应该拥有用于错误报告和管理问题的正确工具。你应该从一开始就考虑监控。分布式系统应具有集中式日志。访问十个不同服务器上的日志以调查问题是不可接受的方法。

#### 解耦

您应该在系统升级期间争取最少的停机时间。这意味着您应该能够独立升级系统的不同部分。通过使组件向后兼容，您可以在不同时间更新服务器和客户端。

通过在组件之间放置队列，您可以暂时将它们分离。这意味着，例如，即使后端关闭，Web服务器仍然可以接受请求。

#### 隔离第三方依赖关系

您应该以不同于您拥有的组件的方式处理控制之外的系统。这意味着使您的系统更能适应第三方故障。您可以通过引入抽象层来减少外部依赖的影响。这意味着当第三方系统出现故障时，您将找到更少的地方来查找错误。

### 结论

要解决这个谬论，您需要使系统易于管理。DevOps，日志记录和监控可以提供帮助。您还需要考虑系统的升级过程。如果升级需要数小时的停机时间，则无法部署每个sprint。没有一个管理员，所以每个人都应该对发布过程负责。

## 7.运输成本为零

### 问题

> 运输成本_不是_零。

这种谬论与第二个谬误有关，即 延迟为零。通过网络传输内容在时间和资源上都有代价。如果第二个谬误讨论了时间方面，那么谬误＃7就会解决资源消耗问题。

这种谬论有两个不同的方面：

#### 网络基础设施的成本

网络基础设施需要付出代价。服务器，SAN，网络交换机，负载平衡器以及负责此设备的人员 - 所有这些都需要花钱。如果您的系统是在内部部署的，那么您需要预先支付这个价格。如果您正在使用云，那么您只需为您使用的内容付费，但您仍然需要付费。

#### 序列化/反序列化的成本

这种谬误的第二个方面是在传输级别和应用程序级别之间传输数据的成本。序列化和反序列化会消耗CPU时间，因此需要花钱。如果您的应用程序是内部部署的，那么如果您不主动监视资源消耗，则会隐藏此成本。但是，如果您的应用程序部署在云端，那么这笔费用就会非常明显，因为您需要为使用的内容付费。

### 解决方案

关于基础设施的成本，你无能为力。您只能确保尽可能高效地使用它。SOAP或XML比JSON更昂贵。JSON比像Google的Protocol Buffers这样的二进制协议更昂贵。根据系统的类型，这可能或多或少重要。例如，对于与视频流或VoIP有关的应用，传输成本更为重要。

### 结论

您应该注意运输成本以及应用程序正在执行的序列化和反序列化程度。这并不意味着您应该优化，除非需要它。您应该对资源消耗进行基准测试和监控，并确定运输成本是否对您有用。

## 8.网络是同质的

### 问题

> 网络_不是_同质的。

同质网络是使用类似配置和相同通信协议的计算机网络。拥有类似配置的计算机是一项艰巨的任务。例如，您几乎无法控制哪些移动设备可以连接到您的应用。这就是为什么重点关注标准协议。

### 解决方案

您应该选择标准格式以避免供应商锁定。这可能意味着XML，JSON或协议缓冲区。有很多选择可供选择。

### 结论

您需要确保系统的组件可以相互通信。使用专有协议会损害应用程序的互操作性。

## 设计分布式系统很难

这些谬论发表于20多年前。但他们今天仍然坚持，其中一些比其他人更多。我认为今天许多开发人员都知道它们，但我们编写的代码并没有显示出来。

我们必须接受这些事实：网络不可靠，不安全并且需要花钱。带宽有限。网络的拓扑结构将发生变化。其组件的配置方式不同。意识到这些限制将有助于我们设计更好的分布式系统。

## 参考文章


原文标题 [《Understanding the 8 Fallacies of Distributed Systems》](https://dzone.com/articles/understanding-the-8-fallacies-of-distributed-syste)

作者：Victor Chircu

译者：February

译文： [https://cloud.tencent.com/developer/article/1370391](https://cloud.tencent.com/developer/article/1370391)
', 0, '2022-10-08 19:13:38', '2022-10-08 19:23:35');

INSERT INTO forum.article_detail
    (article_id, version, content, deleted, create_time, update_time)
VALUES (102, 2, '![](https://files.mdnice.com/user/6227/3452f706-fdf4-4a60-b449-e1f8b94a2db5.png)


分布式的概念存在年头有点久了，在正式进入我们《分布式专栏》之前，感觉有必要来聊一聊，什么是分布式，分布式特点是什么，它又有哪些问题，在了解完这个概念之后，再去看它的架构设计，理论奠基可能帮助会更大

本文将作为专栏的第0篇，将从三个方面来讲述一下我理解的"分布式系统"

- 分布式系统的特点
- 分布式系统面临的挑战
- 如何衡量一个分布式系统

## 1.分布式系统特点

什么是分布式系统，看一下wiki上的描述

### 1.1 定义


> 分布式系统（distributed system）是建立在网络之上的软件系统。正是因为软件的特性，所以分布式系统具有高度的内聚性和透明性。因此，网络和分布式系统之间的区别更多的在于高层软件（特别是操作系统），而不是硬件
> * [分布式系统（建立在网络之上的软件系统）\_百度百科](https://baike.baidu.com/item/%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F/4905336)

---

> 分布式操作系统（Distributed operating system），是一种软件，它是许多独立的，网络连接的，通讯的，并且物理上分离的计算节点的集合[1]。每个节点包含全局总操作系统的一个特定的软件子集。每个软件子集是两个不同的服务置备的复合物[2]。第一个服务是一个普遍存在的最小的内核，或微内核，直接控制该节点的硬件。第二个服务是协调节点的独立的和协同的活动系统管理组件的更高级别的集合。这些组件抽象微内核功能，和支持用户应用程序[3]。
> * [分布式操作系统 - 维基百科，自由的百科全书](https://zh.m.wikipedia.org/zh-hans/%E5%88%86%E5%B8%83%E5%BC%8F%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F)

---

> Distributed system: is a system in which components located on networked computers communicate and coordinate their actions by passing messages. The components interact with each other in order to achieve a common goal[3].
> * [Distributed systems - Computer Science Wiki](https://computersciencewiki.org/index.php/Distributed_systems)



虽然上面几个描述不完全相同，但是含义其实也差不了太多；基于我个人的理解，用大白话来描述一下分布式系统，就是“一个系统的服务能力，由网络上多个节点共同提供”，正如其名的“分布一词”

在了解完分布式系统的概念之后，接下来抓住其主要特点，来加深这个分布式的理解

### 1.2 分布性

分布式系统分布在多个节点（可以理解为多个计算机），这些节点可以是网络上任意的一台计算机，即在空间上没有原则性的限制

### 1.3 对等性

分布式系统中有很多的节点，这些节点之间没有主从、优劣直说，它们应该是对等的，从服务能力来说，访问分布式系统中的任何一个节点，整个服务请求应该都是等价的

看到这里可能就会有一个疑问了，分布式系统中经典主从架构，数据拆分架构，就不满足这个对等特性了啊（这个问题先留着，后续再详情中进行解答）

### 1.4 自治性

分布式系统中的各个节点都有自己的计算能力，各自具有独立的处理数据的功能。通常，彼此在地位上是平等的，无主次之分，既能自治地进行工作，又能利用共享的通信线路来传送信息，协调任务处理。

### 1.5 并发性

分布式系统既然存在多个节点，那么天然就存在多个节点的同事响应请求的能力，即并发性支持，如何做好分布式系统的并发控制则是所有分布式系统需要解决的一个问题


## 2. 分布式系统面临的问题

当系统分布在多个节点之上时，自然而然就带来了很多单机场景下不会有问题，如经典的 [分布式系统的8个谬误 | 一灰灰Learning](https://hhui.top/%E5%88%86%E5%B8%83%E5%BC%8F/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/02.%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F%E7%9A%848%E4%B8%AA%E8%B0%AC%E8%AF%AF/)


### 2.1 全局时钟

分布式系统的多个节点，如何保证每个节点的时钟一致？这个是需要重点考虑的问题

我们知道大名鼎鼎的分布式主键生成算法 “雪花算法” 就是利用了机器时钟来作为算法因子，如果一个系统的多个节点不能保证时钟统一，那这个算法的唯一性将无法得到保障

### 2.2 网络延迟、异常

网络是有开销的，多个节点之间的通信是有成本的，既然存在网络的开销、或异常状况，那么如何保证多个节点的数据一致性呢？ 当无法保证数据的一致性时，如何提供分布式系统的对等性呢？

在经典的CAP理论中，对于P（网络分区）一般都是需要保障的，一个系统存在多个计算节点，那么网络问题将不可避免，网络分区必然会存在


### 2.3 数据一致性

如何保证所有节点中的数据完全一致是一个巨大的挑战，这个问题比较好理解，我们操作分布式系统中的一个节点实现数据修改，如果要保证数据一致性，则要求所有的节点，同步收到这个修改

但是我们需要注意的时，网络是不可靠的，且网络的传输是存在延迟的，如何衡量数据的一致性和服务的可用性则是我们在设计一个分布式系统中需要取舍的

### 2.4 节点异常

机器宕机属于不可抗力因素，如果分布式系统中的一个节点宕机了，整个系统会怎么样？要如何确保机器宕机也不会影响系统的可用性呢？ 机器恢复之后，又应该如何保证数据的一致性呢？ 又应该如何判断一个节点是否正常呢？


### 2.5 资源竞争

前面说到分布式系统天然支持并发，那么随之而来的问题则是如何资源竞争的问题；当一个资源同一时刻只允许一个实例访问时，怎么处理？多个系统同时访问一个资源是否会存在数据版本差异性（如经典的ABA问题）、数据一致性问题？

基于这个问题，分布式锁可以说是应运而生，相信各位开发大佬都不会陌生这个知识点

### 2.6 全局协调

这个协调怎么理解呢？ 举几个简单的实例

- 如何判断分布式系统中那些节点正常提供服务，那些节点故障
- 如一个任务希望在分布式系统中只执行一次，那么应该哪个节点执行这个任务呢？
- 如一组有先后顺序的请求发送给分布式系统，但是由于网络问题，可能出现后面的请求先被系统接收到，这种场景怎么处理呢？
- 一个用户已经登录，如何在所有节点中确认他的身份呢？

### 2.7 一灰灰的小结

实际上分布式系统面临的挑战并不止于上面这些，一个具体的系统面临的问题可能各不相同，但总的来说，分布式系统的理论基础会给我们非常好的指引方向，这一节推荐查看 * [分布式设计模式综述 | 一灰灰Learning](https://hhui.top/%E5%88%86%E5%B8%83%E5%BC%8F/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/01.%E5%88%86%E5%B8%83%E5%BC%8F%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F%E7%BB%BC%E8%BF%B0/)


## 3. 分布式系统的衡量指标

最后再来看一下如何衡量一个分布式系统的“好差”，它的指标有哪些

### 3.1 性能指标

常见的性能指标如rt, QPS, TPS来判断一个系统的承载能力，重点关注是哪个要点

- 响应延迟
- 并发能力
- 事务处理能力

### 3.2 可用性

这个就是传说中你的系统达到几个9的那个指标，即系统的异常时间占总的可用时间的比例

统的可用性可以用系统停服务的时间与正常服务的时间的比例来衡量，也可以用某功能的失败次数与成功次数的比例来衡量。可用性是分布式的重要指标，衡量了系统的鲁棒性，是系统容错能力的体现。


### 3.3 扩展性

系统的可扩展性(scalability)指分布式系统通过扩展集群机器规模提高系统性能（吞吐、延迟、并发）、存储容量、计算能力的特性

最简单来讲，就是你的系统能不能直接加机器，来解决性能瓶颈，如果能加机器，有没有上限（如由于数据库的连接数限制了机器的数量上限， 如机器加到某个程度之后，服务能力没有明显提升）


### 3.4 一致性

分布式系统为了提高可用性，总是不可避免的使用副本的机制，从而引发副本一致性的问题。越是强的一致的性模型，对于用户使用来说使用起来越简单


## 4. 总结

这一篇文章相对来说比较干燥，全是文字描述，介绍下什么是分布式系统，分布系统的特点及面对的问题和衡量指标，提炼一下关键要素，如下


分布式系统的特点

- 分布性
- 对等性
- 并发性
- 自治性

分布式系统面临的挑战

- 全局时钟
- 网络延迟、异常
- 数据一致性
- 节点异常
- 资源竞争
- 全局协调

分布式系统衡量指标

- 性能指标
- 可用性
- 扩展性
- 一致性

<small>

我是一灰灰，欢迎感兴趣的小伙伴关注最近持续更新的分布式专栏：

* [分布式常用的设计模式 | 一灰灰Learning](https://hhui.top/%E5%88%86%E5%B8%83%E5%BC%8F/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/)

最后强烈推荐阅读下面两个万字干货

* [1w5字详细介绍分布式系统的38个技术方案](https://mp.weixin.qq.com/s?__biz=MzU3MTAzNTMzMQ==&mid=2247487507&idx=1&sn=9c4ff02747e8335ee5e3c7765cc80b3c&chksm=fce70bbfcb9082a9a8d972af80f19a9b66a5425c949bc400872727cc2da9f401047a5a523ac4&token=1624762777&lang=zh_CN#rd)
* [基于MySql,Redis,Mq,ES的高可用方案解析](https://mp.weixin.qq.com/s?__biz=MzU3MTAzNTMzMQ==&mid=2247487533&idx=1&sn=cd07d5d601986fd3911858ea5f3a18d4&chksm=fce70b81cb908297fe66eac564028a6c7ef197f8f10921c4dfe05cf8d433b5ee45566099e467&token=1624762777&lang=zh_CN#rd)

</small>', 0, '2022-10-08 19:14:17', '2022-10-08 19:23:43');

-- 将文章绑定到专栏

INSERT INTO forum.column_article
    (column_id, article_id, `section`)
VALUES (1, 100, 1);

INSERT INTO forum.column_article
    (column_id, article_id, `section`)
VALUES (1, 101, 2);

INSERT INTO forum.column_article
    (column_id, article_id, `section`)
VALUES (1, 102, 3);
