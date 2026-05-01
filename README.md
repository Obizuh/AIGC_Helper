# AIGC_Helper

A Spring Boot-based backend service designed to facilitate AIGC (Artificial Intelligence Generated Content) interactions, chat session management, and robust user authentication.

## 🌟 Features

### 1. User Management
* **Registration & Login:** Secure user onboarding and authentication.
* **JWT Authorization:** Stateless API protection using JSON Web Tokens with 12-hour expiration for secure interactions.

### 2. AIGC Chat Module
* **Smart Interaction:** Facilitates messaging with AI models.
* **Smart Title Generation:** Automatically generates contextual chat session titles based on AIGC capabilities.

### 3. Chat History & Session Management
* **Session Control:** Initialize, retrieve, and delete chat sessions.
* **Message History:** Accurately fetch and manage message history associated with specific chat IDs.

### 4. Admin Management
* **Admin Access:** Dedicated registration and login flows for system administrators to securely access background services.

### 5. Entertainment & Gaming
* **Game Module:** Built-in interactive game features intertwined with the AIGC functionality.

## 🛠️ Tech Stack
* **Language:** Java
* **Framework:** Spring Boot
* **Security:** JWT (io.jsonwebtoken)
* **Build Tool:** Maven

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher (recommends 17+)
* Maven (or utilize the provided `mvnw` wrapper)

### Installation & Run
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/aigc_helper.git
   cd aigc_helper
   ```

2. **Build the project:**
   Using Maven wrapper:
   ```bash
   ./mvnw clean package
   ```

3. **Run the application:**
   ```bash
   java -jar target/AIGC_Helper-0.0.1-SNAPSHOT.jar
   # Or using Spring Boot Maven plugin
   ./mvnw spring-boot:run
   ```

## 📂 Project Structure
* `src/main/java/com/example/aigc_helper/`: Core Java source code, including Controllers, Services, Mappers, and Configs.
* `src/main/resources/`: Application configurations (`application.yml`).
* `doc/`: Project documentation and detailed feature lists.

## 📄 License
This project is licensed under the MIT License.

