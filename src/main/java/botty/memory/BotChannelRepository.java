package botty.memory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotChannelRepository extends CrudRepository<BotChannel, Integer> {

	public BotChannel findByMessageId(long asLong);
	
}