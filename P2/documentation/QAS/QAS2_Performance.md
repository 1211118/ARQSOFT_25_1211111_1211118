# Quality Attribute Scenario 2 - Performance

| **Elemento**            | **Declaração**                                                                                   |
|--------------------------|-------------------------------------------------------------------------------------------------|
| **Estímulo**            | O sistema enfrenta um aumento de 25% na demanda durante picos de tráfego, com > Y requisições por período. |
| **Fonte do Estímulo**   | Utilizadores finais a gerar alto volume de requisições simultâneas ao sistema.                   |
| **Ambiente**            | O sistema está em produção, sob alta carga, com múltiplos utilizadores ativos simultaneamente.   |
| **Artefato**            | Infraestrutura do sistema, incluindo serviços de leitura, escrita e armazenamento de dados.     |
| **Resposta**            | O sistema deve processar todas as requisições dentro do SLA, mantendo o tempo de resposta inferior a 1 segundo para operações de leitura e inferior a 2 segundos para operações de escrita. |
| **Medição da Resposta** | 100% das requisições devem ser processadas com sucesso durante picos de demanda, mantendo o tempo de resposta conforme o SLA especificado. |
