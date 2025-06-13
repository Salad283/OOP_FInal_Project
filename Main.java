/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package oop.pkgfinal.project;

import java.util.Scanner;

/**
 *
 * @author bagastyahendraswara
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationSystem reservationSystem = new ReservationSystem();

        System.out.println("Welcome to the Train Reservation System");
        boolean running = true;

        while (running) {
            System.out.println("\n1. Check Available Trains");
            System.out.println("2. Check Seat Availability");
            System.out.println("3. Book a Ticket");
            System.out.println("4. Display Ticket Details");
            System.out.println("5. Cancel a Ticket");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> {
                        reservationSystem.displayAvailableTrains();
                    }
                    case 2 -> {
                        System.out.print("Enter the train number to check seat availability: ");
                        int trainNumberToCheck = Integer.parseInt(scanner.nextLine().trim());
                        int availableSeats = reservationSystem.checkSeatAvailability(trainNumberToCheck);
                        if (availableSeats != -1) {
                            System.out.println("Available seats for Train " + trainNumberToCheck + ": " + availableSeats);
                        } else {
                            System.out.println("Train not found.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter the train number to book a ticket: ");
                        int trainNumberToBook = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter passenger name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Enter phone number: ");
                        String phone = scanner.nextLine().trim();

                        int passengerId = reservationSystem.bookTicket(trainNumberToBook, name, email, phone);
                        if (passengerId != -1) {
                            System.out.println("Booking successful! Passenger ID: " + passengerId);
                        } else {
                            System.out.println("Booking failed.");
                        }
                    }

                    case 4 -> {
                        System.out.print("Enter the passenger ID to display ticket details: ");
                        int passengerIdToDisplay = Integer.parseInt(scanner.nextLine().trim());
                        reservationSystem.displayTicketDetailsById(passengerIdToDisplay);
                    }
                    case 5 -> {
                        System.out.print("Enter the passenger ID to cancel a ticket: ");
                        int passengerIdToCancel = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter train number: ");
                        int trainNumberToCancel = Integer.parseInt(scanner.nextLine().trim());
                        reservationSystem.cancelTicketById(trainNumberToCancel, passengerIdToCancel);
                    }
                    case 6 -> {
                        System.out.println("Exiting. Thank you!");
                        running = false;
                    }
                    default ->
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        scanner.close();
    }
}
