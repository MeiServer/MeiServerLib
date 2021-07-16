package r3qu13m.mei.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public interface DiscordSerializable {
	public void serialize(DataOutputStream dos) throws IOException;

	public void unserialize(DataInputStream dis) throws IOException;

	public static <T> String serialize(final T obj) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(new DeflaterOutputStream(bos));
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
		final DataInputStream dis = new DataInputStream(new InflaterInputStream(bis));
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

	public static void serializeDataset(Map<String, DiscordSerializable> dataset, OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(dataset.keySet().size());

		for (String key : dataset.keySet()) {
			dos.writeUTF(key);
			DiscordSerializable ds = dataset.get(key);
			dos.writeUTF(ds.getClass().getName());
			dos.writeUTF(serialize(dataset.get(key)));
		}
	}

	public static Map<String, DiscordSerializable> unserializeDataset(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		Map<String, DiscordSerializable> ret = new HashMap<>();
		int length = dis.readInt();
		try {
			for (int i = 0; i < length; i++) {
				String key = dis.readUTF();
				Class<?> cls = Class.forName(dis.readUTF());
				ret.put(key, (DiscordSerializable) unserialize(dis.readUTF(), cls));
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}
}
