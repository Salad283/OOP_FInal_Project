package oop.pkgfinal.project;

import java.sql.*;
import static oop.pkgfinal.project.DBConnection.connect;

public class ReservationSystem {

    public ReservationSystem() {
    }

    public void displayAvailableTrains() {
        String query = "SELECT * FROM trains";
        try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                String trainType = rs.getString("type");
                TransportationVehicle train;

                if ("business".equalsIgnoreCase(trainType)) {
                    train = new BusinessTrain(
                            rs.getString("name"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getString("time"),
                            rs.getInt("max_capacity"),
                            rs.getInt("train_number")
                    );
                } else {
                    train = new EconomicTrain(
                            rs.getString("name"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getString("time"),
                            rs.getInt("max_capacity"),
                            rs.getInt("train_number")
                    );
                }

                train.displayInfo();
                int availableSeats = checkSeatAvailability(rs.getInt("train_number"));
                System.out.println("Available Seats: " + availableSeats);
                System.out.println();

                found = true;
            }

            if (!found) {
                System.out.println("No trains available.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching trains: " + e.getMessage());
        }
    }

    private int getBookedSeats(int trainNumber, Connection conn) throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM reservations WHERE train_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(countQuery)) {
            stmt.setInt(1, trainNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int checkSeatAvailability(int trainNumber) {
        String trainQuery = "SELECT max_capacity FROM trains WHERE train_number = ?";
        try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(trainQuery)) {
            stmt.setInt(1, trainNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int maxCapacity = rs.getInt("max_capacity");
                int bookedSeats = getBookedSeats(trainNumber, conn);
                return maxCapacity - bookedSeats;
            }
        } catch (SQLException e) {
            System.out.println("Error checking seat availability: " + e.getMessage());
        }
        return -1;
    }

    public int bookTicket(int trainNumber, String passengerName, String email, String phone) {
        try (Connection conn = DBConnection.connect()) {
            int availableSeats = checkSeatAvailability(trainNumber);
            if (availableSeats <= 0) {
                System.out.println("Train is full.");
                return -1;
            }

            Passenger p = new Passenger(passengerName, email, phone);
            int passengerId = DBConnection.savePassenger(p);

            if (passengerId == -1) {
                System.out.println("Failed to save passenger.");
                return -1;
            }

            DBConnection.saveReservation(passengerId, trainNumber);

            System.out.println("Ticket booked for " + passengerName + " on Train " + trainNumber);
            return passengerId;

        } catch (SQLException e) {
            System.out.println("Error booking ticket: " + e.getMessage());
            return -1;
        }
    }

    public boolean cancelTicket(int trainNumber, String passengerName) {
        String findPassenger = "SELECT id FROM passengers WHERE name = ?";
        String deleteReservation = "DELETE FROM reservations WHERE passenger_id = ? AND train_number = ?";
        try (Connection conn = DBConnection.connect(); PreparedStatement psFind = conn.prepareStatement(findPassenger)) {
            psFind.setString(1, passengerName);
            ResultSet rs = psFind.executeQuery();
            if (rs.next()) {
                int passengerId = rs.getInt("id");
                try (PreparedStatement psDel = conn.prepareStatement(deleteReservation)) {
                    psDel.setInt(1, passengerId);
                    psDel.setInt(2, trainNumber);
                    int rowsDeleted = psDel.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Ticket for " + passengerName + " on Train " + trainNumber + " canceled.");
                        return true;
                    } else {
                        System.out.println("Reservation not found.");
                        return false;
                    }
                }
            } else {
                System.out.println("Passenger not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error canceling ticket: " + e.getMessage());
            return false;
        }
    }

    public void displayTicketDetails(int trainNumber, String passengerName) {
        String query = """
            SELECT t.name AS train_name, t.train_number, p.name AS passenger_name, p.email, p.phone, t.time
            FROM trains t
            JOIN reservations r ON t.train_number = r.train_number
            JOIN passengers p ON r.passenger_id = p.id
            WHERE t.train_number = ? AND p.name = ?
            """;
        try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, trainNumber);
            stmt.setString(2, passengerName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Ticket Details:");
                System.out.println("Train Name: " + rs.getString("train_name"));
                System.out.println("Train Number: " + rs.getInt("train_number"));
                System.out.println("Passenger Name: " + rs.getString("passenger_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Departure Time: " + rs.getString("time"));
            } else {
                System.out.println("Ticket details not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error displaying ticket details: " + e.getMessage());
        }
    }

    public boolean cancelTicketById(int trainNumber, int passengerId) {
        try (Connection conn = DBConnection.connect()) {

            String deleteReservation = "DELETE FROM reservations WHERE train_number = ? AND passenger_id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteReservation);
            stmt.setInt(1, trainNumber);
            stmt.setInt(2, passengerId);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                System.out.println("Reservation canceled for Passenger ID: " + passengerId);

                String checkReservations = "SELECT COUNT(*) FROM reservations WHERE passenger_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkReservations);
                checkStmt.setInt(1, passengerId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {

                    String deletePassenger = "DELETE FROM passengers WHERE id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deletePassenger);
                    deleteStmt.setInt(1, passengerId);
                    deleteStmt.executeUpdate();
                    System.out.println("Passenger ID " + passengerId + " has been deleted from database.");
                }

                return true;
            } else {
                System.out.println("Reservation not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error canceling reservation: " + e.getMessage());
            return false;
        }
    }

    public void displayTicketDetailsById(int passengerId) {
        try (Connection conn = DBConnection.connect()) {
            String query = """
                SELECT p.name, p.email, p.phone, r.train_number, t.name AS train_name, t.source, t.destination, t.time
                FROM passengers p
                JOIN reservations r ON p.id = r.passenger_id
                JOIN trains t ON r.train_number = t.train_number
                WHERE p.id = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, passengerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Ticket Details:");
                System.out.println("Passenger ID: " + passengerId);
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Train Name: " + rs.getString("train_name"));
                System.out.println("Train Number: " + rs.getInt("train_number"));
                System.out.println("Route: " + rs.getString("source") + " to " + rs.getString("destination"));
                System.out.println("Departure Time: " + rs.getString("time"));
            } else {
                System.out.println("No reservation found for Passenger ID: " + passengerId);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving ticket details: " + e.getMessage());
        }
    }

    public static Passenger getPassengerById(int id) {
        String query = "SELECT * FROM passengers WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Passenger(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching passenger: " + e.getMessage());
        }
        return null;
    }

}
