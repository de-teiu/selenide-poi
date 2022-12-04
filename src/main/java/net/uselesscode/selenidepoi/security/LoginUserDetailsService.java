package net.uselesscode.selenidepoi.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.uselesscode.selenidepoi.domain.UserMaster;
import net.uselesscode.selenidepoi.domain.UserMasterExample;
import net.uselesscode.selenidepoi.domain.UserRoleView;
import net.uselesscode.selenidepoi.domain.UserRoleViewExample;
import net.uselesscode.selenidepoi.mapper.UserMasterMapper;
import net.uselesscode.selenidepoi.mapper.UserRoleViewMapper;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	@Autowired
	UserMasterMapper userMasterMapper;

	@Autowired
	UserRoleViewMapper userRoleViewMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserMasterExample userMasterExample = new UserMasterExample();
		userMasterExample.createCriteria().andMailEqualTo(username);

		List<UserMaster> userMasters = userMasterMapper.selectByExample(userMasterExample);
		if (userMasters.isEmpty()) {
			throw new UsernameNotFoundException("Not Found");
		}

		UserMaster userMaster = userMasters.get(0);

		UserRoleViewExample userRoleViewExample = new UserRoleViewExample();
		userRoleViewExample.createCriteria().andUserIdEqualTo(userMaster.getId());

		List<UserRoleView> userRoles = userRoleViewMapper.selectByExample(userRoleViewExample);
		List<String> roleNames = userRoles.stream().map(r -> r.getRoleName()).collect(Collectors.toList());

		LoginUser loginUser = new LoginUser(userMaster.getId(), userMaster.getName(), userMaster.getMail(),
				userMaster.getPassword(), roleNames);
		return new LoginUserDetails(loginUser);
	}

}
