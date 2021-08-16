package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.MeiServerLib;

public class Mod implements DiscordSerializable {

	private UUID id;
	private String mod;
	private String version;
	private List<DistributeFile> files;
	private int priority;

	public Mod(final String par1Mod, final String par2Version, final List<DistributeFile> par3Files,
			final int par4Priority) {
		this.id = UUID.randomUUID();
		this.mod = par1Mod;
		this.version = par2Version;
		this.files = new ArrayList<>();
		this.files.addAll(par3Files);
		this.priority = par4Priority;
	}

	protected Mod() {
		this(null, null, new ArrayList<>(), 0);
	}

	public UUID getID() {
		return this.id;
	}

	public String getVersion() {
		return this.version;
	}

	public String getModName() {
		return this.mod;
	}

	public int getPriority() {
		return this.priority;
	}

	public List<DistributeFile> getFiles() {
		return Collections.unmodifiableList(this.files);
	}

	public boolean hasFile(final DistributeFile file) {
		return this.files.contains(file);
	}

	public void addFile(final DistributeFile file) {
		if (!this.hasFile(file)) {
			this.files.add(file);
		}
	}

	public void removeFile(final DistributeFile file) {
		this.files.remove(file);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Mod)) {
			return false;
		}
		final Mod mod = (Mod) other;
		return mod.getID().equals(this.id) && mod.getModName().equals(this.mod) && mod.getVersion().equals(this.version)
				&& mod.getFiles().equals(this.files);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		dos.writeUTF(this.id.toString());
		dos.writeUTF(this.mod);
		dos.writeUTF(this.version);
		dos.writeInt(this.files.size());
		for (final DistributeFile file : this.files) {
			dos.writeUTF(file.getID().toString());
		}
		dos.writeInt(priority);
	}

	@Override
	public void unserialize(final DataInputStream dis, final int version) throws IOException {
		this.id = UUID.fromString(dis.readUTF());
		this.mod = dis.readUTF();
		this.version = dis.readUTF();
		final int n = dis.readInt();
		this.files = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			this.files.add(MeiServerLib.instance().getDistributeFile(UUID.fromString(dis.readUTF())));
		}
		if (version >= 2) {
			this.priority = dis.readInt();
		} else {
			this.priority = 0;
		}
	}
}
