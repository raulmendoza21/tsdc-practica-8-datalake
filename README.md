# TSCD - Practica 8: Datalake desacoplado y portable

## Descripcion
Este repositorio contiene la implementacion de la Practica 8 de la asignatura
"Tecnologias de Servicios para Ciencia de Datos" del Grado en Ciencia e Ingenieria
de Datos (Universidad de Las Palmas de Gran Canaria).

La practica se centra en la refactorizacion de un servicio tipo Datalake escrito
en Java, cuyo diseno inicial presentaba un fuerte acoplamiento a un proveedor
cloud concreto (AWS S3), generando un problema clasico de vendor lock-in.

El objetivo principal es redisenar el sistema para que la logica del dominio sea
independiente del proveedor de almacenamiento, permitiendo cambiar de backend
sin modificar el codigo del Datalake.

---

## Objetivo de la practica
Eliminar el vendor lock-in del servicio Datalake mediante un redisenio orientado
a objetos, aplicando principios de arquitectura y buenas practicas de ingenieria
del software.

El sistema debe poder operar con distintos proveedores de almacenamiento
(AWS S3, almacenamiento local, u otros futuros) sin cambios en la logica de negocio.

---

## Principios de diseno aplicados
Durante la refactorizacion se han aplicado los siguientes principios:

- Dependency Inversion Principle (DIP)
- Open/Closed Principle (OCP)
- Single Responsibility Principle (SRP)
- Inyeccion de dependencias
- Separacion de responsabilidades

---

## Arquitectura y estructura
La arquitectura se basa en una abstraccion comun para el almacenamiento:

- StorageService (interfaz)
- Datalake (logica de dominio)
- LocalFileStorageService
- S3StorageService

La seleccion del proveedor se realiza mediante configuracion, sin cambios en el
codigo del dominio.

---

## Seleccion del proveedor
El proveedor de almacenamiento se decide en tiempo de ejecucion mediante la
variable de entorno STORAGE_PROVIDER.

Valores soportados:
- local
- s3

---

## Ejecucion del proyecto (proveedor local)

Requisitos:
- Java 21
- Maven

```powershell
$env:STORAGE_PROVIDER="local"
mvn --% -q clean compile exec:java -Dexec.mainClass=Main
```

---

## Ejecucion con AWS S3 (replicable)

```powershell
$env:STORAGE_PROVIDER="s3"
$env:AWS_REGION="us-west-2"
$env:BUCKET_NAME="nombre-del-bucket"
mvn --% -q clean compile exec:java -Dexec.mainClass=Main
```

---

## Entorno de desarrollo
- Java 21
- Maven
- Windows
- Git y GitHub
- AWS SDK for Java v2

---

## Conclusiones
La refactorizacion elimina el vendor lock-in presente en la version original del
sistema, mejorando la mantenibilidad, testabilidad y extensibilidad del proyecto.
