/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.pkgfinal.project;

/**
 *
 * @author bagastyahendraswara
 */
public class EconomicTrain extends Train {

    public EconomicTrain(String name, String source, String destination, String time, int maxCapacity, int trainNumber) {
        super(name, source, destination, time, maxCapacity, trainNumber);
    }

    @Override
    public void displayInfo() {
        System.out.println("[Economic Class Train]");
        super.displayInfo();
    }
}