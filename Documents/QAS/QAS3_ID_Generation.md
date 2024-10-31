# Cenário de Atributo de Qualidade 3 - Configurabilidade na Geração de IDs

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Estímulo** | O sistema precisa gerar IDs únicos para empréstimos e autores em diferentes formatos (UUID, alfanuméricos) de acordo com as especificações do cliente. |
| **Fonte do Estímulo** | Um cliente solicita um formato específico de ID para fins de rastreamento. |
| **Ambiente** | O sistema lida com pedidos de vários clientes, cada um exigindo um formato diferente de IDs. |
| **Artefato** | O serviço de geração de IDs. |
| **Resposta** | O sistema deve gerar IDs no formato solicitado, sem impacto nas operações existentes. |
| **Medição da Resposta** | 100% dos IDs gerados devem corresponder ao formato solicitado, com um atraso máximo de 1 segundo por ID. |
