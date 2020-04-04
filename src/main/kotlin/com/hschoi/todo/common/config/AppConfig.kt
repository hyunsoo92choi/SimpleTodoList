package com.hschoi.todo.common.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Configuration
class AppConfig {
    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper()
    }
}