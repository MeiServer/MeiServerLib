package r3qu13m.mei.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;

abstract public class DiscordSerializable {
	abstract protected void serialize(DataOutputStream dos) throws IOException;

	abstract protected void unserialize(DataInputStream dis) throws IOException;

	public static <T extends DiscordSerializable> String serialize(T obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			obj.serialize(new DataOutputStream(bos));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Base64.getEncoder().encodeToString(bos.toByteArray());
	}

	public static <T extends DiscordSerializable> T unserialize(String encodedData, Class<T> cls) {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encodedData));
		T obj = null;
		try {
			obj = cls.newInstance();
			obj.unserialize(new DataInputStream(bis));
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
}
