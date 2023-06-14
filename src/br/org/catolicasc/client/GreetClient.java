package br.org.catolicasc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GreetClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        // handler para escrita de dados
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // handler lara leitura de dados
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public void sendMessage(String msg) throws IOException {
        out.println(msg);  // manda a msg para o socket
    }

    public String receiveMessage() throws IOException {
        return in.readLine();  // retorna a mensagem recebida do socket
    }

    public static void main(String[] args) {
        GreetClient client = new GreetClient();
        try {
            client.start("127.0.0.1", 12345);

            Scanner scanner = new Scanner(System.in);
            String mensagem;
            String response;
            do {
                System.out.print("Entre uma mensagem (!quit para sair): ");
                mensagem = scanner.nextLine();
                client.sendMessage(mensagem);
                response = client.receiveMessage();
                System.out.println("Resposta do servidor: " + response);
            } while (!"!quit".equals(mensagem) && !"!quit".equals(response));

            client.sendMessage("!quit");
            System.out.println("Desligando cliente...");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            client.stop();
        }
    }
}
