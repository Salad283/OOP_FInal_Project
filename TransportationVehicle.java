/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.pkgfinal.project;

/**
 *
 * @author bagastyahendraswara
 */
public abstract class TransportationVehicle {

    protected String name;
    protected String source;
    protected String destination;
    protected String time;

    public TransportationVehicle(String name, String source, String destination, String time) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    public abstract void displayInfo();
}
