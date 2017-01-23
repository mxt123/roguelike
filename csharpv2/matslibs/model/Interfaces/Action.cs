using System;

namespace rl
{
	public interface Action
	{
		bool move(Map map, Direction d, int distance);
		bool attack(Thing thing);
		String look(Thing thing);
		void take(Map map, Thing thing);
		bool cast(rl.model.Point point);
		String talk(Thing thing);
	}
}

