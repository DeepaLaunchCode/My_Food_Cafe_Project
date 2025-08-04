# MyFoodCafe - Full-Stack Restaurant Application

MyFoodCafe is a full-stack restaurant web application designed to provide a complete and modern digital experience for a restaurant's customers. Built with a dynamic React frontend and a robust Spring Boot backend, it allows users to:

- Browse the menu  
- Place online orders  
- Make and manage table reservations  
- Submit inquiries  

The entire user journey — from client interaction to data persistence — is handled efficiently. Key features are enhanced with SMS and email notifications to keep customers informed.

---

## 🚀 Technologies Used

### Backend
- Java 21  
- Spring Boot 3  
- Spring Data JPA / Hibernate  
- Maven  

### Frontend
- React  
- JavaScript 
- HTML5 & CSS 

### Database
- PostgreSQL for online, MySQL for local system  

### APIs & Services
- **Twilio** – for SMS notifications  
- **Spring Mail** – for email notifications via Gmail  

### Deployment & DevOps
- Docker  
- Render (Backend & Database Hosting)  
- Netlify (Frontend Hosting)  

---

## ⚙️ Local Development Setup

Follow these steps to run the app locally for development and testing.

### Prerequisites

Ensure you have the following installed:

- Git  
- Java JDK 21 or later  
- Apache Maven  
- Node.js and npm  
- PostgreSQL  
- An IDE (e.g., IntelliJ IDEA or VS Code with Lombok plugin)
-Gmail id, Twilio id, render id ( if deploying online)

---
Dependency for Spring boot project is added 

## 🛠️ Installation Steps

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <repository-folder>
```

### 2. Set Up PostgreSQL Database

- Start your local PostgreSQL server.
- Create a database, e.g., `myfoodcafe_db`.
- (Optional) Run schema and sample data SQL scripts if available.

---

### 3. Configure the Backend

```bash
cd myfoodcafe-java-spring-back-end
```

Create `application.properties` inside `src/main/resources/`:

```properties
# Server Port
server.port=8080

# PostgreSQL Configuration
DATASOURCE_URL=jdbc:postgresql://localhost:5432/myfoodcafe_db
DATASOURCE_USER=your_postgres_username
DATASOURCE_PASSWORD=your_postgres_password

# Twilio SMS Configuration
TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=+1234567890

# Gmail Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your_16_digit_google_app_password

# Frontend URL for emails
FRONTEND_URL=http://localhost:5173
```

---

### 4. Run the Backend

```bash
./mvnw spring-boot:run
```

- Backend API will be available at: `http://localhost:8080`  
- API documentation: `http://localhost:8080/swagger-ui.html`

---

### 5. Configure and Run the Frontend

```bash
cd myfoodcafe-react-frontend
npm install
```

Create a `.env` file in the frontend root:

```toml
VITE_API_BASE_URL=http://localhost:8080
```

Start the frontend dev server:

```bash
npm run dev
```

Visit: [http://localhost:5173](http://localhost:5173)

---

## 🧩 Project Planning Links

- **Wireframes**:   https://www.figma.com/design/iIAap9ta3sWU8RmPxV9cz4/my-wireframe1?t=3V4e2cKzVTyv2mFh-0
- **ER Diagram**: oldERD: https://www.figma.com/board/RVrMjgKWQHt8RanTwycWja/ERD-MyFoodCafe?node-id=0-1&p=f&t=JCNMTYHVhaWoK8PR-0 
NEW ERD: https://dbdiagram.io/d/MyFoodCafe-UpdateERD-6890f0fedd90d17865608177 

---

## 🌱 Future Features & Improvements

### 🔐 Admin Dashboard

- **User Story**:  
  - "As a restaurant owner, I want a secure dashboard to manage menu items without needing to touch the database."
  - "As a manager, I want to view and track all incoming orders and reservations."

- **Implementation Plan**:
  - Create protected admin routes in React
  - Role-based access control with Spring Security
  - Add new REST endpoints for admin tasks

---

### 🔑 User Authentication

- Implement JWT-based authentication  
- Allow user signup/login  
- Enable personal order & reservation history

---

### 💳 Online Payments

- Integrate a payment gateway (Stripe/Square)  
- Real-time payment processing for online orders

---

### 📡 Real-Time Order Status Updates

- Use WebSockets to display real-time order updates  
- Examples: “Preparing”, “Out for Delivery”, etc.

---



## 📬 Feedback & Contributions

Feel free to open issues, suggest improvements, or contribute via pull requests!
