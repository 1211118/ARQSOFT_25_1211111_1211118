# Cenário de Atributo de Qualidade 5 - Desempenho de Testes Automatizados

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Estímulo** | O sistema deve executar testes automatizados (unitários, de mutação, de integração e de aceitação) dentro de um limite de tempo definido sob condições normais. |
| **Fonte do Estímulo** | A necessidade de feedback rápido durante os processos de integração contínua. |
| **Ambiente** | Os testes são executados em um pipeline CI/CD. |
| **Artefato** | O conjunto de testes (testes unitários, de integração, de mutação, de aceitação). |
| **Resposta** | Todos os testes devem ser concluídos dentro do tempo alocado para garantir feedback rápido para os desenvolvedores. |
| **Medição da Resposta** | O conjunto completo de testes deve ser executado em até 5 minutos, com um tempo médio de execução de 30 segundos por caso de teste. |
