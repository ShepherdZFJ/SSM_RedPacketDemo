# 用SSM模拟多线程高并发抢红包场景，并分别用悲观锁，乐观锁，redis改善性能及问题
1. 有超发现象，解决方法有：加悲观锁，乐观锁，用redis+lua缓存
2. 请求完成之后有大量剩余，解决办法：加时间戳，按次数重入继续抢红包。
3. 总结：三种办法都可以消除数据不一致性，加悲观锁很简单(for update),这是按照数据库锁机制实现的，但使用悲观锁数据库性能有所下降，线程大量阻塞；通过CAS原理实现的乐观锁有助于提高并发性能，但是其会产生ABA问题，所以需要添加版本号字段进行判断，由于版本号冲突，导致大量的抢红包请求失败，这里可以添加时间戳，按次数重入抢红包来提高，使用乐观锁的缺点是大量sql被执行，增加了数据库的压力；redis+lua脚本能很好的解决上述问题，但是redis的事务和存储都存在不稳定的因素
