# Technical Memo 8 - Book Acquisition Suggestion

## Problema:
Permitir que os leitores sugiram a aquisição de novos livros de forma eficiente, transparente e escalável, sem comprometer a integridade do sistema e sem afetar negativamente a experiência do utilizador.

## Como resolver este problema:
Criar um sistema de sugestão de aquisição de livros utilizando **CQRS** para separar operações de leitura e escrita, **Messaging** (RabbitMQ) para comunicação assíncrona entre os serviços e **Domain Events** para notificar outros serviços sobre a criação de sugestões.

## Resumo da solução:
A implementação consiste em permitir que os leitores façam sugestões de aquisição de novos livros, com processamento assíncrono das sugestões e otimização de leitura através de **CQRS**. A sugestão será gerida por um serviço de comando e o estado do sistema será atualizado utilizando **Domain Events**, enquanto as consultas sobre sugestões de aquisição serão tratadas de forma independente para melhorar a performance.

## Fatores:
- As sugestões de aquisição devem ser tratadas de forma assíncrona para garantir que o sistema permaneça responsivo, mesmo em alta demanda.
- A separação de operações de leitura e escrita através de **CQRS** permite que a consulta sobre o status das sugestões seja feita de forma eficiente, sem afetar o desempenho do processamento das sugestões.
- A comunicação entre os serviços será feita de forma desacoplada, utilizando uma fila de mensagens (RabbitMQ), o que garante a escalabilidade e resiliência do sistema.

## Solução:
- **CQRS**: Implementar a separação entre operações de comando e consulta. As sugestões de aquisição de livros serão tratadas como comandos que criam uma nova sugestão, enquanto as consultas serão feitas por um serviço separado, otimizado para leitura.
- **Messaging (RabbitMQ)**: Usar RabbitMQ para comunicação entre os serviços. Quando um leitor faz uma sugestão de aquisição, uma mensagem é gerada e enviada para os serviços responsáveis pela validação e processamento da sugestão.
- **Domain Events**: Disparar eventos de domínio para notificar outros sistemas ou serviços sobre a criação de uma sugestão de livro, garantindo que a informação seja propagada de forma eficaz.
- **Persistência**: Usar uma estratégia de **Polyglot Persistence**, onde diferentes tipos de base de dados podem ser utilizados conforme o modelo de dados (por exemplo, uma base NoSQL para consultas rápidas de sugestões, e uma base SQL para armazenar transações).

## Motivação:
A sugestão de aquisição de livros é uma funcionalidade que pode ser muito popular, especialmente quando o número de leitores aumenta. A solução com **CQRS**, **Messaging** e **Domain Events** proporciona uma forma escalável e eficiente de lidar com a crescente quantidade de sugestões, sem prejudicar o desempenho ou a experiência do utilizador.

## Alternativas:
- **Processamento Síncrono**: A sugestão poderia ser processada de forma síncrona, o que implicaria uma experiência de utilizador mais lenta e uma possível sobrecarga do sistema em períodos de alta demanda.
- **Monolito**: Poderíamos manter tudo em um único serviço monolítico, mas isso aumentaria a complexidade do sistema, dificultaria a escalabilidade e faria com que as operações de leitura e escrita interferissem umas nas outras.

## Questões pendentes:
- Quais são as regras de negócios para validar as sugestões de aquisição de livros? Como garantir que apenas sugestões relevantes sejam processadas?
- Qual é o impacto de performance ao usar **RabbitMQ** para comunicação assíncrona entre serviços, especialmente em alta demanda?
- Como gerir a consistência de dados entre os serviços, especialmente em caso de falhas ou mensagens perdidas?

