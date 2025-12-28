package storage.aws;

import storage.StorageService;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Objects;

public class S3StorageService implements StorageService {

	private final S3Client s3;
	private final String bucket;

	public S3StorageService(S3Client s3, String bucket) {
		this.s3 = Objects.requireNonNull(s3, "s3");
		this.bucket = Objects.requireNonNull(bucket, "bucket");
		if (bucket.isBlank()) throw new IllegalArgumentException("bucket no puede estar vacío");
	}

	@Override
	public void upload(String key, byte[] data) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(data, "data");

		PutObjectRequest req = PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build();

		s3.putObject(req, RequestBody.fromBytes(data));
	}

	@Override
	public byte[] download(String key) {
		Objects.requireNonNull(key, "key");

		GetObjectRequest req = GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build();

		ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(req);
		return bytes.asByteArray();
	}

	@Override
	public boolean exists(String key) {
		Objects.requireNonNull(key, "key");

		try {
			HeadObjectRequest req = HeadObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.build();
			s3.headObject(req);
			return true;
		} catch (NoSuchKeyException e) {
			return false;
		} catch (software.amazon.awssdk.services.s3.model.S3Exception e) {
			// En S3 real, un 404 puede venir como S3Exception
			if (e.statusCode() == 404) return false;
			throw e;
		}
	}
}
