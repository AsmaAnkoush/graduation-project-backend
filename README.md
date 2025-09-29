
# 💉 SmartVax – Backend

This is the **Spring Boot backend** for the **SmartVax** system – a vaccination management platform for children.  
It supports **AI-powered analysis of parent feedback**, automatic smart reminders, and a built-in **AI chatbot** to assist parents after vaccination.

This backend exposes RESTful APIs and connects to a MySQL database.  
It is part of the **COMP4382 Graduation Project** at Birzeit University.

---

## 📥 How to Download the Project

You can download or clone the project from GitHub:

```bash
git clone https://github.com/AsmaAnkoush/smartvax-backend.git
cd smartvax-backend
```

---

## ▶️ How to Run the Backend

### ✅ Requirements:
- Java 17+
- Maven
- MySQL (configured in `application-dev.yml` or `application-prod.yml`)

### ▶️ Steps:

```bash
./mvnw
```

🔗 The backend will run at:  
**http://localhost:8080**

📄 Swagger UI available at:  
**http://localhost:8080/swagger-ui/index.html**

---

## 📁 Project Structure

- `src/main/java/com/bzu/smartvax/` – Main application code  
- `domain/` – JPA entities  
- `repository/` – Spring Data JPA Repositories  
- `service/` – Business logic  
- `web/rest/` – REST controllers  
- `security/` – Authentication and role-based access  
- `resources/config/` – Application configuration files  

---

## ⭐ Main Features

- 📅 **Vaccination Appointment Management**  
  Create, confirm, reschedule, or cancel appointments. Link appointments to children and vaccination schedules.

- 📋 **Vaccination Certificates API**  
  Provide official data for completed vaccinations for PDF or digital view.

- 📈 **Growth Analysis Engine**  
  Calculates if a child's weight and height are within a normal range based on age (in days).

- 🔔 **Reminder System**  
  Automatically sends reminders before and after appointments. Includes logic for missed doses and critical cases.

- 🤖 **AI Feedback Analyzer**  
  Analyze parent-submitted feedback using a trained Weka model for detecting abnormal symptoms.

- 💬 **AI Chatbot Integration**  
  Allows parents to chat with the system about symptoms. The bot suggests possible causes or recommends contacting health workers.

- 🛡️ **Role-Based Access**  
  APIs are protected and responses are filtered by user role: `PARENT`, `HEALTH_WORKER`, `ADMIN`.

---

## Screenshots

### Home Page
![Home Page](screenshots/HomePageScreen.png)

### Login Page
![Login Page](screenshots/lofginPage.png)

### New Account Registration
![New Account Registration](screenshots/NewAccountRegistrationScreen.png)

### Child Growth Tracking
![Child Growth Tracking](screenshots/ChildGrowthTrackingScreen.png)

### Vaccine Info & AI Symptom Tool
![Vaccine Info & AI Symptom Tool](screenshots/VaccineInfo&AISymptomToolScreen.png)

### SmartVax Comprehensive Post-Vaccination Care Tool
![SmartVax Comprehensive Post-Vaccination Care Tool](screenshots/SmartVaxComprehensivePost-VaccinationCareTool.png)


## 👩‍💻 Team Members

- **Hala Qurt** 
- **Aya Hammad** 
- **Asmaa Ankoush** 

---

## 📝 Notes

- Authentication is handled using session cookies.  
- Uses Liquibase for database migrations.  
- Scheduled tasks implemented for real-time reminder and status updates.  
- API supports integration with frontend and external notification systems.  
- The chatbot and AI analyzer are built-in and require no external AI service.

---
