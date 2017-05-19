-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Temps de generació: 28-04-2017 a les 11:21:53
-- Versió del servidor: 5.7.18-0ubuntu0.16.04.1
-- Versió de PHP: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


CREATE USER 'dax'@'localhost' IDENTIFIED BY 'dax';
GRANT ALL PRIVILEGES ON * . * TO 'dax'@'localhost';
--
-- Base de dades: `pharmacyCompany`
--

drop database pharmacyCompany;
create database pharmacyCompany;
use pharmacyCompany;
-- --------------------------------------------------------

--
-- Estructura de la taula `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Bolcant dades de la taula `products`
--

INSERT INTO `products` (`id`, `name`, `price`) VALUES
(1, 'Product1', 10),
(2, 'Product1', 10),
(3, 'Product2', 20),
(4, 'Product3', 30),
(5, 'Product4', 40),
(6, 'Product5', 50),
(7, 'Product6', 60);

-- --------------------------------------------------------

--
-- Estructura de la taula `purchases`
--

CREATE TABLE `purchases` (
  `id` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `idProduct` int(11) NOT NULL,
  `deliveryDate` date NOT NULL,
  `spcecialRequests` text NOT NULL,
  `specialInstructions` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de la taula `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `surname1` varchar(150) NOT NULL,
  `nick` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `address` varchar(150) NOT NULL,
  `telephone` int(11) DEFAULT NULL,
  `mail` varchar(150) NOT NULL,
  `birthDate` varchar(150) NOT NULL,
  `entryDate` varchar(150) NOT NULL,
  `dropOutDate` varchar(150) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `image` varchar(150) NOT NULL,
  `userType` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Bolcant dades de la taula `users`
--

INSERT INTO `users` (`id`, `name`, `surname1`, `nick`, `password`, `address`, `telephone`, `mail`, `birthDate`, `entryDate`, `dropOutDate`, `active`, `image`, `userType`) VALUES
(1, 'Jhon', 'Peterson', 'user1', '123456', 'Address1', 933333333, 'r1@r.com', '1975-01-01', '2014-01-01', '0000-00-00', 1, 'images/usersImages/user1.jpg', 1),
(2, 'Jhon1', 'Peterson1', 'user2', '123456', 'Address2', 933333333, 'r2@r.com', '1975-01-01', '2014-01-01', '0000-00-00', 1, 'images/usersImages/user2.jpg', 1),
(3, 'Jhon2', 'Peterson2', 'admin1', '123456', 'Address3', 933333333, 'r3@r.com', '1975-01-01', '2014-01-01', '0000-00-00', 1, 'images/usersImages/user3.jpg', 0),
(4, 'Jhon3', 'Peterson3', 'admin2', '123456', 'Address4', 933333333, 'r4@r.com', '1975-01-01', '2014-01-01', '0000-00-00', 1, 'images/usersImages/user4.jpg', 0);

--
-- Indexos per taules bolcades
--

--
-- Index de la taula `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Index de la taula `purchases`
--
ALTER TABLE `purchases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idUser` (`idUser`,`idProduct`),
  ADD KEY `fk_idProduct` (`idProduct`);

--
-- Index de la taula `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT per les taules bolcades
--

--
-- AUTO_INCREMENT per la taula `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT per la taula `purchases`
--
ALTER TABLE `purchases`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT per la taula `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Restriccions per taules bolcades
--

--
-- Restriccions per la taula `purchases`
--
ALTER TABLE `purchases`
  ADD CONSTRAINT `fk_idProduct` FOREIGN KEY (`idProduct`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `fk_idUser` FOREIGN KEY (`idUser`) REFERENCES `users` (`id`);

