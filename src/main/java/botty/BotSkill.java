package botty;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import botty.components.DiscordClientComponent;
import discord4j.core.GatewayDiscordClient;

public abstract class BotSkill {

	private GatewayDiscordClient client;
	
	@Autowired
	private DiscordClientComponent clientComponent;

	public BotSkill() {
		
	}
	
	@PostConstruct
	protected void init() {
		client = clientComponent.getClient();
		
		this.registerSkill();
	}
	
	protected GatewayDiscordClient getClient() {
		return this.client;
	}
	
	protected abstract void registerSkill();
}
