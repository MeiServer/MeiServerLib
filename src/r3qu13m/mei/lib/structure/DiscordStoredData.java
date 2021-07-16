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
		this.mpSeq = null;
		this.modPacks = new HashSet<>();
		this.mods = new HashSet<>();
		this.files = new HashSet<>();
		this.players = new HashSet<>();
	}

	public void setModPackSequence(ModPackSequence obj) {
		this.mpSeq = obj;
	}

	public void setModPacks(Set<ModPack> set) {
		this.modPacks = set;
	}

	public void setMods(Set<Mod> set) {
		this.mods = set;
	}

	public void setFiles(Set<DistributeFile> set) {
		this.files = set;
	}

	public void setPlayers(Set<MeiPlayer> set) {
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

	private <T> Map<UUID, T> genCorrespondingMap(Set<T> set, Function<T, UUID> uuid) {
		Map<UUID, T> ret = new HashMap<>();
		for (T value : set) {
			ret.put(uuid.apply(value), value);
		}
		return ret;
	}

	private <T extends DiscordSerializable> Set<T> readSet(DataInputStream dis, Class<T> clazz) throws IOException {
		int size = dis.readInt();
		Set<T> ret = new HashSet<>();
		for (int i = 0; i < size; i++) {
			ret.add(DiscordSerializable.unserialize(dis, clazz));
		}
		return ret;
	}

	private <T extends DiscordSerializable> void writeSet(DataOutputStream dos, Set<T> set) throws IOException {
		dos.writeInt(set.size());
		for (T value : set) {
			value.serialize(dos);
		}
	}

	@Override
	public void serialize(DataOutputStream dos) throws IOException {
		this.writeSet(dos, this.getPlayers());
		this.writeSet(dos, this.getFiles());
		this.writeSet(dos, this.getMods());
		this.writeSet(dos, this.getModPacks());
		dos.writeBoolean(this.mpSeq != null);
		if (this.mpSeq != null) {
			this.mpSeq.serialize(dos);
		}
	}

	@Override
	public void unserialize(DataInputStream dis) throws IOException {
		this.init();
		this.setPlayers(readSet(dis, MeiPlayer.class));
		this.setFiles(readSet(dis, DistributeFile.class));
		MeiServerLib.instance()
				.setDistributeFileMap(this.genCorrespondingMap(this.getFiles(), DistributeFile::getID)::get);
		this.setMods(readSet(dis, Mod.class));
		MeiServerLib.instance().setModMap(this.genCorrespondingMap(this.getMods(), Mod::getID)::get);
		this.setModPacks(readSet(dis, ModPack.class));
		MeiServerLib.instance().setModPackMap(this.genCorrespondingMap(this.getModPacks(), ModPack::getID)::get);
		if (dis.readBoolean()) {
			this.setModPackSequence(DiscordSerializable.unserialize(dis, ModPackSequence.class));
		}
	}

}
