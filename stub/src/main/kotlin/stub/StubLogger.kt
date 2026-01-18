package stub

import io.eflamm.dragonrequest.domain.Logger

object StubLogger : Logger {
    override fun debug(message: () -> String) {
    }

    override fun info(message: () -> String) {
    }

    override fun warn(message: () -> String) {
    }

    override fun error(message: () -> String, cause: Throwable?) {
    }
}