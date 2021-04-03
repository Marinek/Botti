package botty.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import botty.BotSkill;
import botty.Continueable;
import botty.SkillException;
import botty.domain.frame.Frame;
import botty.domain.frame.GroupCheckFrame;
import botty.memory.MessageFrameMemory;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class GroupCheckSkill extends BotSkill implements Continueable {

	private static final Logger log = Loggers.getLogger(GroupCheckSkill.class);
	
	@Autowired
	private MessageFrameMemory<GroupCheckFrame> groupCheckFrame;

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {
		
		GroupCheckFrame frame = new GroupCheckFrame();
		
		final Message message = event.getMessage();

		int maxMemberCount = getMemberCount(message);
		
		VoiceState memberVoiceState = event.getMember().get().getVoiceState().block();
		VoiceChannel voiceChannel = memberVoiceState.getChannel().block();
		
		Iterable<VoiceState> voiceStates = voiceChannel.getVoiceStates().toIterable();

		List<Member> channelMember = new ArrayList<>(); 

		for(VoiceState vs : voiceStates) {
			Member member = vs.getMember().block();
			log.info("Possible Member: " + member.getDisplayName());

			channelMember.add(member);
		}

		if(maxMemberCount > 0) {
			message.getChannel().block().createMessage("I am searching for " + maxMemberCount + " member." ).block();
		} else {
			message.getChannel().block().createMessage("Here you are my voting on the order of your channel." ).block();
			maxMemberCount = channelMember.size();
		}
		
		Collections.shuffle(channelMember);
		
		frame.setMax(maxMemberCount);
		frame.setMembers(channelMember);
		
		MessageChannel channel = message.getChannel().block();
		
		showMembers(frame, channel);
		
	}

	@Override
	public void continueSkill(GatewayDiscordClient client, ReactionAddEvent event, Frame frame) {
		if(frame instanceof GroupCheckFrame) {
			((GroupCheckFrame) frame).setMax(((GroupCheckFrame) frame).getMax() + 1);
			showMembers((GroupCheckFrame)frame, event.getMessage().block().getChannel().block());
		} 
	}
	
	private void showMembers(GroupCheckFrame frame, MessageChannel channel) {
		for (int i = frame.getCurrent(); i < frame.getMax() && i < frame.getMembers().size(); frame.setCurrent(++i)) {
			Message outputMessage = channel.createMessage((i + 1) + ". " + frame.getMembers().get(i).getDisplayName()).block();
			
			groupCheckFrame.addMessage(outputMessage, frame);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("Error during sleep", e);
			}
		}
	}

	private int getMemberCount(Message message) {
		String[] split = message.getContent().split(" ");
		
		if(split.length == 2) {
			//!groupcheck 5
			return Integer.parseInt(split[1]);
		} else if (split.length == 1) {
			return -1;
		}
		
		throw new SkillException("Please state the maximum player count for your group!");
	}

	@Override
	public String getTrigger() {
		return "!groupcheck";
	}

	@Override
	public String getHelp() {
		return "[amount] Botti will find the chosen ones for you.";
	}


}
