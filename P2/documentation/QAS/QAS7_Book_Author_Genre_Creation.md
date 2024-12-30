# Quality Attribute Scenario 7 - Book, Author, Genre Creation

| **Elemento**            | **Declaração**                                                                                             |
|--------------------------|-----------------------------------------------------------------------------------------------------------|
| **Estímulo**            | O sistema deve permitir a criação simultânea de um Livro, Autor e Género, garantindo consistência e eficiência. |
| **Fonte do Estímulo**   | Um utilizador realiza uma operação de criação que envolve Livro, Autor e Género no mesmo fluxo de trabalho. |
| **Ambiente**            | O sistema opera em um ambiente distribuído com microserviços e comunicação assíncrona.                    |
| **Artefato**            | Serviços responsáveis pela criação de Livro, Autor e Género.                                              |
| **Resposta**            | O sistema deve realizar as operações de criação de forma consistente, garantindo que, em caso de falha, as alterações sejam revertidas corretamente. |
| **Medição da Resposta** | 100% das transações devem ser completadas ou revertidas dentro de 5 segundos, com consistência garantida em todos os serviços. |
