package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.MeiServerLib;

public class ModPack implements DiscordSerializable {
	private UUID id;
	private List<Mod> mods;
	private int version;

	public ModPack(final List<Mod> par1Mods, final int par2Version) {
		this.id = UUID.randomUUID();
		this.mods = new ArrayList<>();
		this.version = par2Version;

		this.mods.addAll(par1Mods);
	}

	public UUID getID() {
		return this.id;
	}

	public List<Mod> getMods() {
		return Collections.unmodifiableList(this.mods);
	}

	public List<DistributeFile> getFiles() {
		return this.getMods().stream().map(Mod::getFiles).flatMap(Collection::stream).collect(Collectors.toList());
	}

	public int getVersion() {
		return this.version;
	}

	public ModPack with(final Mod mod) {
		if (this.has(mod)) {
			return this;
		}

		final List<Mod> mods = new ArrayList<>();
		mods.addAll(this.mods);
		mods.add(mod);
		return new ModPack(mods, this.version + 1);
	}

	public boolean has(final Mod mod) {
		return this.mods.contains(mod);
	}

	public ModPack without(final Mod mod) {
		if (!this.has(mod)) {
			return this;
		}

		final List<Mod> mods = new ArrayList<>();
		mods.addAll(this.mods);
		mods.remove(mod);
		return new ModPack(mods, this.version + 1);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof ModPack)) {
			return false;
		}
		final ModPack modpack = (ModPack) other;
		return modpack.getID().equals(this.id) && modpack.getMods().equals(this.mods)
				&& modpack.getVersion() == this.version;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		dos.writeUTF(this.id.toString());
		dos.writeInt(this.mods.size());
		for (final Mod mod : this.mods) {
			dos.writeUTF(mod.getID().toString());
		}
		dos.writeInt(this.version);
	}

	@Override
	public void unserialize(final DataInputStream dis) throws IOException {
		this.id = UUID.fromString(dis.readUTF());
		final int n = dis.readInt();
		this.mods = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			this.mods.add(MeiServerLib.instance().getMod(UUID.fromString(dis.readUTF())));
		}
		this.version = dis.readInt();
	}

}
