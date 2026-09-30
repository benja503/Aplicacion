package com.example.data

import kotlin.random.Random

data class MotivationQuote(
    val quote: String,
    val author: String,
    val tag: String = "Disciplina"
)

object MotivationQuotesProvider {
    val quotes = listOf(
        MotivationQuote(
            quote = "La disciplina es el puente entre las metas y los logros.",
            author = "Jim Rohn",
            tag = "Disciplina"
        ),
        MotivationQuote(
            quote = "No tienes que ser un genio, solo tienes que ser más constante que los demás.",
            author = "Anónimo",
            tag = "Constancia"
        ),
        MotivationQuote(
            quote = "El éxito es la suma de pequeños esfuerzos repetidos día tras día.",
            author = "Robert Collier",
            tag = "Hábito"
        ),
        MotivationQuote(
            quote = "La energía de hoy forja la libertad de tu mañana.",
            author = "Séneca",
            tag = "Estoicismo"
        ),
        MotivationQuote(
            quote = "No pares cuando estés cansado, para cuando hayas terminado.",
            author = "David Goggins",
            tag = "Fuerza mental"
        ),
        MotivationQuote(
            quote = "Un río corta la roca no por su fuerza, sino por su persistencia.",
            author = "Jim Watkins",
            tag = "Persistencia"
        ),
        MotivationQuote(
            quote = "Estudiar no es llenar un balde, sino encender un fuego.",
            author = "William Butler Yeats",
            tag = "Curiosidad"
        ),
        MotivationQuote(
            quote = "Tu futuro se crea con lo que haces hoy, no con lo que prometes hacer mañana.",
            author = "Robert Kiyosaki",
            tag = "Acción"
        ),
        MotivationQuote(
            quote = "Cada página leída y cada problema resuelto es un paso hacia tu mejor versión.",
            author = "RACHA",
            tag = "Superación"
        ),
        MotivationQuote(
            quote = "La motivación te pone en marcha; el hábito es lo que te mantiene avanzando.",
            author = "Jim Ryun",
            tag = "Hábito"
        ),
        MotivationQuote(
            quote = "El dolor de la disciplina pesa gramos; el dolor del arrepentimiento pesa toneladas.",
            author = "Jim Rohn",
            tag = "Disciplina"
        ),
        MotivationQuote(
            quote = "Somos lo que hacemos repetidamente. La excelencia, entonces, no es un acto sino un hábito.",
            author = "Aristóteles",
            tag = "Excelencia"
        ),
        MotivationQuote(
            quote = "No cuentes los días, haz que los días cuenten.",
            author = "Muhammad Ali",
            tag = "Actitud"
        ),
        MotivationQuote(
            quote = "Trabaja en silencio, deja que tu éxito haga todo el ruido.",
            author = "Frank Ocean",
            tag = "Foco"
        ),
        MotivationQuote(
            quote = "La mente que se abre a una nueva idea jamás vuelve a su tamaño original.",
            author = "Albert Einstein",
            tag = "Crecimiento"
        ),
        MotivationQuote(
            quote = "La concentración implacable vence al talento disperso en cualquier disciplina.",
            author = "Cal Newport",
            tag = "Deep Work"
        ),
        MotivationQuote(
            quote = "Hoy hiciste lo que la mayoría posterga. Mantén encendido ese fuego.",
            author = "RACHA",
            tag = "Racha"
        ),
        MotivationQuote(
            quote = "La paciencia y el estudio sistemático transforman lo difícil en natural.",
            author = "Marie Curie",
            tag = "Ciencia"
        ),
        MotivationQuote(
            quote = "No busques el momento perfecto; toma el momento y hazlo perfecto estudiando.",
            author = "Anónimo",
            tag = "Determinación"
        ),
        MotivationQuote(
            quote = "Quien tiene un porqué para vivir, puede soportar casi cualquier cómo.",
            author = "Friedrich Nietzsche",
            tag = "Propósito"
        ),
        MotivationQuote(
            quote = "El conocimiento es la única riqueza de la que los tiranos no pueden despojarte.",
            author = "Antístenes",
            tag = "Sabiduría"
        ),
        MotivationQuote(
            quote = "Solo un 1% mejor cada día te hace 37 veces mejor en un año.",
            author = "James Clear",
            tag = "Hábitos Atómicos"
        ),
        MotivationQuote(
            quote = "El conocimiento acumulado con disciplina es el interés compuesto de la mente.",
            author = "Naval Ravikant",
            tag = "Compuesto"
        ),
        MotivationQuote(
            quote = "Cuando sientas pereza, recuerda por qué empezaste este camino.",
            author = "RACHA",
            tag = "Claridad"
        ),
        MotivationQuote(
            quote = "Vencerte a ti mismo es la victoria más grande y noble.",
            author = "Platón",
            tag = "Autocontrol"
        ),
        MotivationQuote(
            quote = "La maestría no es un destino, es un camino continuo de práctica deliberada.",
            author = "George Leonard",
            tag = "Maestría"
        ),
        MotivationQuote(
            quote = "No temas avanzar lento, teme únicamente quedarte quieto.",
            author = "Proverbio Chino",
            tag = "Progreso"
        ),
        MotivationQuote(
            quote = "Un día más de estudio, un paso más cerca de tu meta.",
            author = "RACHA",
            tag = "Constancia"
        )
    )

    fun getRandomQuote(excludeIndex: Int = -1): Pair<Int, MotivationQuote> {
        val availableIndices = quotes.indices.filter { it != excludeIndex }
        val nextIndex = if (availableIndices.isNotEmpty()) {
            availableIndices[Random.nextInt(availableIndices.size)]
        } else {
            Random.nextInt(quotes.size)
        }
        return nextIndex to quotes[nextIndex]
    }
}
