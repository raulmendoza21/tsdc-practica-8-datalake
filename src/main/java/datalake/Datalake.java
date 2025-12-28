package datalake;

import storage.StorageService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Datalake {

	private final StorageService storage;

	public Datalake(StorageService storage) {
		this.storage = Objects.requireNonNull(storage, "storage");
	}

	// Ejemplo: subir texto
	public void storeText(String key, String text) {
		if (key == null || key.isBlank()) throw new IllegalArgumentException("key no puede estar vacía");
		if (text == null) throw new IllegalArgumentException("text no puede ser null");

		storage.upload(key, text.getBytes(StandardCharsets.UTF_8));
	}

	// Ejemplo: leer texto
	public String readText(String key) {
		if (key == null || key.isBlank()) throw new IllegalArgumentException("key no puede estar vacía");
		byte[] data = storage.download(key);
		return new String(data, StandardCharsets.UTF_8);
	}

	public boolean exists(String key) {
		if (key == null || key.isBlank()) throw new IllegalArgumentException("key no puede estar vacía");
		return storage.exists(key);
	}
}
