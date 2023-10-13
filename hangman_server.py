import socket
import threading
import random

words = ["beach", "movie", "mountain", "food", "holiday", 
         "concert", "christmas", "home", "book", "music", 
         "internship", "trip"]

def choose_random_word():
    return random.choice(words)

def start_game():
    chosen_word = choose_random_word()
    missing_word = ["_"] * len(chosen_word)
    trials = 10
    return chosen_word, missing_word, trials

def check_word(chosen_word, missing_word, guess):
    if guess in chosen_word:
        for i in range(len(chosen_word)):
            if(chosen_word[i] == guess):
                missing_word[i] = guess
        return True
    return False

def multi_client(client_socket):
    chosen_word, missing_word , trials = start_game()

    while trials > 0:
        if all(letter != '_' for letter in missing_word):
            client_socket.send(f"Congratulations! You won. Your answer is: {chosen_word}\n".encode())
            break

        client_socket.send(f"Word: {' '.join(missing_word)}\nYou have {trials} attempts left\nYour next Guess: ".encode())
        guess = client_socket.recv(1024).decode().lower()

        if len(guess) != 1 or not guess.isalpha():
            client_socket.send("Invalid input. Please try again with a single letter.".encode())
            continue

        if check_word(chosen_word, missing_word, guess):
            continue

        trials -= 1
    client_socket.send(f"The chosen word is {chosen_word}. This is only one-time program. BYE!".encode())
        
    client_socket.close()

def server_program():
    host = socket.gethostname()
    port = 5000

    server_socket = socket.socket()

    try:
        server_socket.bind((host, port))
        server_socket.listen()
        
        print("Wating for the client connection")

        while True:
            #if there's connection
            client_socket, address = server_socket.accept()
            print("Connected with Hangman client")
            print("Connection from: "+str(address)) 

            #for handling multi-client
            client_thread = threading.Thread(target=multi_client, args=(client_socket,))
            client_thread.start()          

    except Exception as e:
        print("An error occurred:", e)
    finally:
        server_socket.close()

if __name__ == '__main__':
    server_program()