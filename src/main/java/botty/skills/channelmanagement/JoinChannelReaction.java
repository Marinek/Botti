package botty.skills.channelmanagement;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.components.DiscordClientComponent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class JoinChannelReaction {
	
	private static final Logger log = Loggers.getLogger(JoinChannelReaction.class);
	
	@Autowired
	private DiscordClientComponent clientComponent;

	@PostConstruct
	protected void init() {
		log.info("init() ");
		GatewayDiscordClient client = clientComponent.getClient();
		
		client.on(ReactionAddEvent.class).subscribe(event -> {
			log.info("Reaction triggert on: " + event.getMessage().block());
			
			boolean isBookReaction = event.getEmoji().asUnicodeEmoji().get().getRaw().equals("\uD83D\uDCD2");
			
			log.info(isBookReaction + "");
		});
		
		client.on(ReactionRemoveEvent.class).subscribe(event -> {
			log.info("Reaction triggert on: " + event.getMessage().block());
			
			boolean isBookReaction = event.getEmoji().asUnicodeEmoji().get().getRaw().equals("\uD83D\uDCD2");
			
			log.info(isBookReaction + "");
		});
	}
}
