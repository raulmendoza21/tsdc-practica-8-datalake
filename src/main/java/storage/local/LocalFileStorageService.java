package storage.local;

import storage.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class LocalFileStorageService implements StorageService {

	private final Path baseDir;

	public LocalFileStorageService(Path baseDir) {
		this.baseDir = Objects.requireNonNull(baseDir, "baseDir");
	}

	private Path resolve(String key) {
		// Evita escapes raros: guardamos key como path relativo seguro
		String safe = key.replace("..", "").replace("\\", "/");
		return baseDir.resolve(safe).normalize();
	}

	@Override
	public void upload(String key, byte[] data) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(data, "data");
		try {
			Files.createDirectories(baseDir);
			Path target = resolve(key);
			if (target.getParent() != null) Files.createDirectories(target.getParent());
			Files.write(target, data);
		} catch (IOException e) {
			throw new RuntimeException("Error escribiendo en LocalFileStorageService", e);
		}
	}

	@Override
	public byte[] download(String key) {
		Objects.requireNonNull(key, "key");
		try {
			Path target = resolve(key);
			return Files.readAllBytes(target);
		} catch (IOException e) {
			throw new RuntimeException("Error leyendo en LocalFileStorageService", e);
		}
	}

	@Override
	public boolean exists(String key) {
		Objects.requireNonNull(key, "key");
		return Files.exists(resolve(key));
	}
}
