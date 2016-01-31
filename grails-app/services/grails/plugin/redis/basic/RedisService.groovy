package grails.plugin.redis.basic

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Transaction

import javax.annotation.PreDestroy

class RedisService {

    def JedisPool redisPool

    @PreDestroy
    public void cleanUp() throws Exception {
        redisPool.destroy()
    }

    def withRedis(Closure closure) {
        Jedis redis = redisPool.resource
        try {
            return closure(redis)
        } finally {
            if (redis != null) {
                redis.close()
            }
        }
    }

    def withTransaction(Closure closure) {
        withRedis { Jedis redis ->
            Transaction transaction = redis.multi()
            try {
                closure(transaction)
            } catch(Exception ex) {
                transaction.discard()
                throw ex
            }
            return transaction.exec()
        }
    }

    def withRedisPool(Closure closure) {
        return closure(redisPool)
    }

    def methodMissing(String name, args) {
        def value = null
        withRedis { Jedis redis ->
            value = redis.invokeMethod(name, args)
        }
        return value
    }
}
