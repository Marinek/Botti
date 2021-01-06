package botty.skills.channelmanagement;

import java.util.List;

import org.springframework.stereotype.Component;

import botty.BotSkill;
import botty.skills.GroupCheckSkill;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel.Type;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class ChannelManagement extends BotSkill {
	private static final Logger log = Loggers.getLogger(GroupCheckSkill.class);

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {
		log.info("Execute...");
		getGroupChannel(client, event);
	}

	public TextChannel getGroupChannel(GatewayDiscordClient client, MessageCreateEvent event) {
		TextChannel groupChannel = null;
		
		Snowflake guildId = event.getGuildId().get();
		
		List<GuildChannel> block = client.getGuildChannels(guildId).filter(c -> c.getType() == Type.GUILD_TEXT).filter(c -> c.getName().equals("lerngruppen")).collectList().block();
		
		if(block.size() == 0) {
			groupChannel = event.getGuild().block().createTextChannel(channel -> {
				channel.setName("lerngruppen");
				channel.setTopic("In diesem Channel können Gruppen verwaltet werden.");
			}).block();
		} else {
			groupChannel = (TextChannel) block.get(0);
		}
		
		return groupChannel;
	}

	@Override
	public String getTrigger() {
		return "!startChannelManagement";
	}

}
