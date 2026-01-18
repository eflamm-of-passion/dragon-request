package io.eflamm.dragonrequest.domain.slf4jlogger

import io.eflamm.dragonrequest.domain.Logger
import org.slf4j.LoggerFactory

object LoggerFactory {
    fun forClass(type: Class<*>): Logger = Slf4jLogger(LoggerFactory.getLogger(type))
}