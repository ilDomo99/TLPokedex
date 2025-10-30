# TLPokedex

A RESTful API service that provides information about Pokemon, including their descriptions, habitats, and legendary
status. The service also offers translations of Pokemon descriptions into Yoda-style and Shakespearean English.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java JDK 21 (https://www.oracle.com/it/java/technologies/downloads/)
- Maven 3.9 or later (https://maven.apache.org/install.html)
- Docker (https://www.docker.com/get-started/)
- Git (optional, for cloning the repository)

## Installation

1. Clone the repository (or download the source code):
   ```bash
   git clone https://github.com/yourusername/TLPokedex.git
   ```

2. Navigate to the project directory:
   ```bash
   cd TLPokedex
   ```

3. Build the application using Maven:
   ```bash
   mvn clean install
   ```

NOTE: using 'mvn clean install' if docker is installed, a docker image will be created

## Docker

How to build the application using the Dockerfile:

1. Use Maven clean package
   ```bash
   mvn clean package
   ```

2. Run the Docker build
   ```bash
   docker build -t tlpokedex
   ```

3. Run the Docker build
   ```bash
   docker run -p 8080:8080 tlpokedex
   ```

## Running the Application

1. Start the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
   
2. Start the application using Docker:
   ```bash
   docker run -p 8080:8080 tlpokedex:1.0.0
   ```

The application will start and listen on `http://localhost:8080` by default.

## Swagger

Full Swagger documentation is available at `http://localhost:8080/swagger-ui/index.html#`

## API Endpoints

The following endpoints are available:

### Get Pokemon Information

- **URL**: `/pokemon/{pokemonName}`
- **Method**: GET
- **Description**: Retrieves basic information about a Pokemon, including its description, habitat, and legendary status
- **Path Parameters**:
    - `pokemonName`: Name of the Pokemon (case-insensitive)

### Get Translated Pokemon Information

- **URL**: `/pokemon/translated/{pokemonName}`
- **Method**: GET
- **Description**: Retrieves Pokemon information with translated description:
    - Yoda-style translation for Pokemon found in cave habitat or if it's legendary
    - Shakespeare translation for all other Pokemon
- **Note**: If the FunTranslation API fails, the original Pokemon description is returned instead
- **Path Parameters**:
    - `pokemonName`: Name of the Pokemon (case-insensitive)

    
### Improvements for Production
- Create a .yml configuration file for each environment containing constants (e.g., API endpoints)
- Remove unnecessary escape characters
- Add logging for class entry and exit points
- Use the POST method instead of the GET method
- Implementing a Cache system