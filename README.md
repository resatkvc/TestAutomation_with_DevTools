# Zipkin-Integrated UI Test Automation

Professional UI test automation framework with distributed tracing capabilities using Zipkin for monitoring and debugging test execution.

## 🚀 Features

- **Selenium WebDriver** based UI automation
- **Distributed Tracing** with Zipkin integration
- **Professional Logging** with SLF4J and Logback
- **Test Data Generation** with JavaFaker
- **Docker Support** for easy deployment
- **Health Monitoring** for test execution
- **Modern Java 21** with latest dependencies

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker Desktop
- Chrome/Firefox browser

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Zipkin-Integrated_UI_Test_Automation
```

### 2. Start Zipkin with Docker
```bash
# Start Zipkin server
docker-compose up -d zipkin
```

### 3. Verify Zipkin is Running
- Open http://localhost:9411 in your browser
- You should see the Zipkin UI interface
- Check Docker Desktop to ensure containers are healthy

### 4. Build the Project
```bash
mvn clean compile
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=SauceDemoCompleteTest
```

### Run with Zipkin Tracing
```bash
# The tests automatically integrate with Zipkin
mvn test -Dzipkin.enabled=true
```

### Using IDE or Terminal
```bash
# From IDE: Right-click on test class and "Run"
# From Terminal: mvn test
```

## 📊 Monitoring & Tracing

### Zipkin Dashboard
- **URL**: http://localhost:9411
- **Features**:
  - Real-time trace visualization
  - Performance metrics
  - Error tracking
  - Service dependencies

### Trace Information
Each test execution creates:
- **Root Span**: Test execution
- **Child Spans**: Individual test steps
- **Tags**: Test metadata (browser, URL, test data)
- **Duration**: Performance metrics

### Sample Trace Structure
```
Test Execution (Root Span)
├── Login Step
├── Navigation Step
├── Data Entry Step
└── Verification Step
```

## 🏗️ Project Structure

```
src/
└── test/java/
    └── proje/com/saucedemo/
        ├── config/
        │   └── WebDriverConfig.java
        ├── pages/
        │   ├── CartPage.java
        │   ├── CheckoutCompletePage.java
        │   ├── CheckoutOverviewPage.java
        │   ├── CheckoutPage.java
        │   ├── InventoryPage.java
        │   └── LoginPage.java
        ├── utils/
        │   ├── TestDataGenerator.java
        │   ├── ZipkinTracer.java
        │   ├── SeleniumTracer.java
        │   └── NetworkTracer.java
        ├── verification/
        │   └── VerificationHelper.java
        └── SauceDemoCompleteTest.java
```

## 🔧 Configuration

### Docker Compose Configuration
```yaml
services:
  zipkin:
    image: openzipkin/zipkin:latest
    ports:
      - "9411:9411"
    environment:
      - STORAGE_TYPE=memory
      - JAVA_OPTS=-Xmx512m -Xms256m
    healthcheck:
      test: ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:9411/health || exit 1"]
```

### Logging Configuration
- **File**: `src/test/resources/logback-test.xml`
- **Level**: INFO
- **Output**: Console + File
- **Rotation**: Daily with size limits

## 📈 Performance Monitoring

### Key Metrics Tracked
- **Test Duration**: Total execution time
- **Step Duration**: Individual step performance
- **Browser Performance**: Page load times
- **Error Rates**: Test failure tracking
- **Resource Usage**: Memory and CPU utilization

### Zipkin Integration Benefits
- **Real-time Monitoring**: Live trace visualization
- **Performance Analysis**: Identify bottlenecks
- **Error Debugging**: Detailed error context
- **Test Optimization**: Performance improvement insights

## 🐛 Troubleshooting

### Zipkin Loading Issues
1. **Check Docker Status**:
   ```bash
   docker ps
   docker logs zipkin
   ```

2. **Restart Zipkin**:
   ```bash
   docker-compose restart zipkin
   ```

3. **Check Health**:
   ```bash
   curl http://localhost:9411/health
   ```

### Test Execution Issues
1. **Check WebDriver**:
   - Ensure Chrome/Firefox is installed
   - Verify WebDriverManager configuration

2. **Check Logs**:
   ```bash
   tail -f logs/saucedemo-test.log
   ```

3. **Verify Network**:
   - Ensure test URLs are accessible
   - Check proxy settings if applicable

## 🔄 Continuous Integration

### GitHub Actions Example
```yaml
name: UI Tests with Zipkin
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    services:
      zipkin:
        image: openzipkin/zipkin:latest
        ports:
          - 9411:9411
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: mvn test
```

## 📝 Logging

### Log Levels
- **ERROR**: Test failures, system errors
- **WARN**: Performance issues, warnings
- **INFO**: Test execution, important events
- **DEBUG**: Detailed debugging information

### Log Files
- **Location**: `logs/` directory
- **Format**: `saucedemo-test.YYYY-MM-DD.log`
- **Rotation**: Daily with size limits

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:
1. Check the troubleshooting section
2. Review existing issues
3. Create a new issue with detailed information
4. Include logs and trace IDs when applicable

---

**Happy Testing! 🧪✨** 