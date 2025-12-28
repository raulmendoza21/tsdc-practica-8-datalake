import datalake.Datalake;
import storage.StorageService;
import storage.aws.S3StorageService;
import storage.local.LocalFileStorageService;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {

		// Cambia esto a "s3" o "local" para demostrar portabilidad
		String provider = System.getenv().getOrDefault("STORAGE_PROVIDER", "local");

		StorageService storage;

		if (provider.equalsIgnoreCase("s3")) {
			String bucket = System.getenv("BUCKET_NAME");
			if (bucket == null || bucket.isBlank()) {
				throw new IllegalStateException("Falta BUCKET_NAME en variables de entorno");
			}

			// Región por env, por defecto us-west-2 (ajusta a tu práctica si procede)
			String regionStr = System.getenv().getOrDefault("AWS_REGION", "us-west-2");
			S3Client s3 = S3Client.builder()
					.region(Region.of(regionStr))
					.build();

			storage = new S3StorageService(s3, bucket);
		} else {
			// Local: guarda en ./data/
			storage = new LocalFileStorageService(Paths.get("data"));
		}

		Datalake datalake = new Datalake(storage);

		String key = "practica8/hola.txt";
		datalake.storeText(key, "Hola desde Datalake desacoplado!");
		System.out.println("EXISTS? " + datalake.exists(key));
		System.out.println("READ: " + datalake.readText(key));
	}
}
