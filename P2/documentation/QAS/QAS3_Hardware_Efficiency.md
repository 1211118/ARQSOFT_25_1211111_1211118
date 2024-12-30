# Quality Attribute Scenario 3 - Hardware Efficiency

| **Elemento**            | **Declaração**                                                                                         |
|--------------------------|-------------------------------------------------------------------------------------------------------|
| **Estímulo**            | O sistema deve lidar com picos de carga sem desperdiçar recursos de hardware, como CPU, memória e armazenamento. |
| **Fonte do Estímulo**   | Operações realizadas por múltiplos utilizadores simultaneamente em momentos de alta demanda.           |
| **Ambiente**            | O sistema está em produção, enfrentando variações de carga ao longo do dia.                           |
| **Artefato**            | Recursos de hardware e serviços dependentes, incluindo base de dados e instâncias de aplicação.       |
| **Resposta**            | O sistema deve alocar e utilizar recursos de hardware de forma eficiente, garantindo que o uso seja ajustado à demanda real. |
| **Medição da Resposta** | Utilização de hardware (CPU, memória e armazenamento) abaixo de 80% durante picos de carga, com uma latência de resposta inferior a 2 segundos para 95% das operações. |
