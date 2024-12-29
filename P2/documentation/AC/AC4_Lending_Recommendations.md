
# Análise Comparativa da Solução de Recomendação de Empréstimos

## Padrões e Táticas Utilizadas

### Strategy Pattern
A interface `RecommendationAlgorithm` e as suas implementações, `AgeGroupRecAlg` e `TopLentBooksRecAlg`, exemplificam o padrão Strategy. Este padrão permite que o sistema selecione diferentes algoritmos de recomendação em tempo de execução, oferecendo flexibilidade na lógica de recomendação.

**Exemplo:**

```java
public interface RecommendationAlgorithm {
    List<Book> getCustomRecommendations(String readerNumber);
}
```

```java
@Component("ageGroupRecAlg")
public class AgeGroupRecAlg implements RecommendationAlgorithm {

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {
        // Lógica para recomendações baseadas na idade do leitor
    }
}
```

```java
@Component("topLentBooksRecAlg")
public class TopLentBooksRecAlg implements RecommendationAlgorithm {

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {
        // Lógica para recomendações baseadas nos livros mais emprestados
    }
}
```

### Dependency Injection
O uso de `@Autowired` permite a injeção de dependências nas classes `AgeGroupRecAlg` e `TopLentBooksRecAlg`, reduzindo o acoplamento entre componentes e facilitando a manutenção. Cada classe pode receber as suas dependências (como `BookRepository` e `ReaderRepository`) sem necessidade de instanciar diretamente essas classes.

**Exemplo:**

```java
@Autowired
public AgeGroupRecAlg(BookRepository bookRepository, ReaderRepository readerRepository, TopLentBooksRecAlg topLentBooksRecAlg) {
    this.bookRepository = bookRepository;
    this.readerRepository = readerRepository;
    this.topLentBooksRecAlg = topLentBooksRecAlg;
}
```

### Configuration Management
A utilização de um ficheiro de propriedades (`library.properties`) para a configuração de parâmetros, como limites de recomendações e idades, permite que a aplicação seja facilmente ajustável sem alterações de código. Isso permite a configuração em tempo de execução.

**Exemplo:**

**library.properties:**

```makefile
# Reader age configuration
childReaderAge=10
adultReaderAge=18
suggestionsLimitPerGenre=3
```

### Defer Binding

O deferimento do binding implica que a escolha do algoritmo de recomendação deve ocorrer em tempo de execução, em vez de ser codificada em tempo de compilação. Isso aumenta a flexibilidade do sistema, permitindo que ele se adapte a diferentes necessidades sem modificações no código.

Utilizando o Spring Framework, a configuração do algoritmo de recomendação é feita através de um ficheiro de propriedades. Essa abordagem permite que o algoritmo correto seja instanciado em tempo de execução, com base na configuração atual. O parâmetro `suggestionsLimitPerGenre` é um exemplo de variável configurável, que pode ser ajustada conforme necessário.

```java
@Value("${suggestionsLimitPerGenre}")
private long maxRecommendationsPerGenre;
```


### Encapsulation

O encapsulamento da lógica de recomendação visa isolar os diferentes algoritmos de recomendação em módulos distintos. Essa abordagem não só promove uma melhor organização do código, mas também facilita a inclusão ou alteração de algoritmos sem impactar outras partes do sistema.

Para implementar esse conceito, foi criada a interface `RecommendationAlgorithm`, que define um método padrão `getCustomRecommendations(String readerNumber)`. As classes que implementam essa interface, como `AgeGroupRecAlg` e `TopLentBooksRecAlg`, fornecem as suas respetivas lógicas de recomendação. Isso permite que cada algoritmo tenha a sua própria lógica encapsulada, tornando mais simples a introdução de novos algoritmos no futuro.

