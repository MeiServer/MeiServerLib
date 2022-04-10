package r3qu13m.mei.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
		Set<UUID> fromSet = this.packDiff.keySet();
		Set<UUID> toSet = other.packDiff.keySet();

		Set<UUID> intersectionSet = new HashSet<>();
		intersectionSet.addAll(fromSet);
		intersectionSet.retainAll(toSet);
		intersectionSet.forEach(uuid -> packDiff.put(uuid, OperationType.IDENTITY));

		Set<UUID> addSet = new HashSet<>();
		addSet.addAll(toSet);
		addSet.removeAll(fromSet);
		addSet.forEach(uuid -> packDiff.put(uuid, OperationType.ADD));

		Set<UUID> delSet = new HashSet<>();
		delSet.addAll(fromSet);
		delSet.removeAll(toSet);
		delSet.forEach(uuid -> packDiff.put(uuid, OperationType.DELETE));

		return new MPVec(packDiff);
	}

	public Map<UUID, OperationType> getDifference() {
		final Map<UUID, OperationType> ret = new HashMap<>();
		for (final UUID key : this.packDiff.keySet()) {
			final OperationType val = this.packDiff.get(key);
			ret.put(key, val);
		}
		return Collections.unmodifiableMap(ret);
	}
}
