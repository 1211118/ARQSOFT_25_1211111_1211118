package contracts

org.springframework.cloud.contract.spec.Contract.make {
    description "should publish a valid book suggestion"
    
    input {
        triggeredBy('publishBookSuggestion()')
    }

    outputMessage {
        sentTo('LMS.book.suggestions')
        body([
            title: $(consumer('Test Book')),
            description: $(consumer('Test Description')),
            genre: $(consumer('Fiction')),
            photoURI: $(consumer('http://example.com/photo.jpg')),
            authors: $(consumer(['Author 1', 'Author 2'])),
            isbn: $(consumer('058116685X')),
            suggestedByReaderId: $(consumer(1)),
            status: $(consumer('PENDING'))
        ])
        headers {
            header('contentType', applicationJson())
            messagingContentType(applicationJson())
        }
    }
}
