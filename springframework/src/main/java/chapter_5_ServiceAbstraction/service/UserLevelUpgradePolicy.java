package chapter_5_ServiceAbstraction.service;

import chapter_5_ServiceAbstraction.domain.User;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);
	void upgradeLevel(User user);
}	
