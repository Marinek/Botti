package botty.skills;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import botty.BotSkill;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class HelpSkill extends BotSkill {

	@Autowired
	private List<BotSkill> botskills;

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {

		final Message message = event.getMessage();

		message.delete().block();

		final MessageChannel channel = message.getAuthor().get().getPrivateChannel().block();

		String helpContent = getHelpContent();

		channel.createMessage(
				"Hi, my name is Botti! Send me commands and I will see to help you...\n" +
						helpContent
				).block();
		
		String s = String.valueOf(223);

	}

	private String getHelpContent() {
		StringBuilder sb = new StringBuilder();

		for (BotSkill botSkill : botskills) {
			if(StringUtils.hasText(botSkill.getHelp())) {
				sb.append("`" + botSkill.getTrigger() + " - " + botSkill.getHelp() + "`\n");
			}
		}

		return sb.toString();
	}

	@Override
	public String getTrigger() {
		return "!help";
	}

}
