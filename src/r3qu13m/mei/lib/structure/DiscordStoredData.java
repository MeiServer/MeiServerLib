package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.MeiServerLib;

public class DiscordStoredData implements DiscordSerializable {

	private ModPackSequence mpSeq;
	private Set<ModPack> modPacks;
	private Set<Mod> mods;
	private Set<DistributeFile> files;
	private Set<MeiPlayer> players;

	public DiscordStoredData() {
		this.init();
	}

	private void init() {
		this.mpSeq = new ModPackSequence();
		this.modPacks = new HashSet<>();
		this.mods = new HashSet<>();
		this.files = new HashSet<>();
		this.players = new HashSet<>();
	}

	public void setModPackSequence(final ModPackSequence obj) {
		this.mpSeq = obj;
	}

	public void setModPacks(final Set<ModPack> set) {
		this.modPacks = set;
	}

	public void setMods(final Set<Mod> set) {
		this.mods = set;
	}

	public void setFiles(final Set<DistributeFile> set) {
		this.files = set;
	}

	public void setPlayers(final Set<MeiPlayer> set) {
		this.players = set;
	}

	public ModPackSequence getModPackSequence() {
		return this.mpSeq;
	}

	public Set<ModPack> getModPacks() {
		return this.modPacks;
	}

	public Set<Mod> getMods() {
		return this.mods;
	}

	public Set<DistributeFile> getFiles() {
		return this.files;
	}

	public Set<MeiPlayer> getPlayers() {
		return this.players;
	}

	private <T> Map<UUID, T> genCorrespondingMap(final Set<T> set, final Function<T, UUID> uuid) {
		final Map<UUID, T> ret = new HashMap<>();
		for (final T value : set) {
			ret.put(uuid.apply(value), value);
		}
		return ret;
	}

	private <T extends DiscordSerializable> Set<T> readSet(final DataInputStream dis, final Class<T> clazz)
			throws IOException {
		final int size = dis.readInt();
		final Set<T> ret = new HashSet<>();
		for (int i = 0; i < size; i++) {
			ret.add(DiscordSerializable.unserialize(dis, clazz));
		}
		return ret;
	}

	private <T extends DiscordSerializable> void writeSet(final DataOutputStream dos, final Set<T> set)
			throws IOException {
		dos.writeInt(set.size());
		for (final T value : set) {
			value.serialize(dos);
		}
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		this.writeSet(dos, this.getPlayers());
		this.writeSet(dos, this.getFiles());
		this.writeSet(dos, this.getMods());
		this.writeSet(dos, this.getModPacks());
		this.mpSeq.serialize(dos);
	}

	@Override
	public void unserialize(final DataInputStream dis) throws IOException {
		this.init();
		this.setPlayers(this.readSet(dis, MeiPlayer.class));
		this.setFiles(this.readSet(dis, DistributeFile.class));
		MeiServerLib.instance()
				.setDistributeFileMap(this.genCorrespondingMap(this.getFiles(), DistributeFile::getID)::get);
		this.setMods(this.readSet(dis, Mod.class));
		MeiServerLib.instance().setModMap(this.genCorrespondingMap(this.getMods(), Mod::getID)::get);
		this.setModPacks(this.readSet(dis, ModPack.class));
		MeiServerLib.instance().setModPackMap(this.genCorrespondingMap(this.getModPacks(), ModPack::getID)::get);
		this.setModPackSequence(DiscordSerializable.unserialize(dis, ModPackSequence.class));
	}

}
