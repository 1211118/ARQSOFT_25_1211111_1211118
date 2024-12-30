# **Technical Memo 1 - Availability**

## Problema:
Garantir a alta disponibilidade do sistema, especialmente em situações de alta demanda ou falhas em partes da infraestrutura.

## Como resolver este problema:
Implementar técnicas de **cloning** de instâncias de serviços, utilização do padrão **Strangler Fig** para transição suave entre versões de sistemas, adotar **Messaging** com RabbitMQ para garantir a resiliência e comunicação eficiente entre serviços, além de aplicar o padrão **Saga** para garantir a consistência e recuperação em transações distribuídas.

## Resumo da solução:
A solução envolve o uso de múltiplas instâncias para garantir disponibilidade em caso de falhas, utilizando **cloning** para distribuir a carga e aumentar a tolerância a falhas. O padrão **Strangler Fig** é utilizado para facilitar a transição de partes do sistema e garantir alta disponibilidade ao longo de mudanças. **RabbitMQ** será usado para garantir que os serviços se comuniquem de maneira resiliente, mesmo em falhas de partes do sistema. O **Saga** será utilizado para garantir que transações distribuídas entre diferentes serviços sejam concluídas de maneira consistente, mesmo em caso de falhas.

## Fatores:
- **Escalabilidade**: A solução deve suportar uma escalabilidade eficiente, aumentando a capacidade do sistema conforme necessário.
- **Resiliência**: O sistema deve ser capaz de se recuperar rapidamente de falhas sem afetar a experiência do utilizador.
- **Consistência**: Garantir que, mesmo com falhas, os dados sejam mantidos consistentes através de transações distribuídas.
- **Transição suave**: A aplicação de novas versões do sistema sem causar interrupções deve ser suportada.

## Solução:
- **Cloning**: Utilização de múltiplas instâncias de cada serviço, implementadas em **máquinas virtuais** ou **containers** para garantir que o sistema continue a funcionar mesmo em caso de falhas.
- **Strangler Fig**: Implementação de novas versões do sistema de forma gradual, com transição suave entre sistemas legados e novos, garantindo continuidade operacional.
- **Messaging (RabbitMQ)**: Uso de RabbitMQ para garantir a comunicação assíncrona e resiliente entre serviços, mesmo quando partes do sistema falham, mantendo a integridade do fluxo de dados.
- **Saga**: Utilização de um padrão de orquestração de transações distribuídas que permite que, em caso de falha em uma parte do sistema, as transações sejam revertidas ou completadas de forma consistente, garantindo que o sistema continue disponível e os dados permaneçam corretos.

## Motivação:
A alta disponibilidade é essencial para garantir que os utilizadores possam acessar o sistema de forma contínua e sem interrupções, especialmente durante picos de demanda. A solução precisa garantir que o sistema possa se adaptar rapidamente a falhas e manter a funcionalidade em todos os cenários. O uso do padrão **Saga** vai garantir que transações distribuídas possam ser geridas de forma resiliente, com rollback ou compensação em caso de falha.

## Alternativas:
- **Sistemas tradicionais de failover**: Utilizar apenas failover tradicional (como clusters) pode ser uma solução, mas ela pode ser mais complexa e difícil de gerir, especialmente com sistemas legados em transição.
- **Falta de clonagem**: Não utilizar clonagem poderia levar a pontos únicos de falha, prejudicando a disponibilidade do sistema.
- **Falta de Saga**: Não usar o padrão **Saga** poderia resultar em inconsistências de dados durante falhas, comprometendo a integridade do sistema.

## Questões pendentes:
- Qual a melhor abordagem para balanceamento de carga entre as instâncias clonadas?
- Como gerir de forma eficiente a comunicação assíncrona com RabbitMQ, considerando a escalabilidade do sistema a longo prazo?
- Qual a estratégia de monitorização mais eficaz para detectar falhas e garantir que as instâncias clonadas estão funcionando corretamente?
- Como garantir que as transações distribuídas sejam bem-sucedidas utilizando **Saga**, mesmo em cenários de falhas em partes do sistema?
