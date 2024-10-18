# Cenário de Atributo de Qualidade 2 - Adaptabilidade de Provedores IAM

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Estímulo** | O sistema deve integrar-se com novos provedores de Gestão de Identidade e Acesso (IAM), como Google e Facebook, sem exigir alterações na lógica principal. |
| **Fonte do Estímulo** | Requisito do cliente para suportar novos provedores de IAM para autenticação de utilizadores. |
| **Ambiente** | O sistema está implementado em um ambiente de produção com utilizadores autenticados através de diferentes provedores de IAM. |
| **Artefato** | A camada de integração de IAM. |
| **Resposta** | O sistema deve integrar novos provedores de IAM de forma contínua, sem tempo de inatividade ou modificação na lógica de autenticação. |
| **Medição da Resposta** | Novos provedores de IAM podem ser adicionados dinamicamente em tempo de execução sem tempo de inatividade. |
