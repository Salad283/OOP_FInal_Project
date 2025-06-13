package oop.pkgfinal.project;

public interface Reservable {

    boolean addPassenger(Passenger p);

    boolean removePassenger(String name);

    boolean hasPassenger(String name);

    Passenger getPassengerDetails(String name);
}
