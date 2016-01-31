import grails.plugin.redis.basic.RedisService
import grails.plugin.redis.basic.config.RedisBasicConfig

class BasicRedisGrailsPlugin {

    def version = '0.1'
    def grailsVersion = '2.4 > *'
    def pluginExcludes = [
        'grails-app/views/error.gsp'
    ]
    def title = 'Basic Redis Plugin' // Headline display name of the plugin
    def author = 'Pablo Vilas'
    def authorEmail = 'pablovilas89@gmail.com'
    def description = '''\
        Basic Redis plugin that wraps jedis library and takes grails-redis plugin configuration style
    '''
    def documentation = 'http://grails.org/plugin/basic-redis'
    def license = 'APACHE'
    def scm = [ url: 'https://github.com/pablovilas/basic-redis' ]

    def doWithSpring = {
        def configureService = RedisBasicConfig.configureService
        def redisConfigMap = application.config.grails.redis ?: [:]

        configureService.delegate = delegate
        configureService(redisConfigMap, "", RedisService)
        redisConfigMap?.connections?.each { connection ->
            configureService(connection.value, connection?.key?.capitalize(), RedisService)
        }
    }
}
