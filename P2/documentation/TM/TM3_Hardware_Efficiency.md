# **Technical Memo 3 - Hardware Efficiency**

## Problema:
O sistema ser capaz de usar os recursos de hardware de forma eficiente, atendendo à demanda sem desperdiçar recursos, como CPU, memória e armazenamento, especialmente em momentos de alta carga. 

## Como resolver este problema:
Adotar o padrão **Cloning** para escalar horizontalmente conforme necessário, e o padrão **Database-per-Service** para garantir que cada serviço tenha a base de dados mais adequada para o seu tipo de carga, otimizando o uso de recursos. Além disso, a utilização do **Strangler Fig** permite que sistemas legados sejam migrados gradualmente, utilizando os recursos de forma mais eficiente.

## Resumo da solução:
A solução proposta consiste em escalar horizontalmente usando **Cloning** para criar instâncias adicionais do sistema, mas de forma controlada, garantindo que o número de instâncias corresponda à demanda real, sem desperdícios. Com o **Database-per-Service**, garantimos que cada serviço tenha a sua base de dados própria, evitando sobrecarga nos sistemas de armazenamento. A abordagem **Strangler Fig** também é relevante, pois permite a migração gradual de componentes, garantindo que os recursos existentes sejam aproveitados antes de serem descontinuados.

## Fatores:
- **Escalabilidade controlada**: A criação de múltiplas instâncias do sistema deve ser feita de maneira que não gere desperdício de recursos.
- **Eficiência no uso de base de dados**: Cada serviço deve ter a base de dados que melhor se adapta ao seu padrão de uso.
- **Redução de sobrecarga de hardware**: Minimizar o uso de recursos excessivos ao longo do tempo, adaptando-se à demanda.
- **Transição de sistemas legados**: A migração gradual ajuda a otimizar o uso de hardware, aproveitando sistemas legados antes de sua desativação.

## Solução:
- **Cloning**: Criar múltiplas instâncias do serviço para distribuir a carga de forma eficiente. Porém, as instâncias devem ser criadas de acordo com a demanda real de uso, evitando criar instâncias excessivas que consumiriam recursos sem necessidade.
- **Database-per-Service**: Cada serviço terá a sua base de dados dedicada, o que ajuda a distribuir a carga e melhora a utilização de recursos de armazenamento. Isso permite que as bases de dados sejam otimizados para os tipos de dados que cada serviço manipula, evitando o uso desnecessário de recursos compartilhados.
- **Strangler Fig**: Utilizar a estratégia de migração gradual, onde os componentes legados são gradualmente substituídos, aproveitando ao máximo os recursos de hardware existentes até que os sistemas legados sejam completamente substituídos. Isso ajuda a otimizar o uso de hardware durante a transição.

## Motivação:
A principal motivação é evitar desperdícios de recursos, garantindo que o sistema utilize apenas o necessário para atender à demanda. A eficiência no uso de hardware é essencial para manter os custos operacionais sob controlo, especialmente quando se lida com escalabilidade em um ambiente de alta carga.

## Alternativas:
- **Escalabilidade vertical**: Em vez de escalar horizontalmente (com **Cloning**), poderia-se optar por melhorar a capacidade das máquinas existentes. No entanto, isso pode levar a um aumento de custo mais significativo e não seria tão flexível quanto a escalabilidade horizontal.
- **Base de dados compartilhada**: Optar por usar uma base de dados compartilhada para todos os serviços pode simplificar a arquitetura, mas pode criar um ponto único de falha e diminuir a eficiência, já que os diferentes serviços podem ter padrões de acesso a dados muito diferentes.

## Questões pendentes:
- Como garantir que o número de instâncias criadas através do **Cloning** esteja otimizado para a demanda real, evitando criar recursos desnecessários?
- Como garantir que a migração de sistemas legados usando o **Strangler Fig** seja realizada de forma gradual sem impactar negativamente o uso de hardware?
- Qual é o impacto de performance do uso de bases de dados dedicados por serviço (**Database-per-Service**) e como garantir que os recursos sejam alocados corretamente?
- Como monitorizar e ajustar dinamicamente a utilização de recursos para evitar excessos ou faltas, especialmente durante picos de demanda?

