package botty.skills;

import org.springframework.stereotype.Component;

import botty.BotSkill;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class HiSkill extends BotSkill {

	@Override
	protected void registerSkill() {
		System.out.println("register skill");
		
		getClient().on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			if ("!hi".equals(message.getContent())) {
				final MessageChannel channel = message.getChannel().block();
				channel.createMessage("Hello " + message.getAuthor().get().getUsername()).block();
			}
		});
	}
}
