package org.contourgara.presentation

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.deepseek.DeepSeekLLMClient
import ai.koog.prompt.executor.clients.deepseek.DeepSeekModels
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking

fun Application.configureRouting() {
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
    }
}
