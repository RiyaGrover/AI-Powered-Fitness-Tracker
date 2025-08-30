AI-Powered Fitness Tracker

Author: Riya Grover
Experience Level: 3 years (Backend & Full Stack focus)
Tech Stack: Java, Spring Boot, Microservices, Keycloak (Auth), PostgreSQL, React, Redux, Material UI, Vite, Docker, Render

Project Overview

The AI-Powered Fitness Tracker is a full-stack application that helps users track physical activities, manage habits, and view weekly activity dashboards. The system emphasizes backend microservices architecture with user authentication, activity management, and habit tracking.

Features
1. User Authentication

Implemented via Keycloak OAuth2.

Supports JWT-based access for secure API calls.

Users are uniquely identified using X-User-ID in microservices.

2. Activity Management

Users can add activities like Running, Walking, or Cycling.

Each activity stores:

Type

Duration (minutes)

Calories burned

AI-generated recommendations for improvement

Activities can be viewed in a detailed view with:

AI recommendations

Safety guidelines

Suggestions and improvements

3. Habit Tracker

Users can add habits with custom frequency (Daily/Weekly).

Complete habits using a single click, updating statistics:

Total habits

Active habits

Completed habits

Completion rate (%)

Backend handles all habit data and stats calculation.

4. Weekly Dashboard

Displays weekly activity trends using a line chart.

Aggregates calories burned per day.

Provides a visual overview of weekly performance.

Architecture Overview
Backend (Spring Boot Microservices)
Service	Responsibility
User Service	Manages users, authentication via Keycloak, habit management APIs
Activity Service	Handles activity CRUD, AI recommendations, analytics
AI Service	Generates AI recommendations based on user activities
Gateway	API gateway for routing requests to microservices
Config Server	Centralized Spring Cloud configuration
Eureka Server	Service discovery for microservices

Key Design Choices:

JWT Authentication via Keycloak for all microservices.

Service-to-service communication via REST.

Modular microservices architecture for scalability.

PostgreSQL for persistent storage of user data, activities, and habits.

Frontend (React + Redux + Material UI)

Pages:

Activities: Add/view activities

Activity Detail: Detailed activity with AI recommendations

Habit Tracker: Add/complete habits and view stats

Weekly Dashboard: Line chart for weekly calories burned

Axios is configured to attach JWT and X-User-ID headers for secure API calls.

Deployment

Frontend:

Built with Vite + React + Material UI

Deployed on Render Web Service

Run commands:

npm install
npm run build
npm run preview


Backend:

Spring Boot services deployed on Render.

Requires:

Running microservices for userservice, activityservice, aiservice

Config server for centralized configuration

Eureka server for service discovery

Environment variables for Keycloak URL, DB credentials, and API keys must be set on Render.

Database:

PostgreSQL (hosted via Render free DB or any cloud DB)

Ensure connectivity for all microservices.

Usage

Login/Register via Keycloak OAuth2.

Navigate to Activities Page:

Add a new activity

Click any activity to view details with AI recommendations

Navigate to Habit Tracker:

Add habits with frequency

Complete habits to update stats

Navigate to Weekly Dashboard:

View weekly calories burned trends

Future Enhancements

Gamification: Badges, streaks, progress tracking

Mobile support (React Native / responsive design)

Integration with Nutrition APIs for diet tracking

Deploy AI model predictions for personalized fitness suggestions

Key Takeaways

Full backend microservices architecture with OAuth2 authentication.

Real-time habit tracking and activity management.

Frontend integrated seamlessly with backend microservices.

Production-ready deployment via Render with minimal downtime.

GitHub Repository: https://github.com/RiyaGrover/AI-Powered-Fitness-Tracker
