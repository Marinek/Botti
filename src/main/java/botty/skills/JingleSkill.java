package botty.skills;

import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;

import botty.BotSkill;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;

@Component
public class JingleSkill extends BotSkill {

	final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

	@Override
	protected void executeSkill(GatewayDiscordClient client, MessageCreateEvent event) {

		playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

		AudioPlayer player = playerManager.createPlayer();

		AudioProvider provider = new LavaPlayerAudioProvider(player);

		AudioSourceManagers.registerRemoteSources(playerManager);

		String[] messageTokens = event.getMessage().getContent().split(" ");

		if(messageTokens.length != 2) {
			return;
		}
		
		final Member member = event.getMember().orElse(null);
		if (member != null) {
			final VoiceState voiceState = member.getVoiceState().block();
			if (voiceState != null) {
				final VoiceChannel channel = voiceState.getChannel().block();
				if (channel != null) {
					// join returns a VoiceConnection which would be required if we were
					// adding disconnection features, but for now we are just ignoring it.
					channel.join(spec -> spec.setProvider(provider)).block();

					final TrackScheduler scheduler = new TrackScheduler(player);
					
					playerManager.loadItem(messageTokens[1], scheduler);
				}
			}
		}
	}

	@Override
	public String getTrigger() {
		return "!youtube";
	}

	public final class LavaPlayerAudioProvider extends AudioProvider {

		private final AudioPlayer player;
		private final MutableAudioFrame frame = new MutableAudioFrame();

		public LavaPlayerAudioProvider(final AudioPlayer player) {
			super(
					ByteBuffer.allocate(
							StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()
							)
					);
			frame.setBuffer(getBuffer());
			this.player = player;
		}

		@Override
		public boolean provide() {
			// AudioPlayer writes audio data to its AudioFrame
			final boolean didProvide = player.provide(frame);
			// If audio was provided, flip from write-mode to read-mode
			if (didProvide) {
				getBuffer().flip();
				System.out.println("JingleSkill.LavaPlayerAudioProvider.provide()" + didProvide);
			}
			return didProvide;
		}

	}

	public final class TrackScheduler implements AudioLoadResultHandler {

		private final AudioPlayer player;

		public TrackScheduler(final AudioPlayer player) {
			this.player = player;
		}

		@Override
		public void trackLoaded(final AudioTrack track) {
			player.playTrack(track);
		}

		@Override
		public void playlistLoaded(final AudioPlaylist playlist) {
		}

		@Override
		public void noMatches() {
		}

		@Override
		public void loadFailed(final FriendlyException exception) {
		}
	}

	@Override
	public String getHelp() {
		return "[youtube url] Botti will play the sound from youtube.";
	}
}
