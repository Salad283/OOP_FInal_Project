/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.pkgfinal.project;

import java.util.ArrayList;

/**
 *
 * @author bagastyahendraswara
 */
public class Train extends TransportationVehicle implements Reservable {

    protected int maxCapacity;
    protected int trainNumber;
    protected ArrayList<Passenger> passengers;

    public Train(String name, String source, String destination, String time, int maxCapacity, int trainNumber) {
        super(name, source, destination, time);
        this.maxCapacity = maxCapacity;
        this.trainNumber = trainNumber;
        this.passengers = new ArrayList<>();
    }

    @Override
    public boolean addPassenger(Passenger p) {
        if (passengers.size() < maxCapacity) {
            passengers.add(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePassenger(String name) {
        return passengers.removeIf(p -> p.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean hasPassenger(String name) {
        return passengers.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name));
    }

    @Override
    public Passenger getPassengerDetails(String name) {
        return passengers.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void displayInfo() {
        System.out.println("Train Name: " + name);
        System.out.println("Train Number: " + trainNumber);
        System.out.println("Route: " + source + " to " + destination);
        System.out.println("Departure Time: " + time);
        System.out.println("Max Capacity: " + maxCapacity);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public int getPassengerStrength() {
        return passengers.size();
    }
}
