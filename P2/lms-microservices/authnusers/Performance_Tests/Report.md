
# **Relatório de Testes de Performance ao Microserviço `authnusers`**

## **Introdução**
Este relatório documenta a execução de testes de performance no microserviço `authnusers`, com foco nos endpoints `/api/public/login` e `/api/public/register`. O objetivo é avaliar o desempenho, escalabilidade e fiabilidade do microserviço sob diferentes condições de carga e uso. Estes testes foram realizados como parte do cumprimento dos critérios não funcionais do projeto, nomeadamente o aumento de desempenho, estabilidade sob carga e operação contínua sem downtime.

Os testes realizados incluíram **Testes de Carga (Load Testing)**, **Testes de Stress (Stress Testing)** e **Testes de Durabilidade (Soak Testing)**. Os resultados obtidos permitem validar a robustez do microserviço e identificar possíveis melhorias.

---

## **Objetivos**
1. Avaliar o comportamento do microserviço sob condições normais de carga.
2. Determinar os limites superiores do desempenho através de testes de stress.
3. Validar a estabilidade do sistema durante períodos prolongados de uso.
4. Garantir que o sistema responde adequadamente às métricas de throughput, latência e ausência de erros, alinhando-se com os requisitos do projeto.

---

## **Metodologia**

### **Testes Realizados**
1. **Testes de Carga (Load Testing):**
   - Simulação de 20 utilizadores simultâneos com um período de ramp-up de 60 segundos.
   - Execução de 5 iterações para cada utilizador, totalizando 100 requisições por endpoint.

2. **Testes de Stress (Stress Testing):**
   - Escalonamento progressivo de carga até 300 utilizadores simultâneos.
   - Avaliação do tempo de resposta e throughput sob carga extrema.

3. **Testes de Durabilidade (Soak Testing):**
   - Carga constante de 50 utilizadores simultâneos durante um período prolongado.
   - Verificação da estabilidade do sistema sob carga contínua.

### **Ferramentas Utilizadas**
- **JMeter**: Utilizado para criar e executar os cenários de teste.
- **Endpoints Testados**:
  - `/api/public/login`: Autenticação de utilizadores.
  - `/api/public/register`: Registo de novos utilizadores.

### **Métricas Avaliadas**
- **Latência Média:** Tempo médio de resposta para cada requisição.
- **Throughput:** Número de requisições processadas por segundo.
- **Taxa de Erro:** Percentagem de requisições com erros.
- **Desvio Padrão:** Variação nos tempos de resposta.

---

## **Resultados**

### **Testes de Carga (Load Testing)**
| Métrica            | Login            | Register         | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 100              | 100              | 200              |
| Latência Média (ms)| 123              | 2                | 62               |
| Latência Mínima (ms)| 120             | 1                | 1                |
| Latência Máxima (ms)| 131             | 8                | 131              |
| Throughput (req/s) | 1.73554          | 1.73931          | 3.47096          |
| Taxa de Erro (%)   | 0.000%           | 0.000%           | 0.000%           |

- **Conclusão**: O microserviço respondeu consistentemente bem com tempos de resposta baixos e ausência de erros. O endpoint `/api/public/register` apresentou menor latência devido à simplicidade da operação.

---

### **Testes de Stress (Stress Testing)**
| Métrica            | Login            | Register         | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 10,525           | 10,352           | 20,877           |
| Latência Média (ms)| 1,640            | 1,473            | 1,557            |
| Latência Mínima (ms)| 121             | 1                | 1                |
| Latência Máxima (ms)| 6,204           | 4,702            | 6,204            |
| Throughput (req/s) | 58.82879         | 57.94214         | 116.69061        |
| Taxa de Erro (%)   | 0.000%           | 0.000%           | 0.000%           |

- **Conclusão**: O microserviço manteve-se funcional sob carga extrema com throughput elevado (116 requisições/segundo). Contudo, observou-se aumento significativo da latência, atingindo picos de mais de 6 segundos no endpoint `/api/public/login`.

---

### **Testes de Durabilidade (Soak Testing)**
| Métrica            | Login            | Register         | Total            |
|--------------------|------------------|------------------|------------------|
| Amostras (#)       | 150              | 150              | 300              |
| Latência Média (ms)| 122              | 1                | 61               |
| Latência Mínima (ms)| 120             | 1                | 1                |
| Latência Máxima (ms)| 129             | 19               | 129              |
| Throughput (req/s) | 2.53507          | 2.54039          | 5.06997          |
| Taxa de Erro (%)   | 0.000%           | 0.000%           | 0.000%           |

- **Conclusão**: O sistema manteve comportamento estável durante todo o teste, com baixa latência e throughput consistente. Não foram observados erros.

---

## **Discussão dos Resultados**
1. **Desempenho Sob Carga Normal:**
   - O microserviço apresentou tempos de resposta baixos e throughput adequado para 20 utilizadores simultâneos.
2. **Escalabilidade:**
   - O sistema lidou com um aumento significativo na carga durante os testes de stress, embora com aumento na latência.
3. **Estabilidade:**
   - Durante os testes de durabilidade, o microserviço mostrou-se robusto e estável, sem degradação de desempenho ao longo do tempo.

---

## **Conclusão**
Os testes realizados demonstraram que o microserviço `authnusers` cumpre os critérios não funcionais do projeto:
- **Aumento de desempenho**: O sistema apresentou tempos de resposta adequados sob carga normal e extrema.
- **Confiabilidade**: Nenhum erro foi registado durante os testes.
- **Operação sem downtime**: O sistema manteve-se acessível durante os testes.

Estes resultados confirmam a capacidade do microserviço de operar de forma eficiente em ambientes distribuídos com requisitos de alta disponibilidade.


