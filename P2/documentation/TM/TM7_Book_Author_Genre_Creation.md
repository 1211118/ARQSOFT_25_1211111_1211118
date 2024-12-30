# Technical Memo 7 - Book, Author, Genre Creation

## Problema:
O sistema precisa permitir a criação de um livro, autor e género de maneira eficiente e integrada no mesmo processo. Isso envolve garantir a criação e associação desses itens de forma transparente, minimizando erros e mantendo a consistência do sistema.

## Como resolver este problema:
Adotar um processo de criação com transações distribuídas utilizando o padrão **Saga**, em conjunto com **CQRS** para separar as operações de comando (escrita) e consulta (leitura), melhorando a escalabilidade e performance do sistema.

## Resumo da solução:
Criar um serviço que possibilite a criação conjunta de Livro, Autor e Género em um único processo, com validações de negócios, consistência transacional e a possibilidade de garantir que, se alguma falha ocorrer, o sistema consiga realizar uma reversão adequada de todas as operações. O uso de **Saga** permite o controle de transações distribuídas e a comunicação entre serviços para garantir a integridade dos dados, enquanto **CQRS** ajuda a separar a escrita e leitura, otimizando o sistema para operações complexas de criação.

## Fatores:
- A criação de um livro pode exigir a criação de um autor e género simultaneamente.
- A operação precisa garantir consistência entre os três componentes (Livro, Autor e Género).
- Transações distribuídas podem envolver diferentes microserviços, necessitando de uma abordagem robusta de gestão de falhas.
- A solução precisa ser escalável e resiliente, permitindo que falhas em uma parte do processo não afetem a criação dos demais itens.
- A separação de **leitura** e **escrita** através de **CQRS** pode otimizar o desempenho de leitura sem impactar a complexidade da criação.

## Solução:
- **Saga**: Implementar uma transação distribuída utilizando o padrão Saga, onde cada serviço (Livro, Autor, Género) é responsável por sua parte do processo de criação e poderá, caso algo dê errado, realizar a compensação ou reversão das alterações realizadas.
  - **Livro**: Criar um serviço dedicado para a criação de livros.
  - **Autor**: Criar um serviço separado para criação de autores.
  - **Género**: Ter um serviço dedicado para gerir e associar géneros aos livros.
- **CQRS**: Separar as operações de leitura e escrita. Para as operações de **criação**, o sistema será mais focado nas **escritas**, enquanto as consultas sobre os dados criados (como informações do livro ou autor) serão tratadas separadamente, em um modelo otimizado para leitura.
- **Eventos de Domínio**: Usar **Domain Events** para disparar ações quando a criação de Livro, Autor ou Género for bem-sucedida ou quando for necessário fazer uma compensação.
- **Messaging (RabbitMQ)**: Utilizar RabbitMQ para comunicação assíncrona entre os serviços. As mensagens podem ser usadas para notificar os serviços sobre a conclusão de suas respectivas ações ou para realizar a compensação em caso de erro.

## Motivação:
A criação de um Livro, Autor e Género no mesmo processo pode ser uma tarefa complexa, principalmente se houver falhas durante a execução de alguma das etapas. A solução com **Saga**, **CQRS** e **messaging** garante que o sistema seja resiliente e capaz de manter a integridade dos dados, mesmo com falhas. Além disso, a separação de **comandos** e **consultas** permite um sistema mais escalável e eficiente, principalmente quando a carga de leitura e escrita no sistema é alta.

## Alternativas:
- **Abordagem Monolítica**: Criar um único serviço monolítico para a criação de Livro, Autor e Género, o que poderia resultar em uma manutenção mais difícil e falhas em larga escala caso algum componente falhasse.
- **Transação Distribuída Tradicional**: Utilizar uma transação distribuída tradicional que exigiria um sistema de gestão de transações centralizado, o que pode se tornar complexo e difícil de escalar.

## Questões pendentes:
- Quais são as regras de negócios que governam a criação de Livro, Autor e Género? Como garantir que essas regras sejam corretamente validadas durante o processo?
- Qual será o impacto na performance ao usar mensagens assíncronas para comunicação entre serviços?
- Como gerir a consistência de dados em serviços desacoplados, especialmente em casos de falhas?
