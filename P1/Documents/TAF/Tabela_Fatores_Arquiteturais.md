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
    <td>Escalabilidade – Persistência de Dados</td>
    <td>O sistema precisa persistir dados em diferentes bases de dados e modelos para diferentes cargas.</td>
    <td>Suporte a diferentes bases de dados (MySQL, MongoDB)</td>
    <td>Falhas na persistência podem impactar a disponibilidade e consistência dos dados de múltiplos serviços.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
  <tr>
    <td>Adaptabilidade – Provedores IAM</td>
    <td>O sistema deve integrar-se dinamicamente com novos provedores de IAM sem alteração na lógica.</td>
    <td>Suporte dinâmico a diferentes provedores de IAM (Google, Facebook).</td>
    <td>Falhas na integração podem causar inconvenientes aos utilizadores ao autenticar com novos provedores.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Configurabilidade – Geração de IDs</td>
    <td>O sistema gera IDs em formatos personalizados, sem afetar operações existentes.</td>
    <td>Suporte a múltiplos formatos de IDs (UUID, alfanuméricos)</td>
    <td>Pequeno impacto, limitado a um cliente específico que precisa de um formato de ID personalizado.</td>
    <td style="background-color:#336633;">Baixa</td>
    <td style="background-color:#336633;">Baixa (1)</td>
    <td style="background-color:#336633;">1</td>
  </tr>
  <tr>
    <td>Flexibilidade – Recomendação de Empréstimos</td>
    <td>O sistema deve alternar entre diferentes algoritmos de recomendação sem tempo de inatividade.</td>
    <td>Suporte a múltiplos algoritmos de recomendação (popularidade, histórico do utilizador).</td>
    <td>Algoritmos de recomendação afetados podem impactar a qualidade das sugestões oferecidas aos utilizadores.</td>
    <td style="background-color:#996600;">Média</td>
    <td style="background-color:#996600;">Média (3)</td>
    <td style="background-color:#996600;">3</td>
  </tr>
  <tr>
    <td>Desempenho – Testes Automatizados</td>
    <td>Todos os testes automatizados devem ser executados dentro de um tempo limite para feedback rápido.</td>
    <td>Execução eficiente de testes unitários, de integração, de mutação e de aceitação.</td>
    <td>Falhas no tempo de execução de testes afetam diretamente o ciclo de desenvolvimento e integração contínua.</td>
    <td style="background-color:#993333;">Alta</td>
    <td style="background-color:#993333;">Alta (4)</td>
    <td style="background-color:#993333;">4</td>
  </tr>
</table>

### Legenda:
- **Frequência/Probabilidade**: Com base na tabela de referência, avaliada como Baixa, Média ou Alta.
- **Risco/Gravidade**: Conforme a tabela de risco de severidade (1 a 4).
- **Prioridade**: Determinada com base na combinação de frequência/probabilidade e gravidade (quanto maior, maior a prioridade).
