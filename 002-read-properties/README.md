
### Setup the project
1. Go to [spring initializr](https://start.spring.io/)
2. Pick `Gradle` as a project, `Kotlin` as  a language and `2.4.0` as a version of Spring Boot.
3. Fill project metadata with
   1. Group `com.example`
   2. Artifact and Name as `http`
   3. Description as `Example HTTP application`
   4. Packaging as `JAR`
   5. Java version as `11`
4. Click on GENERATE button to get a zip file of the project.
5. Extract the zip file to get the project.

### Understand the project structure
* The project has a main class file `PropertiesApplication.kt` under `src/main/kotlin/com/example/properties`. It contains the main function `fun main`. This is where you could define the steps to start the application.
* The project contain two gradle files `build.gradle.kts` and `settings.gradle.kts`. `build.gradle.kts` define the dependencies required for the project. It also defines the tasks that can be configured for the project. More details building build scripts at [Gradle build scripts](https://docs.gradle.org/current/userguide/tutorial_using_tasks.html).
* The purpose of the `settings.gradle.kts` is to define all submodules required for the project. It also defines settings like the project root directory. More details at [Gradle Settings](https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html).

### Add the web-starer dependency
The build a spring application for HTTP, a new dependency - `spring-boot-starter-web` is to be added. Go to `build.gradle.kts` and under dependencies section add the following:

`implementation("org.springframework.boot:spring-boot-starter-web")`


### Start the project
The project can be started using the command, run in the root folder of the project.
` ./gradlew bootRun`


### Create a example controller
Define a new Controller class which can listen to a HTTP request and respond. The controller acts as an interface between external users of the HTTP application and the internal services which can process the request. 
Annotate the controller with `@RestController`, indicating to spring that it's a controller class with responses directly appended to response body instead of rendering a HTML template.

### Define a HTTP endpoint in the controller
Define a function in the controller which maps a defined route to a function handler. The function is annotated with a `@GetMapping` annotation with a route as a parameter. In our case, we map to route `/`. The function is a simple function returning a string message as a response. 

### Associate the properties to the the controller response

#### Associate a single property
Associate a single property using the `@Value` annotation. Pass the property name as a parameter to the [@Value](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Value.html) annotation. As an example, the property message is bound as below:
```
@Value("\${example.properties.message}")
lateinit var message: String
```
 
#### Associate the entire properties file
The entire properties file can be converted into a Kotlin class by defining [@component](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Component.html) data classes. 

A property with name `example.properties.message` can be translated to 3 hierarchical levels - `example` -> `properties` -> `message`. Accordingly define a hierarchy of classes, each containing a [@ConfigurationProperties](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/properties/ConfigurationProperties.html) annotation.
1. Class `AppPropertiesWired` with a variable of type class `ExamplePropertiesWired`.
    - Annotate the class with `@ConfigurationProperties` prefix, with value of the 1st level in the heriarchy.
    - `@ConfigurationProperties(prefix="example")`
2. Class `ExamplePropertiesWired` with a variable of type string `message`. The message string is the leaf level in the hierarchy of properties. 
    - Annotate the class with `@ConfigurationProperties` prefix, with value of the 2nd level in the hierarchy.
    - `@ConfigurationProperties(prefix="properties")`

### Preview the Properties vis HTTP endpoint
Run the application using `./gradlew bootRun` and hit [http://localhost:8080/](http://localhost:8080/) in a browser. As it can be seen, the string message from the properties file is displayed in the browser.

Hope the article gave a glimpse of how to define and use the properties in the code.
