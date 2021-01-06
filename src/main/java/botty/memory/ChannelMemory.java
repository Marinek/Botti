package botty.memory;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import discord4j.common.util.Snowflake;

@KeySpace("channels")
public class ChannelMemory {

	@Id
	private Integer id;
	
	private Snowflake messageId;
	
	private Snowflake channelId;
	
	private Snowflake roleId;

	public Snowflake getMessageId() {
		return messageId;
	}

	public void setMessageId(Snowflake messageId) {
		this.messageId = messageId;
	}

	public Snowflake getChannelId() {
		return channelId;
	}

	public void setChannelId(Snowflake channelId) {
		this.channelId = channelId;
	}

	public Snowflake getRoleId() {
		return roleId;
	}

	public void setRoleId(Snowflake roleId) {
		this.roleId = roleId;
	}
}