```java
// Reccomendation Algorithm Interface

public interface RecommendationAlgorithm {
    List<Book> getCustomRecommendations(String readerNumber);
}
```
```java
// Top Lent Books Reccomendation Algorithm

@Component("topLentBooksRecAlg")
public class TopLentBooksRecAlg implements RecommendationAlgorithm {

    @Value("${suggestionsLimitPerGenre}")
    private long maxRecommendationsPerGenre;

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final GenreRepository genreRepository;

    public TopLentBooksRecAlg(BookRepository bookRepository, ReaderRepository readerRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {
        ReaderDetails reader = readerRepository.findByReaderNumber(readerNumber)
            .orElseThrow(() -> new NotFoundException("Reader not found with the provided ID"));

        // Obter os géneros mais emprestados
        Pageable pageable = PageRequest.of(0, 5); // Top 5 géneros
        Page<GenreBookCountDTO> topGenres = genreRepository.findTop5GenreByBookCount(pageable);

        // Obter o género mais emprestado
        String mostLentGenre = topGenres.getContent().stream()
            .findFirst()
            .map(GenreBookCountDTO::getGenre)
            .orElseThrow(() -> new NotFoundException("No genres found"));

        // Buscar livros desse género
        List<Book> recommendations = bookRepository.findByGenre(mostLentGenre.toString());

        return recommendations.stream()
            .limit(maxRecommendationsPerGenre)
            .collect(Collectors.toList());
    }
}
```
```java

// Age Group Reccomendation Algorithm

@Component("ageGroupRecAlg")
@PropertySource({"classpath:config/library.properties"})
public class AgeGroupRecAlg implements RecommendationAlgorithm {

    @Value("${childReaderAge}")
    private int childReaderAge;

    @Value("${adultReaderAge}")
    private int adultReaderAge;

    @Value("${suggestionsLimitPerGenre}")
    private long maxRecommendationsPerGenre;

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final TopLentBooksRecAlg topLentBooksRecAlg;

    @Autowired
    public AgeGroupRecAlg(BookRepository bookRepository, ReaderRepository readerRepository, TopLentBooksRecAlg topLentBooksRecAlg) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.topLentBooksRecAlg = topLentBooksRecAlg;
    }

    @Override
    public List<Book> getCustomRecommendations(String readerNumber) {

        ReaderDetails reader = readerRepository.findByReaderNumber(readerNumber)
             .orElseThrow(() -> new NotFoundException("Reader not found with the provided ID"));

        if (reader.getInterestList().isEmpty()) {
            throw new NotFoundException("Reader has no interests defined");
        }

        List<Book> recommendations = new ArrayList<>();
        int age = reader.getBirthDate().getAge();

        if (age < childReaderAge) {
            recommendations = getBooksByGenre("Infantil");
            System.out.println(recommendations);
        } else if (age < adultReaderAge) {
            recommendations = getBooksByGenre("Fantasia");
        } else {
            recommendations = topLentBooksRecAlg.getCustomRecommendations(readerNumber);
        }

        return recommendations.stream()
            .limit(maxRecommendationsPerGenre)
            .collect(Collectors.toList());
    }

    private List<Book> getBooksByGenre(String genreName) {
        return bookRepository.findByGenre(genreName);
    }
}
```


## Justificação para a Estrutura

- **Flexibilidade e Escalabilidade**: A utilização do padrão Strategy e da injeção de dependências permite que novas estratégias de recomendação sejam facilmente adicionadas, sem a necessidade de modificar o código existente, promovendo a escalabilidade do sistema.

- **Separação de Preocupações**: Cada classe tem uma responsabilidade clara (ex.: `AgeGroupRecAlg` para recomendações por idade e `TopLentBooksRecAlg` para os géneros mais emprestados), facilitando a manutenção e a testabilidade.

- **Redução de Acoplamento**: A separação das lógicas de recomendação e a configuração através de propriedades externas diminuem o acoplamento entre as classes, permitindo que mudanças em uma parte do sistema não afetem diretamente outras partes.

- **Facilidade de Configuração**: A gestão de configurações em tempo de execução permite ajustes rápidos em parâmetros como limites de recomendações e faixas etárias, permitindo que o sistema se adapte facilmente às necessidades dos utilizadores.

## Conclusão
A implementação da lógica de recomendação de empréstimos segue uma arquitetura modular e bem estruturada, utilizando padrões de design que promovem flexibilidade e escalabilidade. A separação de responsabilidades, juntamente com a configuração externa, assegura que o sistema possa evoluir e se adaptar às mudanças nas necessidades dos usuários e nas regras de negócio. Essa abordagem é fundamental para manter um código de alta qualidade e fácil de manter em um ambiente em constante mudança.
