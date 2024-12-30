# **Technical Memo 4 - Releasability**

## Problema:
Manter ou melhorar a releasability do sistema, garantindo que mudanças no sistema possam ser realizadas de forma eficiente, sem interromper os serviços ou impactar os clientes.

## Como resolver este problema:
Adotar o padrão **Strangler Fig** para facilitar a migração gradual de sistemas legados e a introdução de novos serviços, garantindo que mudanças sejam feitas sem impactar o sistema atual. Utilizar também o padrão **CQRS** para separar claramente os fluxos de leitura e escrita, permitindo que mudanças em um não afetem o outro, e o padrão **Saga** para gerir transações distribuídas de maneira confiável.

## Resumo da solução:
A solução proposta envolve a utilização do **Strangler Fig** para permitir a migração gradual e sem interrupções de sistemas legados para novos sistemas, mantendo o funcionamento do sistema durante todo o processo. O **CQRS** será utilizado para separar a lógica de leitura da de escrita, permitindo modificações e atualizações de dados sem impacto no sistema de leitura, enquanto o padrão **Saga** será usado para gerir transações complexas e distribuídas de forma que erros em uma transação não afetem o restante do processo, garantindo a continuidade das operações.

## Fatores:
- **Mínima interrupção**: A transição de sistemas ou partes do sistema não deve interromper as operações em andamento.
- **Separação de responsabilidades**: Garantir que mudanças em uma parte do sistema não afetem outras partes, especialmente em ambientes com alta demanda.
- **Gestão de transações distribuídas**: A necessidade de gerir transações complexas entre múltiplos serviços sem comprometer a consistência ou a disponibilidade do sistema.
- **Facilidade de implantação**: A solução deve permitir atualizações frequentes com mínima sobrecarga e interrupção.

## Solução:
- **Strangler Fig**: Esta estratégia permite a migração gradual de sistemas legados para novas implementações sem causar interrupções no serviço. Com isso, conseguimos manter a continuidade das operações enquanto fazemos as mudanças necessárias de forma incremental.
- **CQRS (Command Query Responsibility Segregation)**: Ao separar os fluxos de leitura e escrita, conseguimos modificar a lógica de escrita sem afetar as operações de leitura, permitindo um desenvolvimento mais ágil e atualizado sem impactar os consumidores de dados.
- **Saga**: O padrão **Saga** garante que transações distribuídas sejam concluídas de forma confiável e que erros sejam tratados adequadamente, sem afetar o estado geral do sistema. Ele permite a execução de transações complexas de forma consistente em ambientes distribuídos.

## Motivação:
A motivação principal é permitir que as atualizações e alterações no sistema sejam feitas sem interromper os serviços ou afetar os utilizadores. A utilização de estratégias que permitam a migração gradual e a separação de responsabilidades de leitura e escrita facilita a implementação de novos recursos ou melhorias enquanto mantém a estabilidade do sistema. Além disso, a gestão de transações distribuídas garante que as mudanças sejam feitas de forma confiável.

## Alternativas:
- **Implementação de um sistema monolítico**: Embora possa ser mais simples em termos de arquitetura, um sistema monolítico tende a dificultar atualizações e manutenções sem impactar toda a base de utilizadores. Essa abordagem não permite a flexibilidade de mudanças incrementais ou a migração gradual.
- **Desacoplamento completo de serviços**: Optar por uma separação extrema de todos os componentes do sistema pode tornar a integração complexa e exigir maior coordenação entre as equipas, impactando a capacidade de realizar mudanças rápidas e sem interrupções.

## Questões pendentes:
- Como garantir que a migração de sistemas legados para novos serviços usando o **Strangler Fig** seja feita sem comprometer a experiência do utilizador durante o processo?
- Quais ferramentas de orquestração podem ser usadas para garantir a implementação eficiente do padrão **Saga**, especialmente em ambientes com muitos microserviços?
- Como gerir a consistência entre os sistemas de leitura e escrita quando se utiliza **CQRS**, especialmente em casos de alta carga ou alta concorrência?
- Qual a estratégia de versionamento mais adequada para garantir que as mudanças na API não afetem os clientes, especialmente em uma arquitetura baseada em **CQRS** e **Saga**?

