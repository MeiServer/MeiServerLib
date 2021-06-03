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

	public static <T> String serialize(final T obj) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (Serializable.class.isAssignableFrom(obj.getClass())) {
				final ObjectOutputStream oos = new ObjectOutputStream(dos);
				oos.writeObject(obj);
				oos.flush();
			} else if (DiscordSerializable.class.isAssignableFrom(obj.getClass())) {
				((DiscordSerializable) obj).serialize(dos);
			} else {
				throw new RuntimeException("The specified object is not serializable");
			}
			dos.flush();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return Base64.getEncoder().encodeToString(bos.toByteArray());
	}

	@SuppressWarnings("unchecked")
	public static <T> T unserialize(final String encodedData, final Class<T> cls) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(encodedData));
		final DataInputStream dis = new DataInputStream(bis);
		T obj = null;
		try {
			if (Serializable.class.isAssignableFrom(cls)) {
				final ObjectInputStream ois = new ObjectInputStream(dis);
				obj = (T) ois.readObject();
			} else if (DiscordSerializable.class.isAssignableFrom(cls)) {
				final Constructor<T> constructor = cls.getDeclaredConstructor();
				constructor.setAccessible(true);
				obj = constructor.newInstance();
				((DiscordSerializable) obj).unserialize(dis);
			} else {
				throw new RuntimeException("The specified object is not unserializable");
			}
		} catch (InstantiationException | IllegalAccessException | IOException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
}
