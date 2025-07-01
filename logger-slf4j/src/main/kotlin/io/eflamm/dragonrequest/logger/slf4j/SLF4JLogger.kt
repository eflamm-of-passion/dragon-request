package io.eflamm.dragonrequest.logger.slf4j

import io.eflamm.dragonrequest.domain.monitoring.Logger
import org.slf4j.LoggerFactory

class SLF4JLogger(
    className: String,
) : Logger {
    private val logger = LoggerFactory.getLogger(className)

    override fun info(message: String) {
        logger.info(message)
    }

    override fun debug(message: String) {
        logger.debug(message)
    }

    override fun warn(message: String) {
        logger.warn(message)
    }

    override fun error(
        message: String,
        e: Throwable?,
    ) {
        logger.error(message)
        logger.error(e.toString())
    }

    override fun error(
        message: String,
        detailMessages: List<String>?,
        e: Throwable?,
    ) {
        logger.error(message)
        detailMessages?.let {
            it.forEach { detailMessage -> logger.error("↳ $detailMessage") }
        }
        logger.error(e.toString())
    }
}
