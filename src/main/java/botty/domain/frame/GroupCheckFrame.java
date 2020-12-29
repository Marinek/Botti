package botty.domain.frame;

import java.util.List;

import discord4j.core.object.entity.Member;

public class GroupCheckFrame implements Frame {

	private int max;	
	
	private int current;
	
	private List<Member> members;
	
	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "GroupCheckFrame [max=" + max + ", current=" + current + ", members=" + members + "]";
	}

	@Override
	public String getContinueableClazz() {
		return "groupCheckSkill";
	}
	
}
