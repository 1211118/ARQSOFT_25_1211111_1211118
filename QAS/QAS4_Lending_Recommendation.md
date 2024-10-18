# Cenário de Atributo de Qualidade 4 - Flexibilidade na Recomendação de Empréstimos

| **Elemento**  | **Declaração** |
|--------------|---------------|
| **Estímulo** | O sistema precisa recomendar opções de empréstimo com base em vários algoritmos (ex.: popularidade, histórico do utilizador). |
| **Fonte do Estímulo** | Um novo cliente solicita um algoritmo de recomendação personalizado para empréstimo de livros. |
| **Ambiente** | O sistema executa vários algoritmos de recomendação com base nos dados de utilizadores e livros. |
| **Artefato** | O motor de recomendação. |
| **Resposta** | O sistema deve alternar dinamicamente entre diferentes algoritmos de recomendação sem tempo de inatividade. |
| **Medição da Resposta** | 90% dos resultados de recomendação devem ser fornecidos em até 1 segundo, com menos de 5% de taxa de erro em relevância. |
