package botty.skills;

import org.springframework.stereotype.Component;

import botty.BotSkill;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class HiSkill extends BotSkill {

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {

		final Message message = event.getMessage();
		final MessageChannel channel = message.getChannel().block();
		channel.createMessage("Hello " + message.getAuthor().get().getUsername()).block();
	}

	@Override
	public String getTrigger() {
		return "!hi";
	}

	@Override
	public String getHelp() {
		return "I will extend my greetings to you.";
	}
}
