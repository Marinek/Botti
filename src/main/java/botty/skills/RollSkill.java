package botty.skills;

import org.springframework.stereotype.Component;

import botty.BotSkill;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class RollSkill extends BotSkill {

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {

		final Message message = event.getMessage();
		final MessageChannel channel = message.getChannel().block();
		if ("!roll".equals(message.getContent())) {
			channel.createMessage(message.getAuthor().get().getUsername() + " send me " + (int)(Math.random() * 100) + "$!").block();
			message.delete().block();
		} else if (message.getContent().startsWith("!roll")) {
			String[] split = message.getContent().split(" ");

			int number = (int)((split.length -2) * Math.random()) + 1;

			channel.createMessage(message.getAuthor().get().getUsername() + " I choose " + split[number] + "!").block();
		}
	}

	@Override
	public String getTrigger() {
		return "!roll";
	}

	@Override
	public String getHelp() {
		return "[option1 option2 ...] - Botti will roll a number or choose from one of the provided options.";
	}

}
