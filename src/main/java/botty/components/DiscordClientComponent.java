package botty.components;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;

@Component
@ApplicationScope
public class DiscordClientComponent {

	@Value("${token}")
	private String token;
	
	private GatewayDiscordClient client;
	
	@PostConstruct
	public void start() {
		setClient(DiscordClientBuilder.create(token)
				.build()
				.login()
				.block());
		
		client.getEventDispatcher().on(ReadyEvent.class)
		.subscribe(event -> {
			User self = event.getSelf();
			System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
		});

	}

	public GatewayDiscordClient getClient() {
		return client;
	}

	public void setClient(GatewayDiscordClient client) {
		this.client = client;
	}
}
