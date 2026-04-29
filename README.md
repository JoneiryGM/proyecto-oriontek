🌀 OrionTek API - Gestión de Clientes y Direcciones
Este proyecto es una RESTful API de alto rendimiento desarrollada con Java 21 y Spring Boot 3, diseñada para gestionar la relación entre clientes y sus múltiples direcciones bajo un modelo de arquitectura limpia y escalable.

🚀 Tecnologías Core
Java 21: Aprovechando las últimas funcionalidades del lenguaje como Records para DTOs inmutables.

Spring Boot 3.2+: Framework principal para la inyección de dependencias, seguridad y gestión web.

PostgreSQL: Motor de base de datos relacional para persistencia de datos crítica.

Spring Data JPA: Abstracción de persistencia con optimización de consultas mediante JPQL.

Lombok: Reducción de código repetitivo mediante el uso de anotaciones como @Builder y @RequiredArgsConstructor.

Hibernate Validations: Validación estricta de datos de entrada mediante anotaciones de Jakarta Bean Validation.

🏗️ Aspectos Destacados de la Arquitectura
💎 Modelo de Dominio Rico (Rich Domain Model)
A diferencia de los modelos anémicos tradicionales, las entidades de este proyecto (ClienteDomain, DireccionDomain) encapsulan su propia lógica de transformación y estado. Implementan interfaces genéricas de utilidad (ToDTO, TransformFrom) para garantizar una conversión limpia y coherente entre capas.

🛡️ Manejo Global de Excepciones
Implementación de un GlobalExceptionHandler centralizado que garantiza respuestas consistentes y semánticas ante errores de negocio (BusinessException) o recursos no encontrados (ResourceNotFoundException), mejorando la experiencia del consumidor de la API.

📊 Consultas Avanzadas y Paginación
El sistema cuenta con un motor de búsqueda dinámico en la capa de persistencia que permite filtrar clientes por nombre, email y estado, integrando de forma nativa la paginación y ordenamiento de Spring Data para manejar grandes volúmenes de datos con eficiencia.

🔌 Documentación con Swagger/OpenAPI
Integración total con SpringDoc OpenAPI, proporcionando una interfaz interactiva para probar los endpoints y documentar los contratos de la API de forma automática.

🛠️ Buenas Prácticas Aplicadas
Separación de Responsabilidades (SoC): Capas de Controlador, Servicio y Repositorio claramente definidas.

Inmutabilidad: Uso exhaustivo de Java Records para el transporte de datos (DTOs).

Transaccionalidad Optimizada: Uso estratégico de @Transactional(readOnly = true) para mejorar el rendimiento en operaciones de lectura en PostgreSQL.

Logs Estructurados: Implementación de trazabilidad mediante SLF4J para monitoreo de operaciones críticas.

Pruebas Unitarias: Suite de tests desarrollada con JUnit 5 y Mockito, asegurando la integridad de la lógica de negocio en servicios y dominio.

📋 Prerrequisitos
JDK 21

Maven 3.9+

PostgreSQL 15+
