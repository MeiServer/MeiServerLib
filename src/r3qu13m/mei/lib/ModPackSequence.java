package r3qu13m.mei.lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import r3qu13m.mei.lib.structure.ModPack;

public class ModPackSequence implements DiscordSerializable {
	private ModPackSequence() {
		this.seq = new LinkedList<>();
	}

	private static ModPackSequence _instance;

	public static ModPackSequence instance() {
		if (ModPackSequence._instance == null) {
			ModPackSequence._instance = new ModPackSequence();
		}
		return ModPackSequence._instance;
	}

	private Deque<ModPack> seq;
	private Deque<MPVec> vecSeq;

	public void add(final ModPack pack) {
		this.seq.add(pack);
	}

	@Override
	public void serialize(DataOutputStream dos) throws IOException {

	}

	@Override
	public void unserialize(DataInputStream dis) throws IOException {

	}
}
