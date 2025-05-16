# QKart Automation

## 🛍️ Project Name: QKart
**Domain**: E-commerce

QKart is an end-to-end automated testing framework developed for a demo e-commerce platform.
This automation suite validates key user journeys like user registration, login, product search,
cart management, order placement, and payment — ensuring a reliable and seamless shopping experience.

---

## ✅ Features Automated

- 🔐 **User Registration**
- 🔓 **User Login**
- 🔍 **Product Search**
- 🛒 **Add to Cart**
- 💳 **Checkout and Payment**
- 📦 **Place Order**
- 💡 **Purchase from Suggestions**

---

## 🛠️ Tech Stack

| **Component** | **Description**                                                                 |
|---------------|---------------------------------------------------------------------------------|
| **Selenium**  | Web automation tool for simulating user actions                                 |
| **Java**      | Core programming language used to implement test logic and framework structure  |
| **Gradle**    | Build automation and dependency management tool                                 |
| **TestNG**    | Testing framework supporting assertions, grouping, reporting, and parallel runs |
| **Log4j**     | Logging framework used to track and log execution details                       |
| **POM**       | Page Object Model design pattern for scalable and maintainable test code        |

---

✅ Features

🔁 Parallel Test Execution
- Supports parallel execution at both method and class levels.
- Configurable via testng.xml:

🌐 Cross-Browser Testing
- Supports running tests on Chrome, Edge, Firefox, and Safari.
- Set the desired browser in the testng.xml file:

📸 Screenshot on Failure
- Automatically captures a screenshot when any test case fails.
- Useful for debugging and tracking test failures.
- Screenshots are saved in the specified directory (e.g., screenshots/).

---

## 📁 Project Structure

QKart/
└── app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── qkart/
│   │   │       ├── pages/                   # Page Object classes
│   │   │       │   ├── Checkout.java
│   │   │       │   ├── Home.java
│   │   │       │   ├── Login.java
│   │   │       │   ├── PrivacyPolicy.java
│   │   │       │   ├── Register.java
│   │   │       │   ├── SearchResult.java
│   │   │       │   ├── TermsOfService.java
│   │   │       │   └── Thanks.java
│   │   │       └── utility/                 # Utility and config classes
│   │   │           ├── App.java
│   │   │           ├── CommonMethods.java
│   │   │           ├── ContextManager.java
│   │   │           ├── DriverFactory.java
│   │   │           ├── ListenerClass.java
│   │   │           ├── ObjectContext.java
│   │   │           ├── Screenshot.java
│   │   │           └── Setup.java
│   │   └── resources/
│   │       └── log4j2.properties            # Logging configuration
│   └── test/
│       ├── java/
│       │   └── qkart.testcases/             # TestNG test classes
│       │       ├── TestsA.java
│       │       └── TestsB.java
│       └── resources/
│           └── testng.xml                   # TestNG suite configuration
├── build.gradle                             # Gradle build script
├── settings.gradle                          # Gradle settings
└── README.md                                # Project documentation

---

## 🚀 Getting Started

### Prerequisites

- IDE
- Java JDK 14+
- Chrome/Edge/FireFox/Safari browser
- Gradle installed or use `gradlew`

### Setup & Run

1. **Clone the Repository**
   git clone https://github.com/Lakesh-Sahu/QKart_Automation.git
   cd QKart_Automation

2. Run Tests using Gradle
   ./gradlew clean test

3. Or run specific tests with TestNG
   ./gradlew test --tests "qkart.testcases.TestClassName"

📋 Test Configuration
- TestNG configuration can be managed via the testng.xml file for grouping and suite execution.

📝 Logs
- Log output is managed by Log4j and stored in the logs/ directory.
- Each page includes debug-level information for troubleshooting and traceability.

📌 Design Pattern
- Follows Page Object Model (POM):
- Improves readability, maintainability, and reusability of code.
- Each web page is represented by a Java class that encapsulates its elements and interactions.