package org.teamvoided.env

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.autoCloseable
import arrow.fx.coroutines.closeable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.teamvoided.database.SqlDelight

suspend fun ResourceScope.hikari(env: Env.DataSource): HikariDataSource = autoCloseable {
    HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = env.url
            username = env.username
            password = env.password
            driverClassName = env.driver
        }
    )
}


suspend fun ResourceScope.sqlDelight(dataSource: HikariDataSource): SqlDelight {
    val driver = closeable { dataSource.asJdbcDriver() }
    SqlDelight.Schema.create(driver)
    return SqlDelight(driver)
}
