package botty.skills.channelmanagement;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.components.DiscordClientComponent;
import botty.memory.BotChannel;
import botty.memory.BotChannelRepository;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class JoinChannelReaction {
	
	private static final Logger log = Loggers.getLogger(JoinChannelReaction.class);
	
	@Autowired
	private DiscordClientComponent clientComponent;
	
	@Autowired
	private BotChannelRepository bcr;

	@PostConstruct
	protected void init() {
		log.info("init() ");
		GatewayDiscordClient client = clientComponent.getClient();
		
		client.on(ReactionAddEvent.class).subscribe(event -> {
			Message message = event.getMessage().block();
			log.debug("Reaction triggert on: " + message);
			
			boolean isBookReaction = event.getEmoji().asUnicodeEmoji().get().getRaw().equals("\uD83D\uDCD2");
			
			if(isBookReaction && !event.getMember().get().isBot()) {
				BotChannel findByMessageId = bcr.findByMessageId(message.getId().asLong());
				event.getMember().get().addRole(findByMessageId.getRoleId()).block();
			}
		});
		
		client.on(ReactionRemoveEvent.class).subscribe(event -> {
			Message message = event.getMessage().block();
			log.debug("Reaction triggert on: " + message);
			
			boolean isBookReaction = event.getEmoji().asUnicodeEmoji().get().getRaw().equals("\uD83D\uDCD2");
			
			if(isBookReaction && !event.getUser().block().isBot()) {
				BotChannel findByMessageId = bcr.findByMessageId(message.getId().asLong());
				Member memberById = event.getClient().getMemberById(event.getGuildId().get(), event.getUserId()).block();
				memberById.removeRole(findByMessageId.getRoleId()).block();
			}
		});
	}
}
