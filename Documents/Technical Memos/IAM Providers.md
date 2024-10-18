
---
# Technical Memo - IAM Providers

## Problema:
Adotar diferentes provedores de IAM (Identidade e Gestão de Acesso), como Google, Facebook e Azure.

## Como resolver este problema:
Desenvolver uma camada de abstração que permita integrar múltiplos provedores de IAM de maneira transparente e flexível.

## Resumo da solução:
Implementar um intermediário para lidar com as diferentes APIs dos provedores de IAM e permitir a troca dinâmica de provedores, sem impactar o funcionamento do sistema.

## Fatores:
- Diferentes provedores de IAM têm APIs e requisitos distintos.
- A integração com provedores de IAM pode ter implicações de segurança e privacidade.
- A escolha do provedor de IAM pode ser dinâmica, dependendo das necessidades do cliente ou da região.

## Solução:
- **Encapsular** a lógica de autenticação e autorização para que a troca de provedores de IAM não afete o restante do sistema.
- **Usar um intermediário** para isolar as interações com diferentes APIs de IAM.
- **Abstrair serviços comuns** para fornecer uma interface unificada de integração com diferentes provedores de IAM.
- **Deferir binding** para permitir a seleção dinâmica do provedor de IAM em tempo de execução.

## Motivação:
Prover flexibilidade para suportar diferentes provedores de IAM de acordo com as necessidades do cliente, sem comprometer a segurança ou a escalabilidade do sistema.

## Alternativas:
- Integrar diretamente cada provedor de IAM ao sistema, aumentando o acoplamento e dificultando a manutenção e expansão futura.

## Questões pendentes:
- Como gerir as políticas de segurança de forma consistente em diferentes provedores de IAM?
- Como garantir que a integração com os provedores seja transparente e escalável?
---
