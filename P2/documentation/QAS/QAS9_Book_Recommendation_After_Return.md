# Quality Attribute Scenario 9 - Book Recommendation After Return

| **Elemento**            | **Declaração**                                                                                             |
|-------------------------|-----------------------------------------------------------------------------------------------------------|
| **Estímulo**            | O sistema deve permitir que os leitores recomendem (positivamente ou negativamente) um livro após a devolução. |
| **Fonte do Estímulo**   | Um leitor devolve um livro e faz uma recomendação sobre ele (positiva ou negativa).                       |
| **Ambiente**            | O sistema opera em um ambiente distribuído, com serviços desacoplados e comunicação assíncrona via RabbitMQ. |
| **Artefato**            | Serviço de gestão de recomendações de livros.                                                              |
| **Resposta**            | O sistema deve registrar a recomendação de forma eficiente e propagar essa informação para os serviços relevantes, sem impacto na experiência do utilizador. |
| **Medição da Resposta** | 100% das recomendações devem ser processadas de forma assíncrona em até 2 segundos, com integridade garantida entre os serviços. |
