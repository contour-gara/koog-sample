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

        get("/deepseek") {
            runBlocking {
                // Get an API key from the DEEPSEEK_API_KEY environment variable
                val apiKey = System.getenv("DEEPSEEK_API_KEY")
                    ?: error("The API key is not set.")

                // Create an LLM client
                val deepSeekClient = DeepSeekLLMClient(apiKey)

                // Create an agent
                val agent = AIAgent(
                    // Create a prompt executor using the LLM client
                    promptExecutor = SingleLLMPromptExecutor(deepSeekClient),
                    // Provide a model
                    llmModel = DeepSeekModels.DeepSeekChat
                )

                // Run the agent
                val result = agent.run("Hello! How can you help me?")
                println(result)
                call.respondText(result)
            }
        }

        post("/translate") {
            try {
                val request = call.receive<TranslateRequest>()
                call.respond(HttpStatusCode.OK, TranslateResponse("Hello World!"))
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        singlePageApplication {
            useResources = true
            filesPath = "frontend"
            defaultPage = "index.html"
        }
    }
}
