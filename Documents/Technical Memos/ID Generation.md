
---
# Technical Memo - ID Generation

## Problema:
Gerar IDs de empréstimos e de autores em diferentes formatos conforme especificações variadas.

## Como resolver este problema:
Criar uma lógica flexível e desacoplada para geração de IDs que permita escolher diferentes formatos e algoritmos de forma dinâmica.

## Resumo da solução:
Desenvolver um módulo que encapsule a lógica de geração de IDs, oferecendo diferentes opções de formatos e algoritmos configuráveis em tempo de execução.

## Fatores:
- Diferentes clientes podem exigir diferentes formatos de IDs (UUID, sequenciais, alfanuméricos).
- A flexibilidade para alterar o formato das IDs sem afetar o sistema é necessária para suportar requisitos variados de clientes.

## Solução:
- **Encapsular** a lógica de geração de IDs em um módulo independente, para facilitar a troca de algoritmos e formatos.
- **Restringir dependências** mantendo a lógica de geração de IDs desacoplada do restante do sistema.
- **Abstrair serviços comuns** para garantir que diferentes estratégias possam ser aplicadas sem grandes mudanças no sistema.
- **Deferir binding** para possibilitar a escolha de algoritmos de geração de IDs em tempo de execução.

## Motivação:
Permitir a flexibilidade na geração de IDs para diferentes clientes sem complicar o código ou afetar outras partes do sistema.

## Alternativas:
- Codificar diretamente a lógica de geração de IDs, o que dificultaria a adaptação a novos requisitos de clientes.

## Questões pendentes:
- Quais formatos de ID são exigidos por diferentes clientes e como garantir que o sistema possa adaptá-los facilmente?
- Qual o impacto de performance de diferentes métodos de geração de IDs?
---
