# **Technical Memo 5 - API Compatibility**

## Problema:
Garantir que os clientes de software não sejam afetados por mudanças na API, exceto em casos extremos, de forma a assegurar compatibilidade contínua sem quebrar integrações existentes.

## Como resolver este problema:
Adotar o padrão **Strangler Fig** para realizar mudanças incrementais na API, garantindo que as versões anteriores continuem operacionais até que a migração para a nova API esteja completa. Utilizar **Messaging (RabbitMQ)** para comunicação assíncrona, permitindo que os clientes se adaptem gradualmente às mudanças. O padrão **CQRS** será utilizado para separar a leitura e a escrita de dados, minimizando os impactos da mudança de API nos fluxos de dados. Também pode-se aplicar **Domain Events** para notificar as partes interessadas de mudanças relevantes na API sem interromper as operações.

## Resumo da solução:
A solução proposta envolve a aplicação do padrão **Strangler Fig**, permitindo a transição suave entre versões da API e garantindo que os clientes possam continuar utilizando as versões antigas enquanto se adaptam à nova. Além disso, **Messaging (RabbitMQ)** possibilita comunicação assíncrona, dando maior flexibilidade para os clientes, enquanto **CQRS** e **Domain Events** ajudam a manter a integridade e a consistência dos dados sem interromper as operações dos consumidores da API.

## Fatores:
- **Compatibilidade**: As versões antigas da API devem ser mantidas enquanto a nova versão está sendo adotada, garantindo que as integrações dos clientes existentes não sejam interrompidas.
- **Flexibilidade de comunicação**: A transição para novos modelos de API deve ser feita de forma que os consumidores possam se adaptar sem precisar fazer grandes mudanças no seu código.
- **Escalabilidade**: A solução deve permitir que a API seja escalada para suportar novos padrões de comunicação e integrabilidade sem afetar o sistema como um todo.
- **Resiliência**: A solução deve garantir que a migração de API seja realizada de forma confiável, sem comprometer a continuidade do serviço.

## Solução:
- **Strangler Fig**: Ao aplicar o padrão **Strangler Fig**, será possível migrar gradualmente para a nova API sem interromper o funcionamento da versão antiga. Isso permitirá que os clientes se ajustem à nova versão no seu próprio ritmo, sem interrupções ou quebras de compatibilidade.
- **Messaging (RabbitMQ)**: A utilização de **RabbitMQ** para comunicação assíncrona permitirá que os clientes recebam as atualizações da API sem necessidade de integração imediata, dando-lhes tempo para se adaptar às mudanças.
- **CQRS**: Separando a lógica de leitura e escrita, podemos minimizar o impacto das mudanças de API, garantindo que os fluxos de leitura e escrita de dados não sejam afetados pelas alterações na API.
- **Domain Events**: O uso de eventos de domínio facilita a comunicação de mudanças importantes na API sem precisar afetar diretamente os consumidores de dados. Isso pode incluir notificações ou atualizações sobre mudanças nos dados da API.

## Motivação:
A motivação principal é garantir que as mudanças na API não afetem os clientes, permitindo que as integrações existentes continuem operacionais enquanto se realiza a migração para a nova API. Isso é crucial para a continuidade do serviço e para a experiência dos clientes, que não devem ser forçados a atualizar os seus sistemas imediatamente.

## Alternativas:
- **Descontinuação imediata da versão antiga da API**: Embora isso permita uma transição mais rápida, pode causar quebra de integração com os clientes que não estão prontos para migrar para a nova versão.
- **Mudanças sem controlo na versão da API**: Alterar a API sem um processo gradual pode resultar em quebras de compatibilidade e afetar os consumidores, dificultando a manutenção do sistema.

## Questões pendentes:
- Qual o processo exato de migração entre as versões da API? Como garantir que todos os clientes tenham tempo suficiente para se adaptar às mudanças?
- Como garantir que as mudanças na API sejam compatíveis com a estratégia de versionamento e não causem quebras inesperadas?
- Como gerir a integração de novos consumidores com a nova versão da API sem afetar a compatibilidade com os consumidores existentes?

