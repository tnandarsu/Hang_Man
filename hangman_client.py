import socket

def client_program():
    host = socket.gethostname()
    port = 5000

    client_socket = socket.socket()
    client_socket.connect((host,port))

    while True:
        response = client_socket.recv(1024).decode()
        print(response)

        if "Bye" in response:
            client_socket.close()
            break
        else:
            guess = input()
            client_socket.send(guess.encode())

    client_socket.close()
    
if __name__ == '__main__':
    client_program()