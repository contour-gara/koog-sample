package org.contourgara.presentation

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.deepseek.DeepSeekLLMClient
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/translate") {
            try {
                runBlocking {
                    val apiKey = System.getenv("DEEPSEEK_API_KEY")
                        ?: error("The API key is not set.")

                    val agent = AIAgent(
                        promptExecutor = SingleLLMPromptExecutor(DeepSeekLLMClient(apiKey)),
                        llmModel = DeepSeekModels.DeepSeekChat,
                        systemPrompt = """
                            You are a native-level English translator who specializes in casual, conversational language.
                            Translate the Japanese text into friendly, natural English as it would appear in casual speech or social media posts.
                            Keep the tone consistent with the original (humorous, emotional, etc.), and avoid sounding robotic or overly formal.
                        """.trimIndent(),
                        temperature = 0.5,
                    )

                    agent.run(call.receive<TranslateRequest>().text)
                        .let { TranslateResponse(it) }
                        .also { call.respond(HttpStatusCode.OK, it) }
                }
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest, ex.message ?: "Bad Request")
            } catch (ex: SerializationException) {
                call.respond(HttpStatusCode.BadRequest, ex.message ?: "Bad Request")
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ex.message ?: "Internal Server Error")
            }
        }

        singlePageApplication {
            useResources = true
            filesPath = "frontend"
            defaultPage = "index.html"
        }
    }
}
