package com.sec.cryptohdsClient;

import com.sec.cryptohdsClient.handler.ClientHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CryptohdsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptohdsClientApplication.class, args);

		/*LEDGER COMMANDS*/
		final String REGISTER_LEDGER = "register";
		final String BALANCE_LEDGER = "balance";
		final String AUDIT_LEDGER = "audit";

		/*OPERATION COMMANDS*/
		final String SEND_OPERATION = "send";
		final String RECEIVE_OPERATION = "receive";

		final String EXIT = "exit";

		ClientHandler clientHandler = new ClientHandler();

		System.out.println("Ledger commands:");
		System.out.println("	 ->" + REGISTER_LEDGER);
		System.out.println("	 ->" + BALANCE_LEDGER);
		System.out.println("	 ->" + AUDIT_LEDGER);
		System.out.println("----------------------------");
		System.out.println("Operation commands:");
		System.out.println("	 ->" + SEND_OPERATION);
		System.out.println("	 ->" + RECEIVE_OPERATION);
		System.out.println("----------------------------");
		System.out.println("Or simply: ");
		System.out.println("	 ->" + EXIT);
		System.out.println("$> ");

		Scanner reader = new Scanner(System.in);
		String command = reader.nextLine();
		while(!command.equals(EXIT)){

			String ledgerName = "";
			String ledgerPassword = "";

			switch(command){
				case REGISTER_LEDGER:
					System.out.println("Ledger Name: ");
					ledgerName = reader.nextLine();
					System.out.println("Ledger  Password: ");
					ledgerPassword = reader.nextLine();

					clientHandler.register(ledgerName, ledgerPassword);

					System.out.println("$> ");
					break;
				case BALANCE_LEDGER:
					System.out.println("$> ");
					break;
				case AUDIT_LEDGER:
					System.out.println("$> ");
					break;
				case SEND_OPERATION:
					System.out.println("$> ");
					break;
				case RECEIVE_OPERATION:
					System.out.println("$> ");
					break;
				default:
					ledgerName = "";
					ledgerPassword = "";
					break;
			}
			command = "";
		}
		reader.close();

		return;
	}
}