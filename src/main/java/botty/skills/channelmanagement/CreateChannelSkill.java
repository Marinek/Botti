package botty.skills.channelmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.BotSkill;
import botty.SkillException;
import botty.memory.ChannelMemory;
import botty.memory.ChannelMemoryRepository;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

@Component
public class CreateChannelSkill extends BotSkill {

	@Autowired
	private ChannelManagement channelManagement;
	
	@Autowired
	private JoinChannelReaction jcr;
	
	@Autowired
	private ChannelMemoryRepository channelMemoryRepo;
	
	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {
		String content = event.getMessage().getContent();
		
		ChannelMemory channelMemory = new ChannelMemory();
		
		String[] tokens = content.split(" ");
		
		if(tokens.length != 2) {
			throw new SkillException("Usage: !createChannel <channelname>");
		}
		
		TextChannel groupChannel = channelManagement.getGroupChannel(client, event);

		Role channelRole = event.getGuild().block().createRole(role -> {
			role.setMentionable(false);
			role.setName("lg-" + tokens[1]);
			//role.setPermissions()
			
		}).block();
		
		channelMemory.setRoleId(channelRole.getId());
		
		TextChannel newChannel = event.getGuild().block().createTextChannel(channel -> {
			channel.setName("lg-" + tokens[1] );
			channel.setParentId(groupChannel.getCategory().block().getId());
			channel.setPosition(groupChannel.getPosition().block() + 1);
			
			
			
			event.getMember().get().addRole(channelRole.getId()).block();
			
			Message channelMessage = groupChannel.createMessage(message -> {
				StringBuilder contentBuilder = new StringBuilder();
				
				
				contentBuilder.append("Lerngruppe: #lg-" + tokens[1]);
				contentBuilder.append('\n');
				contentBuilder.append("Besitzer: " + event.getMember().get().getMention());
				
				message.setContent(contentBuilder.toString());
			
			}).block();	
			
			channelMessage.addReaction(ReactionEmoji.unicode("\uD83D\uDCD2")).block();
			//channelMessage.addReaction(ReactionEmoji.unicode("0x1F512")).block();
			//channelMessage.addReaction(ReactionEmoji.unicode("0x1F513")).block();
			channelMemory.setMessageId(channelMessage.getId());
		}).block();
		
		channelMemory.setChannelId(newChannel.getId());
		
		newChannel.addRoleOverwrite(channelRole.getId(), PermissionOverwrite.forRole(channelRole.getId(), PermissionSet.of(
					Permission.READ_MESSAGE_HISTORY,
					Permission.SEND_MESSAGES,
					Permission.ATTACH_FILES,
					Permission.EMBED_LINKS,
					Permission.VIEW_CHANNEL,
					Permission.ADD_REACTIONS 
				), PermissionSet.none())).block();
		
		channelMemoryRepo.save(channelMemory);
	}

	@Override
	public String getTrigger() {
		return "!createChannel";
	}

}
