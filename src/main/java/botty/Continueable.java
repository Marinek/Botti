package botty;

import botty.domain.frame.Frame;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.ReactionAddEvent;

public interface Continueable {

	public void continueSkill(GatewayDiscordClient client, ReactionAddEvent event, Frame frame);
	
}
