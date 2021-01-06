package botty;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import botty.components.DiscordClientComponent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.util.Logger;
import reactor.util.Loggers;

public abstract class BotSkill {
	
	private static final Logger log = Loggers.getLogger(BotSkill.class);

	private GatewayDiscordClient client;
	
	@Autowired
	private DiscordClientComponent clientComponent;

	public BotSkill() {
		
	}
	
	@PostConstruct
	protected void init() {
		client = clientComponent.getClient();
		
		client.on(MessageCreateEvent.class).subscribe(event -> {
			if(event.getMember().isPresent() && event.getMember().get().isBot()) {
				return;
			}
			
			final Message message = event.getMessage();
			
			if (message.getContent().startsWith(getTrigger())) {
				try {
					this.executeSkill(client, event);
				} catch (SkillException e) {
					message.getChannel().block().createMessage("Unable to comlply: " + e.getMessage()).block();
					log.error("General Error", e);
				}
			}
		});
		
	}
	
	protected GatewayDiscordClient getClient() {
		return this.client;
	}
	
	protected abstract void executeSkill(GatewayDiscordClient client, MessageCreateEvent event);

	/**
	 * 
	 * @return The name of the skill; which is the same as his trigger. 
	 */
	public abstract String getTrigger();

	public String getHelp() {
		return null;
	}
}
