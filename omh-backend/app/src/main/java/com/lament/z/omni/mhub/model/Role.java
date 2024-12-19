package com.lament.z.omni.mhub.model;

import java.util.Objects;
import java.util.StringJoiner;

import com.lament.z.omni.mhub.security.SecurityConstants;

public class Role {
	private String name;

	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Role )) return false;

		return getName() != null && getName().equals(((Role) o).getName());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
				.add("name='" + name + "'")
				.toString();
	}

	public static Role getDefualRole(){

		return new Role(SecurityConstants.USER);
	}
}
