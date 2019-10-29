# Book-Details: A Showcase of GraphQL with Kotlin and Spring

The purpose of this project is to showcase the basic setup of a GraphQL server. A basic introduction
to GraphQL can be found [here](https://www.howtographql.com/).
This project is the Kotlin adaptation of the
[GraphQl-Java tutorial](https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/)
and is build on top of spring-boot.

## How to build
```bash
gradle clean build
```

## How to run
```bash
gradle bootRun
```
or create an IntelliJ Spring-Boot configuration that starts the main-method in `BookDetailsKtApplication`.

## How to connect

The app is configured (in application.properties) to start on port 8091.
Assuming you're connecting to it from localhost the url therefore is: `http://localhost:8091/graphql`   

A valid request looks like this:
```graphql
{
  bookById(id: "book-3") {
    id
    name
    pageCount
    authors {
      fullName
      booksWritten {
        name
      }
    }
  }
}
```

### UI-Client

A good UI client to connect to a GraphQL server (similar to Postman for http-endpoints) is
[GraphQL Playground](https://github.com/prisma/graphql-playground). It provides additional features
like querry-autocompletion.

![graphql playground][playground]


## Code

In order to create/define a GraphQl server you need to provide a schema and implement DataFetchers
for it's queries and (additional) type properties.

### Schema

The schema for this showcase can be found in `src/main/resources/schema.graphqls` and is pretty
straight forward:
```graphql
type Query {
  bookById(id: ID): Book
}

type Book {
  id: ID!
  name: String!
  pageCount: Int!
  authors: [Author!]!
}

type Author {
  id: ID!
  firstName: String!
  middleNames: String
  lastName: String!
  fullName: String!
  booksWritten: [Book!]!
}
```
This schema example contains types and queries but there exists one other class of definitions:
mutations which are used to update/delete data.

While this example contains type concepts like lists, nullability and recursive data structures,
know that GraphQl offers also other features like e.g. default parameter values,
fragments (the equivalent of db views), enums and union types.

It's highly suggested to take a look at the
[Core Concepts](https://www.howtographql.com/basics/2-core-concepts/)
and
[More GraphQL Concepts](https://www.howtographql.com/advanced/2-more-graphql-concepts/) sections
from the above mentioned introduction to learn more.


[playground]: ./doc/img/graphql-playground.png
