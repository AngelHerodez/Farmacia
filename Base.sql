-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: farmacia
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `presentacion`
--

DROP TABLE IF EXISTS `presentacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `presentacion` (
  `idpresentacion` int NOT NULL AUTO_INCREMENT,
  `presentacion` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`idpresentacion`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presentacion`
--

LOCK TABLES `presentacion` WRITE;
/*!40000 ALTER TABLE `presentacion` DISABLE KEYS */;
INSERT INTO `presentacion` VALUES (1,'Comprimidos'),(2,'Cápsulas'),(3,'Jarabe'),(4,'Suspención'),(5,'Emulsión'),(6,'Inyectables');
/*!40000 ALTER TABLE `presentacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombre` text NOT NULL,
  `fechaCaducidad` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `precio` double NOT NULL,
  `tipo` int NOT NULL,
  `tamaño` int NOT NULL,
  `presentacion` int DEFAULT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `fk_producto_presentacion_idx` (`presentacion`),
  KEY `fk_producto_tipo_idx` (`tipo`),
  CONSTRAINT `fk_producto_presentacion` FOREIGN KEY (`presentacion`) REFERENCES `presentacion` (`idpresentacion`),
  CONSTRAINT `fk_producto_tipo` FOREIGN KEY (`tipo`) REFERENCES `tipo` (`idtipo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (9,'Ibuprofeno','2025-06-14',90,1,600,1,20),(10,'Mejoralito','2023-07-02',299,1,200,1,50);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promocion`
--

DROP TABLE IF EXISTS `promocion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promocion` (
  `idPromocion` int NOT NULL AUTO_INCREMENT,
  `descripcion` text NOT NULL,
  `producto` int NOT NULL,
  `sucursal` int NOT NULL,
  PRIMARY KEY (`idPromocion`),
  KEY `sucursal` (`sucursal`),
  KEY `fk_promociones_producto_idx` (`producto`),
  CONSTRAINT `fk_promociones_producto` FOREIGN KEY (`producto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `promociones_ibfk_2` FOREIGN KEY (`sucursal`) REFERENCES `sucursal` (`idSucursal`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promocion`
--

LOCK TABLES `promocion` WRITE;
/*!40000 ALTER TABLE `promocion` DISABLE KEYS */;
/*!40000 ALTER TABLE `promocion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puesto`
--

DROP TABLE IF EXISTS `puesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puesto` (
  `idPuesto` int NOT NULL AUTO_INCREMENT,
  `puesto` varchar(45) NOT NULL,
  PRIMARY KEY (`idPuesto`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puesto`
--

LOCK TABLES `puesto` WRITE;
/*!40000 ALTER TABLE `puesto` DISABLE KEYS */;
INSERT INTO `puesto` VALUES (1,'Administrador'),(2,'Encargado');
/*!40000 ALTER TABLE `puesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `idStock` int NOT NULL AUTO_INCREMENT,
  `producto` int NOT NULL,
  `sucursal` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`idStock`),
  KEY `fda_producto` (`producto`),
  KEY `sucursal` (`sucursal`),
  CONSTRAINT `stock_ibfk_1` FOREIGN KEY (`producto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `stock_ibfk_2` FOREIGN KEY (`sucursal`) REFERENCES `sucursal` (`idSucursal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sucursal`
--

DROP TABLE IF EXISTS `sucursal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sucursal` (
  `idSucursal` int NOT NULL AUTO_INCREMENT,
  `nombre` text NOT NULL,
  `ciudad` text NOT NULL,
  `calle` text NOT NULL,
  `numeroDeLocal` int NOT NULL,
  PRIMARY KEY (`idSucursal`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sucursal`
--

LOCK TABLES `sucursal` WRITE;
/*!40000 ALTER TABLE `sucursal` DISABLE KEYS */;
INSERT INTO `sucursal` VALUES (1,'xalapa','xalapa','av. xalapa',34),(2,'perote ','perote','av. principal',56),(3,'veracruz','veracruz','av. city',89);
/*!40000 ALTER TABLE `sucursal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo`
--

DROP TABLE IF EXISTS `tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo` (
  `idtipo` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo`
--

LOCK TABLES `tipo` WRITE;
/*!40000 ALTER TABLE `tipo` DISABLE KEYS */;
INSERT INTO `tipo` VALUES (1,'Medicamento'),(2,'Higiene '),(3,'Miscelaneo');
/*!40000 ALTER TABLE `tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trabajador`
--

DROP TABLE IF EXISTS `trabajador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trabajador` (
  `idTrabajador` int NOT NULL AUTO_INCREMENT,
  `nombre` text NOT NULL,
  `apellidoPaterno` text NOT NULL,
  `apellidoMaterno` text NOT NULL,
  `puesto` int NOT NULL,
  `sucursal` int NOT NULL,
  `usuario` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `contraseña` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`idTrabajador`),
  UNIQUE KEY `usuario_unico` (`usuario`),
  KEY `sucursal` (`sucursal`),
  KEY `fk_trabajador_puesto_idx` (`puesto`),
  CONSTRAINT `fk_trabajador_puesto` FOREIGN KEY (`puesto`) REFERENCES `puesto` (`idPuesto`),
  CONSTRAINT `trabajador_ibfk_1` FOREIGN KEY (`sucursal`) REFERENCES `sucursal` (`idSucursal`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trabajador`
--

LOCK TABLES `trabajador` WRITE;
/*!40000 ALTER TABLE `trabajador` DISABLE KEYS */;
INSERT INTO `trabajador` VALUES (1,'sgsgd','gsdsdg','gsdsdg',1,1,'1','1'),(7,'emmanuel','lopez','cruz',2,1,'emma','emma123'),(13,'fddfgfd','fddfgfd','dfgdfgdfg',2,1,'','sdgsdg'),(17,'Martin','Martin','Carmona',1,1,'penes','12345'),(18,'Martin','Martin','Carmona3324224242',2,1,'Martino','safsd'),(21,'juan','lopez','mateos',1,1,'juanito','sdfsfd'),(22,'Martin ','Cruz','Carmona',1,1,'Lecape','dsfffsd');
/*!40000 ALTER TABLE `trabajador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `idVenta` int NOT NULL AUTO_INCREMENT,
  `sucursal` int NOT NULL,
  `total` double NOT NULL,
  PRIMARY KEY (`idVenta`),
  KEY `sucursal` (`sucursal`),
  CONSTRAINT `ventas_ibfk_1` FOREIGN KEY (`sucursal`) REFERENCES `sucursal` (`idSucursal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventaproducto`
--

DROP TABLE IF EXISTS `ventaproducto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventaproducto` (
  `idventaProducto` int NOT NULL AUTO_INCREMENT,
  `idVenta` int NOT NULL,
  `idProducto` int NOT NULL,
  PRIMARY KEY (`idventaProducto`),
  KEY `fk_ventaProducto_venta_idx` (`idVenta`),
  KEY `fk_ventaProducto_producto_idx` (`idProducto`),
  CONSTRAINT `fk_ventaProducto_producto` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `fk_ventaProducto_venta` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventaproducto`
--

LOCK TABLES `ventaproducto` WRITE;
/*!40000 ALTER TABLE `ventaproducto` DISABLE KEYS */;
/*!40000 ALTER TABLE `ventaproducto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-20 15:18:09
