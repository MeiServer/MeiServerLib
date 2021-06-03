package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import r3qu13m.mei.lib.DiscordSerializable;

public class MeiPlayer extends DiscordSerializable {
	private UUID id;
	private String name;
	private String discordId;

	public MeiPlayer(UUID par1Id, String par2Name, String par3DiscordId) {
		this.id = par1Id;
		this.name = par2Name;
		this.discordId = par3DiscordId;
	}

	public UUID getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDiscordID() {
		return this.discordId;
	}

	@Override
	protected void serialize(DataOutputStream dos) throws IOException {
		dos.writeUTF(id.toString());
		dos.writeUTF(name);
		dos.writeUTF(discordId);
	}

	@Override
	protected void unserialize(DataInputStream dis) throws IOException {
		this.id = UUID.fromString(dis.readUTF());
		this.name = dis.readUTF();
		this.discordId = dis.readUTF();
	}
}
