package botty.memory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelMemoryRepository extends CrudRepository<ChannelMemory, Integer> {
	
}