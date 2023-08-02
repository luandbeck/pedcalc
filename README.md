# Kotlin Spring API Integrated with OpenAI API and WhatsApp API Sample

## About The Project

The project consists of creating an example service using kotlin spring that integrates with the WhatsApp API to receive
and send messages and uses the OpenAI API to transform natural language into structured data

Functionally, it was built to calculate the medication dose for pediatricians.

The goal is for the doctor to send a message via WhatsApp with the medication he would like to calculate with some
patient data.

The WhatsApp service forwards this message to our API that calls OpenAI to structure the language that came naturally
and with the structured data the service applies predetermined calculations and calls the WhatsApp API back so that the
message is returned to the doctor.

### Built With

* [Java - 17](https://www.java.com/pt-BR/)
* [Spring Boot - 3.1.2](https://spring.io/projects/spring-boot)
* [Kotlin - 1.8.22](https://kotlinlang.org/)
* [OpenAI Kotlin](https://github.com/Aallam/openai-kotlin)
* [WhatsApp Business](https://developers.facebook.com/docs/whatsapp/cloud-api/get-started)
* [Micrometer](https://micrometer.io/docs/tracing)
* [JUnit](https://junit.org/junit5/)
* [SpringMockK](https://github.com/Ninja-Squad/springmockk)
* [Docker](https://www.docker.com/)
* [Hexagonal Architecture](https://blog.octo.com/en/hexagonal-architecture-three-principles-and-an-implementation-example/)

## Contact

To contact us use the options:

* E-mail  : luanbeck@gmail.com