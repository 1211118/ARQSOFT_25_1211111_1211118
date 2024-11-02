# Quality Attribute Scenario 1 - Escalabilidade na Persistência de Dados

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Estímulo** | O sistema de gestão de bibliotecas precisa persistir dados em diferentes modelos de dados (relacional, documento) e bases de dados (ex.: MySQL, MongoDB) para diferentes cargas de trabalho. |
| **Fonte do Estímulo** | Aumento no volume e variedade de dados de múltiplos tipos de clientes. |
| **Ambiente** | O sistema lida com cargas máximas, com pedidos de persistência de dados de diferentes serviços (ex.: empréstimos, armazenamento de livros). |
| **Artefato** | A camada de persistência de dados. |
| **Resposta** | O sistema deve persistir os dados com sucesso em diferentes sistemas de bases de dados, garantindo consistência e disponibilidade. |
| **Medição da Resposta** | 95% dos pedidos de persistência devem ser atendidos em menos de 2 segundos em cargas normais. |
