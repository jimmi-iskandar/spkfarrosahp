-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2025 at 11:56 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbspkfarrosahp`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_users`
--

CREATE TABLE `admin_users` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `admin_users`
--

INSERT INTO `admin_users` (`admin_id`, `username`, `password`, `full_name`, `email`, `created_at`, `updated_at`) VALUES
(2, 'isk', '$2a$10$yRL8Aq37Ym2TFyD0kcvknet9eRrKVHwldemO4DKyAbjcWMPQWdevm', 'Jimmi Iskandar', 'jim.iskandar20@gmail.com', '2025-05-25 06:13:01', '2025-05-25 17:56:18');

-- --------------------------------------------------------

--
-- Table structure for table `hasil_seleksi_akhir`
--

CREATE TABLE `hasil_seleksi_akhir` (
  `karyawan_id` int(11) NOT NULL,
  `nilai_total_spk` decimal(10,4) NOT NULL,
  `peringkat` int(11) NOT NULL,
  `tanggal_perhitungan` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `hasil_seleksi_akhir`
--

INSERT INTO `hasil_seleksi_akhir` (`karyawan_id`, `nilai_total_spk`, `peringkat`, `tanggal_perhitungan`) VALUES
(1, '0.2503', 1, '2025-05-25 17:08:19'),
(2, '0.2503', 2, '2025-05-25 17:08:19'),
(3, '0.2503', 3, '2025-05-25 17:08:19');

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `karyawan_id` int(11) NOT NULL,
  `kode_karyawan` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama_lengkap` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jabatan` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `alamat` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telepon` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`karyawan_id`, `kode_karyawan`, `nama_lengkap`, `jabatan`, `tanggal_lahir`, `alamat`, `telepon`, `created_at`, `updated_at`) VALUES
(1, 'SP001', 'ahmad', 'Screen Printing', '2025-05-09', 'sumurkidang', '09828726252', '2025-05-23 17:42:06', '2025-05-25 17:36:18'),
(2, 'SP002', 'jepri', 'Screen Printing', '2025-05-09', 'sumurkidang', '9827726252', '2025-05-23 17:43:57', '2025-05-23 17:43:57'),
(3, 'SP003', 'jamal', 'Screen Printing', '2025-05-09', 'pemalang', '09827262525', '2025-05-23 17:44:23', '2025-05-23 17:44:23');

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `kriteria_id` int(11) NOT NULL,
  `kode_kriteria` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama_kriteria` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bobot` decimal(5,3) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`kriteria_id`, `kode_kriteria`, `nama_kriteria`, `bobot`, `created_at`, `updated_at`) VALUES
(13, 'K1', 'Absensi', '0.457', '2025-05-25 17:39:50', '2025-05-25 17:51:49'),
(14, 'K2', 'Produktifitas', '0.204', '2025-05-25 17:40:21', '2025-05-25 17:51:49'),
(15, 'K3', 'Disiplin', '0.263', '2025-05-25 17:40:39', '2025-05-25 17:51:49'),
(16, 'K4', 'Kerja sama tim', '0.077', '2025-05-25 17:40:55', '2025-05-25 17:51:49');

-- --------------------------------------------------------

--
-- Table structure for table `penilaian_alternatif`
--

CREATE TABLE `penilaian_alternatif` (
  `penilaian_id` int(11) NOT NULL,
  `karyawan_id` int(11) NOT NULL,
  `kriteria_id` int(11) NOT NULL,
  `nilai` decimal(10,3) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `perbandingan_kriteria`
--

CREATE TABLE `perbandingan_kriteria` (
  `perbandingan_id` int(11) NOT NULL,
  `kriteria1_id` int(11) NOT NULL,
  `kriteria2_id` int(11) NOT NULL,
  `nilai_perbandingan` decimal(5,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `perbandingan_kriteria`
--

INSERT INTO `perbandingan_kriteria` (`perbandingan_id`, `kriteria1_id`, `kriteria2_id`, `nilai_perbandingan`) VALUES
(135, 13, 14, '3.000'),
(136, 13, 15, '2.000'),
(137, 13, 16, '4.000'),
(138, 14, 15, '1.000'),
(139, 14, 16, '3.000'),
(140, 15, 16, '5.000');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_users`
--
ALTER TABLE `admin_users`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `hasil_seleksi_akhir`
--
ALTER TABLE `hasil_seleksi_akhir`
  ADD PRIMARY KEY (`karyawan_id`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`karyawan_id`),
  ADD UNIQUE KEY `kode_karyawan` (`kode_karyawan`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kriteria_id`),
  ADD UNIQUE KEY `kode_kriteria` (`kode_kriteria`);

--
-- Indexes for table `penilaian_alternatif`
--
ALTER TABLE `penilaian_alternatif`
  ADD PRIMARY KEY (`penilaian_id`),
  ADD UNIQUE KEY `idx_penilaian_unik` (`karyawan_id`,`kriteria_id`),
  ADD KEY `kriteria_id` (`kriteria_id`);

--
-- Indexes for table `perbandingan_kriteria`
--
ALTER TABLE `perbandingan_kriteria`
  ADD PRIMARY KEY (`perbandingan_id`),
  ADD UNIQUE KEY `idx_perbandingan_unik` (`kriteria1_id`,`kriteria2_id`),
  ADD KEY `kriteria2_id` (`kriteria2_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_users`
--
ALTER TABLE `admin_users`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `karyawan`
--
ALTER TABLE `karyawan`
  MODIFY `karyawan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `kriteria`
--
ALTER TABLE `kriteria`
  MODIFY `kriteria_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `penilaian_alternatif`
--
ALTER TABLE `penilaian_alternatif`
  MODIFY `penilaian_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=169;

--
-- AUTO_INCREMENT for table `perbandingan_kriteria`
--
ALTER TABLE `perbandingan_kriteria`
  MODIFY `perbandingan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=141;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `hasil_seleksi_akhir`
--
ALTER TABLE `hasil_seleksi_akhir`
  ADD CONSTRAINT `hasil_seleksi_akhir_ibfk_1` FOREIGN KEY (`karyawan_id`) REFERENCES `karyawan` (`karyawan_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `penilaian_alternatif`
--
ALTER TABLE `penilaian_alternatif`
  ADD CONSTRAINT `penilaian_alternatif_ibfk_1` FOREIGN KEY (`karyawan_id`) REFERENCES `karyawan` (`karyawan_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `penilaian_alternatif_ibfk_2` FOREIGN KEY (`kriteria_id`) REFERENCES `kriteria` (`kriteria_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `perbandingan_kriteria`
--
ALTER TABLE `perbandingan_kriteria`
  ADD CONSTRAINT `perbandingan_kriteria_ibfk_1` FOREIGN KEY (`kriteria1_id`) REFERENCES `kriteria` (`kriteria_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `perbandingan_kriteria_ibfk_2` FOREIGN KEY (`kriteria2_id`) REFERENCES `kriteria` (`kriteria_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
