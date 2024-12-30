# Quality Attribute Scenario 8 - Book Acquisition Suggestion

| **Elemento**            | **Declaração**                                                                                             |
|--------------------------|-----------------------------------------------------------------------------------------------------------|
| **Estímulo**            | Um leitor sugere a aquisição de um novo livro para o sistema.                                             |
| **Fonte do Estímulo**   | Um utilizador final realiza uma sugestão através da interface do sistema.                                 |
| **Ambiente**            | O sistema opera em um ambiente distribuído, com serviços independentes e comunicação assíncrona.          |
| **Artefato**            | Serviço de gestão de sugestões de aquisição de livros.                                                    |
| **Resposta**            | O sistema deve processar a sugestão de forma assíncrona, registrar a sugestão e disponibilizar o estado atualizado para consulta. |
| **Medição da Resposta** | 100% das sugestões devem ser processadas e armazenadas em até 3 segundos, com consistência garantida entre os serviços. |
