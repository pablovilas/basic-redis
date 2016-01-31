grails.project.groupId = 'grails.plugin.redis.basic'
grails.views.default.codec = 'none'
grails.views.gsp.encoding = 'UTF-8'

grails {
       redis {
              poolConfig {
                     maxIdle = 10
                     doesnotexist = true
              }
       }
}