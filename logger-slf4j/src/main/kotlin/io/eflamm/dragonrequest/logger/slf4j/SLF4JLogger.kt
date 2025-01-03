package io.eflamm.dragonrequest.logger.slf4j

import io.eflamm.dragonrequest.domain.monitoring.Logger
import org.slf4j.LoggerFactory

class SLF4JLogger: Logger {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun info(message: String) {
        logger.info(message)
    }

    override fun debug(message: String) {
        logger.debug(message)
    }

    override fun warn(message: String) {
        logger.warn(message)
    }

    override fun error(message: String, e: Throwable?) {
        logger.error(message, e)
    }

}