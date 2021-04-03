package botty.memory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import discord4j.common.util.Snowflake;

@Entity
public class BotChannel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	private Long messageId;
	
	@Column
	private Long channelId;
	
	@Column
	private Long roleId;

	@Column
	private Long ownerId;

	public Snowflake getMessageId() {
		return Snowflake.of(messageId);
	}

	public void setMessageId(Snowflake messageId) {
		this.messageId = messageId.asLong();
	}

	public Snowflake getChannelId() {
		return Snowflake.of(channelId);
	}

	public void setChannelId(Snowflake channelId) {
		this.channelId = channelId.asLong();
	}

	public Snowflake getRoleId() {
		return Snowflake.of(roleId);
	}

	public void setRoleId(Snowflake roleId) {
		this.roleId = roleId.asLong();
	}
	
	public Snowflake getOwnerId() {
		return Snowflake.of(ownerId);
	}

	public void setOwnerId(Snowflake ownerId) {
		this.ownerId = ownerId.asLong();
	}

	@Override
	public String toString() {
		return "BotChannel [id=" + id + ", messageId=" + messageId + ", channelId=" + channelId + ", roleId=" + roleId
				+ ", ownerId=" + ownerId + "]";
	}
	
}
