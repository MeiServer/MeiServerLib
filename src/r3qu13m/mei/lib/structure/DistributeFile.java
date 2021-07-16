package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import r3qu13m.mei.lib.DataType;
import r3qu13m.mei.lib.DiscordSerializable;

public class DistributeFile implements DiscordSerializable {
	private UUID id;
	private DataType type;
	private String name;
	private String hash;
	private String url;

	public DistributeFile(final DataType par1Type, final String par2Name, final String par3Hash, final String par4Url) {
		this.id = UUID.randomUUID();
		this.type = par1Type;
		this.name = par2Name;
		this.hash = par3Hash;
		this.url = par4Url;
	}

	public DistributeFile(final DataType par1Type, final String par2Name, final String par3Hash, final URL par4Url) {
		this(par1Type, par2Name, par3Hash, par4Url.toExternalForm());
	}

	protected DistributeFile() {
		this(null, null, null, (String) null);
	}

	public UUID getID() {
		return this.id;
	}

	public DataType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public String getHash() {
		return this.hash;
	}

	public URL getURL() {
		try {
			return new URL(this.url);
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof DistributeFile)) {
			return false;
		}
		final DistributeFile df = (DistributeFile) other;
		return df.getID().equals(this.id) && df.getType().equals(this.type) && df.getName().equals(this.name)
				&& df.getHash().equals(this.hash) && df.url.equals(this.url);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		dos.writeUTF(this.id.toString());
		dos.writeInt(this.type.ordinal());
		dos.writeUTF(this.name);
		dos.writeUTF(this.hash);
		dos.writeUTF(this.url);
	}

	@Override
	public void unserialize(final DataInputStream dis) throws IOException {
		this.id = UUID.fromString(dis.readUTF());
		this.type = DataType.values()[dis.readInt()];
		this.name = dis.readUTF();
		this.hash = dis.readUTF();
		this.url = dis.readUTF();
	}
}
