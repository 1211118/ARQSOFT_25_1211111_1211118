# Quality Attribute Scenario 1 - Availability

| **Elemento**            | **Declaração**                                                                                 |
|--------------------------|-----------------------------------------------------------------------------------------------|
| **Estímulo**            | O sistema enfrenta uma falha parcial num ou mais serviços ou um pico inesperado de utilização. |
| **Fonte do Estímulo**   | Utilizadores finais, serviços externos ou falhas de infraestrutura.                            |
| **Ambiente**            | O sistema está em produção, a operar sob carga normal ou durante picos de utilização.          |
| **Artefato**            | Infraestrutura de serviços e mecanismos de comunicação entre os serviços.                     |
| **Resposta**            | O sistema deve permanecer operacional, redirecionando tráfego ou utilizando instâncias redundantes, sem interrupções perceptíveis para os utilizadores. |
| **Medição da Resposta** | O tempo de recuperação de falhas deve ser inferior a 2 segundos, e a taxa de disponibilidade do sistema deve ser mantida em pelo menos 99,9%. |
