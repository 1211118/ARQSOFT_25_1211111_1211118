# **Technical Memo 2 - Performance Improvement**

## Problema:
O sistema ser capaz de suportar um aumento de 25% na demanda durante picos de tráfego sem comprometer a performance, especialmente em situações de alto volume de requisições (> Y requisições/periodo).

## Como resolver este problema:
Utilizar os padrões **CQRS** (Command Query Responsibility Segregation) para separar as operações de leitura e escrita, **Polyglot Persistence** para usar diferentes tipos de base de dados para diferentes necessidades de dados, **Messaging (RabbitMQ)** para processar requisições de forma assíncrona, distribuindo a carga e garantindo que o sistema mantenha-se responsivo durante picos de demanda, e **Cloning** para escalar horizontalmente, criando instâncias adicionais do sistema conforme necessário.

## Resumo da solução:
A solução proposta consiste em separar as operações de leitura e escrita utilizando o padrão **CQRS**, que permite otimizar a consulta e a escrita de dados de maneira independente, ajudando a reduzir a carga sobre a base de dados. A utilização de **Polyglot Persistence** permite escolher a base de dados ideal para cada tipo de dados, otimizando o desempenho. Além disso, a messaging com **RabbitMQ** possibilita o processamento assíncrono de requisições, garantindo que as operações mais pesadas não sobrecarreguem o sistema durante picos de tráfego. Para garantir que o sistema suporte altos volumes de requisições, a implementação de **Cloning** (criação de múltiplas instâncias do serviço) permite escalar horizontalmente e balancear a carga entre as instâncias.

## Fatores:
- **Escalabilidade**: A solução precisa ser capaz de aumentar a capacidade do sistema conforme a demanda aumenta.
- **Eficiência de dados**: Diferentes tipos de dados exigem diferentes abordagens de armazenamento e consulta.
- **Desempenho sob alta carga**: O sistema deve ser capaz de suportar um volume maior de requisições sem afetar o tempo de resposta.
- **Assincronismo**: Algumas operações podem ser processadas de forma assíncrona para melhorar a responsividade geral do sistema.
- **Escalabilidade Horizontal**: A implementação de **Cloning** permite que o sistema escale horizontalmente, criando múltiplas instâncias para lidar com a carga de trabalho de forma eficiente.

## Solução:
- **CQRS (Command Query Responsibility Segregation)**: Separar as operações de leitura e escrita para otimizar a performance. As operações de leitura serão otimizadas para consultas rápidas, enquanto as operações de escrita serão tratadas de maneira eficiente, evitando bloqueios no base de dados.
- **Polyglot Persistence**: Utilizar diferentes tipos de bases de dados, como bases NoSQL para consultas rápidas e bases relacionais para dados que exigem consistência e transações. Isso ajuda a garantir que o sistema possa lidar com diferentes tipos de carga de maneira eficiente.
- **Messaging (RabbitMQ)**: Usar RabbitMQ para processar as requisições de maneira assíncrona. As requisições mais pesadas podem ser enviadas para uma fila e processadas em segundo plano, libertando o sistema para continuar a responder a novas requisições.
- **Cloning**: Escalar o sistema horizontalmente através da criação de múltiplas instâncias do sistema. Isso distribui a carga entre diferentes instâncias, permitindo que o sistema lide melhor com picos de tráfego e continue funcionando de forma eficiente, mesmo com um aumento na demanda.

## Motivação:
A performance do sistema durante picos de tráfego é crítica para garantir uma experiência de utilizador satisfatória. A abordagem proposta com **CQRS**, **Polyglot Persistence**, **Messaging** e **Cloning** visa melhorar a escalabilidade e o desempenho do sistema, garantindo que ele possa lidar com um aumento na demanda sem afetar a experiência do utilizador ou o tempo de resposta.

## Alternativas:
- **Escalabilidade horizontal sem CQRS**: Embora a escalabilidade horizontal possa ajudar, a falta de um modelo de separação de leitura e escrita pode não ser tão eficiente quanto o **CQRS** para garantir alta performance.
- **Uso de uma único bases de dados**: Utilizar apenas uma base de dados pode ser mais simples, mas não otimiza o desempenho para tipos de dados diferentes, o que pode ser um gargalo durante picos de carga.
- **Processamento síncrono**: Processar todas as requisições de maneira síncrona pode resultar em lentidão e falhas durante picos de demanda, já que o sistema não seria capaz de lidar com tantas requisições simultâneas.
- **Escalabilidade sem Cloning**: Sem **Cloning**, o sistema dependeria de apenas uma instância, o que pode causar sobrecarga em momentos de alta demanda, comprometendo a performance.

## Questões pendentes:
- Como garantir que a separação entre comandos e consultas no **CQRS** não introduza complexidade desnecessária?
- Qual o tipo ideal de base de dados para cada parte do sistema em termos de performance e consistência?
- Como garantir que o processamento assíncrono via **RabbitMQ** não introduza latência excessiva nas operações críticas?
- Como balancear a escalabilidade horizontal com a complexidade adicional introduzida pelos padrões adotados?
- Como gerir e orquestrar múltiplas instâncias do sistema criadas com **Cloning** para garantir uma distribuição eficiente de carga?

