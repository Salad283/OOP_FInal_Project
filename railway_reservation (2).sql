-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 09, 2025 at 06:51 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `railway_reservation`
--

-- --------------------------------------------------------

--
-- Table structure for table `passengers`
--

CREATE TABLE `passengers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `passengers`
--

INSERT INTO `passengers` (`id`, `name`, `email`, `phone`) VALUES
(2, 'Nicholas Johnson', 'Nicholas@GOOGLE.com', '08987654321'),
(3, 'Nicholas Lee', 'Nicholas@GOON.com', '08223334444'),
(4, 'Bagastya', 'bagastya@gmail.com', '0811111111'),
(5, 'Nicho', 'nicho@go.com', '098888888'),
(10, 'bas', 'a', '21');

-- --------------------------------------------------------

--
-- Stand-in structure for view `passenger_train_reservations`
-- (See below for the actual view)
--
CREATE TABLE `passenger_train_reservations` (
`id` int(11)
,`name` varchar(100)
,`email` varchar(100)
,`phone` varchar(20)
,`train_name` varchar(100)
,`train_number` int(11)
,`source` varchar(100)
,`destination` varchar(100)
,`time` varchar(10)
);

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL,
  `passenger_id` int(11) DEFAULT NULL,
  `train_number` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `passenger_id`, `train_number`) VALUES
(2, 2, 555),
(3, 3, 135),
(4, 4, 123),
(5, 5, 123),
(10, 10, 123);

-- --------------------------------------------------------

--
-- Table structure for table `trains`
--

CREATE TABLE `trains` (
  `train_number` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `source` varchar(100) DEFAULT NULL,
  `destination` varchar(100) DEFAULT NULL,
  `time` varchar(10) DEFAULT NULL,
  `max_capacity` int(11) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `trains`
--

INSERT INTO `trains` (`train_number`, `name`, `source`, `destination`, `time`, `max_capacity`, `type`) VALUES
(123, 'Woosh', 'Jakarta', 'Bandung', '23:05', 100, 'economic'),
(135, 'Surabaya Express', 'Surabaya', 'Malang', '10:00', 55, 'business'),
(555, 'Jogja Rail', 'Jogjakarta', 'Solo', '19:00', 80, 'economic');

-- --------------------------------------------------------

--
-- Structure for view `passenger_train_reservations`
--
DROP TABLE IF EXISTS `passenger_train_reservations`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `passenger_train_reservations`  AS SELECT `p`.`id` AS `id`, `p`.`name` AS `name`, `p`.`email` AS `email`, `p`.`phone` AS `phone`, `t`.`name` AS `train_name`, `t`.`train_number` AS `train_number`, `t`.`source` AS `source`, `t`.`destination` AS `destination`, `t`.`time` AS `time` FROM ((`passengers` `p` join `reservations` `r` on(`p`.`id` = `r`.`passenger_id`)) join `trains` `t` on(`r`.`train_number` = `t`.`train_number`)) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `passengers`
--
ALTER TABLE `passengers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `passenger_id` (`passenger_id`),
  ADD KEY `train_number` (`train_number`);

--
-- Indexes for table `trains`
--
ALTER TABLE `trains`
  ADD PRIMARY KEY (`train_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `passengers`
--
ALTER TABLE `passengers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`id`),
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`train_number`) REFERENCES `trains` (`train_number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
