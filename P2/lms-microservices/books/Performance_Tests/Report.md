# **Relatório de Testes de Performance ao Microserviço `books`**

## **Introdução**
Este relatório apresenta a análise de testes de performance realizados sobre o microserviço `books`, que inclui endpoints para pesquisa de autores e livros. Os testes foram desenvolvidos para verificar a performance, escalabilidade e fiabilidade do sistema em diferentes cenários de uso. Foram realizadas avaliações por meio de **Testes de Carga (Load Testing)**, **Testes de Stress (Stress Testing)** e **Testes de Durabilidade (Soak Testing)**.

Os objetivos principais são determinar como o microserviço lida com cargas normais, picos extremos e uso prolongado, validando assim os requisitos não funcionais do projeto.

---

## **Objetivos**
1. Avaliar o desempenho do microserviço sob condições normais de carga.
2. Identificar os limites de capacidade do sistema através de testes de stress.
3. Validar a estabilidade do sistema durante períodos prolongados.
4. Medir métricas como latência, throughput e taxa de erros para identificar potenciais melhorias.

---

## **Metodologia**

### **Testes Realizados**
1. **Testes de Carga (Load Testing):**
   - Simulação de 20 utilizadores simultâneos com 5 iterações por utilizador.
   - Avaliação dos endpoints `/api/authors` e `/api/books`.

2. **Testes de Stress (Stress Testing):**
   - Incremento progressivo de carga até 300 utilizadores simultâneos.
   - Medição da latência, throughput e taxa de erros sob alta carga.

3. **Testes de Durabilidade (Soak Testing):**
   - Carga contínua de 50 utilizadores durante um longo período.
   - Monitorização de estabilidade e degradação ao longo do tempo.

### **Ferramentas Utilizadas**
- **JMeter**: Para a criação e execução dos cenários de teste.
- **Endpoints Testados**:
  - `/api/authors?name=Manuel%20Antonio%20Pina`: Pesquisa de autores.
  - `/api/books/9789720706386`: Pesquisa de livros por ISBN.

### **Métricas Avaliadas**
- **Latência Média:** Tempo médio de resposta para cada requisição.
- **Throughput:** Número de requisições processadas por segundo.
- **Taxa de Erros:** Percentagem de requisições que resultaram em erro.
- **Desvio Padrão:** Variação nos tempos de resposta.

---

## **Resultados**

### **Testes de Carga (Load Testing)**
| Métrica            | Get Author       | Get Books        | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 100              | 100              | 200              |
| Latência Média (ms)| 3                | 3                | 3                |
| Latência Mínima (ms)| 2               | 2                | 2                |
| Latência Máxima (ms)| 9               | 12               | 12               |
| Throughput (req/s) | 1.75362          | 1.75383          | 3.50711          |
| Taxa de Erro (%)   | 0.000%           | 0.000%           | 0.000%           |

- **Conclusão**: O microserviço apresentou desempenho consistente e latência baixa durante os testes de carga, com throughput total de 3.5 requisições por segundo e ausência de erros.

---

### **Testes de Stress (Stress Testing)**
| Métrica            | Get Author       | Get Books        | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 129,290          | 129,110          | 258,400          |
| Latência Média (ms)| 120              | 113              | 116              |
| Latência Mínima (ms)| 0               | 0                | 0                |
| Latência Máxima (ms)| 2,850           | 2,566            | 2,850            |
| Throughput (req/s) | 723.13079        | 722.14423        | 1,445.25482      |
| Taxa de Erro (%)   | 77.349%          | 75.142%          | 76.246%          |

- **Conclusão**: Sob alta carga, o microserviço manteve um throughput elevado, mas apresentou taxas de erro consideráveis (76%) e tempos de resposta altos, atingindo picos de quase 3 segundos.

---

### **Testes de Durabilidade (Soak Testing)**
| Métrica            | Get Author       | Get Books        | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 150              | 150              | 300              |
| Latência Média (ms)| 2                | 2                | 2                |
| Latência Mínima (ms)| 1               | 1                | 1                |
| Latência Máxima (ms)| 5               | 3                | 5                |
| Throughput (req/s) | 2.55059          | 2.55076          | 5.10100          |
| Taxa de Erro (%)   | 6.000%           | 6.000%           | 6.000%           |

- **Conclusão**: O microserviço manteve latências baixas e um throughput constante durante os testes prolongados. Contudo, foi registada uma pequena taxa de erros (6%).

---

## **Discussão dos Resultados**
1. **Desempenho Sob Carga Normal:**
   - O microserviço mostrou tempos de resposta rápidos (< 10ms) e ausência de erros sob condições normais.
2. **Escalabilidade:**
   - Embora o throughput fosse elevado durante os testes de stress, as taxas de erro significativas sugerem a necessidade de melhorias para suportar cargas extremas.
3. **Estabilidade:**
   - Nos testes de durabilidade, o sistema apresentou comportamento consistente, com variações mínimas nos tempos de resposta.

---

## **Conclusão**
Os testes realizados confirmam que o microserviço `books` cumpre parcialmente os requisitos não funcionais do projeto:
- **Desempenho Adequado**: O sistema responde rapidamente sob cargas normais.
- **Escalabilidade Limitada**: As taxas de erro elevadas sob stress indicam a necessidade de ajustes.
- **Operação Contínua**: O sistema manteve-se funcional durante períodos prolongados.


