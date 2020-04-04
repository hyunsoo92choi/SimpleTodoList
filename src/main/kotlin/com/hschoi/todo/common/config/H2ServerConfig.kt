package com.hschoi.todo.common.config

import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Configuration
class H2ServerConfig {
    @Bean
    @Throws(SQLException::class)
    fun h2TcpServer(): Server? {
        return Server.createTcpServer().start()
    }
}