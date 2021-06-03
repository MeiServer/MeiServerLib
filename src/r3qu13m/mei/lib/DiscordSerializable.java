package r3qu13m.mei.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

public interface DiscordSerializable {
	public void serialize(DataOutputStream dos) throws IOException;

	public void unserialize(DataInputStream dis) throws IOException;

	public static <T extends DiscordSerializable> String serialize(T obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (Serializable.class.isAssignableFrom(obj.getClass())) {
				ObjectOutputStream oos = new ObjectOutputStream(dos);
				oos.writeObject(obj);
				oos.flush();
			} else {
				obj.serialize(dos);
			}
			dos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Base64.getEncoder().encodeToString(bos.toByteArray());
	}

	public static <T extends Serializable> String serialize(T obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(dos);
			oos.writeObject(obj);
			oos.flush();
			dos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Base64.getEncoder().encodeToString(bos.toByteArray());
	}

	public static <T extends DiscordSerializable> T unserialize(String encodedData, Class<T> cls) {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encodedData));
		DataInputStream dis = new DataInputStream(bis);
		T obj = null;
		try {
			Constructor<T> constructor = cls.getDeclaredConstructor();
			constructor.setAccessible(true);
			obj = constructor.newInstance();
			obj.unserialize(dis);
		} catch (InstantiationException | IllegalAccessException | IOException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T unserialize(String encodedData) {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encodedData));
		DataInputStream dis = new DataInputStream(bis);
		T obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(dis);
			obj = (T) ois.readObject();

		} catch (IOException | SecurityException | IllegalArgumentException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
}
