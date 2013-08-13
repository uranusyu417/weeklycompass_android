package com.weeklycompass;

public class Role {
	public int RoleId;
	public String RoleName;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return RoleName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		Role ro = (Role)o;
		return RoleId==ro.RoleId;
	}

}
