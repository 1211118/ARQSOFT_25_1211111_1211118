
# Análise Comparativa da Solução

## 1. Encapsulamento da Lógica de Recomendação

**Definição Inicial:** O encapsulamento da lógica de recomendação visa isolar os diferentes algoritmos de recomendação em módulos distintos. Essa abordagem não só promove uma melhor organização do código, mas também facilita a inclusão ou alteração de algoritmos sem impactar outras partes do sistema.

**Implementação:** Para implementar esse conceito, foi criada a interface `RecommendationAlgorithm`, que define um método padrão `getCustomRecommendations(String readerNumber)`. As classes que implementam essa interface, como `AgeGroupRecAlg` e `TopLentBooksRecAlg`, fornecem as suas respetivas lógicas de recomendação. Isso permite que cada algoritmo tenha a sua própria lógica encapsulada, tornando mais simples a introdução de novos algoritmos no futuro.

```java
public interface RecommendationAlgorithm {
    List<Book> getCustomRecommendations(String readerNumber);
}
```

## 2. Uso de um Intermediário

**Definição Inicial:** O uso de um intermediário serve para abstrair a lógica de escolha do algoritmo de recomendação. Isso proporciona uma camada adicional de flexibilidade, permitindo que a estratégia de recomendação seja facilmente trocada sem necessidade de modificar o código-fonte.

**Implementação:** A configuração do algoritmo de recomendação é gerida através de um ficheiro de configuração (`library.properties`). Dentro desse ficheiro, o parâmetro `recAlg` pode ser alterado para selecionar entre os diferentes algoritmos de recomendação, facilitando mudanças rápidas na lógica de recomendação.

```
recAlg=topLentBooksRecAlg
```

## 3. Abstração de Serviços Comuns

**Definição Inicial:** A abstração de serviços comuns tem como objetivo permitir que diferentes algoritmos de recomendação acessem informações compartilhadas de forma eficiente e consistente. Isso melhora a coesão e reduz a duplicação de código.

**Implementação:** Para atingir essa abstração, os repositórios de livros (`BookRepository`), leitores (`ReaderRepository`) e géneros (`GenreRepository`) são injetados nos algoritmos de recomendação através de injeção de dependência. Isso garante que todos os algoritmos possam interagir com os dados comuns sem precisar recriar as suas próprias instâncias de repositório.


```java
@Autowired
public AgeGroupRecAlg(BookRepository bookRepository, ReaderRepository readerRepository) {
    this.bookRepository = bookRepository;
    this.readerRepository = readerRepository;
    this.genreRepository = genreRepository;
}
```

## 4. Deferimento do Binding

**Definição Inicial:** O deferimento do binding implica que a escolha do algoritmo de recomendação deve ocorrer em tempo de execução, em vez de ser codificada em tempo de compilação. Isso aumenta a flexibilidade do sistema, permitindo que ele se adapte a diferentes necessidades sem modificações no código.

**Implementação:** Utilizando o Spring Framework, a configuração do algoritmo de recomendação é feita através de um ficheiro de configuração. Essa abordagem permite que o algoritmo correto seja instanciado em tempo de execução, com base na configuração atual. O parâmetro `suggestionsLimitPerGenre` é um exemplo de variável configurável, que pode ser ajustada conforme necessário.

```java
@Value("${suggestionsLimitPerGenre}")
private long maxRecommendationsPerGenre;
```

## Excertos de Implementação 

### Algoritmo de Recomendação por Idade
O algoritmo `AgeGroupRecAlg` faz a recomendação de livros com base na idade do leitor. Ele categoriza os leitores em grupos etários e aplica diferentes estratégias de recomendação conforme a faixa etária.

```java
if (age < childReaderAge) {
    recommendations = getBooksByGenre("Infantil");
} else if (age < adultReaderAge) {
    recommendations = getBooksByGenre("Fantasia");
} else {
    recommendations = topLentBooksRecAlg.getCustomRecommendations(readerNumber);
}
```

### Algoritmo de Recomendação dos Livros Mais Emprestados
O algoritmo `TopLentBooksRecAlg` sugere livros populares com base nos géneros mais emprestados. Ele identifica o género mais frequentemente emprestado e busca os livros correspondentes a esse género para recomendar ao leitor.

```java
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
```

## Conclusão

A implementação da lógica de recomendação está alinhada com a visão inicial, apresentando uma arquitetura clara e modular. O encapsulamento dos algoritmos, o uso de intermediários para a seleção do algoritmo de recomendação, e a deferência do binding são características que não apenas proporcionam flexibilidade e escalabilidade ao sistema, mas também garantem que a manutenção futura seja simplificada. Essa abordagem estruturada facilita a adição de novas funcionalidades, assegurando que o sistema continue a evoluir conforme as necessidades dos utilizadores.
