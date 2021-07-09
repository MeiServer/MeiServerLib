package r3qu13m.mei.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import r3qu13m.mei.lib.structure.DistributeFile;
import r3qu13m.mei.lib.structure.ModPack;

public class MPVec {
	private final Map<UUID, OperationType> packDiff;

	public MPVec(final ModPack pack) {
		if (pack == null) {
			throw new RuntimeException("`pack` must not null");
		}
		final List<DistributeFile> files = pack.getFiles();
		this.packDiff = new HashMap<>();

		files.forEach(x -> {
			this.packDiff.put(x.getID(), OperationType.ADD);
		});
	}

	public MPVec(final ModPack from, final ModPack to) {
		this(new MPVec(from).composite(new MPVec(to)).packDiff);
	}

	private MPVec(final Map<UUID, OperationType> map) {
		this.packDiff = map;
	}

	public OperationType getType(final UUID file) {
		if (this.packDiff.containsKey(file)) {
			return this.packDiff.get(file);
		}
		return OperationType.IDENTITY;
	}

	public MPVec getInverse() {
		final Map<UUID, OperationType> packDiff = new HashMap<>();
		for (final UUID k : this.packDiff.keySet()) {
			packDiff.put(k, packDiff.get(k).inverse());
		}
		return new MPVec(packDiff);
	}

	public MPVec composite(final MPVec other) {
		final Map<UUID, OperationType> packDiff = new HashMap<>();
		final Set<UUID> files = this.packDiff.keySet();
		files.addAll(other.packDiff.keySet());
		files.forEach(x -> {
			packDiff.put(x, this.getType(x).composite(other.getType(x)));
		});
		return new MPVec(packDiff);
	}

	public Map<UUID, OperationType> getDifference() {
		Map<UUID, OperationType> ret = new HashMap<>();
		for (UUID key : packDiff.keySet()) {
			OperationType val = packDiff.get(key);
			if (val != OperationType.IDENTITY) {
				ret.put(key, val);
			}
		}
		return Collections.unmodifiableMap(ret);
	}
}
