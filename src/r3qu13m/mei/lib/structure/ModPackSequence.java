package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.MPVec;
import r3qu13m.mei.lib.MeiServerLib;

public class ModPackSequence implements DiscordSerializable {
	ModPackSequence() {
		this.seq = new LinkedList<>();
	}

	private List<ModPack> seq;

	public void add(final ModPack pack) {
		this.seq.add(pack);
	}

	public Optional<ModPack> getLatestPack() {
		if (this.seq.isEmpty()) {
			return Optional.empty();
		}
		return Optional.ofNullable(this.seq.get(this.seq.size() - 1));
	}

	public MPVec getDifference(final ModPack fromPack, final ModPack toPack) {
		if (!this.seq.contains(fromPack) || !this.seq.contains(toPack)) {
			throw new RuntimeException("ModPack Sequence doesn't contains the specified ModPack");
		}
		final int s = this.seq.indexOf(fromPack);
		final int e = this.seq.indexOf(toPack);
		MPVec vec = new MPVec(this.seq.get(s));
		for (int i = s; i < e + 1; i++) {
			vec = vec.composite(new MPVec(this.seq.get(i)));
		}
		return vec;
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		dos.writeInt(this.seq.size());
		for (final ModPack pack : this.seq) {
			dos.writeUTF(pack.getID().toString());
		}
	}

	@Override
	public void unserialize(final DataInputStream dis, final int version) throws IOException {
		final int size = dis.readInt();
		this.seq = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			this.seq.add(MeiServerLib.instance().getModPack(UUID.fromString(dis.readUTF())));
		}
	}
}
