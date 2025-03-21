# News-Article-Recommender

## Introduction
The **News-Article-Recommender** is a system designed to provide personalized news recommendations based on user reading history and preferences. It collects news articles from a dataset, analyzes user interactions, and uses Machine Learning (ML) and Natural Language Processing (NLP) techniques to deliver relevant articles. As users engage with the system by reading, liking, or skipping articles, recommendations are dynamically updated to reflect their evolving interests.

## Key Features
### 1. User Management
- Users can create an account and log in.
- The system stores user preferences, reading history, and interactions (e.g., liked, skipped, or read articles).

### 2. Article Processing and Categorization
- Articles are fetched from a database.
- NLP techniques are used to categorize articles into topics such as Technology, Health, Sports, AI, and more.
- Keyword extraction or a basic ML model helps classify articles automatically.

### 3. Recommendation Engine
- Uses **content-based filtering** techniques to recommend articles based on user interests.
- Tracks reading history and adjusts recommendations over time.
- Implements concurrency to handle multiple users and large-scale article processing efficiently.

### 4. AI/ML Model Integration
- Integrates pre-trained ML models or simple learning algorithms for **sentiment analysis** and **article categorization**.
- Learns from user interactions and updates preferences dynamically.
- Stores model predictions and user data in a file system or database.

### 5. Exception and File Handling
- Implements exception handling for invalid inputs, API failures, and file I/O operations.
- Uses file handling to store article data, user preferences, logs, and backup information.

### 6. Concurrency
- Supports multiple users requesting recommendations simultaneously.
- Utilizes **Java concurrency utilities** to efficiently process large datasets, categorize articles, and generate recommendations in parallel.

## Installation & Setup

### 1. Download and Install Ollama
Visit the official Ollama website: [Ollama Llama 3.2](https://ollama.com/library/llama3.2) and download the required model for your system.

### 2. Install the Model Locally
Once downloaded, install the model by executing the following command in your terminal or command prompt:

```sh
ollama run llama3.2
```

Ensure the model is successfully installed and ready for use.

### 3. Keep Ollama Running
After installation, ensure that Ollama is running in the background to allow the program to access and utilize the model.

### 4. Running the Program
Follow these steps to execute the recommendation system:
1. Start the **Server** class in your application.
2. Run the **Client** class, which initiates the necessary services, including the **Recommendations** function.

## Usage
Once the setup is complete, the system will provide news recommendations based on user preferences and interactions.


