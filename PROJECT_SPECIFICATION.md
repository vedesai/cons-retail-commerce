# Retail Product Service - Project Specification

## 1. Project Overview

### 1.1 Purpose
The Retail Product Service is a Spring Boot-based RESTful API application designed to manage product information for retail operations. It provides comprehensive CRUD (Create, Read, Update, Delete) operations for product management with robust security, validation, and error handling.

### 1.2 Technology Stack
- **Java**: 17 LTS
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Maven
- **Database**: H2 (Development), PostgreSQL (Production)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **API Documentation**: RESTful API
- **Testing**: JUnit 5, Mockito
- **Code Quality**: SonarQube, OWASP Dependency Check
- **CI/CD**: AWS CodePipeline, CodeBuild, CodeDeploy
- **Infrastructure**: AWS EC2, CloudFormation

## 2. Functional Requirements

### 2.1 Product Management
- **Create Product**: Create new products with validation
- **Get Product**: Retrieve products by ID or SKU
- **List Products**: Get all products or filter by active status
- **Update Product**: Update existing product information
- **Delete Product**: Remove products from the system
- **Search Products**: Search products by name
- **Filter Products**: Filter products by category

### 2.2 Product Data Model
- **ID**: Unique identifier (auto-generated)
- **SKU**: Stock Keeping Unit (unique, required, 3-50 characters)
- **Name**: Product name (required, 1-255 characters)
- **Description**: Product description (optional, max 1000 characters)
- **Price**: Product price (required, positive decimal)
- **Quantity**: Stock quantity (required, non-negative integer)
- **Category**: Product category (optional, max 100 characters)
- **Brand**: Product brand (optional, max 100 characters)
- **IsActive**: Active status flag (default: true)
- **CreatedAt**: Timestamp of creation
- **UpdatedAt**: Timestamp of last update

## 3. Non-Functional Requirements

### 3.1 Performance
- API response time: < 200ms for standard operations
- Support for concurrent requests
- Database indexing on SKU and name fields

### 3.2 Security
- OWASP Top 10 compliance
- Input validation on all endpoints
- Secure error handling (no sensitive data exposure)
- Security headers configured
- Authentication required for API access

### 3.3 Reliability
- Transaction management for data consistency
- Exception handling with proper HTTP status codes
- Logging for debugging and monitoring
- Health check endpoint for monitoring

### 3.4 Code Quality
- Minimum 80% test coverage
- SonarQube quality gate compliance
- OWASP dependency vulnerability scanning
- Code follows Java best practices

## 4. API Endpoints

### 4.1 Product Endpoints
- `POST /api/v1/products` - Create a new product
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products/sku/{sku}` - Get product by SKU
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/active` - Get active products
- `GET /api/v1/products/search?name={name}` - Search products by name
- `GET /api/v1/products/category/{category}` - Get products by category
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product

### 4.2 Health Check
- `GET /actuator/health` - Application health status

## 5. Infrastructure

### 5.1 Deployment Architecture
- **Compute**: AWS EC2 instance (t3.medium recommended)
- **Database**: PostgreSQL (production), H2 (development)
- **CI/CD**: AWS CodePipeline with CodeBuild and CodeDeploy
- **Source Control**: GitHub
- **Infrastructure as Code**: AWS CloudFormation

### 5.2 Infrastructure Components
1. **EC2 Instance**: Application server
2. **Security Group**: Network access control
3. **IAM Roles**: Service permissions
4. **S3 Buckets**: Artifact storage
5. **CodePipeline**: CI/CD orchestration
6. **CodeBuild**: Build and test execution
7. **CodeDeploy**: Application deployment

## 6. Development Environment

### 6.1 Prerequisites
- Java 17 JDK
- Maven 3.8+
- Git
- AWS CLI (for deployment)
- AWS Account with appropriate permissions

### 6.2 Local Development Setup
1. Clone the repository
2. Configure `application.yml` for local development
3. Run `mvn clean install` to build
4. Run `mvn spring-boot:run` to start the application
5. Access API at `http://localhost:8080/api/v1/products`

## 7. Testing Strategy

### 7.1 Unit Tests
- Service layer tests with Mockito
- Mapper tests for DTO conversions
- Repository tests (if needed)

### 7.2 Integration Tests
- Controller tests with MockMvc
- End-to-end API tests

### 7.3 Coverage Requirements
- Minimum 80% code coverage
- All critical paths tested
- Edge cases and error scenarios covered

## 8. Deployment Process

### 8.1 Infrastructure Deployment
1. Deploy EC2 infrastructure using CloudFormation template
2. Configure security groups and IAM roles
3. Set up CodePipeline for CI/CD

### 8.2 Application Deployment
1. Code is pushed to GitHub
2. CodePipeline triggers on push to main branch
3. CodeBuild compiles, tests, and packages application
4. CodeDeploy deploys to EC2 instance
5. Application starts automatically

## 9. Monitoring and Logging

### 9.1 Logging
- Application logs: `/var/log/retail-product-service/application.log`
- System logs: Systemd journal
- Log levels: INFO (production), DEBUG (development)

### 9.2 Monitoring
- Health check endpoint: `/actuator/health`
- CloudWatch metrics (if configured)
- Application metrics via Actuator

## 10. Security Considerations

### 10.1 OWASP Compliance
- Input validation on all endpoints
- SQL injection prevention (JPA parameterized queries)
- XSS prevention (proper content types)
- CSRF protection (configured in SecurityConfig)
- Security headers (HSTS, X-Frame-Options, etc.)
- Dependency vulnerability scanning

### 10.2 Best Practices
- No sensitive data in logs
- Secure password handling
- Principle of least privilege for IAM roles
- Regular dependency updates

## 11. Project Structure

```
retail-product-service/
├── src/
│   ├── main/
│   │   ├── java/com/deloitte/retail/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   ├── exception/
│   │   │   └── config/
│   │   └── resources/
│   └── test/
│       └── java/com/deloitte/retail/
├── infrastructure/
│   ├── ec2-infrastructure.yaml
│   ├── codepipeline.yaml
│   ├── appspec.yml
│   └── scripts/
├── pom.xml
├── sonar-project.properties
└── README.md
```

## 12. Version History

- **v1.0.0** - Initial release with CRUD operations

## 13. Contact and Support

For issues or questions, please contact the development team or create an issue in the GitHub repository.

