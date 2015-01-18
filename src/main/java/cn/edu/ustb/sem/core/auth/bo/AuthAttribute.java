package cn.edu.ustb.sem.core.auth.bo;

import org.springframework.security.access.ConfigAttribute;

public class AuthAttribute implements ConfigAttribute {
	private static final long serialVersionUID = 5873421360742903221L;
	private String roleId;
	public AuthAttribute(String roleId) {
		this.roleId = roleId;
	}
	@Override
	public String getAttribute() {
		return roleId;
	}
	@Override
	public int hashCode() {
		return Integer.parseInt(roleId);
	}
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof ConfigAttribute) {
			ConfigAttribute other = (ConfigAttribute)o;
			if (other.getAttribute().equals(roleId)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
