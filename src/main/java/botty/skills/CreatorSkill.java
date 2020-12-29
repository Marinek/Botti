package botty.skills;

import org.springframework.stereotype.Component;

import botty.BotSkill;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

@Component
public class CreatorSkill extends BotSkill {

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {
		final Message message = event.getMessage();
		message.delete().block();
		message.getAuthor().get().getPrivateChannel().block().createMessage("I was created by the leading software architect **Marinek** to serve him.").block();
	}

	@Override
	public String getTrigger() {
		return "!creator";
	}

	@Override
	public String getHelp() {
		return "Lord Gaben will reveal his creator.";
	}


}
