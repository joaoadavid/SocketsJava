package br.org.catolicasc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        // inicializar atributos
        serverSocket = new ServerSocket(port);  // escuta na porta port
        clientSocket = serverSocket.accept();  // espera conexão
        // handler para escrita de dados
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // handler lara leitura de dados
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        clientHandler();
    }

    private void clientHandler() throws IOException {
        String greeting = in.readLine();
        Scanner scanner = new Scanner(System.in);
        String mensagem = "";

        while (!"!quit".equals(greeting) && !"!quit".equals(mensagem)) {
            System.out.println("Cliente: " + greeting);
            System.out.print("Entre uma mensagem (!quit para sair): ");
            mensagem = scanner.nextLine();
            out.println(mensagem);

            greeting = in.readLine();

        }

        out.println("Desligando servidor.");
        System.out.println("Desligando servidor...");
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public static void main(String[] args) {
        GreetServer server = new GreetServer();
        try {
            server.start(12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
