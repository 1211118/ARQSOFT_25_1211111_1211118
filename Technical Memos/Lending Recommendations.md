
---
# Technical Memo - Lending Recommendations

## Problema:
Recomendar empréstimos de acordo com especificações variadas.

## Como resolver este problema:
Criar um sistema de recomendação flexível que permita diferentes algoritmos e critérios para personalizar as sugestões de empréstimos para os clientes.

## Resumo da solução:
Desenvolver um motor de recomendação configurável que permita a escolha de diferentes algoritmos de recomendação e parâmetros em tempo de execução.

## Fatores:
- As recomendações podem ser baseadas em diferentes critérios, como popularidade, género, histórico do utilizador, etc.
- A flexibilidade para alterar os algoritmos de recomendação sem impactar o sistema é necessária para suportar diferentes necessidades de clientes.

## Solução:
- **Encapsular** a lógica de recomendação em um módulo separado, para que diferentes algoritmos possam ser facilmente adicionados ou modificados.
- **Usar um intermediário** para abstrair a escolha do algoritmo de recomendação e facilitar a troca de estratégias.
- **Abstrair serviços comuns** para permitir que diferentes tipos de recomendação sejam aplicados com facilidade.
- **Deferir binding** para permitir que o algoritmo de recomendação seja selecionado em tempo de execução conforme a necessidade.

## Motivação:
Prover flexibilidade e escalabilidade para suportar diferentes necessidades de recomendação, sem comprometer o desempenho ou a modularidade do sistema.

## Alternativas:
- Usar um único algoritmo de recomendação, mas isso limitaria a capacidade de personalizar as recomendações para diferentes tipos de clientes.

## Questões pendentes:
- Quais critérios devem ser utilizados nas recomendações e como garantir que o algoritmo selecione as opções mais relevantes?
- Como garantir que o sistema seja capaz de adaptar-se a novas estratégias de recomendação sem impactar o sistema existente?
---