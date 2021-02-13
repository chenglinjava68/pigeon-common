
package cn.yiidii.pigeon.common.security.service;

import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author lengleng
 * @date 2019/2/1 扩展用户信息
 */
public class PigeonUser extends User {

	/**
	 * 用户ID
	 */
	@Getter
	private Long id;

	public PigeonUser(Long id, String username, String password, boolean enabled,
					  boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
					  Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
	}

}
