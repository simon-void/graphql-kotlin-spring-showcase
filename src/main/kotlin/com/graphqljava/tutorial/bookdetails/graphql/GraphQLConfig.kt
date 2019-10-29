package com.graphqljava.tutorial.bookdetails.graphql

import com.graphqljava.tutorial.bookdetails.backend.Author
import com.graphqljava.tutorial.bookdetails.backend.Book
import com.graphqljava.tutorial.bookdetails.backend.DataService
import graphql.GraphQL
import graphql.schema.DataFetcher
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.context.annotation.Bean

import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.lang.IllegalStateException

@Configuration
class GraphQLProvider(val dataService: DataService) {

    @Bean
    fun graphQL(): GraphQL {
        val sdl: String = readResource("schema.graphqls")
        val graphQLSchema = SchemaGenerator().makeExecutableSchema(
                SchemaParser().parse(sdl),
                buildWiring()
        )
        return GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildWiring(): RuntimeWiring = RuntimeWiring.newRuntimeWiring().apply {
        addTypeWiring("Query", "bookById", bookByIdDataFetcher)
        addTypeWiring("Book", "authors", authorDataFetcher)
        addTypeWiring("Author", "booksWritten", booksWrittenDataFetcher)
    }.build()

    private val bookByIdDataFetcher: DataFetcher<Book?> = dataFetcherByArgument("id"){ bookId: String ->
        dataService.getBook(bookId)
    }

    private val authorDataFetcher: DataFetcher<List<Author>> = dataFetcherBySource{ book: Book ->
        book.authorIds.map {
            dataService.getAuthor(it)
                    ?: throw IllegalStateException("couldn't find author with id $it of book with id: ${book.id}")
        }
    }

    private val booksWrittenDataFetcher: DataFetcher<List<Book>> = dataFetcherBySource{ author: Author ->
        author.bookIds.map {
            dataService.getBook(it)
                    ?: throw IllegalStateException("couldn't find book with id $it of author with id: ${author.id}")
        }
    }
}

private inline fun <reified O, A:Any> dataFetcherByArgument(
    argumentName: String,
    crossinline transform: (A)->O
): DataFetcher<O> =  DataFetcher<O>
{ dataFetchingEnvironment ->
    val argument: A = dataFetchingEnvironment.getArgument(argumentName)
    transform(argument)
}

private inline fun <reified O, S:Any> dataFetcherBySource(
    crossinline transform: (S)->O
): DataFetcher<O> =  DataFetcher<O> { dataFetchingEnvironment ->
    val source: S = dataFetchingEnvironment.getSource()
    transform(source)
}

private fun RuntimeWiring.Builder.addTypeWiring(graphQLType: String, fieldName: String, dataFetcher: DataFetcher<*>) {
    this.type(newTypeWiring(graphQLType).dataFetcher(fieldName, dataFetcher))
}

private fun readResource(path: String): String = ClassPathResource(path).inputStream.reader().readText()