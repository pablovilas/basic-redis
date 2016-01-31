package grails.plugin.redis.basic

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

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

    def withRedisPool(Closure closure) {
        return closure(redisPool)
    }

    def scriptLoad(String script) {
        def sha = null
        withRedis { Jedis redis ->
            sha = redis.scriptLoad(script)
        }
        return sha
    }

    def scriptExists(String sha) {
        def exists = false
        withRedis { Jedis redis ->
            exists = redis.scriptExists(sha)
        }
        return exists
    }

    def evalSha(String sha) {
        def value = false
        withRedis { Jedis redis ->
            value = redis.evalsha(sha)
        }
        return value
    }

    def evalSha(String sha, List<String> keys, List<String> args) {
        def value = false
        withRedis { Jedis redis ->
            value = redis.evalsha(sha, keys, args)
        }
        return value
    }

    def evalSha(String sha, int keyCount, String... params) {
        def value = false
        withRedis { Jedis redis ->
            value = redis.evalsha(sha, keyCount, params)
        }
        return value
    }
}
