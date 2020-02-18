# spring-data-mongodb-encrypt-autoconfiguration
Autoconfigures bol.com's [Spring Data Encryption for MongoDB](https://github.com/bolcom/spring-data-mongodb-encrypt).

## Spring Data Encryption for MongoDB
*Spring Data Encryption for MongoDB* transparently encrypts fields in your Java models that are annotated with `@Encrypt` before writing them to a MongoDB database.

This project is intended to simplify the configuration of *Spring Data Encryption for MongoDB* if your requirements are simple.

This might be suitable:
 * If you don't need to support more than one encryption key.
 * You're not using a custom mapper to map Mongo documents to Java domain objects.
 
The autoconfiguration depends on a single `Configuration` property, `hammingweight.spring.data.mongodb.encrypt.key` being set that
contains the base-64 encoding of a 256-bit AES key. For example, in `application.properties`

```
hammingweight.spring.data.mongodb.encrypt.key=hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA=
```

Of course, putting a key in your application configuration may not be secure. In practice, you might pass the value in an environment variable. For example,

```
export HAMMINGWEIGHT_SPRING_DATA_MONGODB_ENCRYPT_KEY="hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA="
```

or, if you've mounted the secret key in a file (perhaps attached to a Docker container)

```
HAMMINGWEIGHT_SPRING_DATA_MONGODB_ENCRYPT_KEY=$(</var/secrets/aeskey.txt)
```

## Building this project
To build the JAR

```
./mvnw clean install
```

## Adding this project as a Maven Dependency
Once you've built this project, you can add it as a dependency to your own project

```
<dependency>
    <groupId>com.hammingweight</groupId>
    <artifactId>spring-data-mongodb-encrypt-autoconfiguration</artifactId>
    <version>0.0.5</version>
</dependency>
```
 Naturally, you'll also need to add a MongoDB dependency
 
 ```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
 ```
 
## Using this
Just annotate your model's sensitive fields with `@Encrypt` as described at [Spring Data Encryption for MongoDB](https://github.com/bolcom/spring-data-mongodb-encrypt) and set the key and any other `Configuration` properties that you might need for your MongoDB installation. For example,
 
 ```
export SPRING_DATA_MONGODB_PORT=27018
export HAMMINGWEIGHT_SPRING_DATA_MONGODB_ENCRYPT_KEY="hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA="
```

This [unit test](./src/test/java/com/hammingweight/spring/data/mongodb/encrypt/configuration/EncryptionConfiguredTest.java) might be helpful in seeing how to use the *Spring Data Encryption for MongoDB* with autoconfiguration.


