package org.contourgara

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import org.contourgara.presentation.configureRouting

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(): Unit = configureRouting()
