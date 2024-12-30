
# **Comparative Analysis 1 - Persisting Data**

## Repositórios e Injeção de Dependências em Múltiplas Bases de Dados (Com exemplo do Author)

Este documento explica como a injeção de repositórios é configurada para persistir dados em diferentes bases de dados usando Spring e um arquivo de configuração XML (`applicationContext.xml`). A abordagem apresentada permite que diferentes implementações de uma interface de repositório coexistam e que uma base de dados específica seja selecionada dinamicamente para a persistência dos dados. Além disso, é utilizado um conversor para adaptar os modelos entre sistemas de banco de dados distintos.

## Configuração de Repositórios no Arquivo XML

No projeto, o arquivo `applicationContext.xml` é usado para configurar os beans que representam os repositórios e, assim, definir as implementações específicas que serão utilizadas pelo Spring. Cada repositório possui uma implementação específica para uma base de dados relacional (JPA/Hibernate) ou não-relacional (MongoDB), e o Spring injeta automaticamente a implementação definida no XML. Vejamos a estrutura básica deste arquivo:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config></context:annotation-config>

    <!-- Definindo implementações específicas para os repositórios -->
    <bean id="authorRepository" class="pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorMongoRepositoryImpl"/>
    <bean id="bookRepository" class="pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.MongoBookRepository"/>
    <!-- Outros repositórios configurados para MongoDB -->
</beans>
```

### Carregamento Dinâmico e Injeção de Dependência

Na classe `AuthorServiceImpl`, ao utilizar `@Autowired` no campo `applicationContext`, o Spring permite que o serviço carregue dinamicamente o bean configurado para `authorRepository`. Esse comportamento é obtido chamando `applicationContext.getBean("authorRepository")` para obter uma instância específica definida no XML. Dessa forma, as instâncias de repositórios configuradas podem ser trocadas sem modificar o código-fonte.

```java
@Autowired
private ApplicationContext applicationContext;

@PostConstruct
private void initializeRepository() {
    this.authorRepository = (AuthorRepository) applicationContext.getBean("authorRepository");
    this.bookRepository = (BookRepository) applicationContext.getBean("bookRepository");
}
```

### Benefícios da Configuração com `ApplicationContext`

O uso do `ApplicationContext` permite uma grande flexibilidade ao sistema:
- **Troca de Implementações**: Sem alterar o código de serviço, é possível alternar entre bases de dados (por exemplo, de JPA/Hibernate para MongoDB) apenas ao modificar o XML.
- **Redução de Acoplamento**: O serviço não depende diretamente de uma implementação específica da interface `AuthorRepository`.
  
## Implementação de Interfaces para Suporte a Múltiplos Bases de Dados

Para garantir a interoperabilidade, tanto `AuthorRepositoryImpl` (implementação para JPA) quanto `AuthorMongoRepositoryImpl` (implementação para MongoDB) implementam a interface `AuthorRepository`. Assim, o sistema pode acessar ambas as bases de dados utilizando métodos comuns.

### Interface `AuthorRepository`

A interface `AuthorRepository` define métodos que serão implementados nas diferentes versões de repositório:

```java
public interface AuthorRepository {
    Optional<Author> findByAuthorNumber(Long authorNumber);
    List<Author> searchByNameNameStartsWith(String name);
    List<Author> searchByNameName(String name);
    Author save(Author author);
    Iterable<Author> findAll();
    void delete(Author author);
    List<Author> findCoAuthorsByAuthorNumber(Long authorNumber);
}
```

### Implementação para JPA: `AuthorRepositoryImpl`

A implementação `AuthorRepositoryImpl` utiliza JPA e consultas JPQL para interagir com uma base de dados relacional:

```java
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        // Implementação usando JPQL
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (author.getAuthorNumber() == null) {
            entityManager.persist(author);
        } else {
            entityManager.merge(author);
        }
        return author;
    }
    // Outros métodos JPQL
}
```

### Implementação para MongoDB: `AuthorMongoRepositoryImpl`

Para MongoDB, a implementação `AuthorMongoRepositoryImpl` utiliza o `MongoTemplate`, uma API específica do Spring Data para operações em MongoDB:

```java
@Repository
public class AuthorMongoRepositoryImpl implements AuthorRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        AuthorMongo authorMongo = mongoTemplate.findById(authorNumber.toString(), AuthorMongo.class);
        return Optional.ofNullable(AuthorConverter.toJpa(authorMongo));
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name.name").regex("^" + name, "i"));
        List<AuthorMongo> authorsMongo = mongoTemplate.find(query, AuthorMongo.class);
        return authorsMongo.stream().map(AuthorConverter::toJpa).collect(Collectors.toList());
    }
    // Outros métodos para MongoDB
}
```

## Conversão entre Modelos: `AuthorConverter`

Para adaptar as classes `Author` e `AuthorMongo`, que são modelos distintos para JPA e MongoDB, respectivamente, é utilizado o `AuthorConverter`. Este conversor permite que o serviço utilize a mesma estrutura de dados independente do repositório ou base de dados. Este realiza a conversão entre `Author` e `AuthorMongo` e garante que ambos possam ser tratados uniformemente.

### Exemplo de Conversão com `AuthorConverter`

Abaixo estão os métodos de conversão entre as duas representações do modelo:

```java
public class AuthorConverter {
    public static AuthorMongo toMongo(Author author) {
        return new AuthorMongo(
            author.getName(),
            author.getBio(),
            null
        );
    }

    public static Author toJpa(AuthorMongo authorMongo) {
        return new Author(
            authorMongo.getName(),
            authorMongo.getBio(),
            null
        );
    }
}
```


## Vista Lógica
![alt text](../Views/PersistingData.svg)

---