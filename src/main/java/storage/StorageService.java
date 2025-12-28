package storage;

public interface StorageService {
	void upload(String key, byte[] data);
	byte[] download(String key);
	boolean exists(String key);
}
