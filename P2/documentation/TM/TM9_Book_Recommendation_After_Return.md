# Technical Memo 9 - Book Recommendation After Return

## Problema:
Permitir que os leitores recomendem (positivamente ou negativamente) um livro após a sua devolução, garantindo que essa funcionalidade não impacte negativamente o desempenho do sistema e que a informação seja propagada de forma eficiente para outros serviços.

## Como resolver este problema:
Implementar um fluxo baseado em **CQRS** para separar operações de escrita (criação de recomendações) e leitura (consulta de recomendações). Utilizar **Messaging** (RabbitMQ) para comunicação assíncrona entre os serviços e **Domain Events** para notificar outros serviços sobre novas recomendações.

## Resumo da solução:
Ao devolver um livro, o leitor poderá avaliá-lo positivamente ou negativamente. O sistema registrará a recomendação usando **CQRS** para otimizar as operações de leitura e escrita. As mensagens de recomendação serão processadas de forma assíncrona utilizando RabbitMQ, enquanto **Domain Events** serão utilizados para notificar outros serviços relevantes, como sistemas de recomendação ou estatísticas.

## Fatores:
- A experiência do utilizador ao recomendar um livro deve ser rápida e responsiva.
- As recomendações devem ser propagadas eficientemente para outros serviços que as utilizam.
- O sistema deve ser escalável para suportar um grande número de recomendações.

## Solução:
1. **CQRS**:
   - Implementar um modelo de leitura otimizado para consultas sobre as recomendações.
   - Usar um modelo de gravação separado para processar as novas recomendações.
2. **Messaging (RabbitMQ)**:
   - Utilizar RabbitMQ para enviar mensagens sobre novas recomendações para outros serviços, como análise de dados e sistemas de sugestão de leitura.
3. **Domain Events**:
   - Disparar eventos de domínio ao registar uma recomendação, permitindo que outros serviços, como notificações ou sistemas de ranking, sejam atualizados automaticamente.
4. **Persistência**:
   - Adotar **Polyglot Persistence** para armazenar dados de leitura em bases de dados otimizadas para consultas rápidas e dados de gravação em sistemas transacionais.

## Motivação:
Separar as operações de leitura e escrita com **CQRS** e utilizar comunicação assíncrona garante que o sistema permaneça ágil, escalável e resiliente, mesmo sob alta demanda. O uso de **Domain Events** permite a integração transparente com outros serviços, assegurando que os dados de recomendação sejam utilizados de forma eficiente em toda a aplicação.

## Alternativas:
- **Processamento Síncrono**:
  - Permitir que a recomendação seja processada no momento da devolução do livro. Isso poderia impactar negativamente a experiência do utilizador e o desempenho do sistema em períodos de alta demanda.
- **Sistema Monolítico**:
  - Centralizar todas as operações num único serviço monolítico. Isso aumentaria a complexidade e dificultaria a escalabilidade, além de criar dependências fortes entre os serviços.

## Questões pendentes:
- Como priorizar o processamento das recomendações em caso de alta demanda no RabbitMQ?
- Como integrar as recomendações aos sistemas de sugestão de leitura existentes?
- Qual o impacto de performance ao processar grandes volumes de recomendações em tempo real?
