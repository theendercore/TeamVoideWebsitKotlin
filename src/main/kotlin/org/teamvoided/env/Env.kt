package org.teamvoided.env

import java.lang.System.getenv

private const val PORT: Int = 8080
private const val JDBC_URL: String = "jdbc:postgresql://localhost:5432/team-voided-website-db"
private const val JDBC_USER: String = "ender"
private const val JDBC_PW: String = "ender"
private const val JDBC_DRIVER: String = "org.postgresql.Driver"

data class Env(val dataSource: DataSource = DataSource(), val http: Http = Http()) {
    data class Http(
        val host: String = getenv("HOST") ?: "0.0.0.0",
        val port: Int = getenv("SERVER_PORT")?.toIntOrNull() ?: PORT,
    )

    data class DataSource(
        val url: String = getenv("POSTGRES_URL") ?: JDBC_URL,
        val username: String = getenv("POSTGRES_USERNAME") ?: JDBC_USER,
        val password: String = getenv("POSTGRES_PASSWORD") ?: JDBC_PW,
        val driver: String = JDBC_DRIVER,
    )
}