# Retail Product Service

A Spring Boot RESTful API application for managing retail products with comprehensive CRUD operations, built with Java 17 LTS and designed for deployment on AWS EC2.

## Features

- ✅ Complete CRUD operations for Product management
- ✅ Java 17 LTS support
- ✅ RESTful API with proper HTTP status codes
- ✅ Input validation and error handling
- ✅ OWASP security compliance
- ✅ SonarQube code quality checks
- ✅ 80%+ test coverage
- ✅ Infrastructure as Code (CloudFormation)
- ✅ CI/CD pipeline with AWS CodePipeline
- ✅ EC2 deployment ready

## Technology Stack

- **Java**: 17 LTS
- **Spring Boot**: 3.2.0
- **Build Tool**: Maven
- **Database**: H2 (Dev), PostgreSQL (Prod)
- **Security**: Spring Security
- **Testing**: JUnit 5, Mockito
- **Code Quality**: SonarQube, OWASP Dependency Check
- **CI/CD**: AWS CodePipeline, CodeBuild, CodeDeploy
- **Infrastructure**: AWS EC2, CloudFormation

## Prerequisites

- Java 17 JDK
- Maven 3.8+
- Git
- AWS CLI (for deployment)
- AWS Account with appropriate permissions

## Quick Start

### Local Development

1. Clone the repository:
```bash
git clone git@github.com:vedesai/cons-retail-commerce.git
cd cons-retail-commerce
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the API:
```
http://localhost:8080/api/v1/products
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Code Quality Checks

```bash
# Run SonarQube analysis
mvn sonar:sonar

# Run OWASP dependency check
mvn dependency-check:check
```

## API Endpoints

### Product Management

- `POST /api/v1/products` - Create a new product
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products/sku/{sku}` - Get product by SKU
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/active` - Get active products
- `GET /api/v1/products/search?name={name}` - Search products by name
- `GET /api/v1/products/category/{category}` - Get products by category
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product

### Health Check

- `GET /actuator/health` - Application health status

## Example API Usage

### Create Product

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU-001",
    "name": "Test Product",
    "description": "Test Description",
    "price": 99.99,
    "quantity": 100,
    "category": "Electronics",
    "brand": "TestBrand"
  }'
```

### Get Product by ID

```bash
curl http://localhost:8080/api/v1/products/1
```

## Infrastructure Deployment

### Deploy EC2 Infrastructure

The infrastructure can be deployed using AWS CloudFormation via the AWS IaC MCP server.

1. Deploy EC2 infrastructure:
```bash
# Using AWS CLI
aws cloudformation create-stack \
  --stack-name retail-product-service-infra \
  --template-body file://infrastructure/ec2-infrastructure.yaml \
  --parameters ParameterKey=KeyPairName,ParameterValue=your-key-pair \
               ParameterKey=VpcId,ParameterValue=vpc-xxxxx \
               ParameterKey=SubnetId,ParameterValue=subnet-xxxxx
```

2. Deploy CodePipeline:
```bash
aws cloudformation create-stack \
  --stack-name retail-product-service-pipeline \
  --template-body file://infrastructure/codepipeline.yaml \
  --parameters ParameterKey=GitHubToken,ParameterValue=your-github-token \
               ParameterKey=EC2InstanceId,ParameterValue=i-xxxxx \
               ParameterKey=ApplicationBucketName,ParameterValue=your-bucket-name
```

## Project Structure

```
retail-product-service/
├── src/
│   ├── main/
│   │   ├── java/com/deloitte/retail/
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access
│   │   │   ├── model/          # Entity models
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── mapper/         # DTO mappers
│   │   │   ├── exception/      # Custom exceptions
│   │   │   └── config/         # Configuration classes
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-prod.yml
│   └── test/
│       └── java/com/deloitte/retail/
├── infrastructure/
│   ├── ec2-infrastructure.yaml  # EC2 CloudFormation template
│   ├── codepipeline.yaml        # CodePipeline template
│   ├── appspec.yml              # CodeDeploy specification
│   └── scripts/                 # Deployment scripts
├── pom.xml
├── sonar-project.properties
├── PROJECT_SPECIFICATION.md
├── TECHNICAL_BEST_PRACTICES.md
└── README.md
```

## Configuration

### Application Properties

- `application.yml` - Default configuration (H2 database)
- `application-prod.yml` - Production configuration (PostgreSQL)

### Environment Variables

- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `SERVER_PORT` - Server port (default: 8080)

## Security

- OWASP Top 10 compliance
- Input validation on all endpoints
- Secure error handling
- Security headers configured
- Dependency vulnerability scanning

## Testing

- Unit tests with JUnit 5 and Mockito
- Integration tests for controllers
- Minimum 80% code coverage
- JaCoCo coverage reports

## Code Quality

- SonarQube analysis
- OWASP Dependency Check
- Code coverage reporting
- Automated quality gates

## CI/CD Pipeline

The application uses AWS CodePipeline for continuous integration and deployment:

1. **Source**: GitHub repository
2. **Build**: AWS CodeBuild (Maven build, tests, quality checks)
3. **Deploy**: AWS CodeDeploy (deploy to EC2)

## Documentation

- [Project Specification](PROJECT_SPECIFICATION.md) - Detailed project requirements
- [Technical Best Practices](TECHNICAL_BEST_PRACTICES.md) - Development guidelines

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright © 2024 Deloitte. All rights reserved.

## Support

For issues or questions, please create an issue in the GitHub repository.

