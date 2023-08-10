package com.dma.quiz_ph.Model

data class QnAResponse(
    val questions: List<Question>
) {
    data class Question(
        val answers: Answers,
        val correctAnswer: String,
        val question: String,
        val questionImageUrl: String,
        val score: Int
    ) {
        data class Answers(
            val A: String,
            val B: String,
            val C: String,
            val D: String
        )
    }
}