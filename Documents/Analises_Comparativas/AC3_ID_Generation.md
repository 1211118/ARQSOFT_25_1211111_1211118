# Análise Comparativa da Solução de Geração de IDs

## Padrões e Táticas Utilizadas

### Factory Pattern

A classe `LendingFactory` implementa o padrão de fábrica para a criação de instâncias da classe `Lending`. Isso permite a centralização da lógica de criação, o que é especialmente útil ao adicionar lógica adicional ou ao alterar a forma como os objetos são construídos. O uso do `IdGenerator` dentro da `LendingFactory` permite que a geração de IDs seja delegada a uma implementação específica, promovendo a flexibilidade.

**Exemplo:**

```java
@Component
public class LendingFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${idGenerator}")
    private String selectedIdGenerator;

    /**
     * Cria uma nova instância de Lending com o ID gerado.
     */
    public Lending createLending(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents) {
        IdGenerator idGenerator = applicationContext.getBean(selectedIdGenerator, IdGenerator.class);
        String lendingId = idGenerator.generateId();  // gera o lendingId
        return new Lending(book, readerDetails, seq, lendingDuration, fineValuePerDayInCents, lendingId);
    }
}
```


### Dependency Injection

O uso de `@Autowired` e `@Value` para injeção de dependências em `LendingFactory` e `LendingServiceImpl` reduz o acoplamento entre componentes e permite uma configuração mais simples e gerível. Isto permite que as classes dependam de abstrações (interfaces) em vez de implementações concretas, facilitando testes e manutenção.

```java
@Component
public class LendingServiceImpl implements LendingService {

    @Autowired
    private LendingFactory lendingFactory;
    
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    
    // Implementação dos métodos do serviço ...
}
```


### Strategy Pattern

A interface `IdGenerator` e as suas implementações (`AlphanumericIdGenerator` e `HexadecimalIdGenerator`) exemplificam o padrão Strategy. O `LendingFactory` seleciona a implementação apropriada com base na configuração em tempo de execução, permitindo que o sistema escolha como gerar IDs sem necessidade de modificar o código existente. Isso proporciona uma configuração flexível e a possibilidade de adicionar novos tipos de geradores de ID sem afetar o restante do sistema.

```java
// Interface Id Generator

package pt.psoft.g1.psoftg1.idmanagement;

public interface IdGenerator {

    String generateId();
    
}
```
```java
// Hexadecimal Id Generator Algorithm

@Component("hexadecimalIdGenerator")
public class HexadecimalIdGenerator implements IdGenerator {
    
    @Override
    public String generateId() {
        SecureRandom random = new SecureRandom();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            hexString.append(String.format("%x", random.nextInt(16)));
        }
        return hexString.toString();
    }
}
```
```java
// Alphanumeric Id Generator Algorithm

@Component("alphanumericIdGenerator")
public class AlphanumericIdGenerator implements IdGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateId() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }
}
```

### Encapsulation

A lógica de geração de IDs é mantida num módulo independente, permitindo a troca de algoritmos e formatos com facilidade. 

```java
package pt.psoft.g1.psoftg1.idmanagement;
```

### Configuration Management

O uso de um ficheiro de propriedades (`library.properties`) para configurar parâmetros como o algoritmo de geração de IDs permite que a aplicação seja facilmente ajustável sem alterações de código. Isso facilita a implementação de configurações em tempo de execução, permitindo que o comportamento do sistema seja modificado.

`library.properties`:

```makefile
# ID generator configuration
# possible values: hexadecimalIdGenerator, alphanumericIdGenerator
idGenerator=alphanumericIdGenerator
```
`LendingFactory`:
```java
@Component
public class LendingFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${idGenerator}")
    private String selectedIdGenerator;

    // Implementação da factory ...
}
```

## Justificação para o Refactor

- **Flexibilidade e Escalabilidade**: Com a introdução do padrão de fábrica e a injeção de dependências, o sistema pode facilmente ser modificado ou expandido para suportar novas regras de negócio ou métodos de geração de IDs.

- **Separação de Preocupações**: Cada classe tem uma responsabilidade clara e limitada, facilitando a manutenção e os testes. `Lending` lida com a lógica do empréstimo, `LendingFactory` com a criação de objetos, e `LendingServiceImpl` com a lógica de negócios.

- **Redução de Acoplamento**: A separação da lógica de criação de IDs e a configuração baseada em propriedades reduz o acoplamento entre as classes, permitindo que as mudanças em uma parte do sistema não afetem diretamente as outras.


## Conclusão

A implementação da lógica de geração de IDs está alinhada com a visão inicial, apresentando uma arquitetura modular e clara. O encapsulamento dos geradores, o uso de geradores específicos para diferentes necessidades, e a deferência na seleção do método de geração são características que proporcionam flexibilidade e escalabilidade ao sistema. Essa abordagem estruturada facilita a manutenção e a adição de novos métodos de geração, assegurando que o sistema continue a evoluir conforme as necessidades dos utilizadores.
