# Hangman Game Project

This project is an exploration of the classic Hangman game implemented in both Java and Python. It also includes the Python version of the game containerized in Docker for easier deployment and management.

## Hangman - Java Version

The Java version of the Hangman game is a console-based program that allows a user to play the game by guessing letters to uncover a hidden word. The game randomly selects a word, and the user must guess it letter by letter while trying to avoid running out of attempts. 

### Running the Java Version

To run the Java version of Hangman, navigate to the `hangman-java` directory and compile the program using:

```bash
javac Hangman.java
```

Then, run the game with:

```bash
java Hangman
```

## Hangman - Python Version

The Python version of Hangman is similar to the Java version but implemented in Python. It provides the same interactive experience, allowing users to guess the hidden word by providing individual letters.

### Running the Python Version

To run the Python version of Hangman, navigate to the `hangman-python` directory and execute the following command:

```bash
python hangman.py
```

## Hangman - Python Version Containerized in Docker

In addition to the Python version of Hangman, this project demonstrates containerization using Docker. The Python Hangman game is encapsulated in a Docker container, making it easy to package and deploy the application consistently across different environments.

### Building and Running the Docker Container

To build and run the Docker container for the Python Hangman game, navigate to the `hangman-python-docker` directory and execute the following commands:

1. Build the Docker image:

```bash
docker build -t hangman-server:1.0 .
```

2. Run the Docker container:

```bash
docker run -p 5000:5000 hangman-server:1.0
```


This project demonstrates how Docker simplifies the packaging and deployment of applications, ensuring consistent behavior and easy scalability.

## Contributors

- Hannah Ebenezar  64011393
- Kasita Sansanthad  64011426
- Theint Nandar Su  64011752
