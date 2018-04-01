package com.sec.cryptohdsclient;

import com.sec.cryptohdsclient.handler.ClientHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class CryptohdsClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CryptohdsClientApplication.class, args);

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

		Scanner reader = new Scanner(System.in);

		// command will be stored here
		String command = null;

		while(!EXIT.equals(command)){

			// clear variables
			command = null;
			String ledgerName = "";
			String ledgerPassword = "";

			System.out.print("$> ");
			System.out.flush();

			// read next command
			command = reader.nextLine();

			switch(command){
				case REGISTER_LEDGER:
					System.out.println("Ledger Name: ");
					ledgerName = reader.nextLine();
					System.out.println("Ledger  Password: ");
					ledgerPassword = reader.nextLine();

					clientHandler.register(ledgerName, ledgerPassword);

					break;
				case BALANCE_LEDGER:
					break;
				case AUDIT_LEDGER:
					break;
				case SEND_OPERATION:
					break;
				case RECEIVE_OPERATION:
					break;
				default:
					break;
			}
		}
		reader.close();
		context.close();

		return;
	}
}