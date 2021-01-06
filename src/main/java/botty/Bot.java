package botty;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.components.DiscordClientComponent;
import botty.domain.frame.GroupCheckFrame;
import botty.memory.ChannelMemory;
import botty.memory.ChannelMemoryRepository;
import botty.memory.MessageFrameMemory;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Message;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class Bot {
	
    private static final Logger log = Loggers.getLogger(Bot.class);
	
	@Autowired
	private DiscordClientComponent clientComponent;
	
	@Autowired
	private MessageFrameMemory<GroupCheckFrame> gcfm;
	
	@Autowired
	private Map<String, Continueable> map;
	
	@Autowired
	private List<BotSkill> botSkills;
	
	@Autowired
	private ChannelMemoryRepository cmr;
	
	@PostConstruct
	public void start() {
		
		GatewayDiscordClient client = clientComponent.getClient();

		client.on(PresenceUpdateEvent.class).subscribe(event -> {
			log.debug(String.valueOf(event));
		});

		for (BotSkill botSkill : botSkills) {
			log.info("Loaded Skill: " + botSkill.getTrigger());
		}
		
		client.on(ReactionAddEvent.class).subscribe(event -> {
			log.info("Reaction added on: " + event.getMessage().block());
			
			Message message = event.getMessage().block();
			
			GroupCheckFrame frame = gcfm.getFrame(message);
			
			if(frame != null) {
				log.info(frame.toString());
				
				if(map.containsKey(frame.getContinueableClazz())) {
					Continueable bean = map.get(frame.getContinueableClazz());
					bean.continueSkill(client, event, frame);
				}
			}
			
		});
		
		log.info(cmr.findAll().toString());

		client.onDisconnect().block();
	}

}