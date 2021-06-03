package r3qu13m.mei.lib;

import java.util.UUID;
import java.util.function.Function;

import r3qu13m.mei.lib.structure.DistributeFile;

public final class MeiServerLib {
	private static MeiServerLib _instance;

	public static MeiServerLib instance() {
		if (MeiServerLib._instance == null) {
			MeiServerLib._instance = new MeiServerLib();
		}
		return MeiServerLib._instance;
	}

	private Function<UUID, DistributeFile> dfMap;

	private MeiServerLib() {

	}

	synchronized public DistributeFile getDistributeFile(final UUID id) {
		if (this.dfMap == null) {
			throw new RuntimeException("dfMap must not null");
		}

		return this.dfMap.apply(id);
	}

	synchronized public void setMap(final Function<UUID, DistributeFile> func) {
		this.dfMap = func;
	}
}
