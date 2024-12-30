# Technical Memo 6 - SOA API Strategy

## Problema:
Garantir que a arquitetura do sistema seja orientada a serviços (SOA) com conectividade baseada em APIs, adotando uma abordagem modular e escalável para suportar crescimento e mudanças constantes.

## Como resolver este problema:
Adotar uma estratégia de SOA utilizando microserviços baseados em APIs, implementando padrões como **Strangler Fig**, **Messaging (RabbitMQ)** e **Domain Events** para promover a comunicação desacoplada e garantir a flexibilidade, escalabilidade e resiliência do sistema.

## Resumo da solução:
Implementar uma estratégia SOA que gradualmente migra a arquitetura monolítica para uma arquitetura baseada em microserviços, com comunicação assíncrona e desacoplada utilizando **RabbitMQ**. Utilizar **Domain Events** para permitir reatividade entre os serviços sem dependências diretas, garantindo que a comunicação entre serviços seja eficiente e escalável.

## Fatores:
- **Estratégia de API-led connectivity** para garantir que cada serviço tenha uma API bem definida e que a comunicação entre eles seja padronizada.
- **Transição gradual de monolítico para microserviços** usando o padrão **Strangler Fig**.
- **Desacoplamento** entre serviços para melhorar a escalabilidade, a resiliência e a flexibilidade.
- **Reatividade** no sistema utilizando **Domain Events** para garantir que eventos importantes sejam propagados entre os serviços.

## Solução:
- **Strangler Fig**: Migrar de uma arquitetura monolítica para microserviços de forma gradual, substituindo progressivamente partes do sistema legado com novos serviços baseados em APIs.
- **Messaging (RabbitMQ)**: Utilizar RabbitMQ para comunicação assíncrona e desacoplada entre serviços, permitindo maior escalabilidade e resiliência.
- **Domain Events**: Definir eventos de domínio que permitam que serviços reagentes sejam notificados de mudanças importantes e reajam sem depender diretamente dos outros serviços, promovendo um sistema mais modular e reativo.

## Motivação:
A motivação para adotar a estratégia de **SOA com APIs** é garantir que o sistema seja flexível, escalável e capaz de evoluir rapidamente à medida que os requisitos de negócios mudam. A utilização de **Strangler Fig**, **Messaging (RabbitMQ)** e **Domain Events** proporciona a modularidade e desacoplamento necessários para lidar com mudanças dinâmicas.

## Alternativas:
- **Manter a arquitetura monolítica**: Não adotar a transição para microserviços e continuar com a arquitetura monolítica, o que pode resultar em dificuldades de escalabilidade e alta acoplamento.
- **Utilizar apenas comunicação síncrona** entre os serviços: Isso poderia levar a problemas de desempenho, falta de escalabilidade e falhas de serviço em caso de alta demanda.

## Questões pendentes:
- Como garantir que a comunicação assíncrona entre serviços não afete a performance geral do sistema?
- Quais eventos de domínio são necessários para garantir que os serviços reagem adequadamente às mudanças sem causar dependências entre eles?
- Como gerir a transição gradual sem afetar a operação dos serviços existentes durante a migração?
