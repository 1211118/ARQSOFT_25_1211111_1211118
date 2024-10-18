
---
# Technical Memo 1 - Data Persistence

## Problema:
Como persistir diferentes modelos de dados em diferentes tipos de bases de dados (relacionais e não relacionais).

## Como resolver este problema:
Criar uma camada intermediária que permita a comunicação com diferentes tipos de bases de dados, permitindo alternar entre modelos de dados de forma flexível e eficiente.

## Resumo da solução:
Adotar a criação de uma camada de abstração que gere a persistência de dados de maneira desacoplada, utilizando diferentes tipos de bases de dados (relacionais e não relacionais), e permitindo que a escolha da base de dados ocorra em tempo de execução.

## Fatores:
- Bases de dados diferentes (relacionais e não relacionais) têm características e requisitos de desempenho distintos.
- A flexibilidade é necessária para que o sistema suporte diversos tipos de dados e SGBDs, conforme as necessidades dos clientes.
- A operação simultânea com diferentes tipos de base de dados exige uma abordagem desacoplada.

## Solução:
- Criar um **intermediário** que permita a troca de bases de dados sem afetar o restante do sistema.
- **Abstrair serviços comuns** de persistência de dados para facilitar a integração com novos tipos de base de dados.
- **Deferir binding** para permitir que o tipo de base de dados a ser utilizado seja escolhido e configurado em tempo de execução.

## Motivação:
Garantir que o sistema seja flexível o suficiente para suportar diferentes modelos de dados e SGBDs, atendendo às necessidades específicas de cada cliente sem comprometer a manutenção do código.

## Alternativas:
- Acoplar diretamente a lógica de persistência com implementações específicas de bases de dados, o que limitaria a flexibilidade e dificultaria futuras mudanças.

## Questões pendentes:
- Quais partes do sistema precisam ser modificadas para permitir essa flexibilidade?
- Como garantir que a camada de abstração seja eficiente e não introduza overhead no sistema?
---
