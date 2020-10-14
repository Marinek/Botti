package botty;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.components.DiscordClientComponent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class Bot {
	
	@Autowired
	private DiscordClientComponent clientComponent;
	
	@Autowired
	private Map<String, BotSkill> botSkills;

	@PostConstruct
	public void start() {
		
		GatewayDiscordClient client = clientComponent.getClient();

		client.on(PresenceUpdateEvent.class).subscribe(event -> {
			System.out.println(event);
		});

		client.on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			if ("!creator".equals(message.getContent())) {
				
				message.delete().block();
				message.getAuthor().get().getPrivateChannel().block().createMessage("I was created by the leading software architect **Marinek** to serve him.").block();
			}
		});


		client.on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			final MessageChannel channel = message.getChannel().block();
			if ("!roll".equals(message.getContent())) {
				channel.createMessage(message.getAuthor().get().getUsername() + " send me " + (int)(Math.random() * 100) + "$!").block();
				message.delete().block();
			} else if (message.getContent().startsWith("!roll")) {
				String[] split = message.getContent().split(" ");
				
				int number = (int)((split.length -2) * Math.random()) + 1;
				
				channel.createMessage(message.getAuthor().get().getUsername() + " I want " + split[number] + " from you!").block();
			}
			
		});
		
		client.on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			if ("!help".equals(message.getContent())) {
				
				final MessageChannel channel = message.getAuthor().get().getPrivateChannel().block();
				channel.createMessage(
						"Hi, my name is Lord Gaben! Send me commands and I will see to help you...\n" +
						"`!hi - I will extend my greetings to you.`\n" +
						"`!roll [option1 option2 ...] - I will ask you for a random amount of $$$ or make a decision for you.`\n" +
						"`!creator - Lord Gaben will reveal his creator.`\n"
						
						).block();
			}
		});

		client.onDisconnect().block();
	}

}