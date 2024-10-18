
---
# Technical Memo - Automated Testing

## Problema:
Melhorar a cobertura e a qualidade dos testes automatizados.

## Como resolver este problema:
Introduzir uma abordagem estruturada e escalável para testes automatizados, com diferentes tipos de testes (unitários, de mutação, de integração e de aceitação) que cubram adequadamente o sistema.

## Resumo da solução:
Adotar uma arquitetura de testes em camadas, com testes modulares e independentes, que permitam a verificação das diferentes partes do sistema de forma eficiente e confiável.

## Fatores:
- Testes unitários são essenciais para garantir que as partes individuais do sistema funcionem corretamente.
- Testes de mutação ajudam a verificar a robustez dos testes e a sua capacidade de detetar falhas.
- Testes de integração e aceitação garantem que os diferentes componentes e o sistema como um todo funcionem conforme esperado.

## Solução:
- **Encapsular** os diferentes tipos de testes em camadas separadas para garantir que sejam modulares e reutilizáveis.
- **Restringir dependências** para garantir que os testes possam ser executados de forma independente.
- **Refatorar** os testes existentes para melhorar a cobertura e a eficiência.
- **Deferir binding** para permitir a configuração dinâmica dos cenários de teste, adaptando-se às mudanças no sistema.

## Motivação:
Garantir a qualidade do sistema através de testes bem estruturados, que aumentem a confiabilidade e a capacidade de manutenção do código.

## Alternativas: 
- Utilizar apenas testes manuais, o que seria mais demorado e propenso a erros humanos.

## Questões pendentes:
- Como garantir uma boa cobertura de teste sem comprometer a manutenção e a performance do sistema?
- Quais ferramentas e frameworks serão usados para garantir a qualidade dos testes?
---
