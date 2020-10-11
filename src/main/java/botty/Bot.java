package botty;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

@Component
public class Bot {
	
	@Value("${token}")
	private String token;

	@PostConstruct
	public void start() {
		GatewayDiscordClient client = DiscordClientBuilder.create(token)
				.build()
				.login()
				.block();


		client.getEventDispatcher().on(ReadyEvent.class)
		.subscribe(event -> {
			User self = event.getSelf();
			System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
		});

		client.on(PresenceUpdateEvent.class).subscribe(event -> {
			System.out.println(event);
		});

		client.on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			if ("!hi".equals(message.getContent())) {
				final MessageChannel channel = message.getChannel().block();
				channel.createMessage("Hello " + message.getAuthor().get().getUsername()).block();
			}
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