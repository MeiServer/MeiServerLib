package r3qu13m.mei.lib;

import java.util.UUID;
import java.util.function.Function;

import r3qu13m.mei.lib.structure.DistributeFile;
import r3qu13m.mei.lib.structure.Mod;
import r3qu13m.mei.lib.structure.ModPack;

public final class MeiServerLib {
	private static MeiServerLib _instance;

	public static MeiServerLib instance() {
		if (MeiServerLib._instance == null) {
			MeiServerLib._instance = new MeiServerLib();
		}
		return MeiServerLib._instance;
	}

	private Function<UUID, DistributeFile> dfMap;
	private Function<UUID, Mod> modMap;
	private Function<UUID, ModPack> mpMap;

	private MeiServerLib() {

	}

	synchronized public DistributeFile getDistributeFile(final UUID id) {
		if (this.dfMap == null) {
			throw new RuntimeException("dfMap must not null");
		}

		return this.dfMap.apply(id);
	}

	synchronized public void setDistributeFileMap(final Function<UUID, DistributeFile> func) {
		this.dfMap = func;
	}

	synchronized public Mod getMod(final UUID id) {
		if (this.modMap == null) {
			throw new RuntimeException("modMap must not null");
		}

		return this.modMap.apply(id);
	}

	synchronized public void setModMap(final Function<UUID, Mod> func) {
		this.modMap = func;
	}

	synchronized public ModPack getModPack(final UUID id) {
		if (this.mpMap == null) {
			throw new RuntimeException("mpMap must not null");
		}

		return this.mpMap.apply(id);
	}

	synchronized public void setModPackMap(final Function<UUID, ModPack> func) {
		this.mpMap = func;
	}
}
