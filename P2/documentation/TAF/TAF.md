# Table of Architectural Factors

<table>
  <tr>
    <th>Fator</th>
    <th>Qualidade do Cenário</th>
    <th>Variabilidade</th>
    <th>Impacto nos Stakeholders</th>
    <th>Frequência/Probabilidade</th>
    <th>Risco/Gravidade</th>
    <th>Prioridade (para resolução)</th>
  </tr>
  <tr>
    <td>Disponibilidade – Recuperação de Falhas</td>
    <td>O sistema deve garantir alta disponibilidade e tolerância a falhas.</td>
    <td>Capacidade de reconfigurar rapidamente os serviços após falhas.</td>
    <td>Falhas podem afetar a experiência do utilizador, causando indisponibilidade de serviços essenciais.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
  <tr>
    <td>Desempenho – Tempo de Resposta</td>
    <td>O sistema deve garantir tempos de resposta rápidos para todas as operações de leitura e escrita.</td>
    <td>Otimização de queries e carga equilibrada em servidores.</td>
    <td>Impacto direto na experiência do utilizador, podendo levar à frustração caso o sistema não responda rapidamente.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
  <tr>
    <td>Eficiência de Hardware – Processamento de Dados</td>
    <td>O sistema deve ser capaz de processar grandes volumes de dados com eficiência, sem sobrecarregar o hardware.</td>
    <td>Utilização eficiente de recursos de CPU e memória.</td>
    <td>Falhas no desempenho do hardware podem afetar a escalabilidade do sistema e aumentar os custos operacionais.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Reusabilidade – Atualizações e Patches</td>
    <td>O sistema deve permitir atualizações sem impactar a estabilidade do serviço.</td>
    <td>Facilidade na integração de novas funcionalidades e correções sem afetar componentes existentes.</td>
    <td>Impacta diretamente os desenvolvedores e a manutenção contínua do sistema.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Compatibilidade de API – Interoperabilidade</td>
    <td>O sistema precisa garantir que as APIs sejam compatíveis com diversas versões e clientes.</td>
    <td>Suporte a diferentes versões de API e mecanismos de fallback.</td>
    <td>Falhas de compatibilidade podem afetar os utilizadores e integradores de APIs.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
  <tr>
    <td>Estratégia SOA – Integração de Serviços</td>
    <td>O sistema deve garantir que os serviços sejam facilmente integrados com outros sistemas via SOA.</td>
    <td>Flexibilidade nas interfaces de serviço para garantir uma integração simplificada.</td>
    <td>A integração inadequada pode causar problemas na comunicação entre serviços e afetar a eficiência do sistema.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Criação de Livro, Autor e Género – Consistência de Dados</td>
    <td>O sistema deve garantir que a criação de livros, autores e géneros seja feita de maneira integrada e sem falhas.</td>
    <td>Uso de transações distribuídas e compensações.</td>
    <td>Falhas podem afetar a criação de novos registros e comprometer a integridade da base de dados.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
  <tr>
    <td>Sugestões de Aquisição de Livros – Processamento Assíncrono</td>
    <td>O sistema deve processar sugestões de aquisição de livros de forma assíncrona e escalável.</td>
    <td>Uso de filas de mensagens e eventos de domínio para comunicação assíncrona.</td>
    <td>Atrasos no processamento podem afetar a experiência do utilizador e o funcionamento do sistema.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Recomendação de Livro Após Devolução – Escalabilidade e Desempenho</td>
    <td>O sistema deve permitir que as recomendações sejam feitas rapidamente após a devolução de livros, sem impactar o desempenho.</td>
    <td>Separação entre leitura e escrita para operações de recomendação.</td>
    <td>Recomendações mal processadas ou lentas podem afetar a experiência do utilizador e a satisfação com o sistema.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
</table>

### Legenda:
- **Frequência/Probabilidade**: Com base na tabela de referência, avaliada como Baixa, Média ou Alta.
- **Risco/Gravidade**: Conforme a tabela de risco de severidade (1 a 4).
- **Prioridade**: Determinada com base na combinação de frequência/probabilidade e gravidade (quanto maior, maior a prioridade).
