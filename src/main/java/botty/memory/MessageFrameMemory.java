package botty.memory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import botty.domain.frame.Frame;
import discord4j.core.object.entity.Message;

@Repository
public class MessageFrameMemory<T extends Frame> {

	private Map<Message, T> messageToFrameMap = new HashMap<>();

	public void addMessage(Message message, T frame) {
		if(message != null) {
			messageToFrameMap.put(message, frame);
		}
	}
	
	public T getFrame(Message message) {
		if(messageToFrameMap.containsKey(message)) {
			return messageToFrameMap.get(message);
		} else {
			return null;
		}
	}
	
}
