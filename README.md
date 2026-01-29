# HR Management System

A comprehensive Human Resource Management application designed to streamline employee administration, document handling, and lifecycle tracking.

## 🚀 Key Features

*   **Employee Management**: Onboard, update, and manage employee records.
*   **Document Handling**: Upload and download Offer Letters, KYC, and Resignation documents.
*   **Resignation Workflow**: Manage resignations and track status.
*   **Modern UI**: Professional interface with responsive design.
*   **Secure**: JWT-based authentication and role-based access.

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
*   **Java JDK 17** or higher
*   **Node.js** (v18+) and **npm**
*   **MySQL Server**
*   **Maven**

---

## ⚙️ Installation & Setup

### 1. Database Setup
1.  Open your MySQL client (Workbench or Command Line).
2.  Create the database:
    ```sql
    CREATE DATABASE hr_db;
    ```
3.  The application is configured to use:
    *   **Database**: `hr_db`
    *   **Username**: `root`
    *   **Password**: `root@123`
    *   *Note: If your credentials differ, update them in `backend/src/main/resources/application.properties`.*

### 2. Backend Setup (Spring Boot)
1.  Open a terminal and navigate to the backend directory:
    ```bash
    cd backend
    ```
2.  Build the application:
    ```bash
    mvn clean install
    ```
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
    *The backend server will start on **http://localhost:8080**.*

### 3. Frontend Setup (Angular)
1.  Open a **new** terminal window and navigate to the frontend directory:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    ng serve
    ```
4.  Open your browser and navigate to:
    **http://localhost:4200**

---

## 🔑 Default Credentials

*   **Admin / HR**:
    *   Username: `admin`
    *   Password: `admin` (or check `data.sql` if seeded)

## 🛠 Tech Stack
*   **Frontend**: Angular 17, TypeScript
*   **Backend**: Java Spring Boot 3, Hibernate
*   **Database**: MySQL
